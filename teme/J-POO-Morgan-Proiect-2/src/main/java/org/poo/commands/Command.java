package org.poo.commands;

/**
 * Interface for all commands.
 * Each command will implement the execute method.
 */
public interface Command {

    /**
     * Execute the command.
     * This method will be overridden by each command and
     * will contain the logic for the command.
     */
    void execute();
}
