package com.ebay.assignment.creditlimittracker.datainput;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component("prnStreamFactory")
class PrnResourceStreamFactory extends ResourceStreamFactory {

    PrnResourceStreamFactory(@Value("classpath:data/Workbook2.prn") Resource resource) {
        super(resource);
    }

    @Override
    String getCharSet() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public String getSource() {
        return "prn-file";
    }
}
