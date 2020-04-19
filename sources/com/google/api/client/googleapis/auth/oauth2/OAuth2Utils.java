package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.Beta;
import com.zipow.videobox.util.ZMDomainUtil;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public class OAuth2Utils {
    private static final int COMPUTE_PING_CONNECTION_TIMEOUT_MS = 500;
    private static final String DEFAULT_METADATA_SERVER_URL = "http://169.254.169.254";
    private static final Logger LOGGER = Logger.getLogger(OAuth2Utils.class.getName());
    private static final int MAX_COMPUTE_PING_TRIES = 3;
    static final Charset UTF_8 = Charset.forName("UTF-8");

    static <T extends Throwable> T exceptionWithCause(T t, Throwable th) {
        t.initCause(th);
        return t;
    }

    static boolean headersContainValue(HttpHeaders httpHeaders, String str, String str2) {
        Object obj = httpHeaders.get(str);
        if (obj instanceof Collection) {
            for (Object next : (Collection) obj) {
                if ((next instanceof String) && ((String) next).equals(str2)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean runningOnComputeEngine(HttpTransport httpTransport, SystemEnvironmentProvider systemEnvironmentProvider) {
        HttpResponse execute;
        if (Boolean.parseBoolean(systemEnvironmentProvider.getEnv("NO_GCE_CHECK"))) {
            return false;
        }
        GenericUrl genericUrl = new GenericUrl(getMetadataServerUrl(systemEnvironmentProvider));
        int i = 1;
        while (i <= 3) {
            try {
                HttpRequest buildGetRequest = httpTransport.createRequestFactory().buildGetRequest(genericUrl);
                buildGetRequest.setConnectTimeout(500);
                execute = buildGetRequest.execute();
                boolean headersContainValue = headersContainValue(execute.getHeaders(), "Metadata-Flavor", "Google");
                execute.disconnect();
                return headersContainValue;
            } catch (SocketTimeoutException unused) {
                i++;
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Failed to detect whether we are running on Google Compute Engine.", e);
                i++;
            } catch (Throwable th) {
                execute.disconnect();
                throw th;
            }
        }
        return false;
    }

    public static String getMetadataServerUrl() {
        return getMetadataServerUrl(SystemEnvironmentProvider.INSTANCE);
    }

    static String getMetadataServerUrl(SystemEnvironmentProvider systemEnvironmentProvider) {
        String env = systemEnvironmentProvider.getEnv("GCE_METADATA_HOST");
        if (env == null) {
            return DEFAULT_METADATA_SERVER_URL;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(ZMDomainUtil.ZM_URL_HTTP);
        sb.append(env);
        return sb.toString();
    }
}
