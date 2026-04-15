package com.erp.assurance.tunisie.crm.repository;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.shared.enums.AmlRiskLevel;
import com.erp.assurance.tunisie.shared.enums.ClientType;
import com.erp.assurance.tunisie.shared.enums.KycStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByCinAndDeletedFalse(String cin);

    Optional<Client> findByEmailAndDeletedFalse(String email);

    @Query("SELECT c FROM Client c WHERE c.deleted = false " +
            "AND (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) " +
            "AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
            "AND (:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:clientType IS NULL OR c.clientType = :clientType)")
    Page<Client> searchClients(@Param("firstName") String firstName,
                               @Param("lastName") String lastName,
                               @Param("email") String email,
                               @Param("clientType") ClientType clientType,
                               Pageable pageable);

    Page<Client> findByKycStatusAndDeletedFalse(KycStatus kycStatus, Pageable pageable);

    Page<Client> findByAmlRiskLevelAndDeletedFalse(AmlRiskLevel amlRiskLevel, Pageable pageable);

    Page<Client> findByDeletedFalse(Pageable pageable);

    boolean existsByCinAndDeletedFalse(String cin);

    boolean existsByEmailAndDeletedFalse(String email);
}
