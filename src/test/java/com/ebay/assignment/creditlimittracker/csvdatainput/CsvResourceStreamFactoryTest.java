package com.ebay.assignment.creditlimittracker.csvdatainput;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@ExtendWith(MockitoExtension.class)
class CsvResourceStreamFactoryTest {

    private CsvResourceStreamFactory factory;

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        factory = new CsvResourceStreamFactory(new ByteArrayResource("Test data".getBytes(StandardCharsets.UTF_8.name())));
    }

    @Test
    void getReaderTest() throws IOException {
        final InputStreamReader reader = factory.reader();

        Assertions.assertEquals("Test data", new BufferedReader(reader).readLine());
        Assertions.assertEquals("UTF8", reader.getEncoding());
    }

    @Test
    void getSourceTest() {
        Assertions.assertEquals("csv-file", factory.getSource());
    }
}