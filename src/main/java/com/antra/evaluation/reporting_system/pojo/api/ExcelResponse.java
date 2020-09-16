package com.antra.evaluation.reporting_system.pojo.api;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
public class ExcelResponse {

    //fields
    private String fileId;
    private String filename;
    private String submitter;
    private String description;
    private File file;
}
