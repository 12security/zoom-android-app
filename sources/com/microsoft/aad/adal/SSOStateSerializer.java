package com.microsoft.aad.adal;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.cookie.ClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

final class SSOStateSerializer {
    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(TokenCacheItem.class, new TokenCacheItemSerializationAdapater()).create();
    @SerializedName("tokenCacheItems")
    private final List<TokenCacheItem> mTokenCacheItems = new ArrayList();
    @SerializedName("version")
    private final int version = 1;

    private int getVersion() {
        return 1;
    }

    private SSOStateSerializer() {
    }

    private SSOStateSerializer(TokenCacheItem tokenCacheItem) {
        if (tokenCacheItem != null) {
            this.mTokenCacheItems.add(tokenCacheItem);
            return;
        }
        throw new IllegalArgumentException("tokenItem is null");
    }

    private TokenCacheItem getTokenItem() throws AuthenticationException {
        List<TokenCacheItem> list = this.mTokenCacheItems;
        if (list != null && !list.isEmpty()) {
            return (TokenCacheItem) this.mTokenCacheItems.get(0);
        }
        throw new AuthenticationException(ADALError.TOKEN_CACHE_ITEM_NOT_FOUND, "There is no token cache item in the SSOStateContainer.");
    }

    private String internalSerialize() {
        return GSON.toJson((Object) this);
    }

    private TokenCacheItem internalDeserialize(String str) throws AuthenticationException {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.getInt(ClientCookie.VERSION_ATTR) == getVersion()) {
                return ((SSOStateSerializer) GSON.fromJson(str, SSOStateSerializer.class)).getTokenItem();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Fail to deserialize because the blob version is incompatible. The version of the serializedBlob is ");
            sb.append(jSONObject.getInt(ClientCookie.VERSION_ATTR));
            sb.append(". And the target class version is ");
            sb.append(getVersion());
            throw new DeserializationAuthenticationException(sb.toString());
        } catch (JsonParseException | JSONException e) {
            throw new DeserializationAuthenticationException(e.getMessage());
        }
    }

    static String serialize(TokenCacheItem tokenCacheItem) {
        return new SSOStateSerializer(tokenCacheItem).internalSerialize();
    }

    static TokenCacheItem deserialize(String str) throws AuthenticationException {
        return new SSOStateSerializer().internalDeserialize(str);
    }
}
