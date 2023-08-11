package com.zbh.tce.controller;

import com.zbh.tce.dto.DataTablesOutput;
import org.springframework.data.domain.Page;

class BaseController {
    protected <T> DataTablesOutput<T> createDataTableOutput(Page<T> data, Long totalCount) {
        DataTablesOutput<T> dataTableOutput = new DataTablesOutput<T>();
        dataTableOutput.setRecordsTotal(totalCount);
        dataTableOutput.setRecordsFiltered(data.getContent().size());
        dataTableOutput.setData(data.getContent());
        return dataTableOutput;
    }
}
