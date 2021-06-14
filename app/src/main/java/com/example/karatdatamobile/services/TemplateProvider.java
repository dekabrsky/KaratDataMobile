package com.example.karatdatamobile.services;

import android.content.res.Resources;

import com.example.karatdatamobile.interfaces.ITemplateProvider;
import com.example.karatdatamobile.R;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//todo
public class TemplateProvider implements ITemplateProvider {

    private static final Pattern userDataPattern = Pattern.compile("^\\$\\(\\w+\\)$");

    private final Resources resources;

    public TemplateProvider(Resources resources) {
        this.resources = resources;
    }

    @Override
    public ArrayList<String> getTemplateNames() {
        ArrayList<String> templateNames = new ArrayList<>();
        templateNames.add("Базовый шаблон");
        return templateNames;
    }

    @Override
    public ArrayList<String> getUserFieldNames(String templateName) throws IOException {
        ArrayList<String> userFieldNames = new ArrayList<>();

        InputStream template = resources.openRawResource(R.raw.template);

        Workbook workbook = new XSSFWorkbook(template);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            for (Cell cell : row) {

                String cellValue = cell.getStringCellValue();

                Matcher userDataMatcher = userDataPattern.matcher(cellValue);
                if (userDataMatcher.find()) {
                    String paramName = userDataMatcher.group()
                            .replace("$", "")
                            .replace("(", "")
                            .replace(")", "");
                    userFieldNames.add(paramName);
                }
            }
        }

        return userFieldNames;
    }

    @Override
    public InputStream createEmptyReport(String templateName) {
        return resources.openRawResource(R.raw.template);
    }
}
