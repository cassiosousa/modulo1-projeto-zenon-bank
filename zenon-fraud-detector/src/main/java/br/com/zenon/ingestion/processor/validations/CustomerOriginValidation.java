package br.com.zenon.ingestion.processor.validations;

import br.com.zenon.domain.TransactionFieldKey;
import br.com.zenon.exceptions.AmountException;
import br.com.zenon.exceptions.IndexException;
import br.com.zenon.exceptions.TransactionValidateException;

import java.math.BigDecimal;
import java.util.Map;

import static br.com.zenon.domain.TransactionFieldKey.*;

public class CustomerOriginValidation implements TransactionValidation  {
    @Override
    public void validate(Map<TransactionFieldKey, Object> mapLine) throws TransactionValidateException {
        if (!mapLine.containsKey(CUSTOMER_ORIGIN_NAME)) {
            throw new IndexException("Customer origin name index not found");
        }
        Object value = mapLine.get(CUSTOMER_ORIGIN_NAME);
        if (value == null || value.toString().isEmpty()) {
            throw new AmountException("Invalid " + value + " for amount");
        }

        if (!mapLine.containsKey(CUSTOMER_ORIGIN_NEW_BALANCE)) {
            throw new IndexException("Customer origin new balance index not found");
        }
        Object newBalance = mapLine.get(CUSTOMER_ORIGIN_NEW_BALANCE);
        if (newBalance == null || new BigDecimal(newBalance.toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountException("Invalid customer origin" + newBalance + " for new balance");
        }

        if (!mapLine.containsKey(CUSTOMER_ORIGIN_OLD_BALANCE)) {
            throw new IndexException("Customer origin old balance index not found");
        }
        Object oldBalance = mapLine.get(CUSTOMER_ORIGIN_OLD_BALANCE);
        if (oldBalance == null || new BigDecimal(oldBalance.toString()).compareTo(BigDecimal.ZERO) < 0) {
            throw new AmountException("Invalid customer origin" + oldBalance + " for old balance");
        }
    }
}
