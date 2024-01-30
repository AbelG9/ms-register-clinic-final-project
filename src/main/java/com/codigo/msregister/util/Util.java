package com.codigo.msregister.util;

import java.sql.Timestamp;

public class Util {
    public static boolean isNullOrEmpty(String data) {
        return data == null || data.isEmpty();
    }
    public static Timestamp getActualTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
