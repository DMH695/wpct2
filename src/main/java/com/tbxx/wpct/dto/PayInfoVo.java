package com.tbxx.wpct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayInfoVo {
    String villageName;
    String buildNo;
    String name;
    String payStatus;
    Integer pageSize;
    Integer pageNum;
    LocalDateTime payBeginTime;
    LocalDateTime payEndTime;

}
