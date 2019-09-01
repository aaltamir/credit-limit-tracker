package com.ebay.assignment.creditlimittracker.csvdatainput;

import com.ebay.assignment.creditlimittracker.datainput.DataInputException;
import com.ebay.assignment.creditlimittracker.datainput.DataReceiver;
import com.ebay.assignment.creditlimittracker.datainput.StreamFactory;
import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Process "CSV" files.
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CsvDataReceiver implements DataReceiver {

    private final StreamFactory csvStreamFactory;

    /**
     * Receives the CSV data from the stream provided by the factory and convert each line to a CreditLimitInfo
     * object. This receiver ignore values with wrong format logging an error. The idea of ignoring values is not to
     * loose the rest of the information of the stream.
     * For privacy reasons the lines are never displayed in the log. The position is the only data provided plus extra
     * information telling what is wrong.
     * @return One CreditLimitInfo per line
     */
    @Override
    public List<CreditLimitInfo> receive() {

        final ArrayList<CreditLimitInfo> result =  new ArrayList<>();

        try(final BufferedReader reader = new BufferedReader(csvStreamFactory.reader())) {
            long lineNumber = 0;

            final CSVParser csvParser = CSVFormat.RFC4180.withFirstRecordAsHeader().withTrim().parse(reader);

            for (final CSVRecord record : csvParser) {
                lineNumber++;

                BigDecimal creditLimit;

                final String creditLimitStr = getStringRecord(record, "Credit Limit", lineNumber);

                if(creditLimitStr != null) {
                    try {
                        creditLimit = new BigDecimal(creditLimitStr);
                    } catch (final NumberFormatException e) {
                        log.error("Invalid credit limit value ({}) at position {}. Source: {}", creditLimitStr, lineNumber, csvStreamFactory.getSource(), e);
                        creditLimit = null;
                    }
                } else {
                    creditLimit = null;
                }

                LocalDate birthDay;

                final String birthDayStr = getStringRecord(record, "Birthday", lineNumber);

                if(birthDayStr != null) {
                    try {
                        birthDay = LocalDate.parse(birthDayStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    } catch (final DateTimeParseException e) {
                        log.error("Unexpected birth date format ({}) at position {}. Source: {}", birthDayStr, lineNumber, csvStreamFactory.getSource(), e);
                        birthDay = null;
                    }
                } else {
                    birthDay = null;
                }

                result.add(CreditLimitInfo.builder()
                        .name(getStringRecord(record, "Name", lineNumber))
                        .address(getStringRecord(record, "Address", lineNumber))
                        .postcode(getStringRecord(record, "Postcode", lineNumber))
                        .phone(getStringRecord(record,"Phone", lineNumber))
                        .creditLimit(creditLimit)
                        .birthday(birthDay)
                        .source(csvStreamFactory.getSource())
                        .build());
            }
        } catch (IOException e) {
            throw new DataInputException("Error reading CSV data", e);
        }
        return result;
    }

    private String getStringRecord(final CSVRecord record, final String name, final long lineNumber) {

        String recordValue;

        try {
            recordValue = record.get(name);
        } catch(final IllegalArgumentException e) {
            log.error("Row with name {} not found at position {}. Source: {}", name, lineNumber, csvStreamFactory.getSource(), e);
            recordValue = null;
        }

        return recordValue;
    }


}
