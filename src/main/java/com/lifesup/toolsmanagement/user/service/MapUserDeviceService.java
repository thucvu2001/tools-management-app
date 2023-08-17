package com.lifesup.toolsmanagement.user.service;

import com.lifesup.toolsmanagement.common.service.GenericService;
import com.lifesup.toolsmanagement.user.dto.MapUserDeviceDTO;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;

import java.util.List;
import java.util.UUID;

public interface MapUserDeviceService extends GenericService<MapUserDevice, MapUserDeviceDTO, UUID> {
    MapUserDeviceDTO update(MapUserDeviceDTO mapUserDeviceDTO, UUID id);

    List<MapUserDevice> getMapUserDeviceByUserIdAndTransactionId(UUID userId, UUID transactionId);
}
