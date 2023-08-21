package com.lifesup.toolsmanagement.user.service;

import com.lifesup.toolsmanagement.common.util.Mapper;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import com.lifesup.toolsmanagement.user.repository.MapUserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MapUserDeviceServiceImpl implements MapUserDeviceService {
    private final MapUserDeviceRepository mapUserDeviceRepository;
    private final Mapper mapper;

    @Override
    public JpaRepository<MapUserDevice, UUID> getRepository() {
        return this.mapUserDeviceRepository;
    }

    @Override
    public Mapper getMapper() {
        return this.mapper;
    }

    @Override
    public void update(MapUserDevice mapUserDevice) {
        Optional<MapUserDevice> optionalMapUserDevice = mapUserDeviceRepository.findById(mapUserDevice.getId());
        if (optionalMapUserDevice.isEmpty()) {
            throw new RuntimeException("MapUserDevice with ID: " + mapUserDevice.getDeviceId() + " not found");
        }
        MapUserDevice curMapUserDevice = optionalMapUserDevice.get();
        curMapUserDevice.setExpDate(null);
        curMapUserDevice.setDeleted(true);

        mapUserDeviceRepository.save(curMapUserDevice);
    }

    @Override
    public List<MapUserDevice> getMapUserDeviceByUserIdAndTransactionId(UUID userId, UUID transactionId) {
        return mapUserDeviceRepository.findByUserIdAndTransactionId(userId, transactionId);
    }
}
