package com.ebay.assignment.creditlimittracker.tracker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;

@Component
@RequiredArgsConstructor
class CompletionServiceFactory {

    private final Executor executor;

    CompletionService<List<CreditLimitInfo>> create() {
        return new ExecutorCompletionService<>(executor);
    }
}
