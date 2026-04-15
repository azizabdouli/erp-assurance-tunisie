package com.erp.assurance.tunisie.crm.dto;

import com.erp.assurance.tunisie.shared.enums.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientSearchCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private ClientType clientType;
}
