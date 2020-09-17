package com.antra.evaluation.reporting_system.endpoint;

import com.antra.evaluation.reporting_system.pojo.api.*;
import com.antra.evaluation.reporting_system.service.ExcelService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ExcelGenerationController {

    // === constants ===
    private final ExcelService excelService;

    // === constructor
    @Autowired
    public ExcelGenerationController(ExcelService excelService) {
        this.excelService = excelService;
    }

    // === controller methods ===
    @PostMapping("/excel")
    @ApiOperation("Generate Excel")
    public ResponseEntity<ExcelResponse> createExcel(@RequestBody @Validated ExcelRequest request) throws IOException {
        ExcelResponse response = excelService.createAndSaveFile(request); //potential exception

        log.info("Create and save new single-sheet excel file. ID: " + response.getFileId() +
                ". File name: " +response.getFilename() + ".xlsx");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/excel/auto")
    @ApiOperation("Generate Multi-Sheet Excel Using Split field")
    public ResponseEntity<ExcelResponse> createMultiSheetExcel(@RequestBody @Validated MultiSheetExcelRequest request) throws IOException {
        var response = excelService.createAndSaveMultiSheetFile(request); //potential exception

        log.info("Create and save new multi-sheet excel file. ID: " + response.getFileId() +
                ". File name: " +response.getFilename()  + ".xlsx");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/excel/batchCreate")
    @ApiOperation("accept multiple data at the same time and generate multiple single-sheet excel at once")
    public ResponseEntity<List<ExcelResponse>> createBatchExcel
            (@RequestBody BatchExcelRequest request) throws IOException
    {
        List<ExcelRequest> requestList = request.getRequestList();
        List<ExcelResponse> responseList = new ArrayList<>();
        for(ExcelRequest currentRequest : requestList){
            var response = excelService.createAndSaveFile(currentRequest); //potential exception

            log.info("Create and save new single-sheet excel file. ID: " + response.getFileId() +
                    ". File name: " + response.getFilename()  + ".xlsx");

            responseList.add(response);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PostMapping("/excel/auto/batchCreate")
    @ApiOperation("accept multiple data at the same time and generate multiple multi-sheet excel at once")
    public ResponseEntity<List<ExcelResponse>> createBatchMultiSheetExcel
            (@RequestBody @Validated BatchMultiSheetExcelRequest request) throws IOException
    {
        List<MultiSheetExcelRequest> requestList = request.getRequestList();
        List<ExcelResponse> responseList = new ArrayList<>();
        for(MultiSheetExcelRequest currentRequest : requestList){
            var response = excelService.createAndSaveMultiSheetFile(currentRequest); //potential exception

            log.info("Create and save new multi-sheet excel file. ID: " + response.getFileId() +
                    ". File name: " + response.getFilename()  + ".xlsx");

            responseList.add(response);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @GetMapping("/excel")
    @ApiOperation("List all existing files")
    public ResponseEntity<List<ExcelResponse>> listExcels() {
        var response = excelService.listAllExcel();

        log.info("Listing all the files.");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/excel/{id}/content")
    public void downloadExcel(@PathVariable String id, HttpServletResponse response) throws IOException {
        var excelResponse = excelService.getExcelInfoById(id);
        if(excelResponse == null) throw new FileNotFoundException();

        InputStream fis = excelService.getExcelBodyById(id);

        log.info("Downloading file. ID: " +excelResponse.getFileId()
                + ". File name: " + excelResponse.getFilename()  + ".xlsx");

        response.setHeader("Content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=" + excelResponse.getFilename() +".xlsx");
        FileCopyUtils.copy(fis, response.getOutputStream());
    }

    @PostMapping("/excel/batchRetrieve")
    @ApiOperation("download multiple files in one request. Downloaded File should be in zip format.")
    public void downloadMultiExcel(@RequestBody BatchDownloadRequest request, HttpServletResponse response) throws IOException {
        InputStream fis = excelService.getMultiExcelBodyById(request.getIdList());

        log.info("Downloading multiple files. IDs: " + request.getIdList());

        response.setHeader("Content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=multiExcelCompressed.zip");
        FileCopyUtils.copy(fis, response.getOutputStream());
    }

    @DeleteMapping("/excel/{id}")
    public ResponseEntity<ExcelResponse> deleteExcel(@PathVariable String id) {
        var excelResponse = excelService.deleteFileById(id);

        log.info("Deleting file. ID: " +excelResponse.getFileId()
                + ". File name: " + excelResponse.getFilename()  + ".xlsx");

        return new ResponseEntity<>(excelResponse, HttpStatus.OK);
    }




    // === Exception handling ===

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandlerFileNotFound(Exception ex) {
        ErrorResponse error = new ErrorResponse();
        error.setErrorCode(HttpStatus.NOT_FOUND.value());
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
// Log - done
// Exception handling - done
// Validation - done
