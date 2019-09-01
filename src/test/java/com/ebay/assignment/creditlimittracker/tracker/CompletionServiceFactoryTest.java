package com.ebay.assignment.creditlimittracker.tracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;

@ExtendWith(MockitoExtension.class)
class CompletionServiceFactoryTest {

    @Mock
    private Executor executor;

    @InjectMocks
    private CompletionServiceFactory factory;

    @Test
    void factoryTest() {
        CompletionService service = factory.create();
        Assertions.assertNotNull(service);
    }
}