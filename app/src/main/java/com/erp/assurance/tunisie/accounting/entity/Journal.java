package com.erp.assurance.tunisie.accounting.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.JournalType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Journal extends AuditableEntity {

    @Column(name = "journal_code", nullable = false, unique = true)
    private String journalCode;

    @Column(name = "journal_name", nullable = false)
    private String journalName;

    @Enumerated(EnumType.STRING)
    @Column(name = "journal_type", nullable = false)
    private JournalType journalType;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "journal", cascade = CascadeType.ALL)
    @Builder.Default
    private List<JournalEntry> entries = new ArrayList<>();
}
