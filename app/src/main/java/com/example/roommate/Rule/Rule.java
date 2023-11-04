package com.example.roommate.Rule;

import com.example.roommate.dday.Dday;

public class Rule implements Comparable<Rule> {
    private String roomCode;
    private String rule;
    private String ruleDetail;
    private String type;

    public Rule() {}

    public Rule(String roomCode, String rule, String ruleDetail, String type) {
        this.roomCode = roomCode;
        this.rule = rule;
        this.ruleDetail = ruleDetail;
        this.type = type;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRuleDetail() {
        return ruleDetail;
    }

    public void setRuleDetail(String ruleDetail) {
        this.ruleDetail = ruleDetail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Rule rule) {
        return this.rule.compareTo(rule.rule);
    }
}
