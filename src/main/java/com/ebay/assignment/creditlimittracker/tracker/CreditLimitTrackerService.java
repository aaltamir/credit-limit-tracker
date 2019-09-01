package com.ebay.assignment.creditlimittracker.tracker;

import com.ebay.assignment.creditlimittracker.datainput.DataReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * This service uses all the data receivers in the system and collect the data from them. The data is joined and returned.
 * No specific criteria has been defined for sorting so the data is received as it's generated in the data receivers.
 */
@Slf4j
@Service
public class CreditLimitTrackerService {
    private final Integer receivingDataTimeout;

    private final List<DataReceiver> dataReceivers;

    private final CompletionServiceFactory completionServiceFactory;

    public CreditLimitTrackerService(@Value("${receivingDataTimeout}") final Integer receivingDataTimeout,
                                     final List<DataReceiver> dataReceivers,
                                     final CompletionServiceFactory completionServiceFactory) {
        this.receivingDataTimeout = receivingDataTimeout;
        this.dataReceivers = dataReceivers;
        this.completionServiceFactory = completionServiceFactory;
    }

    public List<CreditLimitInfo> getCreditLimitInformation() {
        final CompletionService<List<CreditLimitInfo>> completionService = completionServiceFactory.create();
        dataReceivers.forEach(dataReceiver -> completionService.submit(dataReceiver::receive));

        final List<CreditLimitInfo> receivedData = new ArrayList<>();

        for(int i = 0; i < dataReceivers.size(); i++) {

            final Future<List<CreditLimitInfo>> futureResult;

            try {
                 futureResult = completionService.poll(receivingDataTimeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // We want to return the data up to now.
                log.error("Unexpected exception when retrieving data. Incomplete data is received", e);
                break;
            }

            if(futureResult == null) {
                log.error("Timeout receiving data. Incomplete data is received");
                break;
            }

            // We always expect to receive data from all receivers
            try {
                receivedData.addAll(futureResult.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error retrieving data from the receiver. Data from the receiver has not been retrieved", e);
            }
        }
        return receivedData;
    }
}
