package com.box.androidsdk.content.auth;

import android.content.Context;
import android.content.Intent;
import com.box.androidsdk.content.BoxApiUser;
import com.box.androidsdk.content.BoxConfig;
import com.box.androidsdk.content.BoxConstants;
import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.BoxException.RefreshFailure;
import com.box.androidsdk.content.models.BoxCollaborator;
import com.box.androidsdk.content.models.BoxJsonObject;
import com.box.androidsdk.content.models.BoxMapJsonObject;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxUser;
import com.box.androidsdk.content.utils.BoxLogUtils;
import com.box.androidsdk.content.utils.SdkUtils;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import p021us.zoom.androidlib.util.TimeUtil;

public class BoxAuthentication {
    public static final ThreadPoolExecutor AUTH_EXECUTOR = SdkUtils.createDefaultThreadPoolExecutor(1, 1, TimeUtil.ONE_HOUR_IN_SECONDS, TimeUnit.SECONDS);
    private static String TAG = BoxAuthentication.class.getName();
    private static BoxAuthentication mAuthentication = new BoxAuthentication();
    private int EXPIRATION_GRACE = 1000;
    /* access modifiers changed from: private */
    public AuthStorage authStorage = new AuthStorage();
    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, BoxAuthenticationInfo> mCurrentAccessInfo;
    /* access modifiers changed from: private */
    public ConcurrentLinkedQueue<WeakReference<AuthListener>> mListeners = new ConcurrentLinkedQueue<>();
    /* access modifiers changed from: private */
    public AuthenticationRefreshProvider mRefreshProvider;
    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, FutureTask> mRefreshingTasks = new ConcurrentHashMap<>();

    public interface AuthListener {
        void onAuthCreated(BoxAuthenticationInfo boxAuthenticationInfo);

        void onAuthFailure(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc);

        void onLoggedOut(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc);

        void onRefreshed(BoxAuthenticationInfo boxAuthenticationInfo);
    }

    public static class AuthStorage {
        private static final String AUTH_MAP_STORAGE_KEY;
        private static final String AUTH_STORAGE_LAST_AUTH_USER_ID_KEY;
        private static final String AUTH_STORAGE_NAME;

        static {
            StringBuilder sb = new StringBuilder();
            sb.append(AuthStorage.class.getCanonicalName());
            sb.append("_SharedPref");
            AUTH_STORAGE_NAME = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(AuthStorage.class.getCanonicalName());
            sb2.append("_authInfoMap");
            AUTH_MAP_STORAGE_KEY = sb2.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(AuthStorage.class.getCanonicalName());
            sb3.append("_lastAuthUserId");
            AUTH_STORAGE_LAST_AUTH_USER_ID_KEY = sb3.toString();
        }

        /* access modifiers changed from: protected */
        public void storeAuthInfoMap(Map<String, BoxAuthenticationInfo> map, Context context) {
            HashMap hashMap = new HashMap();
            for (String str : map.keySet()) {
                hashMap.put(str, map.get(str));
            }
            context.getSharedPreferences(AUTH_STORAGE_NAME, 0).edit().putString(AUTH_MAP_STORAGE_KEY, new BoxMapJsonObject(hashMap).toJson()).apply();
        }

        /* access modifiers changed from: protected */
        public void clearAuthInfoMap(Context context) {
            context.getSharedPreferences(AUTH_STORAGE_NAME, 0).edit().remove(AUTH_MAP_STORAGE_KEY).apply();
        }

        /* access modifiers changed from: protected */
        public void storeLastAuthenticatedUserId(String str, Context context) {
            if (SdkUtils.isEmptyString(str)) {
                context.getSharedPreferences(AUTH_STORAGE_NAME, 0).edit().remove(AUTH_STORAGE_LAST_AUTH_USER_ID_KEY).apply();
            } else {
                context.getSharedPreferences(AUTH_STORAGE_NAME, 0).edit().putString(AUTH_STORAGE_LAST_AUTH_USER_ID_KEY, str).apply();
            }
        }

