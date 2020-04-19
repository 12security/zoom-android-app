package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxObject;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class BoxFutureTask<E extends BoxObject> extends FutureTask<BoxResponse<E>> {
    protected ArrayList<OnCompletedListener<E>> mCompletedListeners = new ArrayList<>();
    protected final BoxRequest mRequest;

    public interface OnCompletedListener<E extends BoxObject> {
        void onCompleted(BoxResponse<E> boxResponse);
    }

    public BoxFutureTask(Class<E> cls, final BoxRequest boxRequest) {
        super(new Callable<BoxResponse<E>>() {
            public BoxResponse<E> call() throws Exception {
                BoxObject boxObject = null;
                try {
                    e = null;
                    boxObject = BoxRequest.this.send();
                } catch (Exception e) {
                    e = e;
                }
                return new BoxResponse<>(boxObject, e, BoxRequest.this);
            }
        });
        this.mRequest = boxRequest;
    }

    /* access modifiers changed from: protected */
    public void done() {
        BoxResponse boxResponse;
        try {
            boxResponse = (BoxResponse) get();
            e = null;
        } catch (InterruptedException e) {
            e = e;
            boxResponse = null;
        } catch (ExecutionException e2) {
            e = e2;
            boxResponse = null;
        }
        if (e != null) {
            boxResponse = new BoxResponse(null, new BoxException("Unable to retrieve response from FutureTask.", e), this.mRequest);
        }
        Iterator it = getCompletionListeners().iterator();
        while (it.hasNext()) {
            ((OnCompletedListener) it.next()).onCompleted(boxResponse);
        }
    }

    public ArrayList<OnCompletedListener<E>> getCompletionListeners() {
        return this.mCompletedListeners;
    }

    public BoxFutureTask<E> addOnCompletedListener(OnCompletedListener<E> onCompletedListener) {
        this.mCompletedListeners.add(onCompletedListener);
        return this;
    }
}
