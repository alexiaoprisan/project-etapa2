package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class CashWithdrawalError extends Transaction {

    public CashWithdrawalError(int timestamp, String description) {
        super(timestamp, description);
    }

    @Override
    public void toJson(ObjectNode node) {
        node.put("description", getDescription());
        node.put("timestamp", getTimestamp());
    }
}
