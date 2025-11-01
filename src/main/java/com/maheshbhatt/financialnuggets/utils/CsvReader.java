package com.maheshbhatt.financialnuggets.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;

public final class CsvReader {

    private CsvReader() { }

    /**
     * Read all rows from the provided CSV input stream.
     * @param inputStream CSV data input stream
     * @return list of rows, each row is a String[] of values
     * @throws IOException when reading/parsing fails
     */
    public static List<String[]> readCsv(InputStream inputStream) throws IOException {
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(inputStream)).build()) {
            return reader.readAll();
        } catch (Exception e) {
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new IOException("Failed to read CSV", e);
        }
    }
}

