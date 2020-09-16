package com.antra.evaluation.reporting_system.repo;

import com.antra.evaluation.reporting_system.pojo.report.ExcelFile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ExcelRepositoryImpl implements ExcelRepository {
    // TODO: 9/15/20 manipulate the map for this class instead of database stuff

    // used to save metadata
    // metadata could be generatedTime, filename, file size or fields
    // i will use the current time as the id
    private final Map<String, ExcelFile> excelData;
//    private volatile long id;

    public ExcelRepositoryImpl() {
        this.excelData = new ConcurrentHashMap<>();
//        this.id = 0;
    }

    @Override
    public Optional<ExcelFile> getFileById(String id) {
        return Optional.ofNullable(excelData.get(id));
    }

    @Override
    public ExcelFile saveFile(ExcelFile file) {
        /*LocalDateTime currentTime = LocalDateTime.now();
        String timeId = currentTime.toString();
        file.setFileId(timeId);*/

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

