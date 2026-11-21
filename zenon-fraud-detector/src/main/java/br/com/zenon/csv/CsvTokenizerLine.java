package br.com.zenon.csv;

import java.util.StringTokenizer;

public class CsvTokenizerLine {
    private final String CSV_DELIMINATOR = ",";

    public StringTokenizer tokenizer(String line) {
        return new StringTokenizer(line, CSV_DELIMINATOR);
    }
}
