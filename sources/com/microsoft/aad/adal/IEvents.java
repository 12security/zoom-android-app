package com.microsoft.aad.adal;

import android.util.Pair;
import java.util.List;
import java.util.Map;

interface IEvents {
    int getDefaultEventCount();

    List<Pair<String, String>> getEvents();

    void processEvent(Map<String, String> map);

    void setProperty(String str, String str2);
}
