package com.yida.modules.yde.util;

import org.apache.commons.lang3.StringUtils;

public class HtmlUtils {
    public static String removeStyle(String html) {
        String styleHtml = "<style " + StringUtils.substringBetween(html, "<style ", "style>") + "style>";
        return StringUtils.remove(html, styleHtml);
    }
}
