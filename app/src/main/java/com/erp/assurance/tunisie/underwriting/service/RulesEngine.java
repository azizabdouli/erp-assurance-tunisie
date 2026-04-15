package com.erp.assurance.tunisie.underwriting.service;

import com.erp.assurance.tunisie.underwriting.entity.UnderwritingRule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RulesEngine {

    private final ObjectMapper objectMapper;

    public UnderwritingRule.ActionType evaluateRules(List<UnderwritingRule> rules, Map<String, Object> riskData) {
        for (UnderwritingRule rule : rules) {
            if (!rule.isActive()) continue;
            try {
                Map<String, Object> condition = objectMapper.readValue(
                        rule.getConditionJson(), new TypeReference<>() {});
                if (evaluateCondition(condition, riskData)) {
                    log.info("Rule matched: {}", rule.getRuleName());
                    return rule.getActionType();
                }
            } catch (Exception e) {
                log.warn("Error evaluating rule {}: {}", rule.getRuleName(), e.getMessage());
            }
        }
        return UnderwritingRule.ActionType.ACCEPT;
    }

    @SuppressWarnings("unchecked")
    private boolean evaluateCondition(Map<String, Object> condition, Map<String, Object> data) {
        if (condition.containsKey("AND")) {
            List<Map<String, Object>> subConditions = (List<Map<String, Object>>) condition.get("AND");
            return subConditions.stream().allMatch(c -> evaluateCondition(c, data));
        }
        if (condition.containsKey("OR")) {
            List<Map<String, Object>> subConditions = (List<Map<String, Object>>) condition.get("OR");
            return subConditions.stream().anyMatch(c -> evaluateCondition(c, data));
        }
        if (condition.containsKey("NOT")) {
            Map<String, Object> subCondition = (Map<String, Object>) condition.get("NOT");
            return !evaluateCondition(subCondition, data);
        }

        String field = (String) condition.get("field");
        String operator = (String) condition.get("operator");
        Object expected = condition.get("value");
        Object actual = data.get(field);

        if (actual == null) return false;

        return switch (operator) {
            case "eq" -> actual.toString().equals(expected.toString());
            case "gt" -> Double.parseDouble(actual.toString()) > Double.parseDouble(expected.toString());
            case "lt" -> Double.parseDouble(actual.toString()) < Double.parseDouble(expected.toString());
            case "gte" -> Double.parseDouble(actual.toString()) >= Double.parseDouble(expected.toString());
            case "lte" -> Double.parseDouble(actual.toString()) <= Double.parseDouble(expected.toString());
            default -> false;
        };
    }
}
