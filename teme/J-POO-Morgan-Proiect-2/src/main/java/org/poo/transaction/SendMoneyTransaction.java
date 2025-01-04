package org.poo.transaction;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class representing a transaction for sending money
 * from one account to another.
 */
public final class SendMoneyTransaction extends Transaction {
    private final String senderIBAN;
    private final String receiverIBAN;
    private final double amount;
    private final String transferType;
    private final String currency;

    /**
     * Constructs a SendMoneyTransaction with the specified details.
     *
     * @param timestamp    The timestamp of the transaction.
     * @param description  The description of the transaction.
     * @param sender       The IBAN of the sender's account.
     * @param receiver     The IBAN of the receiver's account.
     * @param amount       The amount of money being sent.
     * @param currency     The currency of the transaction.
     * @param transferType The type of transfer (e.g., domestic, international).
     */
    public SendMoneyTransaction(final int timestamp,
                                final String description,
                                final String sender,
                                final String receiver,
                                final double amount,
                                final String currency,
                                final String transferType) {
        super(timestamp, description);
        this.senderIBAN = sender;
        this.receiverIBAN = receiver;
        this.amount = amount;
        this.currency = currency;
        this.transferType = transferType;
    }

    /**
     * Returns the IBAN of the sender.
     *
     * @return The sender's IBAN.
     */
    public String getSenderIBAN() {
        return senderIBAN;
    }

    /**
     * Returns the IBAN of the receiver.
     *
     * @return The receiver's IBAN.
     */
    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    /**
     * Returns the amount of money being sent.
     *
     * @return The amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the currency of the transaction.
     *
     * @return The currency.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the type of transfer.
     *
     * @return The transfer type.
     */
    public String getTransferType() {
        return transferType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void toJson(final ObjectNode node) {
        node.put("amount", getAmount() + " " + getCurrency());
        node.put("description", getDescription());
        node.put("receiverIBAN", getReceiverIBAN());
        node.put("senderIBAN", getSenderIBAN());
        node.put("timestamp", getTimestamp());
        node.put("transferType", getTransferType());
    }
}
