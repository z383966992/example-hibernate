package utils;

import java.io.UnsupportedEncodingException;

public class URLDecoder {
    public static String decode(String str) {

        try {
            if (str != null) {
                str = java.net.URLDecoder.decode(str, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}