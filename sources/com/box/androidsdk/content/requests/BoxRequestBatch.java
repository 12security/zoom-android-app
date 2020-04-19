package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.models.BoxObject;
import java.util.ArrayList;
import java.util.Iterator;

public class BoxRequestBatch extends BoxRequest<BoxResponseBatch, BoxRequestBatch> {
    protected ArrayList<BoxRequest> mRequests = new ArrayList<>();

    public BoxRequestBatch() {
        super(BoxResponseBatch.class, null, null);
    }

    public BoxRequestBatch addRequest(BoxRequest boxRequest) {
        this.mRequests.add(boxRequest);
        return this;
    }

    public BoxResponseBatch send() throws BoxException {
        BoxResponseBatch boxResponseBatch = new BoxResponseBatch();
        Iterator it = this.mRequests.iterator();
        while (it.hasNext()) {
            BoxRequest boxRequest = (BoxRequest) it.next();
            BoxObject boxObject = null;
            try {
                e = null;
                boxObject = boxRequest.send();
            } catch (Exception e) {
                e = e;
            }
            boxResponseBatch.addResponse(new BoxResponse(boxObject, e, boxRequest));
        }
        return boxResponseBatch;
    }
}
