package com.example.karatdatamobile.Services;

import android.content.res.Resources;

import com.example.karatdatamobile.Interfaces.ITemplateProvider;
import com.example.karatdatamobile.R;

import java.io.InputStream;
import java.util.ArrayList;

//todo
public class TemplateProvider implements ITemplateProvider {

    private final Resources resources;

    public TemplateProvider(Resources resources) {
        this.resources = resources;
    }

    @Override
    public ArrayList<String> getTemplateNames() {
        return null;
    }

    @Override
    public ArrayList<String> getUserFieldNames(String templateName) {
        return null;
    }

    @Override
    public InputStream createEmptyReport(String templateName) {
        return resources.openRawResource(R.raw.template);
    }
}
