package com.antra.evaluation.reporting_system;

import com.antra.evaluation.reporting_system.endpoint.ExcelGenerationController;
import com.antra.evaluation.reporting_system.pojo.api.ExcelResponse;
import com.antra.evaluation.reporting_system.service.ExcelService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.mockito.ArgumentMatchers.*;

public class APITest {
    @Mock
    ExcelService excelService;

    @BeforeEach
    public void configMock() {
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.standaloneSetup(new ExcelGenerationController(excelService));
    }

    @Test
    //Test for single-sheet excel generation api ----> /excel POST
    public void testExcelGeneration2() throws IOException {
        ExcelResponse excelResponse = new ExcelResponse();
        excelResponse.setFileId("YIKES TEST");
        String jsonString = "{\"description\": \"this is dummy description.\", \"headers\":[\"Name\",\"Age\"], \"data\":[[\"Teresa\",\"5\"],[\"Daniel\",\"1\"]], \"submitter\": \"Mr.Dummy\" }";

        Mockito.when(excelService.createAndSaveFile(any())).thenReturn(excelResponse);

        given().accept("application/json").contentType(ContentType.JSON)
                .body(jsonString)
                .post("/excel").peek()
                .then().assertThat()
                .statusCode(200)
                .body("fileId", Matchers.notNullValue());
    }

    @Test
    //Test for multi-sheet excel generation api ----> /excel/auto POST
    public void testMultiSheetExcelGeneration() throws IOException {
        ExcelResponse excelResponse = new ExcelResponse();
        excelResponse.setFileId("YIKES MultiSheet TEST");
        String jsonString = "{\"description\": \"this is dummy description.\", \"headers\":[\"Name\",\"Age\"], \"data\":[[\"Teresa\",\"5\"],[\"Daniel\",\"1\"]], \"submitter\": \"Mr.Dummy\", \"splitBy\": \"Age\" }";

        Mockito.when(excelService.createAndSaveMultiSheetFile(any())).thenReturn(excelResponse);

        given().accept("application/json").contentType(ContentType.JSON)
                .body(jsonString)
                .post("/excel/auto").peek()
                .then().assertThat()
                .statusCode(200)
                .body("fileId", Matchers.notNullValue());
    }


    /*
    * ==================================================================================
    *
    * Because the create batch single-sheet api and create batch multi-sheet api
    * are based on previous 2 tested api, so I will not test these api anymore.
    *
    * ==================================================================================
    */




    @Test
    //Test for single file download ----> /excel/{id}/content GET
    public void testFileDownload() throws FileNotFoundException {
        Mockito.when(excelService.getExcelBodyById(anyString())).thenReturn(new FileInputStream("temp.xlsx"));
        Mockito.when(excelService.getExcelInfoById(anyString())).thenReturn(new ExcelResponse());
        given().accept("application/json").get("/excel/{id}/content", "dummyId123").peek().
                then().assertThat()
                .statusCode(200);
    }

    @Test
    //Test for batch files download as a compressed zip file ----> /excel/batchRetrieve POST
    public void testBatchFilesDownload() throws IOException {
        Mockito.when(excelService.getMultiExcelBodyById(any())).thenReturn(new FileInputStream("temp.zip"));
        String jsonString = "{ \"idList\": [\"dummyID1\", \"dummyID2\", \"asdfghjkl\"] }";
        given().accept("application/json").contentType(ContentType.JSON)
                .body(jsonString)
                .post("/excel/batchRetrieve").peek()
                .then().assertThat()
                .statusCode(200);
    }

    @Test
    //Test for getting all files meta info ----> /excel GET
    public void testListFiles(){
        List<ExcelResponse> responses = new ArrayList<>();
        Mockito.when(excelService.listAllExcel()).thenReturn(responses);
        given().accept("application/json").get("/excel").peek().
                then().assertThat()
                .statusCode(200);
    }



    @Test
    //Test for deleting one single file ----> /excel/{id} DELETE
    public void testDeleteFile(){
        Mockito.when(excelService.deleteFileById(anyString())).thenReturn(new ExcelResponse());
        given().accept("application/json").delete("/excel/{id}", "dummyId123").peek().
                then().assertThat()
                .statusCode(200);
    }
}
