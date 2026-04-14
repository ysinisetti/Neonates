package com.neonates.request;

import com.neonates.Enum.HospitalType;
import lombok.Data;

@Data
public class HospitalRequestDTO {
    private String hospitalName;
    private HospitalType hospitalType;
    private String city;
    private String state;
    private String spocName;
    private String spocPhone;
}
