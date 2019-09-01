package com.ebay.assignment.creditlimittracker.weboutput;

import com.ebay.assignment.creditlimittracker.tracker.CreditLimitInfo;
import com.ebay.assignment.creditlimittracker.tracker.CreditLimitTrackerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditLimitTrackerControllerTest {

    @Mock
    private CreditLimitTrackerService creditLimitTrackerService;

    @InjectMocks
    private CreditLimitTrackerController controller;

    @Mock
    private Model model;

    @Test
    void getCreditLimitsTest() {
        final List<CreditLimitInfo> resultService = Collections.singletonList(mock(CreditLimitInfo.class));

        when(creditLimitTrackerService.getCreditLimitInformation()).thenReturn(resultService);

        final String view = controller.index(model);

        verify(creditLimitTrackerService).getCreditLimitInformation();
        verify(model).addAttribute("creditLimits", resultService);

        Assertions.assertEquals("index", view);
    }
}