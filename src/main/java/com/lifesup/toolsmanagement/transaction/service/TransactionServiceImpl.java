package com.lifesup.toolsmanagement.transaction.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lifesup.toolsmanagement.common.util.Mapper;
import com.lifesup.toolsmanagement.security.service.JWTService;
import com.lifesup.toolsmanagement.transaction.model.Transaction;
import com.lifesup.toolsmanagement.transaction.repository.TransactionRepository;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import com.lifesup.toolsmanagement.user.model.User;
import com.lifesup.toolsmanagement.user.service.MapUserDeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final MapUserDeviceService mapUserDeviceService;
    private final Mapper mapper;
    private final JWTService jwtService;
    private static final String SECRET_KEY = "c342dd5ff691b05ce02f7d0dd292a65eb4d89965";


    @Override
    public JpaRepository<Transaction, UUID> getRepository() {
        return this.transactionRepository;
    }

    @Override
    public Mapper getMapper() {
        return this.mapper;
    }

    @Override
    public List<Transaction> getTransactionByUserId(UUID userId) {
        return transactionRepository.findByUserId(LocalDate.now(), userId);
    }

    @Override
    public String createTransaction(int year, int amountDevice) {
        BigDecimal value = BigDecimal.valueOf(50L * year * amountDevice);
        log.info(value.toString());
        return jwtService.generateToken(year, amountDevice, value);
    }

    @Override
    public String checkTransaction(User user, String token, BigDecimal amountMoney) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        String valueString = decodedJWT.getClaim("value").asString();
        BigDecimal value = new BigDecimal(valueString);
        if (amountMoney.compareTo(value) < 0) {
            return "Not enough money";
        } else {
            int amountDevice = decodedJWT.getClaim("amountDevice").asInt();
            int year = decodedJWT.getClaim("year").asInt();
            Transaction transaction = Transaction.builder()
                    .amountDevice(amountDevice)
                    .createdDate(LocalDate.now())
                    .expDate(LocalDate.now().plusYears(year))
                    .isActive(true)
                    .isDeleted(false)
                    .token(token)
                    .value(value)
                    .user(user)
                    .build();
            transactionRepository.save(transaction);
            return "Recharge successful";
        }
    }

    @Override
    public void checkExpiration() {
        List<Transaction> transactionList = transactionRepository.findAll();
        for (Transaction transaction : transactionList) {
            if (LocalDate.now().isAfter(transaction.getExpDate())) {
                transaction.setDeleted(true);
                transaction.setExpDate(null);
                transactionRepository.save(transaction);
                Set<MapUserDevice> mapUserDeviceSet = transaction.getMapUserDeviceSet();
                for (MapUserDevice mapUserDevice : mapUserDeviceSet) {
                    mapUserDevice.setExpDate(null);
                    mapUserDevice.setDeleted(true);
                    mapUserDeviceService.update(mapUserDevice);
                }
            }
        }
    }

    @Override
    public void exportTransactionToExcel(LocalDate startDate, LocalDate endDate) throws IOException {
        String fileName = "transaction-" + endDate.getMonthValue() + ".xlsx";
        String filePath = "C:\\Users\\vuvan\\Desktop\\New folder\\" + fileName;
        List<Transaction> transactionList = transactionRepository.findTransactionsByDateRange(startDate, endDate);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Transaction ID");
        headerRow.createCell(1).setCellValue("Value");
        headerRow.createCell(2).setCellValue("Exp Date");
        headerRow.createCell(3).setCellValue("Amount Device");
        headerRow.createCell(4).setCellValue("Token");
        headerRow.createCell(5).setCellValue("Active");
        headerRow.createCell(6).setCellValue("Created Date");
        headerRow.createCell(7).setCellValue("Deleted");
        headerRow.createCell(8).setCellValue("User ID");

        int rowColumn = 1;
        for (Transaction transaction : transactionList) {
            Row row = sheet.createRow(rowColumn);
            row.createCell(0).setCellValue(transaction.getId().toString());
            row.createCell(1).setCellValue(transaction.getValue().toString());
            row.createCell(2).setCellValue(transaction.getExpDate().toString());
            row.createCell(3).setCellValue(transaction.getAmountDevice());
            row.createCell(4).setCellValue(transaction.getToken());
            row.createCell(5).setCellValue(transaction.isActive());
            row.createCell(6).setCellValue(transaction.getCreatedDate().toString());
            row.createCell(7).setCellValue(transaction.isDeleted());
            row.createCell(8).setCellValue(transaction.getUser().getId().toString());
            ++rowColumn;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }
}
