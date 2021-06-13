package com.example.karatdatamobile.main;

class ReportsState {
    private String nameReport;
    private String formatReport;

    public ReportsState(String nameReport, String formatReport){

        this.nameReport=nameReport;
        this.formatReport=formatReport;
    }

    public String getName() {
        return this.nameReport;
    }

    public void setName(String nameReport) {
        this.nameReport = nameReport;
    }

    public String getCapital() {
        return this.formatReport;
    }

    public void setCapital(String formatReport) {
        this.formatReport = formatReport;
    }
}
