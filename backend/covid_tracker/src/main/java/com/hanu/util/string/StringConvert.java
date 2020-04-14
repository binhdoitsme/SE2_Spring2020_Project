package com.hanu.util.string;

public class StringConvert {
    public static String camelToSnakeCase(String source) {
        if (source == null) {
            return new String();
        }
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return source.replaceAll(regex, replacement)
                    .replaceAll("^(.+)(_)$", "$1")
                    .toLowerCase();
    }
}