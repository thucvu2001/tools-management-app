package com.lifesup.toolsmanagement.user.repository;

import com.lifesup.toolsmanagement.user.model.MapUserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapUserDeviceRepository extends JpaRepository<MapUserDevice, Integer> {

    @Query(nativeQuery = true, value = """
            select * 
            from map_user_device as mud
            where mud.user_id = ?1 and mud.transaction_id = ?2
            """)
    List<MapUserDevice> findByUserIdAndTransactionId(Integer userId, Integer transactionId);
}
