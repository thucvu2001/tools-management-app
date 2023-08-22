package com.lifesup.toolsmanagement.user.service;

import com.lifesup.toolsmanagement.common.service.GenericService;
import com.lifesup.toolsmanagement.user.dto.MapUserDeviceDTO;
import com.lifesup.toolsmanagement.user.model.MapUserDevice;

import java.util.List;

public interface MapUserDeviceService extends GenericService<MapUserDevice, MapUserDeviceDTO, Integer> {
    void update(MapUserDevice mapUserDevice);

    List<MapUserDevice> getMapUserDeviceByUserIdAndTransactionId(Integer userId, Integer transactionId);
}
