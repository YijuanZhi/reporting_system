package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ExcelService {
    InputStream getExcelBodyById(String id);

    InputStream getMultiExcelBodyById(List<String> idList) throws IOException;

    ExcelResponse getExcelInfoById(String id);

    ExcelResponse createAndSaveFile(ExcelRequest request) throws IOException;

    ExcelResponse createAndSaveMultiSheetFile(MultiSheetExcelRequest request) throws IOException;

    List<ExcelResponse> listAllExcel();
    
    ExcelResponse deleteFileById(String id);
}
