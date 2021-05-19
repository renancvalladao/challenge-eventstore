package net.intelie.challenges;

import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

public class EventStoreSet implements EventStore{

    /*
    * The ConcurrentSkipListSet is a data structure that is already thread-safe. This structure also ensures
    * fast access to any key. It's a ordered structure, and it doesn't accept null or duplicated elements.
    * I thought that this structure was a good solution to the challenge.
    */

    private final ConcurrentSkipListSet<Event> eventSet;

    public EventStoreSet() {
        eventSet = new ConcurrentSkipListSet<>();
    }

    /*
     * The ConcurrentSkipListSet already has a method for inserting an element.
     */
    @Override
    public void insert(Event event) {
        eventSet.add(event);
    }

    /*
     * The ConcurrentSkipListSet has a method for removing elements that satisfy the given predicate.
     */
    @Override
    public void removeAll(String type) {
        eventSet.removeIf(event -> event.type().equals(type));
    }

    /*
     * Since the event store is sorted first by type and then by timestamp, a subset is created using the type
     * and the start and end timestamp.
     */
    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        Event firstEvent = new Event(type, startTime);
        Event lastEvent = new Event(type, endTime);
        NavigableSet<Event> subSet = eventSet.subSet(firstEvent, true, lastEvent, false);
        return new EventIteratorSet(subSet);
    }
}
