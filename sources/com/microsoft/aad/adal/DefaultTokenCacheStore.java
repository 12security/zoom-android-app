package com.microsoft.aad.adal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DefaultTokenCacheStore implements ITokenCacheStore, ITokenStoreQuery {
    private static final Object LOCK = new Object();
    private static final String SHARED_PREFERENCE_NAME = "com.microsoft.aad.adal.cache";
    private static final String TAG = "DefaultTokenCacheStore";
    private static final int TOKEN_VALIDITY_WINDOW = 10;
    @SuppressLint({"StaticFieldLeak"})
    private static StorageHelper sHelper = null;
    private static final long serialVersionUID = 1;
    private Context mContext;
    private Gson mGson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTimeAdapter()).create();
    private SharedPreferences mPrefs;

    @SuppressLint({"WrongConstant"})
    public DefaultTokenCacheStore(Context context) {
        if (context != null) {
            this.mContext = context;
            if (!StringExtensions.isNullOrBlank(AuthenticationSettings.INSTANCE.getSharedPrefPackageName())) {
                try {
                    this.mContext = context.createPackageContext(AuthenticationSettings.INSTANCE.getSharedPrefPackageName(), 0);
                } catch (NameNotFoundException unused) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Package name:");
                    sb.append(AuthenticationSettings.INSTANCE.getSharedPrefPackageName());
                    sb.append(" is not found");
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            this.mPrefs = this.mContext.getSharedPreferences(SHARED_PREFERENCE_NAME, 0);
            if (this.mPrefs != null) {
                validateSecretKeySetting();
                return;
            }
            throw new IllegalStateException(ADALError.DEVICE_SHARED_PREF_IS_NOT_AVAILABLE.getDescription());
        }
        throw new IllegalArgumentException("Context is null");
    }

    /* access modifiers changed from: protected */
    public StorageHelper getStorageHelper() {
        synchronized (LOCK) {
            if (sHelper == null) {
                Logger.m236v(TAG, "Started to initialize storage helper");
                sHelper = new StorageHelper(this.mContext);
                Logger.m236v(TAG, "Finished to initialize storage helper");
            }
        }
        return sHelper;
    }

    private String encrypt(String str) {
        try {
            return getStorageHelper().encrypt(str);
        } catch (IOException | GeneralSecurityException e) {
            Logger.m232e(TAG, "Encryption failure. ", "", ADALError.ENCRYPTION_FAILED, e);
            return null;
        }
    }

    private String decrypt(String str, String str2) {
        if (!StringExtensions.isNullOrBlank(str)) {
            try {
                return getStorageHelper().decrypt(str2);
            } catch (IOException | GeneralSecurityException e) {
                Logger.m232e(TAG, "Decryption failure. ", "", ADALError.DECRYPTION_FAILED, e);
                removeItem(str);
                return null;
            }
        } else {
            throw new IllegalArgumentException("key is null or blank");
        }
    }

    public TokenCacheItem getItem(String str) {
        if (str != null) {
            if (this.mPrefs.contains(str)) {
                String decrypt = decrypt(str, this.mPrefs.getString(str, ""));
                if (decrypt != null) {
                    return (TokenCacheItem) this.mGson.fromJson(decrypt, TokenCacheItem.class);
                }
            }
            return null;
        }
        throw new IllegalArgumentException("The key is null.");
    }

    public void removeItem(String str) {
        if (str == null) {
            throw new IllegalArgumentException("key");
        } else if (this.mPrefs.contains(str)) {
            Editor edit = this.mPrefs.edit();
            edit.remove(str);
            edit.apply();
        }
    }

    public void setItem(String str, TokenCacheItem tokenCacheItem) {
        if (str == null) {
            throw new IllegalArgumentException("key");
        } else if (tokenCacheItem != null) {
            String encrypt = encrypt(this.mGson.toJson((Object) tokenCacheItem));
            if (encrypt != null) {
                Editor edit = this.mPrefs.edit();
                edit.putString(str, encrypt);
                edit.apply();
                return;
            }
            Logger.m231e(TAG, "Encrypted output is null. ", "", ADALError.ENCRYPTION_FAILED);
        } else {
            throw new IllegalArgumentException("item");
        }
    }

    public void removeAll() {
        Editor edit = this.mPrefs.edit();
        edit.clear();
        edit.apply();
    }

    public Iterator<TokenCacheItem> getAll() {
        Map all = this.mPrefs.getAll();
        ArrayList arrayList = new ArrayList(all.values().size());
        for (Entry entry : all.entrySet()) {
            String decrypt = decrypt((String) entry.getKey(), (String) entry.getValue());
            if (decrypt != null) {
                arrayList.add((TokenCacheItem) this.mGson.fromJson(decrypt, TokenCacheItem.class));
            }
        }
        return arrayList.iterator();
    }

    public Set<String> getUniqueUsersWithTokenCache() {
        Iterator all = getAll();
        HashSet hashSet = new HashSet();
        while (all.hasNext()) {
            TokenCacheItem tokenCacheItem = (TokenCacheItem) all.next();
            if (tokenCacheItem.getUserInfo() != null && !hashSet.contains(tokenCacheItem.getUserInfo().getUserId())) {
                hashSet.add(tokenCacheItem.getUserInfo().getUserId());
            }
        }
        return hashSet;
    }

    public List<TokenCacheItem> getTokensForResource(String str) {
        Iterator all = getAll();
        ArrayList arrayList = new ArrayList();
        while (all.hasNext()) {
            TokenCacheItem tokenCacheItem = (TokenCacheItem) all.next();
            if (str.equals(tokenCacheItem.getResource())) {
                arrayList.add(tokenCacheItem);
            }
        }
        return arrayList;
    }

    public List<TokenCacheItem> getTokensForUser(String str) {
        Iterator all = getAll();
        ArrayList arrayList = new ArrayList();
        while (all.hasNext()) {
            TokenCacheItem tokenCacheItem = (TokenCacheItem) all.next();
            if (tokenCacheItem.getUserInfo() != null && tokenCacheItem.getUserInfo().getUserId().equalsIgnoreCase(str)) {
                arrayList.add(tokenCacheItem);
            }
        }
        return arrayList;
    }

    public void clearTokensForUser(String str) {
        for (TokenCacheItem tokenCacheItem : getTokensForUser(str)) {
            if (!(tokenCacheItem.getUserInfo() == null || tokenCacheItem.getUserInfo().getUserId() == null || !tokenCacheItem.getUserInfo().getUserId().equalsIgnoreCase(str))) {
                try {
                    removeItem(CacheKey.createCacheKey(tokenCacheItem));
                } catch (AuthenticationException e) {
                    Logger.m232e(TAG, "Fail to create cache key. ", "", e.getCode(), e);
                }
            }
        }
    }

    public List<TokenCacheItem> getTokensAboutToExpire() {
        Iterator all = getAll();
        ArrayList arrayList = new ArrayList();
        while (all.hasNext()) {
            TokenCacheItem tokenCacheItem = (TokenCacheItem) all.next();
            if (isAboutToExpire(tokenCacheItem.getExpiresOn())) {
                arrayList.add(tokenCacheItem);
            }
        }
        return arrayList;
    }

    private void validateSecretKeySetting() {
        if (AuthenticationSettings.INSTANCE.getSecretKeyData() == null && VERSION.SDK_INT < 18) {
            throw new IllegalArgumentException("Secret key must be provided for API < 18. Use AuthenticationSettings.INSTANCE.setSecretKey()");
        }
    }

    private boolean isAboutToExpire(Date date) {
        return date != null && date.before(getTokenValidityTime().getTime());
    }

    private static Calendar getTokenValidityTime() {
        Calendar instance = Calendar.getInstance();
        instance.add(13, 10);
        return instance;
    }

    public boolean contains(String str) {
        if (str != null) {
            return this.mPrefs.contains(str);
        }
        throw new IllegalArgumentException("key");
    }
}
