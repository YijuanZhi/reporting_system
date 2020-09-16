package com.antra.evaluation.reporting_system.pojo.report;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.time.LocalDateTime;

@Setter
@Getter
public class ExcelFile {
    // TODO: 9/15/20 because we have convert the excel data to files, we only need the file info(meta) here

    // metadata could be generatedTime, filename, file size or fields
    // fields
    private String fileId;
    private String filename;
    private String submitter;
    private String description;
    private File file;
    private String fileSize;
    private LocalDateTime generatedTime;
}
