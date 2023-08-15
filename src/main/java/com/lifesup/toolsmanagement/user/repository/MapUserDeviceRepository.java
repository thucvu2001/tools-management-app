package com.lifesup.toolsmanagement.user.repository;

import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MapUserDeviceRepository extends JpaRepository<MapUserDevice, UUID> {

}
