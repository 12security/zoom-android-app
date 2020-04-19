package p021us.zoom.androidlib.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Process;
import android.provider.CalendarContract.EventDays;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@TargetApi(14)
/* renamed from: us.zoom.androidlib.util.EventLoader */
public class EventLoader {
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private LinkedBlockingQueue<LoadRequest> mLoaderQueue;
    private LoaderThread mLoaderThread;
    /* access modifiers changed from: private */
    public ContentResolver mResolver;
    /* access modifiers changed from: private */
    public AtomicInteger mSequenceNumber = new AtomicInteger();

    /* renamed from: us.zoom.androidlib.util.EventLoader$LoadEventDaysRequest */
    private static class LoadEventDaysRequest implements LoadRequest {
        private static final String[] PROJECTION = {"startDay", "endDay"};
        public boolean[] eventDays;
        public int numDays;
        public int startDay;
        public Runnable uiCallback;

        public void skipRequest(EventLoader eventLoader) {
        }

        public LoadEventDaysRequest(int i, int i2, boolean[] zArr, Runnable runnable) {
            this.startDay = i;
            this.numDays = i2;
            this.eventDays = zArr;
            this.uiCallback = runnable;
        }

        /* JADX INFO: finally extract failed */
        public void processRequest(EventLoader eventLoader) {
            Handler access$000 = eventLoader.mHandler;
            ContentResolver access$100 = eventLoader.mResolver;
            Arrays.fill(this.eventDays, false);
            Cursor query = EventDays.query(access$100, this.startDay, this.numDays, PROJECTION);
            try {
                int columnIndexOrThrow = query.getColumnIndexOrThrow("startDay");
                int columnIndexOrThrow2 = query.getColumnIndexOrThrow("endDay");
                while (query.moveToNext()) {
                    int i = query.getInt(columnIndexOrThrow);
                    int i2 = query.getInt(columnIndexOrThrow2);
                    int min = Math.min(i2 - this.startDay, 30);
                    for (int max = Math.max(i - this.startDay, 0); max <= min; max++) {
                        this.eventDays[max] = true;
                    }
                }
                if (query != null) {
                    query.close();
                }
                access$000.post(this.uiCallback);
            } catch (Throwable th) {
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventLoader$LoadEventsRequest */
    private static class LoadEventsRequest implements LoadRequest {
        public Runnable cancelCallback;
        public ArrayList<Event> events;

        /* renamed from: id */
        public int f510id;
        public int numDays;
        public int startDay;
        public Runnable successCallback;

        public LoadEventsRequest(int i, int i2, int i3, ArrayList<Event> arrayList, Runnable runnable, Runnable runnable2) {
            this.f510id = i;
            this.startDay = i2;
            this.numDays = i3;
            this.events = arrayList;
            this.successCallback = runnable;
            this.cancelCallback = runnable2;
        }

        public void processRequest(EventLoader eventLoader) {
            Event.loadEvents(eventLoader.mContext, this.events, this.startDay, this.numDays, this.f510id, eventLoader.mSequenceNumber);
            if (this.f510id == eventLoader.mSequenceNumber.get()) {
                eventLoader.mHandler.post(this.successCallback);
            } else {
                eventLoader.mHandler.post(this.cancelCallback);
            }
        }

        public void skipRequest(EventLoader eventLoader) {
            eventLoader.mHandler.post(this.cancelCallback);
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventLoader$LoadRequest */
    private interface LoadRequest {
        void processRequest(EventLoader eventLoader);

        void skipRequest(EventLoader eventLoader);
    }

    /* renamed from: us.zoom.androidlib.util.EventLoader$LoaderThread */
    private static class LoaderThread extends Thread {
        EventLoader mEventLoader;
        LinkedBlockingQueue<LoadRequest> mQueue;

        public LoaderThread(LinkedBlockingQueue<LoadRequest> linkedBlockingQueue, EventLoader eventLoader) {
            this.mQueue = linkedBlockingQueue;
            this.mEventLoader = eventLoader;
        }

        public void shutdown() {
            try {
                this.mQueue.put(new ShutdownRequest());
            } catch (InterruptedException unused) {
                Log.e("Cal", "LoaderThread.shutdown() interrupted!");
            }
        }

        public void run() {
            Process.setThreadPriority(10);
            while (true) {
                try {
                    LoadRequest loadRequest = (LoadRequest) this.mQueue.take();
                    while (!this.mQueue.isEmpty()) {
                        loadRequest.skipRequest(this.mEventLoader);
                        loadRequest = (LoadRequest) this.mQueue.take();
                    }
                    if (!(loadRequest instanceof ShutdownRequest)) {
                        loadRequest.processRequest(this.mEventLoader);
                    } else {
                        return;
                    }
                } catch (InterruptedException unused) {
                    Log.e("Cal", "background LoaderThread interrupted!");
                }
            }
        }
    }

    /* renamed from: us.zoom.androidlib.util.EventLoader$ShutdownRequest */
    private static class ShutdownRequest implements LoadRequest {
        public void processRequest(EventLoader eventLoader) {
        }

        public void skipRequest(EventLoader eventLoader) {
        }

        private ShutdownRequest() {
        }
    }

    public EventLoader(Context context) {
        this.mContext = context;
        this.mLoaderQueue = new LinkedBlockingQueue<>();
        this.mResolver = context.getContentResolver();
    }

    public void startBackgroundThread() {
        this.mLoaderThread = new LoaderThread(this.mLoaderQueue, this);
        this.mLoaderThread.start();
    }

    public void stopBackgroundThread() {
        this.mLoaderThread.shutdown();
    }

    public void loadEventsInBackground(int i, ArrayList<Event> arrayList, int i2, Runnable runnable, Runnable runnable2) {
        LoadEventsRequest loadEventsRequest = new LoadEventsRequest(this.mSequenceNumber.incrementAndGet(), i2, i, arrayList, runnable, runnable2);
        try {
            this.mLoaderQueue.put(loadEventsRequest);
        } catch (InterruptedException unused) {
            Log.e("Cal", "loadEventsInBackground() interrupted!");
        }
    }

    /* access modifiers changed from: 0000 */
    public void loadEventDaysInBackground(int i, int i2, boolean[] zArr, Runnable runnable) {
        try {
            this.mLoaderQueue.put(new LoadEventDaysRequest(i, i2, zArr, runnable));
        } catch (InterruptedException unused) {
            Log.e("Cal", "loadEventDaysInBackground() interrupted!");
        }
    }
}
