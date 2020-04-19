package com.microsoft.aad.adal;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class AuthorityValidationMetadataCache {
    private static final String ALIASES = "aliases";
    static final String META_DATA = "metadata";
    private static final String PREFERRED_CACHE = "preferred_cache";
    private static final String PREFERRED_NETWORK = "preferred_network";
    private static final String TAG = "AuthorityValidationMetadataCache";
    static final String TENANT_DISCOVERY_ENDPOINT = "tenant_discovery_endpoint";
    private static ConcurrentMap<String, InstanceDiscoveryMetadata> sAadAuthorityHostMetadata = new ConcurrentHashMap();

    private AuthorityValidationMetadataCache() {
    }

    static boolean containsAuthorityHost(URL url) {
        return sAadAuthorityHostMetadata.containsKey(url.getHost().toLowerCase(Locale.US));
    }

    static boolean isAuthorityValidated(URL url) {
        return containsAuthorityHost(url) && getCachedInstanceDiscoveryMetadata(url).isValidated();
    }

    static InstanceDiscoveryMetadata getCachedInstanceDiscoveryMetadata(URL url) {
        return (InstanceDiscoveryMetadata) sAadAuthorityHostMetadata.get(url.getHost().toLowerCase(Locale.US));
    }

    static void processInstanceDiscoveryMetadata(URL url, Map<String, String> map) throws JSONException {
        boolean containsKey = map.containsKey(TENANT_DISCOVERY_ENDPOINT);
        String str = (String) map.get("metadata");
        String lowerCase = url.getHost().toLowerCase(Locale.US);
        if (!containsKey) {
            sAadAuthorityHostMetadata.put(lowerCase, new InstanceDiscoveryMetadata(false));
        } else if (StringExtensions.isNullOrBlank(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":processInstanceDiscoveryMetadata");
            Logger.m236v(sb.toString(), "No metadata returned from instance discovery.");
            sAadAuthorityHostMetadata.put(lowerCase, new InstanceDiscoveryMetadata(lowerCase, lowerCase));
        } else {
            processInstanceDiscoveryResponse(str);
        }
    }

    static void updateInstanceDiscoveryMap(String str, InstanceDiscoveryMetadata instanceDiscoveryMetadata) {
        sAadAuthorityHostMetadata.put(str.toLowerCase(Locale.US), instanceDiscoveryMetadata);
    }

    static Map<String, InstanceDiscoveryMetadata> getAuthorityValidationMetadataCache() {
        return Collections.unmodifiableMap(sAadAuthorityHostMetadata);
    }

    static void clearAuthorityValidationCache() {
        sAadAuthorityHostMetadata.clear();
    }

    private static void processInstanceDiscoveryResponse(String str) throws JSONException {
        JSONArray jSONArray = new JSONArray(str);
        for (int i = 0; i < jSONArray.length(); i++) {
            InstanceDiscoveryMetadata processSingleJsonArray = processSingleJsonArray(new JSONObject(jSONArray.get(i).toString()));
            for (String lowerCase : processSingleJsonArray.getAliases()) {
                sAadAuthorityHostMetadata.put(lowerCase.toLowerCase(Locale.US), processSingleJsonArray);
            }
        }
    }

    private static InstanceDiscoveryMetadata processSingleJsonArray(JSONObject jSONObject) throws JSONException {
        String string = jSONObject.getString(PREFERRED_NETWORK);
        String string2 = jSONObject.getString(PREFERRED_CACHE);
        JSONArray jSONArray = jSONObject.getJSONArray(ALIASES);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(jSONArray.getString(i));
        }
        return new InstanceDiscoveryMetadata(string, string2, arrayList);
    }
}
