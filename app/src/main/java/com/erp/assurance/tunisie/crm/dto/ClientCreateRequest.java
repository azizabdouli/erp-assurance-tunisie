package com.erp.assurance.tunisie.crm.dto;

import com.erp.assurance.tunisie.shared.enums.ClientType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateRequest {

    @NotNull(message = "Client type is required")
    private ClientType clientType;

    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;

    @NotBlank(message = "CIN is required")
    private String cin;

    private LocalDate dateOfBirth;
    private String companyName;
    private String registrationNumber;
    private String taxId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String governorate;
    private String postalCode;
    private String iban;
    private String bankName;
}
