package com.neonates.request;

import com.neonates.Enum.DonorType;
import lombok.Data;

@Data
public class DonorRequest {
    private String donorName;
    private DonorType donorType;
    private String contactEmail;
    private Boolean activeFlag;
}
