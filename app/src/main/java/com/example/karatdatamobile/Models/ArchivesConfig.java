package com.example.karatdatamobile.Models;

import java.util.ArrayList;

public class ArchivesConfig {
    private final ArrayList<String> titles = new ArrayList<>();

    public ArchivesConfig(String response) {
        String[] bytes = response.split(" ");
        for (String aByte : bytes) {
            char code = aByte.charAt(0);
            if (code == 'd'){
                char subCode = aByte.charAt(1);
                titles.add(Titles.cfgSubCodeToTitle.get(subCode));
            }
            else if (Titles.cfgCodeToTitle.containsKey(code))
                titles.add(Titles.cfgCodeToTitle.get(code));
            else if (code == 'f') break;
        }
    }

    public ArrayList<String> getTitles(){
        return titles;
    }
}
