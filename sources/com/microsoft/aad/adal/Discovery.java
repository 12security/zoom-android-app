package com.microsoft.aad.adal;

import android.content.Context;
import android.net.Uri.Builder;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;

final class Discovery {
    private static final Set<String> AAD_WHITELISTED_HOSTS = Collections.synchronizedSet(new HashSet());
    private static final Map<String, Set<URI>> ADFS_VALIDATED_AUTHORITIES = Collections.synchronizedMap(new HashMap());
    private static final String API_VERSION_KEY = "api-version";
    private static final String API_VERSION_VALUE = "1.1";
    private static final String AUTHORIZATION_COMMON_ENDPOINT = "/common/oauth2/authorize";
    private static final String AUTHORIZATION_ENDPOINT_KEY = "authorization_endpoint";
    private static final String INSTANCE_DISCOVERY_SUFFIX = "common/discovery/instance";
    private static final String TAG = "Discovery";
    private static final String TRUSTED_QUERY_INSTANCE = "login.microsoftonline.com";
    private static volatile ReentrantLock sInstanceDiscoveryNetworkRequestLock;
    private Context mContext;
    private UUID mCorrelationId;
    private final IWebRequestHandler mWebrequestHandler = new WebRequestHandler();

    public Discovery(Context context) {
        initValidList();
        this.mContext = context;
    }

    /* access modifiers changed from: 0000 */
    public void validateAuthorityADFS(URL url, String str) throws AuthenticationException {
        if (!StringExtensions.isNullOrBlank(str)) {
            validateADFS(url, str);
            return;
        }
        throw new IllegalArgumentException("Cannot validate AD FS Authority with domain [null]");
    }

    /* access modifiers changed from: 0000 */
    public void validateAuthority(URL url) throws AuthenticationException {
        verifyAuthorityValidInstance(url);
        if (!AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
            String lowerCase = url.getHost().toLowerCase(Locale.US);
            if (!AAD_WHITELISTED_HOSTS.contains(url.getHost().toLowerCase(Locale.US))) {
                lowerCase = TRUSTED_QUERY_INSTANCE;
            }
            try {
                sInstanceDiscoveryNetworkRequestLock = getLock();
                sInstanceDiscoveryNetworkRequestLock.lock();
                performInstanceDiscovery(url, lowerCase);
            } finally {
                sInstanceDiscoveryNetworkRequestLock.unlock();
            }
        }
    }

