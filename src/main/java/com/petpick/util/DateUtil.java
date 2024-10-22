package com.petpick.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    // 현재 날짜와 시각
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    // 포맷된 현재 날짜
    public static String getFormattedCurrentDate(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().format(formatter);
    }
}
