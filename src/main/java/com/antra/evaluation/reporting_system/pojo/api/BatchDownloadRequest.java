package com.antra.evaluation.reporting_system.pojo.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class BatchDownloadRequest {

    //this class is for batch download request format

    private List<String> idList;
}
