package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.report.*;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExcelServiceImpl implements ExcelService {
    // it is in service layer we should put business logic!

    // === constants ===
    private final ExcelRepository excelRepository;
    private final ExcelGenerationService excelGenerationService;

    // === constructor ===
    @Autowired
    public ExcelServiceImpl(ExcelRepository excelRepository, ExcelGenerationService excelGenerationService) {
        this.excelRepository = excelRepository;
        this.excelGenerationService = excelGenerationService;
    }

    // === public methods ===
    @Override
    public InputStream getExcelBodyById(String id) {
        // TODO: 9/15/20 find the file and return it, we should read the filename from the Optional object

        Optional<ExcelFile> fileInfo = excelRepository.getFileById(id);

        if (fileInfo.isPresent()) {
            File file = new File(fileInfo.get().getFilename() + ".xlsx");
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ExcelResponse createAndSaveFile(ExcelRequest request) throws IOException {
        LocalDateTime currentTime = LocalDateTime.now();
        String timeId = currentTime.toString();
        String filename = request.getSubmitter()/* + timeId*/;

        //create ExcelData and save it
        ExcelData excelData = new ExcelData();
        excelData.setTitle(filename);
        excelData.setGeneratedTime(currentTime);

        //this is for single view excel output
        //create ExcelSheet for the ExcelData
        List<ExcelDataSheet> sheets = new ArrayList<>();
        ExcelDataSheet excelDataSheet = new ExcelDataSheet();
        excelDataSheet.setTitle(filename);
        excelData.setSheets(sheets);
        sheets.add(excelDataSheet);

        //convert headers to ExcelDataHeader ....... sign
        List<String> headers = request.getHeaders();
        List<ExcelDataHeader> dataHeaders = new ArrayList<>();
        for(String curHeader : headers) {
            ExcelDataHeader dataHeader = new ExcelDataHeader();
            dataHeader.setName(curHeader);
            dataHeader.setType(ExcelDataType.STRING);
            dataHeader.setWidth(0);
            dataHeaders.add(dataHeader);
        }

        //convert string data to Object data ........ YIKES
        List<List<String>> data = request.getData();
        List<List<Object>> dataRows = new ArrayList<>();
        for(List<String> curRow : data) {
            List<Object> curDataRow = new ArrayList<>(curRow);
            dataRows.add(curDataRow);
        }

        excelDataSheet.setHeaders(dataHeaders);
        excelDataSheet.setDataRows(dataRows);

        File file = excelGenerationService.generateExcelReport(excelData);

        // TODO: 9/16/20 later on, modifies this method to accept multiple sheets




        //create ExcelFile and save it
        ExcelFile excelFile = new ExcelFile();
        excelFile.setFileId(timeId);
        excelFile.setFilename(filename);
        excelFile.setSubmitter(request.getSubmitter());
        excelFile.setDescription(request.getDescription());
        excelFile.setFile(file);
        excelRepository.saveFile(excelFile);

        //create ExcelResponse and return it
        ExcelResponse excelResponse = new ExcelResponse();
        excelResponse.setFileId(timeId);
        excelResponse.setFilename(filename);
        excelResponse.setSubmitter(request.getSubmitter());
        excelResponse.setDescription(request.getDescription());
        excelResponse.setFile(file);

        return excelResponse;
    }
}
