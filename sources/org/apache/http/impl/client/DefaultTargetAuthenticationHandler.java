package org.apache.http.impl.client;

import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE)
@Deprecated
public class DefaultTargetAuthenticationHandler extends AbstractAuthenticationHandler {
    public boolean isAuthenticationRequested(HttpResponse httpResponse, HttpContext httpContext) {
        Args.notNull(httpResponse, "HTTP response");
        return httpResponse.getStatusLine().getStatusCode() == 401;
    }

    public Map<String, Header> getChallenges(HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException {
        Args.notNull(httpResponse, "HTTP response");
        return parseChallenges(httpResponse.getHeaders("WWW-Authenticate"));
    }

    /* access modifiers changed from: protected */
    public List<String> getAuthPreferences(HttpResponse httpResponse, HttpContext httpContext) {
        List<String> list = (List) httpResponse.getParams().getParameter(AuthPNames.TARGET_AUTH_PREF);
        if (list != null) {
            return list;
        }
        return super.getAuthPreferences(httpResponse, httpContext);
    }
}
