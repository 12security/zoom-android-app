package com.microsoft.aad.adal;

import android.content.Context;
import android.os.Handler;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class AuthenticationParameters {
    public static final String AUTHENTICATE_HEADER = "WWW-Authenticate";
    public static final String AUTHORITY_KEY = "authorization_uri";
    public static final String AUTH_HEADER_INVALID_FORMAT = "Invalid authentication header format";
    public static final String AUTH_HEADER_MISSING = "WWW-Authenticate header was expected in the response";
    public static final String AUTH_HEADER_MISSING_AUTHORITY = "WWW-Authenticate header is missing authorization_uri.";
    public static final String AUTH_HEADER_WRONG_STATUS = "Unauthorized http response (status code 401) was expected";
    public static final String BEARER = "bearer";
    public static final String RESOURCE_KEY = "resource_id";
    private static final String TAG = "AuthenticationParameters";
    private static ExecutorService sThreadExecutor = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public static IWebRequestHandler sWebRequest = new WebRequestHandler();
    private String mAuthority;
    private String mResource;

    public interface AuthenticationParamCallback {
        void onCompleted(Exception exc, AuthenticationParameters authenticationParameters);
    }

    private static final class Challenge {
        private static final String REGEX_SPLIT_UNQUOTED_COMMA = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        private static final String REGEX_SPLIT_UNQUOTED_EQUALS = "=(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        private static final String REGEX_STRING_TOKEN_WITH_SCHEME = "^([^\\s|^=]+)[\\s|\\t]+([^=]*=[^=]*)+$";
        private static final String REGEX_UNQUOTED_LOOKAHEAD = "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        private static final String SUFFIX_COMMA = ", ";
        private Map<String, String> mParameters;
        private String mScheme;

        private Challenge(String str, Map<String, String> map) {
            this.mScheme = str;
            this.mParameters = map;
        }

        public String getScheme() {
            return this.mScheme;
        }

        public Map<String, String> getParameters() {
            return this.mParameters;
        }

        static Challenge parseChallenge(String str) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(str)) {
                String parseScheme = parseScheme(str);
                StringBuilder sb = new StringBuilder();
                sb.append("Scheme value [");
                sb.append(parseScheme);
                sb.append("]");
                Logger.m234i("AuthenticationParameters:parseChallenge", "Parsing scheme", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[");
                sb2.append(str);
                sb2.append("]");
                Logger.m234i("AuthenticationParameters:parseChallenge", "Removing scheme from source challenge", sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Parsing challenge substr. Total length: ");
                sb3.append(str.length());
                sb3.append(" Scheme index: ");
                sb3.append(parseScheme.length());
                sb3.append(1);
                Logger.m236v("AuthenticationParameters:parseChallenge", sb3.toString());
                return new Challenge(parseScheme, parseParams(str.substring(parseScheme.length() + 1)));
            }
            Logger.m238w("AuthenticationParameters:parseChallenge", "Cannot parse null/empty challenge.");
            throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_MISSING);
        }

        private static String parseScheme(String str) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(str)) {
                int indexOf = str.indexOf(32);
                int indexOf2 = str.indexOf(9);
                if (indexOf >= 0 || indexOf2 >= 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Parsing scheme with indices: indexOfFirstSpace[");
                    sb.append(indexOf);
                    sb.append("] indexOfFirstTab[");
                    sb.append(indexOf2);
                    sb.append("]");
                    Logger.m236v("AuthenticationParameters:parseScheme", sb.toString());
                    if (indexOf > -1 && (indexOf < indexOf2 || indexOf2 < 0)) {
                        return str.substring(0, indexOf);
                    }
                    if (indexOf2 > -1 && (indexOf2 < indexOf || indexOf < 0)) {
                        return str.substring(0, indexOf2);
                    }
                    Logger.m238w("AuthenticationParameters:parseScheme", "Unexpected/malformed/missing scheme.");
                    throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
                }
                Logger.m238w("AuthenticationParameters:parseScheme", "Couldn't locate space/tab char - returning input String");
                return str;
            }
            Logger.m238w("AuthenticationParameters:parseScheme", "Cannot parse an empty/blank challenge");
            throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_MISSING);
        }

        private static Map<String, String> parseParams(String str) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(str)) {
                HashMap hashMap = new HashMap();
                StringBuilder sb = new StringBuilder();
                sb.append("in-value [");
                sb.append(str);
                sb.append("]");
                Logger.m234i("AuthenticationParameters:parseParams", "Splitting on unquoted commas...", sb.toString());
                String[] split = str.split(REGEX_SPLIT_UNQUOTED_COMMA, -1);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("out-value [");
                sb2.append(Arrays.toString(split));
                sb2.append("]");
                Logger.m234i("AuthenticationParameters:parseParams", "Splitting on unquoted commas...", sb2.toString());
                int length = split.length;
                int i = 0;
                while (i < length) {
                    String str2 = split[i];
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("in-value [");
                    sb3.append(str2);
                    sb3.append("]");
                    Logger.m234i("AuthenticationParameters:parseParams", "Splitting on unquoted equals...", sb3.toString());
                    String[] split2 = str2.split(REGEX_SPLIT_UNQUOTED_EQUALS, -1);
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("out-value [");
                    sb4.append(Arrays.toString(split2));
                    sb4.append("]");
                    Logger.m234i("AuthenticationParameters:parseParams", "Splitting on unquoted equals...", sb4.toString());
                    if (split2.length == 2) {
                        Logger.m236v("AuthenticationParameters:parseParams", "Trimming split-string whitespace");
                        String trim = split2[0].trim();
                        String trim2 = split2[1].trim();
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("key[");
                        sb5.append(trim);
                        sb5.append("]");
                        Logger.m234i("AuthenticationParameters:parseParams", "", sb5.toString());
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("value[");
                        sb6.append(trim2);
                        sb6.append("]");
                        Logger.m234i("AuthenticationParameters:parseParams", "", sb6.toString());
                        if (hashMap.containsKey(trim)) {
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("Redundant key: ");
                            sb7.append(trim);
                            Logger.m239w(AuthenticationParameters.TAG, "Key/value pair list contains redundant key. ", sb7.toString(), ADALError.DEVELOPER_BEARER_HEADER_MULTIPLE_ITEMS);
                        }
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("put(");
                        sb8.append(trim);
                        sb8.append(SUFFIX_COMMA);
                        sb8.append(trim2);
                        sb8.append(")");
                        Logger.m234i("AuthenticationParameters:parseParams", "", sb8.toString());
                        hashMap.put(trim, trim2);
                        i++;
                    } else {
                        Logger.m238w("AuthenticationParameters:parseParams", "Splitting on equals yielded mismatched key/value.");
                        throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
                    }
                }
                if (!hashMap.isEmpty()) {
                    return hashMap;
                }
                Logger.m238w("AuthenticationParameters:parseParams", "Parsed params were empty.");
                throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
            }
            Logger.m238w("AuthenticationParameters:parseParams", "ChallengeSansScheme was null/empty");
            throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
        }

        static List<Challenge> parseChallenges(String str) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(str)) {
                ArrayList arrayList = new ArrayList();
                String str2 = "AuthenticationParameters:parseChallenges";
                String str3 = "Separating challenges...";
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("input[");
                    sb.append(str);
                    sb.append("]");
                    Logger.m234i(str2, str3, sb.toString());
                    for (String parseChallenge : separateChallenges(str)) {
                        arrayList.add(parseChallenge(parseChallenge));
                    }
                    return arrayList;
                } catch (ResourceAuthenticationChallengeException e) {
                    Logger.m239w("AuthenticationParameters:parseChallenges", "Encountered error during parsing...", e.getMessage(), null);
                    throw e;
                } catch (Exception e2) {
                    Logger.m239w("AuthenticationParameters:parseChallenges", "Encountered error during parsing...", e2.getMessage(), null);
                    throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
                }
            } else {
                Logger.m238w("AuthenticationParameters:parseChallenges", "Cannot parse empty/blank challenges.");
                throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_MISSING);
            }
        }

        private static List<String> separateChallenges(String str) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append("input[");
                sb.append(str);
                sb.append("]");
                Logger.m234i("AuthenticationParameters:separateChallenges", "Splitting input String on unquoted commas", sb.toString());
                String[] split = str.split(REGEX_SPLIT_UNQUOTED_COMMA, -1);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("output[");
                sb2.append(Arrays.toString(split));
                sb2.append("]");
                Logger.m234i("AuthenticationParameters:separateChallenges", "Splitting input String on unquoted commas", sb2.toString());
                sanitizeWhitespace(split);
                List extractTokensContainingScheme = extractTokensContainingScheme(split);
                String[] strArr = new String[extractTokensContainingScheme.size()];
                for (int i = 0; i < strArr.length; i++) {
                    strArr[i] = "";
                }
                writeParsedChallenges(split, extractTokensContainingScheme, strArr);
                sanitizeParsedSuffixes(strArr);
                return Arrays.asList(strArr);
            }
            Logger.m238w("AuthenticationParameters:separateChallenges", "Input String was null");
            throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
        }

        private static void writeParsedChallenges(String[] strArr, List<String> list, String[] strArr2) {
            int i = -1;
            for (String str : strArr) {
                if (list.contains(str)) {
                    i++;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(SUFFIX_COMMA);
                    strArr2[i] = sb.toString();
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(strArr2[i]);
                    sb2.append(str);
                    sb2.append(SUFFIX_COMMA);
                    strArr2[i] = sb2.toString();
                }
            }
        }

        private static void sanitizeParsedSuffixes(String[] strArr) {
            for (int i = 0; i < strArr.length; i++) {
                if (strArr[i].endsWith(SUFFIX_COMMA)) {
                    strArr[i] = strArr[i].substring(0, strArr[i].length() - 2);
                }
            }
        }

        private static List<String> extractTokensContainingScheme(String[] strArr) throws ResourceAuthenticationChallengeException {
            ArrayList arrayList = new ArrayList();
            for (String str : strArr) {
                if (containsScheme(str)) {
                    arrayList.add(str);
                }
            }
            return arrayList;
        }

        private static boolean containsScheme(String str) throws ResourceAuthenticationChallengeException {
            if (!StringExtensions.isNullOrBlank(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append("input[");
                sb.append(str);
                sb.append("]");
                Logger.m234i("AuthenticationParameters:containsScheme", "Testing token contains scheme", sb.toString());
                boolean matches = Pattern.compile(REGEX_STRING_TOKEN_WITH_SCHEME).matcher(str).matches();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Matches? [");
                sb2.append(matches);
                sb2.append("]");
                Logger.m234i("AuthenticationParameters:containsScheme", "Testing String contains scheme", sb2.toString());
                return matches;
            }
            Logger.m238w("AuthenticationParameters:containsScheme", "Null/blank potential scheme token");
            throw new ResourceAuthenticationChallengeException(AuthenticationParameters.AUTH_HEADER_INVALID_FORMAT);
        }

        private static void sanitizeWhitespace(String[] strArr) {
            Logger.m236v("AuthenticationParameters:sanitizeWhitespace", "Sanitizing whitespace");
            for (int i = 0; i < strArr.length; i++) {
                strArr[i] = strArr[i].trim();
            }
        }
    }

    public String getAuthority() {
        return this.mAuthority;
    }

    public String getResource() {
        return this.mResource;
    }

    public AuthenticationParameters() {
    }

    AuthenticationParameters(String str, String str2) {
        this.mAuthority = str;
        this.mResource = str2;
    }

    public static void createFromResourceUrl(Context context, final URL url, final AuthenticationParamCallback authenticationParamCallback) {
        if (authenticationParamCallback != null) {
            Logger.m236v(TAG, "createFromResourceUrl");
            final Handler handler = new Handler(context.getMainLooper());
            sThreadExecutor.submit(new Runnable() {
                public void run() {
                    HashMap hashMap = new HashMap();
                    hashMap.put("Accept", WebRequestHandler.HEADER_ACCEPT_JSON);
                    try {
                        try {
                            onCompleted(null, AuthenticationParameters.parseResponse(AuthenticationParameters.sWebRequest.sendGet(url, hashMap)));
                        } catch (ResourceAuthenticationChallengeException e) {
                            onCompleted(e, null);
                        }
                    } catch (IOException e2) {
                        onCompleted(e2, null);
                    }
                }

                /* access modifiers changed from: 0000 */
                public void onCompleted(final Exception exc, final AuthenticationParameters authenticationParameters) {
                    handler.post(new Runnable() {
                        public void run() {
                            authenticationParamCallback.onCompleted(exc, authenticationParameters);
                        }
                    });
                }
            });
            return;
        }
        throw new IllegalArgumentException(QueryParameters.CALLBACK);
    }

    public static AuthenticationParameters createFromResponseAuthenticateHeader(String str) throws ResourceAuthenticationChallengeException {
        if (!StringExtensions.isNullOrBlank(str)) {
            Logger.m236v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing challenges - BEGIN");
            List parseChallenges = Challenge.parseChallenges(str);
            Logger.m236v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing challenge - END");
            Challenge challenge = null;
            Logger.m236v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Looking for Bearer challenge.");
            Iterator it = parseChallenges.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Challenge challenge2 = (Challenge) it.next();
                if (BEARER.equalsIgnoreCase(challenge2.getScheme())) {
                    Logger.m236v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Found Bearer challenge.");
                    challenge = challenge2;
                    break;
                }
            }
            if (challenge != null) {
                Map parameters = challenge.getParameters();
                String str2 = (String) parameters.get("authorization_uri");
                String str3 = (String) parameters.get(RESOURCE_KEY);
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                sb.append(str2);
                sb.append("]");
                Logger.m234i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Bearer authority", sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("[");
                sb2.append(str3);
                sb2.append("]");
                Logger.m234i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Bearer resource", sb2.toString());
                if (!StringExtensions.isNullOrBlank(str2)) {
                    Logger.m236v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing leading/trailing \"\"'s (authority)");
                    String replaceAll = str2.replaceAll("^\"|\"$", "");
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("[");
                    sb3.append(replaceAll);
                    sb3.append("]");
                    Logger.m234i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Sanitized authority value", sb3.toString());
                    if (!StringExtensions.isNullOrBlank(replaceAll)) {
                        if (!StringExtensions.isNullOrBlank(str3)) {
                            Logger.m236v("AuthenticationParameters:createFromResponseAuthenticateHeader", "Parsing leading/trailing \"\"'s (resource)");
                            str3 = str3.replaceAll("^\"|\"$", "");
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("[");
                            sb4.append(replaceAll);
                            sb4.append("]");
                            Logger.m234i("AuthenticationParameters:createFromResponseAuthenticateHeader", "Sanitized resource value", sb4.toString());
                        }
                        return new AuthenticationParameters(replaceAll, str3);
                    }
                    Logger.m238w("AuthenticationParameters:createFromResponseAuthenticateHeader", "Sanitized authority is null/empty.");
                    throw new ResourceAuthenticationChallengeException(AUTH_HEADER_MISSING_AUTHORITY);
                }
                Logger.m238w("AuthenticationParameters:createFromResponseAuthenticateHeader", "Null/empty authority.");
                throw new ResourceAuthenticationChallengeException(AUTH_HEADER_MISSING_AUTHORITY);
            }
            Logger.m238w("AuthenticationParameters:createFromResponseAuthenticateHeader", "Did not locate Bearer challenge.");
            throw new ResourceAuthenticationChallengeException(AUTH_HEADER_INVALID_FORMAT);
        }
        Logger.m238w("AuthenticationParameters:createFromResponseAuthenticateHeader", "authenticateHeader was null/empty.");
        throw new ResourceAuthenticationChallengeException(AUTH_HEADER_MISSING);
    }

    /* access modifiers changed from: private */
    public static AuthenticationParameters parseResponse(HttpWebResponse httpWebResponse) throws ResourceAuthenticationChallengeException {
        if (httpWebResponse.getStatusCode() == 401) {
            Map responseHeaders = httpWebResponse.getResponseHeaders();
            if (responseHeaders != null && responseHeaders.containsKey("WWW-Authenticate")) {
                List list = (List) responseHeaders.get("WWW-Authenticate");
                if (list != null && list.size() > 0) {
                    return createFromResponseAuthenticateHeader((String) list.get(0));
                }
            }
            throw new ResourceAuthenticationChallengeException(AUTH_HEADER_MISSING);
        }
        throw new ResourceAuthenticationChallengeException(AUTH_HEADER_WRONG_STATUS);
    }
}
