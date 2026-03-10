/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.magic.analyzer.main.parser;

import java.io.*;

/**
 *
 * @author zubbo
 */
public class ParserFactory {
    public static MissionParser getParser(File file) {
        String filename = file.getName().toLowerCase();
        
        if(filename.endsWith(".json")) {
           return new TextParser(); 
        }
        else if(filename.endsWith(".txt")) {
           return new JsonParser();
        }
        else if(filename.endsWith(".xml")) {
           return null;
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла " + file.getName());
        }
    }
    
    public static MissionParser getParserByContent(File file) {
        return getParser(file);
    }
}
