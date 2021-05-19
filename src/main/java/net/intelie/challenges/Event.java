package net.intelie.challenges;

import javax.swing.plaf.basic.BasicDesktopIconUI;

/**
 * This is just an event stub, feel free to expand it if needed.
 */
public class Event implements Comparable<Event> {
    private final String type;
    private final long timestamp;

    public Event(String type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }

    public String type() {
        return type;
    }

    public long timestamp() {
        return timestamp;
    }

    /*
     * Since the ConcurrentSkipListSet is an ordered structure, its elements must have the compareTo method. In this
     * case, the elements are first sorted by type and then by timestamp. This order facilitates the query method,
     * because the elements are grouped first by type.
     */
    @Override
    public int compareTo(Event otherEvent) {
        if (this.type().equals(otherEvent.type())) {
            if (this.timestamp() > otherEvent.timestamp()) {
                return 1;
            } else if (this.timestamp() < otherEvent.timestamp()) {
                return -1;
            }
            return 0;
        } else {
            if (this.type().compareTo(otherEvent.type()) > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}