    private static void validateADFS(URL url, String str) throws AuthenticationException {
        try {
            URI uri = url.toURI();
            if (ADFS_VALIDATED_AUTHORITIES.get(str) == null || !((Set) ADFS_VALIDATED_AUTHORITIES.get(str)).contains(uri)) {
                if (ADFSWebFingerValidator.realmIsTrusted(uri, new WebFingerMetadataRequestor().requestMetadata(new WebFingerMetadataRequestParameters(url, new DRSMetadataRequestor().requestMetadata(str))))) {
                    if (ADFS_VALIDATED_AUTHORITIES.get(str) == null) {
                        ADFS_VALIDATED_AUTHORITIES.put(str, new HashSet());
                    }
                    ((Set) ADFS_VALIDATED_AUTHORITIES.get(str)).add(uri);
                    return;
                }
                throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
            }
        } catch (URISyntaxException unused) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, "Authority URL/URI must be RFC 2396 compliant to use AD FS validation");
        }
    }

    public void setCorrelationId(UUID uuid) {
        this.mCorrelationId = uuid;
    }

    private void initValidList() {
        if (AAD_WHITELISTED_HOSTS.isEmpty()) {
            AAD_WHITELISTED_HOSTS.add("login.windows.net");
            AAD_WHITELISTED_HOSTS.add(TRUSTED_QUERY_INSTANCE);
            AAD_WHITELISTED_HOSTS.add("login.chinacloudapi.cn");
            AAD_WHITELISTED_HOSTS.add("login.microsoftonline.de");
            AAD_WHITELISTED_HOSTS.add("login-us.microsoftonline.com");
            AAD_WHITELISTED_HOSTS.add("login.microsoftonline.us");
        }
    }

    private void performInstanceDiscovery(URL url, String str) throws AuthenticationException {
        if (!AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
            HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
            try {
                AuthorityValidationMetadataCache.processInstanceDiscoveryMetadata(url, sendRequest(buildQueryString(str, getAuthorizationCommonEndpoint(url))));
                if (!AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(url.getHost());
                    AuthorityValidationMetadataCache.updateInstanceDiscoveryMap(url.getHost(), new InstanceDiscoveryMetadata(url.getHost(), url.getHost(), arrayList));
                }
                if (!AuthorityValidationMetadataCache.isAuthorityValidated(url)) {
                    throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
                }
            } catch (IOException | JSONException e) {
                Logger.m232e("Discovery:performInstanceDiscovery", "Error when validating authority. ", "", ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e);
                throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE, e.getMessage(), (Throwable) e);
            }
        }
    }

    private Map<String, String> sendRequest(URL url) throws IOException, JSONException, AuthenticationException {
        StringBuilder sb = new StringBuilder();
        sb.append("queryUrl: ");
        sb.append(url);
        Logger.m237v(TAG, "Sending discovery request to query url. ", sb.toString(), null);
        HashMap hashMap = new HashMap();
        hashMap.put("Accept", WebRequestHandler.HEADER_ACCEPT_JSON);
        UUID uuid = this.mCorrelationId;
        if (uuid != null) {
            hashMap.put(AAD.CLIENT_REQUEST_ID, uuid.toString());
            hashMap.put(AAD.RETURN_CLIENT_REQUEST_ID, "true");
        }
        try {
            ClientMetrics.INSTANCE.beginClientMetricsRecord(url, this.mCorrelationId, hashMap);
            HttpWebResponse sendGet = this.mWebrequestHandler.sendGet(url, hashMap);
            ClientMetrics.INSTANCE.setLastError(null);
            Map<String, String> parseResponse = parseResponse(sendGet);
            if (!parseResponse.containsKey(OAuth2.ERROR_CODES)) {
                ClientMetrics.INSTANCE.endClientMetricsRecord(ClientMetricsEndpointType.INSTANCE_DISCOVERY, this.mCorrelationId);
                return parseResponse;
            }
            String str = (String) parseResponse.get(OAuth2.ERROR_CODES);
            ClientMetrics.INSTANCE.setLastError(str);
            ADALError aDALError = ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Fail to valid authority with errors: ");
            sb2.append(str);
            throw new AuthenticationException(aDALError, sb2.toString());
        } catch (Throwable th) {
            ClientMetrics.INSTANCE.endClientMetricsRecord(ClientMetricsEndpointType.INSTANCE_DISCOVERY, this.mCorrelationId);
            throw th;
        }
    }

    static void verifyAuthorityValidInstance(URL url) throws AuthenticationException {
        if (url == null || StringExtensions.isNullOrBlank(url.getHost()) || !url.getProtocol().equals("https") || !StringExtensions.isNullOrBlank(url.getQuery()) || !StringExtensions.isNullOrBlank(url.getRef()) || StringExtensions.isNullOrBlank(url.getPath())) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_INSTANCE);
        }
    }

    private Map<String, String> parseResponse(HttpWebResponse httpWebResponse) throws JSONException {
        return HashMapExtensions.getJsonResponse(httpWebResponse);
    }

    private String getAuthorizationCommonEndpoint(URL url) {
        return new Builder().scheme("https").authority(url.getHost()).appendPath(AUTHORIZATION_COMMON_ENDPOINT).build().toString();
    }

    private URL buildQueryString(String str, String str2) throws MalformedURLException {
        Builder builder = new Builder();
        builder.scheme("https").authority(str);
        builder.appendEncodedPath(INSTANCE_DISCOVERY_SUFFIX).appendQueryParameter(API_VERSION_KEY, API_VERSION_VALUE).appendQueryParameter(AUTHORIZATION_ENDPOINT_KEY, str2);
        return new URL(builder.build().toString());
    }

    private static ReentrantLock getLock() {
        if (sInstanceDiscoveryNetworkRequestLock == null) {
            synchronized (Discovery.class) {
                if (sInstanceDiscoveryNetworkRequestLock == null) {
                    sInstanceDiscoveryNetworkRequestLock = new ReentrantLock();
                }
            }
        }
        return sInstanceDiscoveryNetworkRequestLock;
    }

    static Set<String> getValidHosts() {
        return AAD_WHITELISTED_HOSTS;
    }
}
