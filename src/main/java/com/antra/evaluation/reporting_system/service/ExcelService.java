package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;

import java.io.IOException;
import java.io.InputStream;

public interface ExcelService {
    InputStream getExcelBodyById(String id);

    ExcelResponse createAndSaveFile(ExcelRequest request) throws IOException;

    ExcelResponse createAndSaveMultiSheetFile(MultiSheetExcelRequest request) throws IOException;
}
