package datetime;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTest {
    @Test
    void test_toISO8601_shouldReturnAValidISO8601DateTimeWithTimeZoneString() {
        DateTime sut = new DateTime(
            2024, 1, 1, 1, 12, 12, 125, "America/Caracas"
        );
        assertEquals("2024-01-01T01:12:12.125-04:00", sut.toISO8601(DateTime.Formats.LONG_ISO8601));
    }

    @Test
    void test_toISO8601_shouldReturnAValidISO8601DateString() {
        DateTime sut = new DateTime(
                2024, 1, 1, 1, 12, 12, 125, "Asia/Tokyo"
        );
        assertEquals("2024-01-01", sut.toISO8601(DateTime.Formats.SHORT_ISO8601));
    }

    @Test
    void test_toISO8601_shouldThrowAnIllegalArgumentExceptionForNullFormat() {
        DateTime sut = new DateTime(
                2024, 1, 1, 1, 12, 12, 125, "America/Caracas"
        );
        assertThrows(IllegalArgumentException.class, () -> sut.toISO8601(null));
    }

    @ParameterizedTest
    @CsvSource({
            "16, 1",
            "17, 2",
            "18, 3",
            "19, 4",
            "20, 5",
            "21, 6",
            "22, 7"
    })
    void test_getDayOfWeek_shouldReturnAValidDayOfWeek(int day, int expectedDayOfWeek) {
        DateTime sut = new DateTime(
                2024, 9, day, 1, 12, 12, 125, "America/Sao_Paulo"
        );
        assertEquals(expectedDayOfWeek, sut.getDayOfWeek().getValue());
    }


    @ParameterizedTest
    @CsvSource({
            "UTC,              Z",
            "America/Sao_Paulo,-03:00",
            "Europe/Paris,              +01:00"})
    void test_now_shouldReturnAValidZonedDateTime(String zoneId, String timeOffset) {
        ZonedDateTime mockedNow = ZonedDateTime.of(
            2024, 1, 1, 1, 12, 12, 125*1_000_000, ZoneId.of(zoneId)
        );
        try (MockedStatic<ZonedDateTime> mockedZonedDateTime = mockStatic(ZonedDateTime.class)) {
            mockedZonedDateTime.when(() -> ZonedDateTime.now(ZoneId.of(zoneId))).thenReturn(mockedNow);

            ZonedDateTime actualDateTime = DateTime.now(zoneId);
            assertInstanceOf(ZonedDateTime.class, actualDateTime);
            assertEquals(
                    "2024-01-01T01:12:12.125" + timeOffset,
                    actualDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"UTC", "America/Sao_Paulo", "Europe/Paris"})
    void test_getZoneById_shouldReturnAValidZoneId(String zoneId) {
        assertInstanceOf(ZoneId.class, DateTime.getZoneById(zoneId));
    }

    @Test
    void test_getZoneById_shouldThrowAnIllegalArgumentExceptionWhenZoneIdIsInvalid() {
        assertThrows(
                IllegalArgumentException.class,
                () -> DateTime.getZoneById("Zone/ID")
        );
    }
}