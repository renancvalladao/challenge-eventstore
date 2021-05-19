package net.intelie.challenges;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class EventTest {
    @Test
    public void thisIsAWarning() throws Exception {
        Event event = new Event("some_type", 123L);

        //THIS IS A WARNING:
        //Some of us (not everyone) are coverage freaks.
        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }

    /*
     * This test checks if the insertion is working and if the events are ordered.
     */
    @Test
    public void testInsertion() {
        EventStore eventStoreSet = new EventStoreSet();
        Event t1ts4 = new Event("type1", 4);
        eventStoreSet.insert(t1ts4);
        Event t2ts1 = new Event("type2", 1);
        eventStoreSet.insert(t2ts1);
        Event t2ts2 = new Event("type2", 2);
        eventStoreSet.insert(t2ts2);
        Event t1ts1 = new Event("type1", 1);
        eventStoreSet.insert(t1ts1);
        EventIterator iterator = eventStoreSet.query("type1", 0, 5);
        iterator.moveNext();
        assertEquals(t1ts1, iterator.current());
        iterator.moveNext();
        assertEquals(t1ts4, iterator.current());
        assertFalse(iterator.moveNext());
        iterator = eventStoreSet.query("type2", 0, 5);
        iterator.moveNext();
        assertEquals(t2ts1, iterator.current());
        iterator.moveNext();
        assertEquals(t2ts2, iterator.current());
        assertFalse(iterator.moveNext());
    }

    /*
     * This test checks if the events are being removed when using the removeAll method.
     */
    @Test
    public void testRemoveAll() {
        EventStore eventStoreSet = new EventStoreSet();
        Event t1ts1 = new Event("type1", 1);
        eventStoreSet.insert(t1ts1);
        Event t1ts2 = new Event("type1", 2);
        eventStoreSet.insert(t1ts2);
        Event t1ts3 = new Event("type1", 3);
        eventStoreSet.insert(t1ts3);
        Event t2ts1 = new Event("type2", 1);
        eventStoreSet.insert(t2ts1);
        eventStoreSet.removeAll("type1");
        EventIterator iterator = eventStoreSet.query("type1", 0, 5);
        assertFalse(iterator.moveNext());
        iterator = eventStoreSet.query("type2", 0, 5);
        assertTrue(iterator.moveNext());
    }

    /*
     * This test checks if the insertion is working and if the events are ordered, but using concurrency.
     * I put a timeout of one second for all threads to finish executing.
     */
    @Test
    public void testInsertionWithThreads() throws InterruptedException {
        EventStore eventStoreSet = new EventStoreSet();
        AtomicBoolean running = new AtomicBoolean();
        AtomicInteger overlaps = new AtomicInteger();
        int threads = 50;
        ExecutorService service = Executors.newFixedThreadPool(threads);
        for (int t = 0; t < threads; t++) {
            final Event event = new Event("type1", t);
            service.submit(
                    () -> {
                        if (running.get()) {
                            overlaps.incrementAndGet();
                        }
                        running.set(true);
                        eventStoreSet.insert(event);
                        running.set(false);
                    }
            );
        }
        TimeUnit.SECONDS.sleep(1);
        EventIterator iterator = eventStoreSet.query("type1", 0, threads);
        int i = 0;
        while(iterator.moveNext()) {
            Event currentEvent = iterator.current();
            assertEquals(i, currentEvent.timestamp());
            i++;
        }
        assertTrue(overlaps.get() > 0);
    }

    /*
     * This test checks if duplicate elements are not inserted.
     */
    @Test
    public void insertDuplicated() {
        EventStore eventStoreSet = new EventStoreSet();
        Event t1ts1 = new Event("type1", 1);
        eventStoreSet.insert(t1ts1);
        Event t1ts2 = new Event("type1", 2);
        eventStoreSet.insert(t1ts2);
        Event t1ts2d = new Event("type1", 2);
        eventStoreSet.insert(t1ts2d);
        EventIterator iterator = eventStoreSet.query("type1", 0, 5);
        int numberOfEvents = 0;
        while(iterator.moveNext()) {
            numberOfEvents++;
        }
        assertEquals(2, numberOfEvents);
    }
}