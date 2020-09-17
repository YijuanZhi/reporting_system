package com.antra.evaluation.reporting_system.pojo.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BatchExcelRequest {

    //this class is for batch single-sheet excel creation request format

    private List<ExcelRequest> requestList;
}
