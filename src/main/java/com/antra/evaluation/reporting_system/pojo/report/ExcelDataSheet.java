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

    private String title; // sheet name
    private List<ExcelDataHeader> headers; //headers
    private List<List<Object>> dataRows; //content?????? Why use Object?

}
