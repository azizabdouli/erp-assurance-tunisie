package com.erp.assurance.tunisie.underwriting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "underwriting_rules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UnderwritingRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "rule_description", columnDefinition = "TEXT")
    private String ruleDescription;

    @Column(name = "condition_json", columnDefinition = "TEXT", nullable = false)
    private String conditionJson;

    @Column(name = "action_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(name = "action_value")
    private String actionValue;

    @Column(name = "priority")
    @Builder.Default
    private int priority = 0;

    @Column(name = "active")
    @Builder.Default
    private boolean active = true;

    public enum ActionType {
        ACCEPT, REJECT, REFER, APPLY_LOADING, APPLY_DISCOUNT
    }
}
