package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class ExcelRepositoryImpl implements ExcelRepository {
    // used to save metadata
    // metadata could be generatedTime, filename, file size and fields

    private final Map<String, ExcelFile> excelData;

    public ExcelRepositoryImpl() {
        this.excelData = new ConcurrentHashMap<>();
    }

    @Override
    public Optional<ExcelFile> getFileById(String id) {
        return Optional.ofNullable(excelData.get(id));
    }

    @Override
    public ExcelFile saveFile(ExcelFile file) {
        String fileId = file.getFileId();
        excelData.put(fileId, file);
        return file;
    }

    @Override
    public ExcelFile deleteFile(String id) { //only remove the ExcelFile from the map could do the job
        if(!excelData.containsKey(id)) return null;
        return excelData.remove(id);
    }

    @Override
    public List<ExcelFile> getFiles() {
        List<ExcelFile> ret = new ArrayList<>();
        for(String key : excelData.keySet()) {
            ret.add(excelData.get(key));
        }
        return ret;
    }
}

