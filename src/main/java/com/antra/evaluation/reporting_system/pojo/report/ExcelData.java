package com.antra.evaluation.reporting_system.pojo.report;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ExcelData {

    /**
     * Data Stucture:
     *
     * ExcelData
     *      - title (for filename)
     *      - generatedTime
     *      - ExcelDataSheet(s)
     */

    private String title; //filename
    private LocalDateTime generatedTime;
    private List<ExcelDataSheet> sheets;

}
