package com.antra.evaluation.reporting_system.endpoint;

import com.antra.evaluation.reporting_system.pojo.api.ExcelRequest;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.pojo.api.MultiSheetExcelRequest;
import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ExcelGenerationController {
    // TODO: 9/15/20 for this file we should not retrieve the ExcelData, we should only get the ExcelFile info

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
        // TODO: 9/16/20 first we need to check if the splitBy field exists in headers field, if not throw exception????
        var response = excelService.createAndSaveMultiSheetFile(request); //potential exception

        log.info("Create and save new multi-sheet excel file. ID: " + response.getFileId() +
                ". File name: " +response.getFilename()  + ".xlsx");

        return new ResponseEntity<>(response, HttpStatus.OK);
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
        InputStream fis = excelService.getExcelBodyById(id);
        var excelResponse = excelService.getExcelInfoById(id);

        log.info("Downloading file. ID: " +excelResponse.getFileId()
                + ". File name: " + excelResponse.getFilename()  + ".xlsx");

        response.setHeader("Content-Type","application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=" + excelResponse.getFilename() +".xlsx");
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
    // TODO: 9/15/20 Exception handling
}
// Log - done
// Exception handling
// Validation - done
