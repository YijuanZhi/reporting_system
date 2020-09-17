package com.antra.evaluation.reporting_system.pojo.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExcelRequest {

    private String description;
    private List<String> headers; //column names
    private List<List<String>> data; //column content
    private String submitter;

}
