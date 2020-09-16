package com.antra.evaluation.reporting_system.pojo.api;

import com.antra.evaluation.reporting_system.pojo.report.ExcelData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelRequest {
    // TODO: 9/15/20 add validation

    private String description;
    private List<String> headers; //column names
    private List<List<String>> data; //column content
    private String submitter;

    /*public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }*/
}
