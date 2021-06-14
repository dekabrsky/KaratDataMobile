package com.example.karatdatamobile.Interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface ITemplateProvider {

    ArrayList<String> getTemplateNames();

    ArrayList<String> getUserFieldNames(String templateName) throws IOException;

    InputStream createEmptyReport(String templateName);
}
