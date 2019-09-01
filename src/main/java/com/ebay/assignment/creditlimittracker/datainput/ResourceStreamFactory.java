package com.ebay.assignment.creditlimittracker.datainput;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
public abstract class ResourceStreamFactory implements StreamFactory {

    private final Resource resource;

    @Override
    public InputStreamReader reader() throws IOException {
        return new InputStreamReader(resource.getInputStream(), getCharSet());
    }

    public abstract String getCharSet();

}
