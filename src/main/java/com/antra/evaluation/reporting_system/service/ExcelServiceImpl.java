package com.antra.evaluation.reporting_system.service;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.*;
import com.antra.evaluation.reporting_system.repo.ExcelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
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
        Optional<ExcelFile> fileInfo = excelRepository.getFileById(id);

        if (fileInfo.isPresent()) {
            File file = fileInfo.get().getFile();
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public InputStream getMultiExcelBodyById(List<String> idList) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("multiExcelCompressed.zip"));


        for (String id : idList) {
            Optional<ExcelFile> fileInfo = excelRepository.getFileById(id);

            if(fileInfo.isPresent()){
                File fileToZip = fileInfo.get().getFile();
                try{
                    FileInputStream fis = new FileInputStream(fileToZip);
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        zipOut.finish();
        zipOut.close();
        return new FileInputStream("multiExcelCompressed.zip");
    }

    @Override
    public ExcelResponse getExcelInfoById(String id) {
        Optional<ExcelFile> fileInfo = excelRepository.getFileById(id);
        if(fileInfo.isPresent()) {
            ExcelFile file = fileInfo.get();
            return convertExcelFile2Response(file);
        }
        return null;
    }


    @Override
    public ExcelResponse createAndSaveFile(ExcelRequest request) throws IOException {

        log.info("Accepting new request: " + request);

        //this is for single view excel output
        // this method only need to fill the ExcelData field ---- List<ExcelDataSheet> sheets
        ExcelData excelData = new ExcelData();

        //title --- create ExcelSheet for the ExcelData
        ExcelDataSheet excelDataSheet = new ExcelDataSheet();
        excelDataSheet.setTitle("Sheet1");

        //headers --- convert headers to ExcelDataHeader ....... sign
        List<ExcelDataHeader> dataHeaders = convertHeaders(request.getHeaders());
        excelDataSheet.setHeaders(dataHeaders);

        //dataRows --- convert string data to Object data ........ YIKES
        List<List<Object>> dataRows = convertDataRows(request.getData());
        excelDataSheet.setDataRows(dataRows);

        List<ExcelDataSheet> sheets = new ArrayList<>();
        sheets.add(excelDataSheet);
        excelData.setSheets(sheets);
        return createAndSaveHelper(excelData, request);
    }

    @Override
    public ExcelResponse createAndSaveMultiSheetFile(MultiSheetExcelRequest request) throws IOException {
        // just sort and divide the MultiSheetExcelRequest into a ExcelData
        // this method only need to fill the ExcelData field ---- List<ExcelDataSheet> sheets
        String splitBy = request.getSplitBy();
        List<String> headers = request.getHeaders();
        List<List<String>> data = request.getData();
        int index = -1;

        //find the index of the splitBy first
        for(int i = 0; i < headers.size(); ++i) {
            String cur = headers.get(i);
            if(cur.equals(splitBy)) {
                index = i;
                break;
            }
        }

        // set of all the distinct values in the splitBy field
        Set<String> set = new HashSet<>();
        for(List<String> curRow : data) set.add(curRow.get(index));


        //now lets build the ExcelDataSheets
        List<ExcelDataHeader> sheetHeaders = convertHeaders(headers);
        List<ExcelDataSheet> sheets = new ArrayList<>();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            String curVal = (String) it.next();
            ExcelDataSheet sheet = new ExcelDataSheet();
            sheet.setTitle(curVal);
            sheet.setHeaders(sheetHeaders);

            //now select the rows
            List<List<String>> strDataRows = new ArrayList<>();
            for(List<String> curRow : data) {
                if(curRow.get(index).equals(curVal))
                    strDataRows.add(curRow);
            }
            //now set the dataRows
            sheet.setDataRows(convertDataRows(strDataRows));

            //add the current ExcelDataSheet into the sheets list
            sheets.add(sheet);
        }

        //now pass the ExcelData to the helper method, return the returned value
        ExcelData excelData = new ExcelData();
        excelData.setSheets(sheets);
        return createAndSaveHelper(excelData, request);
    }

    @Override
    public List<ExcelResponse> listAllExcel() {
        List<ExcelResponse> responses = new ArrayList<>();
        List<ExcelFile> files = excelRepository.getFiles();
        for(ExcelFile file : files) {
            responses.add(convertExcelFile2Response(file));
        }
        return responses;
    }

    @Override
    public ExcelResponse deleteFileById(String id) {
        ExcelFile excelFile = excelRepository.deleteFile(id);
        return convertExcelFile2Response(excelFile);
    }


    // === private helper methods ===

    private List<ExcelDataHeader> convertHeaders(List<String> headers) {
        //convert headers to ExcelDataHeader ....... sign
        List<ExcelDataHeader> dataHeaders = new ArrayList<>();
        for(String curHeader : headers) {
            ExcelDataHeader dataHeader = new ExcelDataHeader();
            dataHeader.setName(curHeader);
            dataHeader.setType(ExcelDataType.STRING);
            dataHeader.setWidth(0);
            dataHeaders.add(dataHeader);
        }
        return dataHeaders;
    }

    private List<List<Object>> convertDataRows(List<List<String>> data) {
        //convert string data to Object data ........ YIKES
        List<List<Object>> dataRows = new ArrayList<>();
        for(List<String> curRow : data) {
            List<Object> curDataRow = new ArrayList<>(curRow);
            dataRows.add(curDataRow);
        }
        return dataRows;
    }

    private ExcelResponse convertExcelFile2Response(ExcelFile file) {
        //this method converts an ExcelFile into a ExcelResponse
        ExcelResponse excelResponse = new ExcelResponse();
        excelResponse.setFileId(file.getFileId());
        excelResponse.setFilename(file.getFilename());
        excelResponse.setSubmitter(file.getSubmitter());
        excelResponse.setDescription(file.getDescription());
        excelResponse.setFile(file.getFile());
        excelResponse.setFileSize(file.getFileSize());
        excelResponse.setGeneratedTime(file.getGeneratedTime());
        return excelResponse;
    }

    private ExcelResponse createAndSaveHelper(ExcelData excelData, ExcelRequest request) throws IOException {
        /*
         This method will help single/multi sheet creation/save methods

         It passes the completed ExcelData to the generator to produce the target file,
         saves the ExcelFile(meta), and returns ExcelResponse
        */

        //Complete the ExcelData fields: title and generatedTime
        LocalDateTime currentTime = LocalDateTime.now();
        String filename = (request.getSubmitter() + currentTime.toString())
                .replaceAll("[^a-zA-Z0-9]", "");
        String uuid = UUID.randomUUID().toString().replace("-", "");
        excelData.setTitle(filename);
        excelData.setGeneratedTime(currentTime);

        //ExcelData creation finished, now pass it to the excel generator
        File file = excelGenerationService.generateExcelReport(excelData);

        //create ExcelFile and save it
        ExcelFile excelFile = new ExcelFile();
        excelFile.setFileId(uuid);
        excelFile.setFilename(filename);
        excelFile.setSubmitter(request.getSubmitter());
        excelFile.setDescription(request.getDescription());
        excelFile.setFile(file);
        excelFile.setFileSize(file.length() + " bytes");
        excelFile.setGeneratedTime(currentTime);

        excelRepository.saveFile(excelFile);

        //create ExcelResponse and return it
        return convertExcelFile2Response(excelFile);
    }
}
