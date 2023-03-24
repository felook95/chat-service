package hu.martin.chatter.application;

import hu.martin.chatter.application.time.TimeProvider;

import java.time.LocalDateTime;
import java.util.Iterator;

public class OrderedTimeProvider implements TimeProvider {

    private final Iterator<LocalDateTime> returnDateTimes;

    public OrderedTimeProvider(Iterable<LocalDateTime> returnDateTimes) {
        this.returnDateTimes = returnDateTimes.iterator();
    }

    @Override
    public LocalDateTime now() {
        LocalDateTime next = returnDateTimes.next();
        return next;
    }
}
