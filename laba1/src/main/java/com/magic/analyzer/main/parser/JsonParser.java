/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.magic.analyzer.main.parser;

import com.fasterxml.jackson.databind.*;
import com.magic.analyzer.main.model.*;
import java.io.*;

/**
 *
 * @author zubbo
 */
public class JsonParser extends BaseParser {
   private final ObjectMapper objectMapper; 

   public JsonParser() {
       this.objectMapper = new ObjectMapper();
   }
   
   @Override
    public Mission parse(File file) throws IOException {
        JsonNode root = objectMapper.readTree(file);
        
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
            if (sorcerersNode.isArray()) {
                for (JsonNode sorcererNode : sorcerersNode) {
                    Sorcerer sorcerer = new Sorcerer();
                    sorcerer.setName(getString(sorcererNode, "name"));
                    sorcerer.setRank(getRank(sorcererNode, "rank"));
                    mission.addSorcerer(sorcerer);
                }
            }
        }
        
        if (root.has("techniques")) {
            JsonNode techniquesNode = root.get("techniques");
            if (techniquesNode.isArray()) {
                for (JsonNode techniqueNode : techniquesNode) {
                    Technique technique = new Technique();
                    technique.setName(getString(techniqueNode, "name"));
                    technique.setType(getTechniqueType(techniqueNode, "type"));
                    technique.setDamage(getLong(techniqueNode, "damage"));
                    
                    String ownerName = getString(techniqueNode, "owner");
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
        }
        
        return mission;
    }
}
