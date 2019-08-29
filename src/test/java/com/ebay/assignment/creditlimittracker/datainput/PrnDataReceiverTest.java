package com.ebay.assignment.creditlimittracker.datainput;

import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PrnDataReceiverTest {

    @Mock
    private StreamFactory prnStreamFactory;

    @InjectMocks
    private PrnDataReceiver dataReceiver;


    @Test
    void processPnrContentTest() throws IOException {

        final String testData =
                "Name            Address               Postcode Phone         Credit Limit Birthday\n" +
                "Johnson, John   Voorstraat 32         3122gg   020 3849381        1000000 19870101\n" +
                "Anderson, Paul  Dorpsplein 3A         4532 AA  030 3458986       10909300 19651203";

        Mockito.when(prnStreamFactory.reader())
                .thenReturn(new InputStreamReader(new ByteArrayInputStream(testData.getBytes())));

        final List<CreditLimitInfo> result = dataReceiver.receive();

        Assertions.assertEquals(2, result.size());
    }
}