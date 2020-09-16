package com.antra.evaluation.reporting_system.pojo.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExcelData {
    // TODO: 9/15/20 add validation

    /**
     * Data Stucture:
     *
     * ExcelData
     *      - title (for filename)
     *      - generatedTime
     *      - ExcelDataSheet(s)
     */

    private String title; //submitter + LocalDateTime
    private LocalDateTime generatedTime;
    private List<ExcelDataSheet> sheets;

    /*public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(LocalDateTime generatedTime) {
        this.generatedTime = generatedTime;
    }

    public List<ExcelDataSheet> getSheets() {
        return sheets;
    }

    public void setSheets(List<ExcelDataSheet> sheets) {
        this.sheets = sheets;
    }*/
}
