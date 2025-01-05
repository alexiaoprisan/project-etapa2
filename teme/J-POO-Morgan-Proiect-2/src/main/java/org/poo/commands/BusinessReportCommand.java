package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.user.UserRegistry;

public class BusinessReportCommand implements Command {

    private final UserRegistry userRegistry;
    private final ArrayNode output;
    private final int startTimestamp;
    private final int endTimestamp;
    private final String account;
    private final String type;
    private final int timestamp;

    public BusinessReportCommand(UserRegistry userRegistry, ArrayNode output, int startTimestamp, int endTimestamp, String account, String type, int timestamp) {
        this.userRegistry = userRegistry;
        this.output = output;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.account = account;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public void execute() {

    }
}
