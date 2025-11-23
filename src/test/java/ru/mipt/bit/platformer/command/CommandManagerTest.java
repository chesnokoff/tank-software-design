package ru.mipt.bit.platformer.command;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandManagerTest {
    @Test
    void executesSubmittedCommandsInOrder() {
        CommandManager manager = new CommandManager();
        AtomicInteger counter = new AtomicInteger();
        manager.submit(counter::incrementAndGet);
        manager.submit(counter::incrementAndGet);

        manager.executeAll();

        assertEquals(2, counter.get());
    }
}
