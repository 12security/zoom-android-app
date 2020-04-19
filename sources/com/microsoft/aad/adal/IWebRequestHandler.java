package com.microsoft.aad.adal;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public interface IWebRequestHandler {
    HttpWebResponse sendGet(URL url, Map<String, String> map) throws IOException;

    HttpWebResponse sendPost(URL url, Map<String, String> map, byte[] bArr, String str) throws IOException;

    void setRequestCorrelationId(UUID uuid);
}
