package com.ebay.assignment.creditlimittracker.datainput;

import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Process "PRN" files assuming fixed size of the fields and the following order:
 * Name: 0 - 15
 * Address: 16 - 37
 * Postcode: 38 - 46
 * Phone: 47 - 60
 * Credit Limit: 61 - 73
 * BirthDay: 74 - 81
 *
 * This is a length per line of 82 characters
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PrnDataReceiver {

    private final StreamFactory prnStreamFactory;

    /**
     * Receives the PNR data from the stream provided by the factory and convert each line to a CreditLimitInfo
     * object. This receiver ignore lines with wrong format logging an error. The idea of ignoring lines is not to
     * loose the rest of the information of the stream.
     * For privacy reasons the lines are never displayed in the log. The position is the only data provided plus extra
     * information telling what is wrong.
     * @return One CreditLimitInfo per line
     */
    public List<CreditLimitInfo> receive() {

        final ArrayList<CreditLimitInfo> result =  new ArrayList<>();

        try(final BufferedReader reader = new BufferedReader(prnStreamFactory.reader())) {

            String line;

            long lineNumber = 0;

            // Skip the header
            reader.readLine();

            while((line = reader.readLine()) != null) {

                lineNumber++;

                if(line.length() != 82) {
                    // We just skip this line to process the rest. The error is logged.
                    log.error("Invalid line length ({}) at position {}. Source: {}", line.length(), lineNumber, prnStreamFactory.getSource());
                    continue;
                }

                // The data is received in cents

                final BigDecimal creditLimit;

                try {
                    creditLimit = new BigDecimal(line.substring(61, 74).trim()).divide(new BigDecimal(100), RoundingMode.UNNECESSARY);
                } catch(final NumberFormatException e) {
                    log.error("Invalid credit limit value ({}) at position {}. Source: {}", line.substring(61, 74), lineNumber, prnStreamFactory.getSource(), e);
                    continue;
                }

                final LocalDate birthDay;

                try {
                    birthDay = LocalDate.parse(line.substring(74, 82).trim(), DateTimeFormatter.ofPattern("yyyyMMdd"));
                } catch(final DateTimeParseException e) {
                    log.error("Unexpected birth date format ({}) at position {}. Source: {}", line.substring(74, 82), lineNumber, prnStreamFactory.getSource(), e);
                    continue;
                }

                result.add(CreditLimitInfo.builder()
                        .name(line.substring(0, 16).trim())
                        .address(line.substring(16, 38).trim())
                        .postcode(line.substring(38, 47).trim())
                        .phone(line.substring(47, 61).trim())
                        .creditLimit(creditLimit)
                        .birthday(birthDay)
                        .build());
            }

        } catch (IOException e) {
            throw new DataInputException("Error reading PNR data", e);
        }

        return result;
    }

}
