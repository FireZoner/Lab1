/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.magic.analyzer.main.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.magic.analyzer.main.model.*;
import com.magic.analyzer.main.model.enums.*;

/**
 *
 * @author zubbo
 */
public abstract class BaseParser implements MissionParser {
    
    protected String getString(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asText() : null;
    }

    protected long getLong(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value != null && !value.isNull() ? value.asLong() : 0;
    }

    protected Outcome getOutcome(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return Outcome.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный Outcome: " + value);
            return null;
        }
    }
    
    protected ThreatLevel getThreatLevel(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return ThreatLevel.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный ThreatLevel: " + value);
            return null;
        }
    }
    
    protected Rank getRank(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return Rank.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный Rank: " + value);
            return null;
        }
    }
    
    protected TechniqueType getTechniqueType(JsonNode node, String field) {
        String value = getString(node, field);
        if (value == null) return null;
        
        try {
            return TechniqueType.valueOf(value);
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный TechniqueType: " + value);
            return null;
        }
    }
    
    protected Sorcerer findSorcererByName(Mission mission, String name) {
        if (name == null) return null;
        
        for (Sorcerer s : mission.getSorcerers()) {
            if (name.equals(s.getName())) {
                return s;
            }
        }
        return null;
    }
}
