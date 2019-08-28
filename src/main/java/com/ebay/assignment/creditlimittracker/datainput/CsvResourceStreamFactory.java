package com.ebay.assignment.creditlimittracker.datainput;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("csvStreamFactory")
class CsvResourceStreamFactory extends ResourceStreamFactory {

    CsvResourceStreamFactory(@Value("classpath:data/Workbook2.csv") Resource resource) {
        super(resource);
    }

    @Override
    String getCharSet() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public String getSource() {
        return "csv-file";
    }
}
