package com.lifesup.toolsmanagement.user.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapUserDeviceDTO {
    private Integer deviceId;
    private LocalDate expDate;
}
