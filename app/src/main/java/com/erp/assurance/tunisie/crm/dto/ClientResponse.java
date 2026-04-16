package com.erp.assurance.tunisie.crm.dto;

import com.erp.assurance.tunisie.shared.enums.AmlRiskLevel;
import com.erp.assurance.tunisie.shared.enums.ClientType;
import com.erp.assurance.tunisie.shared.enums.KycStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private UUID id;
    private ClientType clientType;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private String cin;
    private LocalDate dateOfBirth;
    private String companyName;
    private String registrationNumber;
    private KycStatus kycStatus;
    private AmlRiskLevel amlRiskLevel;
    private String city;
    private String governorate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
