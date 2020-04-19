package com.microsoft.aad.adal;

import com.google.gson.Gson;
import java.util.UUID;

abstract class AbstractMetadataRequestor<MetadataType, MetadataRequestOptions> {
    private UUID mCorrelationId;
    private Gson mGson;
    private final IWebRequestHandler mWebrequestHandler = new WebRequestHandler();

    /* access modifiers changed from: 0000 */
    public abstract MetadataType parseMetadata(HttpWebResponse httpWebResponse) throws Exception;

    /* access modifiers changed from: 0000 */
    public abstract MetadataType requestMetadata(MetadataRequestOptions metadatarequestoptions) throws Exception;

    AbstractMetadataRequestor() {
    }

    public final void setCorrelationId(UUID uuid) {
        this.mCorrelationId = uuid;
    }

    public final UUID getCorrelationId() {
        return this.mCorrelationId;
    }

    /* access modifiers changed from: 0000 */
    public synchronized Gson parser() {
        if (this.mGson == null) {
            this.mGson = new Gson();
        }
        return this.mGson;
    }

    /* access modifiers changed from: 0000 */
    public IWebRequestHandler getWebrequestHandler() {
        return this.mWebrequestHandler;
    }
}
