package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsEvent.EventRealTimeServerRequest;
import com.box.androidsdk.content.requests.BoxRequestsEvent.GetEnterpriseEvents;
import com.box.androidsdk.content.requests.BoxRequestsEvent.GetUserEvents;
import com.box.androidsdk.content.utils.RealTimeServerConnection;
import com.box.androidsdk.content.utils.RealTimeServerConnection.OnChangeListener;

public class BoxApiEvent extends BoxApi {
    public BoxApiEvent(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getEventsUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUri());
        sb.append("/events");
        return sb.toString();
    }

    public GetUserEvents getUserEventsRequest() {
        return new GetUserEvents(getEventsUrl(), this.mSession);
    }

    public GetEnterpriseEvents getEnterpriseEventsRequest() {
        return new GetEnterpriseEvents(getEventsUrl(), this.mSession);
    }

    public RealTimeServerConnection getLongPollServerConnection(OnChangeListener onChangeListener) {
        return new RealTimeServerConnection(new EventRealTimeServerRequest(getEventsUrl(), this.mSession), onChangeListener, this.mSession);
    }
}
