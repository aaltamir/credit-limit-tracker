package com.ebay.assignment.creditlimittracker.datainput;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Interface used to make an abstraction layer between the type of sources and the stream of data.
 * This can be useful in the future if instead of local files we want to get the information from FTP or
 * other repositories.
 */
public interface StreamFactory {
    InputStreamReader reader() throws IOException;

    /**
     * Tells a name representing the origin of the information
     * @return String representing the origin of the information
     */
    String getSource();
}
