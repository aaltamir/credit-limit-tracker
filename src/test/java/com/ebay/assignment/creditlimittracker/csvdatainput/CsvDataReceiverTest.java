package com.ebay.assignment.creditlimittracker.csvdatainput;

import com.ebay.assignment.creditlimittracker.datainput.StreamFactory;
import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvDataReceiverTest {

    @Mock
    private StreamFactory prnStreamFactory;

    @InjectMocks
    private CsvDataReceiver dataReceiver;

    @BeforeEach
    void setUp() {
        when(prnStreamFactory.getSource()).thenReturn("source-csv");
    }

    @Test
    void processCsvContentTest() throws IOException {
        final String testData =
                "Name,Address,Postcode,Phone,Credit Limit,Birthday\n" +
                        "\"Johnson, John\",Voorstraat 32,3122gg,020 3849381,10000,01/01/1987\n" +
                        "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986,109093,03/12/1965";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Johnson, John", result.get(0).getName());
        Assertions.assertEquals("Voorstraat 32", result.get(0).getAddress());
        Assertions.assertEquals("3122gg", result.get(0).getPostcode());
        Assertions.assertEquals("020 3849381", result.get(0).getPhone());
        Assertions.assertEquals(new BigDecimal("10000"), result.get(0).getCreditLimit());
        Assertions.assertEquals(LocalDate.of(1987,1, 1), result.get(0).getBirthday());
        Assertions.assertEquals("source-csv", result.get(0).getSource());
    }

    @Test
    void processCsvContentOneElementNotExistTest() throws IOException {
        final String testData =
                "Name,Address,Postcode,Phone,Credit Limit,Birthday\n" +
                        "\"Johnson, John\",Voorstraat 32,3122gg,10000,01/01/1987\n" +
                        "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986,109093,03/12/1965";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("10000", result.get(0).getPhone());
        Assertions.assertNull(result.get(0).getCreditLimit());
        Assertions.assertNull(result.get(0).getBirthday());
    }

    @Test
    void processCsvInvalidNumberTest() throws IOException {
        final String testData =
                "Name,Address,Postcode,Phone,Credit Limit,Birthday\n" +
                        "\"Johnson, John\",Voorstraat 32,3122gg,020 3849381,10000,01/01/1987\n" +
                        "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986,109a093,03/12/1965";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertNull( result.get(1).getCreditLimit());
    }

    @Test
    void processCsvCreditLimitNotExistTest() throws IOException {
        final String testData =
                "Name,Address,Postcode,Phone,Credit Limit,Birthday\n" +
                        "\"Johnson, John\",Voorstraat 32,3122gg,020 3849381,10000,01/01/1987\n" +
                        "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertNull( result.get(1).getCreditLimit());
    }

    @Test
    void processCsvInvalidDateTest() throws IOException {
        final String testData =
                "Name,Address,Postcode,Phone,Credit Limit,Birthday\n" +
                        "\"Johnson, John\",Voorstraat 32,3122gg,020 3849381,10000,01/01/1987\n" +
                        "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986,109093,03/13/1965";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertNull(result.get(1).getBirthday());
    }

    @Test
    void processCsvNotExistBirthDateTest() throws IOException {
        final String testData =
                "Name,Address,Postcode,Phone,Credit Limit,Birthday\n" +
                        "\"Johnson, John\",Voorstraat 32,3122gg,020 3849381,10000,01/01/1987\n" +
                        "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986,109093";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertNull(result.get(1).getBirthday());
    }
}