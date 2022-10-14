package com.healthy.library.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupCopy {
    public static Document parse(String html) {
        if(html==null){
            return Jsoup.parse("", "");
        }
        return Jsoup.parse(html, "");
    }

}
