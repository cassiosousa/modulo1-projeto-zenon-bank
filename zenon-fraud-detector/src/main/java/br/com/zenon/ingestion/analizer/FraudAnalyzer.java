package br.com.zenon.ingestion.analizer;

import br.com.zenon.domain.Transaction;

import java.util.List;

 interface FraudAnalyzer {
    void analyzer(List<Transaction> transactions);
}
