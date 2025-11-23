package ru.mipt.bit.platformer.command;

import java.util.ArrayDeque;
import java.util.Queue;
/** */
public class CommandManager {
    /** Queue. */
    private final Queue<Command> queue = new ArrayDeque<>();

    /**
     * @param command Command.
     */
    public void submit(Command command) {
        queue.add(command);
    }

    /** */
    public void executeAll() {
        while (!queue.isEmpty()) {
            queue.poll().execute();
        }
    }
}
