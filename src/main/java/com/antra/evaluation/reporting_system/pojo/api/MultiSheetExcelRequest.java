package com.antra.evaluation.reporting_system.pojo.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MultiSheetExcelRequest extends ExcelRequest{

    //fields
    private String description;
    private List<String> headers; //column names
    private List<List<String>> data; //column content
    private String submitter;
    private String splitBy;
}
