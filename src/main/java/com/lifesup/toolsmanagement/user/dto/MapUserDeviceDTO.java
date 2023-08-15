package com.lifesup.toolsmanagement.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapUserDeviceDTO {
    private String deviceId;
    private LocalDate expDate;
}
