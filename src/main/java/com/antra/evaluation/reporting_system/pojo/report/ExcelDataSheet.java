package com.antra.evaluation.reporting_system.pojo.report;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelDataSheet {
    // one sheet create one page of excel page,
    // if parent ExcelData has more than one ExcelDataSheet, it create a excel file with multiple pages

    /**
     *      ExcelDataSheet
     *              - title (required)
     *              - headers (ExcelDataHeader)
     *                   - name
     *                   - width
     *                   - type
     *              - dataRows (content)
     *                   - List of objects/values
     */

    private String title; // TODO: 9/16/20 this is the name the sheet page, change it to something useful
    private List<ExcelDataHeader> headers; //headers
    private List<List<Object>> dataRows; //content?????? Why use Object?

    /*public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ExcelDataHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<ExcelDataHeader> headers) {
        this.headers = headers;
    }

    public List<List<Object>> getDataRows() {
        return dataRows;
    }

    public void setDataRows(List<List<Object>> dataRows) {
        this.dataRows = dataRows;
    }*/
}
