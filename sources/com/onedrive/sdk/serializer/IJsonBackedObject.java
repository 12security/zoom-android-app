package com.onedrive.sdk.serializer;

import com.google.gson.JsonObject;

public interface IJsonBackedObject {
    void setRawObject(ISerializer iSerializer, JsonObject jsonObject);
}
