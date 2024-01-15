package com.example.vkusers.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;

/**
 * Утилита для конвертирования LocalDateTime.
 */
@UtilityClass
public class DateTimeUtil {

    private static final String PATTERN_FOR_FORMATTER = "yyyy-MM-dd['T'HH:mm:ss[.SSS]]";
    private static final String PATTERN_FOR_FORMATTER_VK_DATE = "d.M[.yyyy]";

    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder().appendPattern(PATTERN_FOR_FORMATTER)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .toFormatter();

    private static final DateTimeFormatter FORMATTER_VK =
            new DateTimeFormatterBuilder().appendPattern(PATTERN_FOR_FORMATTER_VK_DATE)
                    .toFormatter();

    /**
     * Конвертирование строки в LocalDateTime.
     *
     * @param date дата в формате yyyy-MM-dd или yyyy-MM-ddTHH:mm:ss
     * @return LocalDateTime.
     */
    public LocalDateTime fromStringToLocalDateTime(String date) {
        return LocalDateTime.from(FORMATTER.parse(date));
    }

    public LocalDate fromStringToLocalDate(String date) {
        return LocalDate.from(FORMATTER.parse(date));
    }


    /**
     * Конвертирование строки в формате VK в LocalDateTime.
     *
     * @param dStr дата в формате dd.MM.yyyy или dd.MM
     * @return LocalDate.
     */
    public LocalDate fromVkString(String dStr) {
        TemporalAccessor parsed = FORMATTER_VK.parse(dStr);
        boolean hasYear = parsed.isSupported(ChronoField.YEAR);
        boolean hasYearEra = parsed.isSupported(ChronoField.YEAR_OF_ERA);
        if (!hasYear && !hasYearEra) {
            return MonthDay.parse(dStr, FORMATTER_VK).atYear(1900);
        }
        return LocalDate.from(FORMATTER_VK.parse(dStr));
    }

    /**
     * Конвертирование LocalDateTime в строку
     * @param date дата для конвертации
     * @param dateTimeFormatter пользовательский форматер если null то форматирует по шаблону dd.MM.yyyy
     * @return
     */
    public String toString(LocalDate date, DateTimeFormatter dateTimeFormatter){
        DateTimeFormatter formatter = dateTimeFormatter != null ? dateTimeFormatter : DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(date);
    }

    /**
     * Конвертирование LocalDate в строку
     * @param date дата для конвертации
     * @param dateTimeFormatter пользовательский форматер если null то форматирует по шаблону dd.MM.yyyy
     * @return
     */
    public String toString(LocalDateTime date, DateTimeFormatter dateTimeFormatter){
        DateTimeFormatter formatter = dateTimeFormatter != null ? dateTimeFormatter : DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(date);
    }
}
