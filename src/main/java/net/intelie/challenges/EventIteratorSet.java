package net.intelie.challenges;

import java.util.Iterator;
import java.util.NavigableSet;

public class EventIteratorSet implements EventIterator {

    Iterator<Event> iterator;
    Event currentEvent;

    public EventIteratorSet(NavigableSet<Event> subSet) {
        this.iterator = subSet.iterator();
        currentEvent = null;
    }

    /*
     * If there's a next event, the current event will be updated.
     */
    @Override
    public boolean moveNext() {
        if(iterator.hasNext()) {
            currentEvent = iterator.next();
            return true;
        }
        return false;
    }

    /*
     * This method returns the current event, if it's not null.
     */
    @Override
    public Event current() {
        if (currentEvent != null) {
            return currentEvent;
        } else {
            throw new IllegalStateException();
        }
    }

    /*
    * If the current event is not null, it's removed.
    */
    @Override
    public void remove() {
        if (currentEvent != null) {
            iterator.remove();
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public void close() throws Exception {

    }
}
