package com.example.karatdatamobile.Models;

import java.util.ArrayList;
import java.util.Collections;

public class ArchivesConfig {
    private final ArrayList<String> titles = new ArrayList<>();

    public ArchivesConfig(String response) {
        String[] bytes = response.split(" ");
        for (String aByte : bytes) {
            char code = aByte.charAt(0);
            if (code == 'd'){
                char subCode = aByte.charAt(1);
                titles.add(RecordTitles.cfgSubCodeToTitle.get(subCode));
            }
            else if (RecordTitles.cfgCodeToTitle.containsKey(code))
                titles.add(RecordTitles.cfgCodeToTitle.get(code));
            else if (code == 'f') break;
        }
    }

    public ArrayList<String> getTitles(){
        return titles;
    }
}
