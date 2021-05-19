package net.intelie.challenges;

public class App {
    public static void main(String[] args) {

        /*
         * I was using this class to test the implementation.
         */

        EventStore eventStoreSet = new EventStoreSet();
        Event t1ts1 = new Event("type1", 1);
        eventStoreSet.insert(t1ts1);
        Event t1ts2 = new Event("type1", 2);
        eventStoreSet.insert(t1ts2);
        Event t1ts3 = new Event("type1", 3);
        eventStoreSet.insert(t1ts3);
        Event t2ts1 = new Event("type2", 1);
        eventStoreSet.insert(t2ts1);
        Event t2ts2 = new Event("type2", 2);
        eventStoreSet.insert(t2ts2);

        EventIterator iterator = eventStoreSet.query("type1", 0, 5);

        System.out.println("->Type 1...");
        while (iterator.moveNext()) {
            Event currentEvent = iterator.current();
            System.out.println("type: " + currentEvent.type() + " timestamp: " + currentEvent.timestamp());
        }

        iterator = eventStoreSet.query("type2", 0, 5);

        System.out.println("->Type 2...");
        while (iterator.moveNext()) {
            Event currentEvent = iterator.current();
            System.out.println("type: " + currentEvent.type() + " timestamp: " + currentEvent.timestamp());
            iterator.remove();
        }
        eventStoreSet.removeAll("type1");

        iterator = eventStoreSet.query("type1", 0, 5);

        System.out.println("->Type 1 after removeAll...");
        while (iterator.moveNext()) {
            Event currentEvent = iterator.current();
            System.out.println("type: " + currentEvent.type() + " timestamp: " + currentEvent.timestamp());
        }

        iterator = eventStoreSet.query("type2", 0, 5);

        System.out.println("->Type 2 after removeIterator...");
        while (iterator.moveNext()) {
            Event currentEvent = iterator.current();
            System.out.println("type: " + currentEvent.type() + " timestamp: " + currentEvent.timestamp());
        }
    }
}
