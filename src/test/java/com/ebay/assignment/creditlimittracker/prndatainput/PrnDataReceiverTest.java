package com.ebay.assignment.creditlimittracker.prndatainput;

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
class PrnDataReceiverTest {

    @Mock
    private StreamFactory prnStreamFactory;

    @InjectMocks
    private PrnDataReceiver dataReceiver;

    @BeforeEach
    void setUp() {
        when(prnStreamFactory.getSource()).thenReturn("source-pnr");
    }

    @Test
    void processPnrContentTest() throws IOException {
        final String testData =
                "Name            Address               Postcode Phone         Credit Limit Birthday\n" +
                "Johnson, John   Voorstraat 32         3122gg   020 3849381        1000000 19870101\n" +
                "Anderson, Paul  Dorpsplein 3A         4532 AA  030 3458986       10909300 19651203";

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
        Assertions.assertEquals("source-pnr", result.get(0).getSource());
    }

    @Test
    void processPnrInvalidLineTest() throws IOException {
        final String testData =
                "Name            Address               Postcode Phone         Credit Limit Birthday\n" +
                        "Johnson, John   Voorstraat 32         3122gg   020 384381        1000000 19870101\n" +
                        "Anderson, Paul  Dorpsplein 3A         4532 AA  030 3458986       10909300 19651203";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Anderson, Paul", result.get(0).getName());
    }

    @Test
    void processPnrInvalidNumberTest() throws IOException {
        final String testData =
                "Name            Address               Postcode Phone         Credit Limit Birthday\n" +
                        "Johnson, John   Voorstraat 32         3122gg   020 3849381        1000000 19870101\n" +
                        "Anderson, Paul  Dorpsplein 3A         4532 AA  030 3458986       109a9300 19651203";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertNull( result.get(1).getCreditLimit());
    }

    @Test
    void processPnrInvalidDateTest() throws IOException {
        final String testData =
                "Name            Address               Postcode Phone         Credit Limit Birthday\n" +
                        "Johnson, John   Voorstraat 32         3122gg   020 3849381        1000000 19870101\n" +
                        "Anderson, Paul  Dorpsplein 3A         4532 AA  030 3458986       10909300 -9651203";

        when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
        Assertions.assertNull(result.get(1).getBirthday());
    }
}