        /* access modifiers changed from: protected */
        public String getLastAuthentictedUserId(Context context) {
            return context.getSharedPreferences(AUTH_STORAGE_NAME, 0).getString(AUTH_STORAGE_LAST_AUTH_USER_ID_KEY, null);
        }

        /* access modifiers changed from: protected */
        public ConcurrentHashMap<String, BoxAuthenticationInfo> loadAuthInfoMap(Context context) {
            ConcurrentHashMap<String, BoxAuthenticationInfo> concurrentHashMap = new ConcurrentHashMap<>();
            String string = context.getSharedPreferences(AUTH_STORAGE_NAME, 0).getString(AUTH_MAP_STORAGE_KEY, "");
            if (string.length() > 0) {
                BoxMapJsonObject boxMapJsonObject = new BoxMapJsonObject();
                boxMapJsonObject.createFromJson(string);
                for (Entry entry : boxMapJsonObject.getPropertiesAsHashMap().entrySet()) {
                    BoxAuthenticationInfo boxAuthenticationInfo = null;
                    if (entry.getValue() instanceof String) {
                        boxAuthenticationInfo = new BoxAuthenticationInfo();
                        boxAuthenticationInfo.createFromJson((String) entry.getValue());
                    } else if (entry.getValue() instanceof BoxAuthenticationInfo) {
                        boxAuthenticationInfo = (BoxAuthenticationInfo) entry.getValue();
                    }
                    concurrentHashMap.put(entry.getKey(), boxAuthenticationInfo);
                }
            }
            return concurrentHashMap;
        }
    }

    public interface AuthenticationRefreshProvider {
        boolean launchAuthUi(String str, BoxSession boxSession);

        BoxAuthenticationInfo refreshAuthenticationInfo(BoxAuthenticationInfo boxAuthenticationInfo) throws BoxException;
    }

    public static class BoxAuthenticationInfo extends BoxJsonObject {
        public static final String FIELD_ACCESS_TOKEN = "access_token";
        public static final String FIELD_BASE_DOMAIN = "base_domain";
        public static final String FIELD_CLIENT_ID = "client_id";
        public static final String FIELD_EXPIRES_IN = "expires_in";
        private static final String FIELD_REFRESH_TIME = "refresh_time";
        public static final String FIELD_REFRESH_TOKEN = "refresh_token";
        public static final String FIELD_USER = "user";
        private static final long serialVersionUID = 2878150977399126399L;

        public BoxAuthenticationInfo clone() {
            BoxAuthenticationInfo boxAuthenticationInfo = new BoxAuthenticationInfo();
            cloneInfo(boxAuthenticationInfo, this);
            return boxAuthenticationInfo;
        }

        public static void cloneInfo(BoxAuthenticationInfo boxAuthenticationInfo, BoxAuthenticationInfo boxAuthenticationInfo2) {
            boxAuthenticationInfo.setAccessToken(boxAuthenticationInfo2.accessToken());
            boxAuthenticationInfo.setRefreshToken(boxAuthenticationInfo2.refreshToken());
            boxAuthenticationInfo.setRefreshTime(boxAuthenticationInfo2.getRefreshTime());
            boxAuthenticationInfo.setClientId(boxAuthenticationInfo2.getClientId());
            boxAuthenticationInfo.setBaseDomain(boxAuthenticationInfo2.getBaseDomain());
            if (boxAuthenticationInfo.getUser() == null) {
                boxAuthenticationInfo.setUser(boxAuthenticationInfo2.getUser());
            }
        }

        public String getClientId() {
            return (String) this.mProperties.get("client_id");
        }

        public String accessToken() {
            return (String) this.mProperties.get("access_token");
        }

        public String refreshToken() {
            return (String) this.mProperties.get("refresh_token");
        }

        public Long expiresIn() {
            return (Long) this.mProperties.get("expires_in");
        }

        public Long getRefreshTime() {
            return (Long) this.mProperties.get(FIELD_REFRESH_TIME);
        }

        public void setRefreshTime(Long l) {
            this.mProperties.put(FIELD_REFRESH_TIME, l);
        }

