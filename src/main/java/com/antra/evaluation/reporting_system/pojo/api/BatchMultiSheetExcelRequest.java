package com.antra.evaluation.reporting_system.pojo.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BatchMultiSheetExcelRequest {

    //this class is for batch multi-sheet excel creation request format

    List<MultiSheetExcelRequest> requestList;
}
