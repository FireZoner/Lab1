/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.magic.analyzer.main.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.magic.analyzer.main.model.*;
import java.io.*;

/**
 *
 * @author zubbo
 */
public class XmlParser extends BaseParser {
    private final XmlMapper xmlMapper;
    
    public XmlParser() {
        this.xmlMapper = new XmlMapper();
    }
    
    @Override
    public Mission parse(File file) throws IOException {
        JsonNode root = xmlMapper.readTree(file);
        
        Mission mission = new Mission();
        
        mission.setMissionId(getString(root, "missionId"));
        mission.setDate(getString(root, "date"));
        mission.setLocation(getString(root, "location"));
        mission.setOutcome(getOutcome(root, "outcome"));
        mission.setDamageCost(getLong(root, "damageCost"));
        
        String comment = getString(root, "comment");
        if (comment == null) {
            comment = getString(root, "note");
        }
        mission.setComment(comment);
        
        if (root.has("curse")) {
            JsonNode curseNode = root.get("curse");
            Curse curse = new Curse();
            curse.setName(getString(curseNode, "name"));
            curse.setThreatLevel(getThreatLevel(curseNode, "threatLevel"));
            mission.setCurse(curse);
        }

        if (root.has("sorcerers")) {
            JsonNode sorcerersNode = root.get("sorcerers");
            if (sorcerersNode.has("sorcerer")) {
                JsonNode sorcererNode = sorcerersNode.get("sorcerer");
                if (sorcererNode.isArray()) {
                    for (JsonNode sNode : sorcererNode) {
                        parseSorcerer(sNode, mission);
                    }
                } else {
                    parseSorcerer(sorcererNode, mission);
                }
            }
        }
        
        if (root.has("techniques")) {
            JsonNode techniquesNode = root.get("techniques");
            if (techniquesNode.has("technique")) {
                JsonNode techniqueNode = techniquesNode.get("technique");
                if (techniqueNode.isArray()) {
                    for (JsonNode tNode : techniqueNode) {
                        parseTechnique(tNode, mission);
                    }
                } else {
                    parseTechnique(techniqueNode, mission);
                }
            }
        }
        
        return mission;
    }

    private void parseSorcerer(JsonNode node, Mission mission) {
        Sorcerer sorcerer = new Sorcerer();
        sorcerer.setName(getString(node, "name"));
        sorcerer.setRank(getRank(node, "rank"));
        mission.addSorcerer(sorcerer);
    }
    
    private void parseTechnique(JsonNode node, Mission mission) {
        Technique technique = new Technique();
        technique.setName(getString(node, "name"));
        technique.setType(getTechniqueType(node, "type"));
        technique.setDamage(getLong(node, "damage"));
        
        String ownerName = getString(node, "owner");
        if (ownerName != null) {
            Sorcerer owner = findSorcererByName(mission, ownerName);
            if (owner != null) {
                technique.setOwner(owner);
            } else {
                technique.setOwner(new Sorcerer(ownerName, null));
            }
        }
        
        mission.addTechnique(technique);
    }
}
