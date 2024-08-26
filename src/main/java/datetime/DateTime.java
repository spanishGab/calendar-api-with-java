package  datetime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateTime {
    protected static final String DEFAULT_ZONE_ID = "UTC";
    protected ZonedDateTime instance;

    public DateTime() {
        this.instance = DateTime.now(DateTime.DEFAULT_ZONE_ID);
    }

    public DateTime(String zone) {
        this.instance = DateTime.now(zone);
    }

    public DateTime(
        int year,
        int month,
        int day,
        int hour,
        int minute,
        int second,
        int millisecond,
        String zone
    ) {
        this.instance = ZonedDateTime.of(
            year,
            month,
            day,
            hour,
            minute,
            second,
            millisecond * 1_000_000,
            DateTime.getZoneById(zone)
        );
    }

    public String toISO8601(Formats format) throws  IllegalArgumentException {
        String formattedInstance;
        switch (format) {
            case LONG_ISO8601 -> formattedInstance = this.instance.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            case SHORT_ISO8601 -> formattedInstance = this.instance.format(DateTimeFormatter.ISO_LOCAL_DATE);
            case null, default -> throw new IllegalArgumentException("invalid date format");
        }
        return formattedInstance;
    }

    protected static ZonedDateTime now(String zone) {
        return ZonedDateTime.now(DateTime.getZoneById(zone));
    }

    protected static ZoneId getZoneById(String zoneId) throws IllegalArgumentException {
        try {
            return ZoneId.of(zoneId);
        } catch (java.time.zone.ZoneRulesException error) {
            throw new IllegalArgumentException(String.format("invalid zone ID: %s", zoneId));
        }
    }
}
