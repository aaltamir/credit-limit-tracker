package com.ebay.assignment.creditlimittracker.tracker;

import com.ebay.assignment.creditlimittracker.datainput.DataReceiver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditLimitTrackerServiceTest {
    @Mock
    private CompletionService<List<CreditLimitInfo>> completionService;

    @Mock
    private CompletionServiceFactory completionServiceFactory;

    private CreditLimitTrackerService trackerService;

    @Mock
    private DataReceiver receiver1;

    @Mock
    private DataReceiver receiver2;

    @Mock
    private CreditLimitInfo info1;

    @Mock
    private CreditLimitInfo info2;

    @Captor
    private ArgumentCaptor<Callable<List<CreditLimitInfo>>> callableCaptor;

    @BeforeEach
    void setUp() {
        when(completionServiceFactory.create()).thenReturn(completionService);
        trackerService = new CreditLimitTrackerService(10000, Arrays.asList(receiver1, receiver2),
                completionServiceFactory);
    }

    @Test
    void getCreditLimitInformationTest() throws Exception {

        when(completionService.poll(anyLong(), any()))
                .thenReturn(CompletableFuture.completedFuture(Collections.singletonList(info1)))
                .thenReturn(CompletableFuture.completedFuture(Collections.singletonList(info2)));

        final List<CreditLimitInfo> result = trackerService.getCreditLimitInformation();

        verify(completionServiceFactory).create();
        verify(completionService, times(2)).submit(callableCaptor.capture());

        Assertions.assertEquals(2, callableCaptor.getAllValues().size());
        callableCaptor.getAllValues().get(0).call();
        callableCaptor.getAllValues().get(1).call();

        verify(receiver1).receive();
        verify(receiver2).receive();

        verify(completionService, times(2)).poll(10000, TimeUnit.MILLISECONDS);

        Assertions.assertIterableEquals(Arrays.asList(info1, info2), result);
    }

    @Test
    void getCreditLimitInformationWhenErrorInOneReceiverTest() throws Exception {
        when(completionService.poll(anyLong(), any()))
                .thenReturn(CompletableFuture.completedFuture(Collections.singletonList(info1)))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("dummy problem")));

        final List<CreditLimitInfo> result = trackerService.getCreditLimitInformation();

        Assertions.assertIterableEquals(Collections.singletonList(info1), result);
    }

    @Test
    void getCreditLimitInformationWhenTimeOutErrorInOneReceiverTest() throws Exception {
        when(completionService.poll(anyLong(), any()))
                .thenReturn(CompletableFuture.completedFuture(Collections.singletonList(info1)))
                .thenReturn(null);

        final List<CreditLimitInfo> result = trackerService.getCreditLimitInformation();

        Assertions.assertIterableEquals(Collections.singletonList(info1), result);
    }
}