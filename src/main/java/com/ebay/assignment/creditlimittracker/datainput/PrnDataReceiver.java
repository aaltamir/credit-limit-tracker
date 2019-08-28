package com.ebay.assignment.creditlimittracker.datainput;

import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
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

    public List<CreditLimitInfo> receive() {

        final ArrayList<CreditLimitInfo> result =  new ArrayList<>();

        try(final BufferedReader reader = new BufferedReader(prnStreamFactory.reader())) {

            String line;

            long lineNumber = 0;

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
                    creditLimit = new BigDecimal(line.substring(61, 73)).divide(new BigDecimal(100));
                } catch(final NumberFormatException e) {
                    log.error("Invalid credit limit value ({}) at position {}. Source: {}", line.substring(61, 73), lineNumber, prnStreamFactory.getSource(), e);
                    continue;
                }



            }

        } catch (IOException e) {

        }

        return result;
    }

}
