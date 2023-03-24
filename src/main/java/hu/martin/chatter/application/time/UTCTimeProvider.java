package hu.martin.chatter.application.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class UTCTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
