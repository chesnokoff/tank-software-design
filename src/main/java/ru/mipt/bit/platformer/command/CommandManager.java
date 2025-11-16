package ru.mipt.bit.platformer.command;

import java.util.ArrayDeque;
import java.util.Queue;
import ru.mipt.bit.platformer.InternalContext;

/** */
public class CommandManager {
    /** Queue. */
    private final Queue<Command> queue = new ArrayDeque<>();

    /** Context. */
    private final InternalContext context;

    public CommandManager(InternalContext context) {
        this.context = context;
        this.context.register(CommandManager.class, this);
    }

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
