package ru.netology.util;

public final class ServletUtil {
    public static Long parseId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
    }
}
