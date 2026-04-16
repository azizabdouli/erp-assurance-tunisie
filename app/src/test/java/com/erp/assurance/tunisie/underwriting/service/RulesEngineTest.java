package com.erp.assurance.tunisie.underwriting.service;

import com.erp.assurance.tunisie.underwriting.entity.UnderwritingRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RulesEngineTest {
    private RulesEngine rulesEngine;

    @BeforeEach
    void setUp() {
        rulesEngine = new RulesEngine(new ObjectMapper());
    }

    @Test
    void evaluateRules_SimpleCondition_Matches() {
        UnderwritingRule rule = createRule("{\"field\":\"age\",\"operator\":\"gt\",\"value\":\"65\"}", UnderwritingRule.ActionType.REFER);
        Map<String, Object> data = new HashMap<>();
        data.put("age", "70");
        assertThat(rulesEngine.evaluateRules(List.of(rule), data)).isEqualTo(UnderwritingRule.ActionType.REFER);
    }

    @Test
    void evaluateRules_NoMatch_ReturnsAccept() {
        UnderwritingRule rule = createRule("{\"field\":\"age\",\"operator\":\"gt\",\"value\":\"65\"}", UnderwritingRule.ActionType.REJECT);
        Map<String, Object> data = new HashMap<>();
        data.put("age", "30");
        assertThat(rulesEngine.evaluateRules(List.of(rule), data)).isEqualTo(UnderwritingRule.ActionType.ACCEPT);
    }

    @Test
    void evaluateRules_ANDCondition() {
        UnderwritingRule rule = createRule("{\"AND\":[{\"field\":\"age\",\"operator\":\"gt\",\"value\":\"60\"},{\"field\":\"claims\",\"operator\":\"gt\",\"value\":\"3\"}]}", UnderwritingRule.ActionType.REJECT);
        Map<String, Object> data = new HashMap<>();
        data.put("age", "65");
        data.put("claims", "5");
        assertThat(rulesEngine.evaluateRules(List.of(rule), data)).isEqualTo(UnderwritingRule.ActionType.REJECT);
    }

    @Test
    void evaluateRules_NoRules_ReturnsAccept() {
        assertThat(rulesEngine.evaluateRules(List.of(), new HashMap<>())).isEqualTo(UnderwritingRule.ActionType.ACCEPT);
    }

    @Test
    void evaluateRules_InactiveRule_Skipped() {
        UnderwritingRule rule = createRule("{\"field\":\"age\",\"operator\":\"gt\",\"value\":\"65\"}", UnderwritingRule.ActionType.REJECT);
        rule.setActive(false);
        Map<String, Object> data = new HashMap<>();
        data.put("age", "70");
        assertThat(rulesEngine.evaluateRules(List.of(rule), data)).isEqualTo(UnderwritingRule.ActionType.ACCEPT);
    }

    private UnderwritingRule createRule(String json, UnderwritingRule.ActionType action) {
        UnderwritingRule rule = new UnderwritingRule();
        rule.setId(UUID.randomUUID());
        rule.setRuleName("Test");
        rule.setConditionJson(json);
        rule.setActionType(action);
        rule.setActive(true);
        return rule;
    }
}
