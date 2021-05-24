package com.example.karatdatamobile.Interfaces;

import java.io.InputStream;
import java.util.ArrayList;

public interface ITemplateProvider {

    ArrayList<String> getTemplateNames();

    ArrayList<String> getUserFieldNames(String templateName);

    InputStream createEmptyReport(String templateName);
}
