package com.microsoft.aad.adal;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.lang.reflect.Type;

public final class TokenCacheItemSerializationAdapater implements JsonDeserializer<TokenCacheItem>, JsonSerializer<TokenCacheItem> {
    private static final String TAG = "TokenCacheItemSerializationAdapater";

    public JsonElement serialize(TokenCacheItem tokenCacheItem, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(OAuth2.AUTHORITY, new JsonPrimitive(tokenCacheItem.getAuthority()));
        jsonObject.add("refresh_token", new JsonPrimitive(tokenCacheItem.getRefreshToken()));
        jsonObject.add("id_token", new JsonPrimitive(tokenCacheItem.getRawIdToken()));
        jsonObject.add("foci", new JsonPrimitive(tokenCacheItem.getFamilyClientId()));
        return jsonObject;
    }

    public TokenCacheItem deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        throwIfParameterMissing(asJsonObject, OAuth2.AUTHORITY);
        throwIfParameterMissing(asJsonObject, "id_token");
        throwIfParameterMissing(asJsonObject, "foci");
        throwIfParameterMissing(asJsonObject, "refresh_token");
        String asString = asJsonObject.get("id_token").getAsString();
        TokenCacheItem tokenCacheItem = new TokenCacheItem();
        try {
            IdToken idToken = new IdToken(asString);
            tokenCacheItem.setUserInfo(new UserInfo(idToken));
            tokenCacheItem.setTenantId(idToken.getTenantId());
            tokenCacheItem.setAuthority(asJsonObject.get(OAuth2.AUTHORITY).getAsString());
            tokenCacheItem.setIsMultiResourceRefreshToken(true);
            tokenCacheItem.setRawIdToken(asString);
            tokenCacheItem.setFamilyClientId(asJsonObject.get("foci").getAsString());
            tokenCacheItem.setRefreshToken(asJsonObject.get("refresh_token").getAsString());
            return tokenCacheItem;
        } catch (AuthenticationException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(": Could not deserialize into a tokenCacheItem object");
            throw new JsonParseException(sb.toString(), e);
        }
    }

    private void throwIfParameterMissing(JsonObject jsonObject, String str) {
        if (!jsonObject.has(str)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append("Attribute ");
            sb.append(str);
            sb.append(" is missing for deserialization.");
            throw new JsonParseException(sb.toString());
        }
    }
}
