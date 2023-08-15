package com.lifesup.toolsmanagement.user.service;

import com.lifesup.toolsmanagement.common.util.Mapper;
import com.lifesup.toolsmanagement.user.dto.MapUserDeviceDTO;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import com.lifesup.toolsmanagement.user.repository.MapUserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MapUserDeviceServiceImpl implements MapUserDeviceService {
    private final MapUserDeviceRepository mapUserDeviceRepository;
    private final Mapper mapper;
    private final ModelMapper modelMapper;

    @Override
    public JpaRepository<MapUserDevice, UUID> getRepository() {
        return this.mapUserDeviceRepository;
    }

    @Override
    public Mapper getMapper() {
        return this.mapper;
    }

    @Override
    public MapUserDeviceDTO update(MapUserDeviceDTO mapUserDeviceDTO, UUID id) {
        Optional<MapUserDevice> mapUserDeviceOptional = mapUserDeviceRepository.findById(id);
        if (mapUserDeviceOptional.isEmpty()) {
            throw new RuntimeException("MapUserDevice with ID: " + id + " not found");
        }
        MapUserDevice mapUserDevice = mapUserDeviceOptional.get();
        mapUserDevice.setDeviceId(mapUserDeviceDTO.getDeviceId());
        mapUserDevice.setExpDate(mapUserDeviceDTO.getExpDate());
        return modelMapper.map(mapUserDevice, MapUserDeviceDTO.class);
    }
}
