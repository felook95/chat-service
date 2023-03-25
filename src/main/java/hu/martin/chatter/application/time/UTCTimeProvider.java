package hu.martin.chatter.application.time;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class UTCTimeProvider implements TimeProvider {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
