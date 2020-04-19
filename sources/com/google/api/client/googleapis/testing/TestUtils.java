package com.google.api.client.googleapis.testing;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class TestUtils {
    private static final String UTF_8 = "UTF-8";

    public static Map<String, String> parseQuery(String str) throws IOException {
        HashMap hashMap = new HashMap();
        for (String split : Splitter.m94on('&').split(str)) {
            ArrayList newArrayList = Lists.newArrayList(Splitter.m94on('=').split(split));
            if (newArrayList.size() == 2) {
                hashMap.put(URLDecoder.decode((String) newArrayList.get(0), "UTF-8"), URLDecoder.decode((String) newArrayList.get(1), "UTF-8"));
            } else {
                throw new IOException("Invalid Query String");
            }
        }
        return hashMap;
    }

    private TestUtils() {
    }
}