        public void setClientId(String str) {
            this.mProperties.put("client_id", str);
        }

        public void setAccessToken(String str) {
            this.mProperties.put("access_token", str);
        }

        public void setRefreshToken(String str) {
            this.mProperties.put("refresh_token", str);
        }

        public void setBaseDomain(String str) {
            this.mProperties.put(FIELD_BASE_DOMAIN, str);
        }

        public String getBaseDomain() {
            return (String) this.mProperties.get(FIELD_BASE_DOMAIN);
        }

        public void setUser(BoxUser boxUser) {
            this.mProperties.put("user", boxUser);
        }

        public BoxUser getUser() {
            return (BoxUser) this.mProperties.get("user");
        }

        public void wipeOutAuth() {
            setUser(null);
            setClientId(null);
            setAccessToken(null);
            setRefreshToken(null);
        }

        /* access modifiers changed from: protected */
        public void parseJSONMember(Member member) {
            String name = member.getName();
            JsonValue value = member.getValue();
            if (name.equals("access_token")) {
                this.mProperties.put("access_token", value.asString());
            } else if (name.equals("refresh_token")) {
                this.mProperties.put("refresh_token", value.asString());
            } else if (name.equals("user")) {
                this.mProperties.put("user", BoxCollaborator.createCollaboratorFromJson(value.asObject()));
            } else if (name.equals("expires_in")) {
                this.mProperties.put("expires_in", Long.valueOf(value.asLong()));
            } else if (name.equals(FIELD_REFRESH_TIME)) {
                this.mProperties.put(FIELD_REFRESH_TIME, Long.valueOf(SdkUtils.parseJsonValueToLong(value)));
            } else if (name.equals("client_id")) {
                this.mProperties.put("client_id", value.asString());
            } else {
                super.parseJSONMember(member);
            }
        }
    }

    private BoxAuthentication() {
    }

    private BoxAuthentication(AuthenticationRefreshProvider authenticationRefreshProvider) {
        this.mRefreshProvider = authenticationRefreshProvider;
    }

    public BoxAuthenticationInfo getAuthInfo(String str, Context context) {
        if (str == null) {
            return null;
        }
        return (BoxAuthenticationInfo) getAuthInfoMap(context).get(str);
    }

    public Map<String, BoxAuthenticationInfo> getStoredAuthInfo(Context context) {
        return getAuthInfoMap(context);
    }

    public String getLastAuthenticatedUserId(Context context) {
        return this.authStorage.getLastAuthentictedUserId(context);
    }

    public static BoxAuthentication getInstance() {
        return mAuthentication;
    }

    public void setAuthStorage(AuthStorage authStorage2) {
        this.authStorage = authStorage2;
    }

    public AuthStorage getAuthStorage() {
        return this.authStorage;
    }

    public synchronized void startAuthenticationUI(BoxSession boxSession) {
        startAuthenticateUI(boxSession);
    }

    public synchronized void onAuthenticated(BoxAuthenticationInfo boxAuthenticationInfo, Context context) {
        getAuthInfoMap(context).put(boxAuthenticationInfo.getUser().getId(), boxAuthenticationInfo.clone());
        this.authStorage.storeLastAuthenticatedUserId(boxAuthenticationInfo.getUser().getId(), context);
        this.authStorage.storeAuthInfoMap(this.mCurrentAccessInfo, context);
        for (AuthListener onAuthCreated : getListeners()) {
            onAuthCreated.onAuthCreated(boxAuthenticationInfo);
        }
    }

    public synchronized void onAuthenticationFailure(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        for (AuthListener onAuthFailure : getListeners()) {
            onAuthFailure.onAuthFailure(boxAuthenticationInfo, exc);
        }
    }

    public synchronized void onLoggedOut(BoxAuthenticationInfo boxAuthenticationInfo, Exception exc) {
        for (AuthListener onLoggedOut : getListeners()) {
            onLoggedOut.onLoggedOut(boxAuthenticationInfo, exc);
        }
    }

