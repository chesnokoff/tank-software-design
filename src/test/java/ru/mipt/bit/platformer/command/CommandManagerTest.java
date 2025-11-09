package ru.mipt.bit.platformer.command;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.InternalContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class CommandManagerTest {
    @Test
    void executesSubmittedCommandsInOrder() {
        InternalContext context = new InternalContext();
        CommandManager manager = new CommandManager(context);
        AtomicInteger counter = new AtomicInteger();
        manager.submit(counter::incrementAndGet);
        manager.submit(counter::incrementAndGet);

        manager.executeAll();

        assertEquals(2, counter.get());
    }
}
