package com.microsoft.aad.adal;

import java.util.Map;

public interface IDispatcher {
    void dispatchEvent(Map<String, String> map);
}
