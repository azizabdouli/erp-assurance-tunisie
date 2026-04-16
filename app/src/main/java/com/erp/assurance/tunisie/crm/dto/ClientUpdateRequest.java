package com.erp.assurance.tunisie.crm.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateRequest {
    private String firstName;
    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String governorate;
    private String postalCode;
    private String iban;
    private String bankName;
}