    public Set<AuthListener> getListeners() {
        LinkedHashSet<AuthListener> linkedHashSet = new LinkedHashSet<>();
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            AuthListener authListener = (AuthListener) ((WeakReference) it.next()).get();
            if (authListener != null) {
                linkedHashSet.add(authListener);
            }
        }
        if (this.mListeners.size() > linkedHashSet.size()) {
            this.mListeners = new ConcurrentLinkedQueue<>();
            for (AuthListener weakReference : linkedHashSet) {
                this.mListeners.add(new WeakReference(weakReference));
            }
        }
        return linkedHashSet;
    }

    private void clearCache(BoxSession boxSession) {
        File cacheDir = boxSession.getCacheDir();
        if (cacheDir.exists()) {
            File[] listFiles = cacheDir.listFiles();
            if (listFiles != null) {
                for (File deleteFilesRecursively : listFiles) {
                    deleteFilesRecursively(deleteFilesRecursively);
                }
            }
        }
    }

    private void deleteFilesRecursively(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File deleteFilesRecursively : listFiles) {
                        deleteFilesRecursively(deleteFilesRecursively);
                    }
                }
            }
            file.delete();
        }
    }

    public synchronized void logout(BoxSession boxSession) {
        BoxUser user = boxSession.getUser();
        if (user != null) {
            clearCache(boxSession);
            Context applicationContext = boxSession.getApplicationContext();
            String id = user.getId();
            getAuthInfoMap(boxSession.getApplicationContext());
            BoxAuthenticationInfo boxAuthenticationInfo = (BoxAuthenticationInfo) this.mCurrentAccessInfo.get(id);
            try {
                new BoxApiAuthentication(boxSession).revokeOAuth(boxAuthenticationInfo.refreshToken(), boxSession.getClientId(), boxSession.getClientSecret()).send();
                e = null;
            } catch (Exception e) {
                e = e;
                BoxLogUtils.m12e(TAG, "logout", e);
            }
            this.mCurrentAccessInfo.remove(id);
            if (this.authStorage.getLastAuthentictedUserId(applicationContext) != null && id.equals(id)) {
                this.authStorage.storeLastAuthenticatedUserId(null, applicationContext);
            }
            this.authStorage.storeAuthInfoMap(this.mCurrentAccessInfo, applicationContext);
            onLoggedOut(boxAuthenticationInfo, e);
        }
    }

    public synchronized void logoutAllUsers(Context context) {
        getAuthInfoMap(context);
        for (String boxSession : this.mCurrentAccessInfo.keySet()) {
            logout(new BoxSession(context, boxSession));
        }
    }

    public synchronized FutureTask<BoxAuthenticationInfo> create(BoxSession boxSession, String str) throws BoxException {
        FutureTask<BoxAuthenticationInfo> doCreate;
        doCreate = doCreate(boxSession, str);
        AUTH_EXECUTOR.submit(doCreate);
        return doCreate;
    }

    public synchronized FutureTask<BoxAuthenticationInfo> refresh(BoxSession boxSession) throws BoxException {
        BoxUser user = boxSession.getUser();
        if (user == null) {
            return doRefresh(boxSession, boxSession.getAuthInfo());
        }
        getAuthInfoMap(boxSession.getApplicationContext());
        final BoxAuthenticationInfo boxAuthenticationInfo = (BoxAuthenticationInfo) this.mCurrentAccessInfo.get(user.getId());
        if (boxAuthenticationInfo == null) {
            this.mCurrentAccessInfo.put(user.getId(), boxSession.getAuthInfo());
            boxAuthenticationInfo = (BoxAuthenticationInfo) this.mCurrentAccessInfo.get(user.getId());
        }
        if (!boxSession.getAuthInfo().accessToken().equals(boxAuthenticationInfo.accessToken())) {
            BoxAuthenticationInfo.cloneInfo(boxSession.getAuthInfo(), boxAuthenticationInfo);
            return new FutureTask<>(new Callable<BoxAuthenticationInfo>() {
                public BoxAuthenticationInfo call() throws Exception {
                    return boxAuthenticationInfo;
                }
            });
        }
        FutureTask<BoxAuthenticationInfo> futureTask = (FutureTask) this.mRefreshingTasks.get(user.getId());
        if (futureTask != null && !futureTask.isCancelled() && !futureTask.isDone()) {
            return futureTask;
        }
        return doRefresh(boxSession, boxAuthenticationInfo);
    }

    private FutureTask<BoxAuthenticationInfo> doCreate(final BoxSession boxSession, final String str) {
        return new FutureTask<>(new Callable<BoxAuthenticationInfo>() {
            public BoxAuthenticationInfo call() throws Exception {
                BoxCreateAuthRequest createOAuth = new BoxApiAuthentication(boxSession).createOAuth(str, boxSession.getClientId(), boxSession.getClientSecret());
                BoxAuthenticationInfo boxAuthenticationInfo = new BoxAuthenticationInfo();
                BoxAuthenticationInfo.cloneInfo(boxAuthenticationInfo, boxSession.getAuthInfo());
                BoxAuthenticationInfo boxAuthenticationInfo2 = (BoxAuthenticationInfo) createOAuth.send();
                boxAuthenticationInfo.setAccessToken(boxAuthenticationInfo2.accessToken());
                boxAuthenticationInfo.setRefreshToken(boxAuthenticationInfo2.refreshToken());
                boxAuthenticationInfo.setRefreshTime(Long.valueOf(System.currentTimeMillis()));
                boxAuthenticationInfo.setUser((BoxUser) new BoxApiUser(new BoxSession(boxSession.getApplicationContext(), boxAuthenticationInfo, (AuthenticationRefreshProvider) null)).getCurrentUserInfoRequest().send());
                BoxAuthentication.getInstance().onAuthenticated(boxAuthenticationInfo, boxSession.getApplicationContext());
                return boxAuthenticationInfo;
            }
        });
    }

    public synchronized void addListener(AuthListener authListener) {
        this.mListeners.add(new WeakReference(authListener));
    }

    /* access modifiers changed from: protected */
    public synchronized void startAuthenticateUI(BoxSession boxSession) {
        Context applicationContext = boxSession.getApplicationContext();
        Intent createOAuthActivityIntent = OAuthActivity.createOAuthActivityIntent(applicationContext, boxSession, isBoxAuthAppAvailable(applicationContext) && boxSession.isEnabledBoxAppAuthentication());
        createOAuthActivityIntent.addFlags(268435456);
        applicationContext.startActivity(createOAuthActivityIntent);
    }

    /* access modifiers changed from: private */
    public RefreshFailure handleRefreshException(BoxException boxException, BoxAuthenticationInfo boxAuthenticationInfo) {
        RefreshFailure refreshFailure = new RefreshFailure(boxException);
        getInstance().onAuthenticationFailure(boxAuthenticationInfo, refreshFailure);
        return refreshFailure;
    }

    private FutureTask<BoxAuthenticationInfo> doRefresh(BoxSession boxSession, BoxAuthenticationInfo boxAuthenticationInfo) throws BoxException {
        final boolean z = boxAuthenticationInfo.getUser() == null && boxSession.getUser() == null;
        String userId = (!SdkUtils.isBlank(boxSession.getUserId()) || !z) ? boxSession.getUserId() : boxAuthenticationInfo.accessToken();
        final BoxSession boxSession2 = boxSession;
        final BoxAuthenticationInfo boxAuthenticationInfo2 = boxAuthenticationInfo;
        final String str = userId;
        C04393 r1 = new Callable<BoxAuthenticationInfo>() {
            public BoxAuthenticationInfo call() throws Exception {
                BoxAuthenticationInfo boxAuthenticationInfo;
                if (boxSession2.getRefreshProvider() != null) {
                    try {
                        boxAuthenticationInfo = boxSession2.getRefreshProvider().refreshAuthenticationInfo(boxAuthenticationInfo2);
                    } catch (BoxException e) {
                        BoxAuthentication.this.mRefreshingTasks.remove(str);
                        throw BoxAuthentication.this.handleRefreshException(e, boxAuthenticationInfo2);
                    }
                } else if (BoxAuthentication.this.mRefreshProvider != null) {
                    try {
                        boxAuthenticationInfo = BoxAuthentication.this.mRefreshProvider.refreshAuthenticationInfo(boxAuthenticationInfo2);
                    } catch (BoxException e2) {
                        BoxAuthentication.this.mRefreshingTasks.remove(str);
                        throw BoxAuthentication.this.handleRefreshException(e2, boxAuthenticationInfo2);
                    }
                } else {
                    String refreshToken = boxAuthenticationInfo2.refreshToken() != null ? boxAuthenticationInfo2.refreshToken() : "";
                    String clientId = boxSession2.getClientId() != null ? boxSession2.getClientId() : BoxConfig.CLIENT_ID;
                    String clientSecret = boxSession2.getClientSecret() != null ? boxSession2.getClientSecret() : BoxConfig.CLIENT_SECRET;
                    if (SdkUtils.isBlank(clientId) || SdkUtils.isBlank(clientSecret)) {
                        throw BoxAuthentication.this.handleRefreshException(new BoxException("client id or secret not specified", 400, "{\"error\": \"bad_request\",\n  \"error_description\": \"client id or secret not specified\"}", null), boxAuthenticationInfo2);
                    }
                    try {
                        boxAuthenticationInfo = new BoxApiAuthentication(boxSession2).refreshOAuth(refreshToken, clientId, clientSecret).send();
                    } catch (BoxException e3) {
                        BoxAuthentication.this.mRefreshingTasks.remove(str);
                        throw BoxAuthentication.this.handleRefreshException(e3, boxAuthenticationInfo2);
                    }
                }
                if (boxAuthenticationInfo != null) {
                    boxAuthenticationInfo.setRefreshTime(Long.valueOf(System.currentTimeMillis()));
                }
                BoxAuthenticationInfo.cloneInfo(boxSession2.getAuthInfo(), boxAuthenticationInfo);
                if (!(!z && boxSession2.getRefreshProvider() == null && BoxAuthentication.this.mRefreshProvider == null)) {
                    boxAuthenticationInfo2.setUser((BoxUser) new BoxApiUser(boxSession2).getCurrentUserInfoRequest().send());
                }
                BoxAuthentication.this.getAuthInfoMap(boxSession2.getApplicationContext()).put(boxAuthenticationInfo2.getUser().getId(), boxAuthenticationInfo);
                BoxAuthentication.this.authStorage.storeAuthInfoMap(BoxAuthentication.this.mCurrentAccessInfo, boxSession2.getApplicationContext());
                Iterator it = BoxAuthentication.this.mListeners.iterator();
                while (it.hasNext()) {
                    AuthListener authListener = (AuthListener) ((WeakReference) it.next()).get();
                    if (authListener != null) {
                        authListener.onRefreshed(boxAuthenticationInfo);
                    }
                }
                if (!boxSession2.getUserId().equals(boxAuthenticationInfo2.getUser().getId())) {
                    boxSession2.onAuthFailure(boxAuthenticationInfo2, new BoxException("Session User Id has changed!"));
                }
                BoxAuthentication.this.mRefreshingTasks.remove(str);
                return boxAuthenticationInfo2;
            }
        };
        FutureTask<BoxAuthenticationInfo> futureTask = new FutureTask<>(r1);
        this.mRefreshingTasks.put(userId, futureTask);
        AUTH_EXECUTOR.execute(futureTask);
        return futureTask;
    }

    /* access modifiers changed from: private */
    public ConcurrentHashMap<String, BoxAuthenticationInfo> getAuthInfoMap(Context context) {
        if (this.mCurrentAccessInfo == null) {
            this.mCurrentAccessInfo = this.authStorage.loadAuthInfoMap(context);
        }
        return this.mCurrentAccessInfo;
    }

    public static boolean isBoxAuthAppAvailable(Context context) {
        return context.getPackageManager().queryIntentActivities(new Intent(BoxConstants.REQUEST_BOX_APP_FOR_AUTH_INTENT_ACTION), 65600).size() > 0;
    }
}
