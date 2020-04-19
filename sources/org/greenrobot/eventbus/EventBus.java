package org.greenrobot.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

public class EventBus {
    private static final EventBusBuilder DEFAULT_BUILDER = new EventBusBuilder();
    public static String TAG = "EventBus";
    static volatile EventBus defaultInstance;
    private static final Map<Class<?>, List<Class<?>>> eventTypesCache = new HashMap();
    private final AsyncPoster asyncPoster;
    private final BackgroundPoster backgroundPoster;
    private final ThreadLocal<PostingThreadState> currentPostingThreadState;
    private final boolean eventInheritance;
    private final ExecutorService executorService;
    private final int indexCount;
    private final boolean logNoSubscriberMessages;
    private final boolean logSubscriberExceptions;
    private final Logger logger;
    private final Poster mainThreadPoster;
    private final MainThreadSupport mainThreadSupport;
    private final boolean sendNoSubscriberEvent;
    private final boolean sendSubscriberExceptionEvent;
    private final Map<Class<?>, Object> stickyEvents;
    private final SubscriberMethodFinder subscriberMethodFinder;
    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final boolean throwSubscriberException;
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    interface PostCallback {
        void onPostCompleted(List<SubscriberExceptionEvent> list);
    }

    static final class PostingThreadState {
        boolean canceled;
        Object event;
        final List<Object> eventQueue = new ArrayList();
        boolean isMainThread;
        boolean isPosting;
        Subscription subscription;

        PostingThreadState() {
        }
    }

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    public static EventBusBuilder builder() {
        return new EventBusBuilder();
    }

    public static void clearCaches() {
        SubscriberMethodFinder.clearCaches();
        eventTypesCache.clear();
    }

    public EventBus() {
        this(DEFAULT_BUILDER);
    }

    EventBus(EventBusBuilder eventBusBuilder) {
        this.currentPostingThreadState = new ThreadLocal<PostingThreadState>() {
            /* access modifiers changed from: protected */
            public PostingThreadState initialValue() {
                return new PostingThreadState();
            }
        };
        this.logger = eventBusBuilder.getLogger();
        this.subscriptionsByEventType = new HashMap();
        this.typesBySubscriber = new HashMap();
        this.stickyEvents = new ConcurrentHashMap();
        this.mainThreadSupport = eventBusBuilder.getMainThreadSupport();
        MainThreadSupport mainThreadSupport2 = this.mainThreadSupport;
        this.mainThreadPoster = mainThreadSupport2 != null ? mainThreadSupport2.createPoster(this) : null;
        this.backgroundPoster = new BackgroundPoster(this);
        this.asyncPoster = new AsyncPoster(this);
        this.indexCount = eventBusBuilder.subscriberInfoIndexes != null ? eventBusBuilder.subscriberInfoIndexes.size() : 0;
        this.subscriberMethodFinder = new SubscriberMethodFinder(eventBusBuilder.subscriberInfoIndexes, eventBusBuilder.strictMethodVerification, eventBusBuilder.ignoreGeneratedIndex);
        this.logSubscriberExceptions = eventBusBuilder.logSubscriberExceptions;
        this.logNoSubscriberMessages = eventBusBuilder.logNoSubscriberMessages;
        this.sendSubscriberExceptionEvent = eventBusBuilder.sendSubscriberExceptionEvent;
        this.sendNoSubscriberEvent = eventBusBuilder.sendNoSubscriberEvent;
        this.throwSubscriberException = eventBusBuilder.throwSubscriberException;
        this.eventInheritance = eventBusBuilder.eventInheritance;
        this.executorService = eventBusBuilder.executorService;
    }

    public void register(Object obj) {
        List<SubscriberMethod> findSubscriberMethods = this.subscriberMethodFinder.findSubscriberMethods(obj.getClass());
        synchronized (this) {
            for (SubscriberMethod subscribe : findSubscriberMethods) {
                subscribe(obj, subscribe);
            }
        }
    }

    private void subscribe(Object obj, SubscriberMethod subscriberMethod) {
        Class<?> cls = subscriberMethod.eventType;
        Subscription subscription = new Subscription(obj, subscriberMethod);
        CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(cls);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
            this.subscriptionsByEventType.put(cls, copyOnWriteArrayList);
        } else if (copyOnWriteArrayList.contains(subscription)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Subscriber ");
            sb.append(obj.getClass());
            sb.append(" already registered to event ");
            sb.append(cls);
            throw new EventBusException(sb.toString());
        }
        int size = copyOnWriteArrayList.size();
        int i = 0;
        while (true) {
            if (i > size) {
                break;
            } else if (i == size || subscriberMethod.priority > ((Subscription) copyOnWriteArrayList.get(i)).subscriberMethod.priority) {
                copyOnWriteArrayList.add(i, subscription);
            } else {
                i++;
            }
        }
        copyOnWriteArrayList.add(i, subscription);
        List list = (List) this.typesBySubscriber.get(obj);
        if (list == null) {
            list = new ArrayList();
            this.typesBySubscriber.put(obj, list);
        }
        list.add(cls);
        if (!subscriberMethod.sticky) {
            return;
        }
        if (this.eventInheritance) {
            for (Entry entry : this.stickyEvents.entrySet()) {
                if (cls.isAssignableFrom((Class) entry.getKey())) {
                    checkPostStickyEventToSubscription(subscription, entry.getValue());
                }
            }
            return;
        }
        checkPostStickyEventToSubscription(subscription, this.stickyEvents.get(cls));
    }

    private void checkPostStickyEventToSubscription(Subscription subscription, Object obj) {
        if (obj != null) {
            postToSubscription(subscription, obj, isMainThread());
        }
    }

    private boolean isMainThread() {
        MainThreadSupport mainThreadSupport2 = this.mainThreadSupport;
        if (mainThreadSupport2 != null) {
            return mainThreadSupport2.isMainThread();
        }
        return true;
    }

    public synchronized boolean isRegistered(Object obj) {
        return this.typesBySubscriber.containsKey(obj);
    }

    private void unsubscribeByEventType(Object obj, Class<?> cls) {
        List list = (List) this.subscriptionsByEventType.get(cls);
        if (list != null) {
            int size = list.size();
            int i = 0;
            while (i < size) {
                Subscription subscription = (Subscription) list.get(i);
                if (subscription.subscriber == obj) {
                    subscription.active = false;
                    list.remove(i);
                    i--;
                    size--;
                }
                i++;
            }
        }
    }

    public synchronized void unregister(Object obj) {
        List<Class> list = (List) this.typesBySubscriber.get(obj);
        if (list != null) {
            for (Class unsubscribeByEventType : list) {
                unsubscribeByEventType(obj, unsubscribeByEventType);
            }
            this.typesBySubscriber.remove(obj);
        } else {
            Logger logger2 = this.logger;
            Level level = Level.WARNING;
            StringBuilder sb = new StringBuilder();
            sb.append("Subscriber to unregister was not registered before: ");
            sb.append(obj.getClass());
            logger2.log(level, sb.toString());
        }
    }

    public void post(Object obj) {
        PostingThreadState postingThreadState = (PostingThreadState) this.currentPostingThreadState.get();
        List<Object> list = postingThreadState.eventQueue;
        list.add(obj);
        if (!postingThreadState.isPosting) {
            postingThreadState.isMainThread = isMainThread();
            postingThreadState.isPosting = true;
            if (!postingThreadState.canceled) {
                while (!list.isEmpty()) {
                    try {
                        postSingleEvent(list.remove(0), postingThreadState);
                    } finally {
                        postingThreadState.isPosting = false;
                        postingThreadState.isMainThread = false;
                    }
                }
                return;
            }
            throw new EventBusException("Internal error. Abort state was not reset");
        }
    }

    public void cancelEventDelivery(Object obj) {
        PostingThreadState postingThreadState = (PostingThreadState) this.currentPostingThreadState.get();
        if (!postingThreadState.isPosting) {
            throw new EventBusException("This method may only be called from inside event handling methods on the posting thread");
        } else if (obj == null) {
            throw new EventBusException("Event may not be null");
        } else if (postingThreadState.event != obj) {
            throw new EventBusException("Only the currently handled event may be aborted");
        } else if (postingThreadState.subscription.subscriberMethod.threadMode == ThreadMode.POSTING) {
            postingThreadState.canceled = true;
        } else {
            throw new EventBusException(" event handlers may only abort the incoming event");
        }
    }

    public void postSticky(Object obj) {
        synchronized (this.stickyEvents) {
            this.stickyEvents.put(obj.getClass(), obj);
        }
        post(obj);
    }

    public <T> T getStickyEvent(Class<T> cls) {
        T cast;
        synchronized (this.stickyEvents) {
            cast = cls.cast(this.stickyEvents.get(cls));
        }
        return cast;
    }

    public <T> T removeStickyEvent(Class<T> cls) {
        T cast;
        synchronized (this.stickyEvents) {
            cast = cls.cast(this.stickyEvents.remove(cls));
        }
        return cast;
    }

    public boolean removeStickyEvent(Object obj) {
        synchronized (this.stickyEvents) {
            Class cls = obj.getClass();
            if (!obj.equals(this.stickyEvents.get(cls))) {
                return false;
            }
            this.stickyEvents.remove(cls);
            return true;
        }
    }

    public void removeAllStickyEvents() {
        synchronized (this.stickyEvents) {
            this.stickyEvents.clear();
        }
    }

    public boolean hasSubscriberForEvent(Class<?> cls) {
        CopyOnWriteArrayList copyOnWriteArrayList;
        List lookupAllEventTypes = lookupAllEventTypes(cls);
        if (lookupAllEventTypes != null) {
            int size = lookupAllEventTypes.size();
            for (int i = 0; i < size; i++) {
                Class cls2 = (Class) lookupAllEventTypes.get(i);
                synchronized (this) {
                    copyOnWriteArrayList = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(cls2);
                }
                if (copyOnWriteArrayList != null && !copyOnWriteArrayList.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void postSingleEvent(Object obj, PostingThreadState postingThreadState) throws Error {
        boolean z;
        Class<SubscriberExceptionEvent> cls = obj.getClass();
        if (this.eventInheritance) {
            List lookupAllEventTypes = lookupAllEventTypes(cls);
            z = false;
            for (int i = 0; i < lookupAllEventTypes.size(); i++) {
                z |= postSingleEventForEventType(obj, postingThreadState, (Class) lookupAllEventTypes.get(i));
            }
        } else {
            z = postSingleEventForEventType(obj, postingThreadState, cls);
        }
        if (!z) {
            if (this.logNoSubscriberMessages) {
                Logger logger2 = this.logger;
                Level level = Level.FINE;
                StringBuilder sb = new StringBuilder();
                sb.append("No subscribers registered for event ");
                sb.append(cls);
                logger2.log(level, sb.toString());
            }
            if (this.sendNoSubscriberEvent && cls != NoSubscriberEvent.class && cls != SubscriberExceptionEvent.class) {
                post(new NoSubscriberEvent(this, obj));
            }
        }
    }

    private boolean postSingleEventForEventType(Object obj, PostingThreadState postingThreadState, Class<?> cls) {
        CopyOnWriteArrayList copyOnWriteArrayList;
        synchronized (this) {
            copyOnWriteArrayList = (CopyOnWriteArrayList) this.subscriptionsByEventType.get(cls);
        }
        if (copyOnWriteArrayList == null || copyOnWriteArrayList.isEmpty()) {
            return false;
        }
        Iterator it = copyOnWriteArrayList.iterator();
        while (it.hasNext()) {
            Subscription subscription = (Subscription) it.next();
            postingThreadState.event = obj;
            postingThreadState.subscription = subscription;
            try {
                postToSubscription(subscription, obj, postingThreadState.isMainThread);
                if (postingThreadState.canceled) {
                    break;
                }
            } finally {
                postingThreadState.event = null;
                postingThreadState.subscription = null;
                postingThreadState.canceled = false;
            }
        }
        return true;
    }

    private void postToSubscription(Subscription subscription, Object obj, boolean z) {
        switch (subscription.subscriberMethod.threadMode) {
            case POSTING:
                invokeSubscriber(subscription, obj);
                return;
            case MAIN:
                if (z) {
                    invokeSubscriber(subscription, obj);
                    return;
                } else {
                    this.mainThreadPoster.enqueue(subscription, obj);
                    return;
                }
            case MAIN_ORDERED:
                Poster poster = this.mainThreadPoster;
                if (poster != null) {
                    poster.enqueue(subscription, obj);
                    return;
                } else {
                    invokeSubscriber(subscription, obj);
                    return;
                }
            case BACKGROUND:
                if (z) {
                    this.backgroundPoster.enqueue(subscription, obj);
                    return;
                } else {
                    invokeSubscriber(subscription, obj);
                    return;
                }
            case ASYNC:
                this.asyncPoster.enqueue(subscription, obj);
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown thread mode: ");
                sb.append(subscription.subscriberMethod.threadMode);
                throw new IllegalStateException(sb.toString());
        }
    }

    private static List<Class<?>> lookupAllEventTypes(Class<?> cls) {
        List<Class<?>> list;
        synchronized (eventTypesCache) {
            list = (List) eventTypesCache.get(cls);
            if (list == null) {
                list = new ArrayList<>();
                for (Class<?> cls2 = cls; cls2 != null; cls2 = cls2.getSuperclass()) {
                    list.add(cls2);
                    addInterfaces(list, cls2.getInterfaces());
                }
                eventTypesCache.put(cls, list);
            }
        }
        return list;
    }

    static void addInterfaces(List<Class<?>> list, Class<?>[] clsArr) {
        for (Class<?> cls : clsArr) {
            if (!list.contains(cls)) {
                list.add(cls);
                addInterfaces(list, cls.getInterfaces());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void invokeSubscriber(PendingPost pendingPost) {
        Object obj = pendingPost.event;
        Subscription subscription = pendingPost.subscription;
        PendingPost.releasePendingPost(pendingPost);
        if (subscription.active) {
            invokeSubscriber(subscription, obj);
        }
    }

    /* access modifiers changed from: 0000 */
    public void invokeSubscriber(Subscription subscription, Object obj) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber, new Object[]{obj});
        } catch (InvocationTargetException e) {
            handleSubscriberException(subscription, obj, e.getCause());
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unexpected exception", e2);
        }
    }

    private void handleSubscriberException(Subscription subscription, Object obj, Throwable th) {
        if (obj instanceof SubscriberExceptionEvent) {
            if (this.logSubscriberExceptions) {
                Logger logger2 = this.logger;
                Level level = Level.SEVERE;
                StringBuilder sb = new StringBuilder();
                sb.append("SubscriberExceptionEvent subscriber ");
                sb.append(subscription.subscriber.getClass());
                sb.append(" threw an exception");
                logger2.log(level, sb.toString(), th);
                SubscriberExceptionEvent subscriberExceptionEvent = (SubscriberExceptionEvent) obj;
                Logger logger3 = this.logger;
                Level level2 = Level.SEVERE;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Initial event ");
                sb2.append(subscriberExceptionEvent.causingEvent);
                sb2.append(" caused exception in ");
                sb2.append(subscriberExceptionEvent.causingSubscriber);
                logger3.log(level2, sb2.toString(), subscriberExceptionEvent.throwable);
            }
        } else if (!this.throwSubscriberException) {
            if (this.logSubscriberExceptions) {
                Logger logger4 = this.logger;
                Level level3 = Level.SEVERE;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Could not dispatch event: ");
                sb3.append(obj.getClass());
                sb3.append(" to subscribing class ");
                sb3.append(subscription.subscriber.getClass());
                logger4.log(level3, sb3.toString(), th);
            }
            if (this.sendSubscriberExceptionEvent) {
                post(new SubscriberExceptionEvent(this, th, obj, subscription.subscriber));
            }
        } else {
            throw new EventBusException("Invoking subscriber failed", th);
        }
    }

    /* access modifiers changed from: 0000 */
    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventBus[indexCount=");
        sb.append(this.indexCount);
        sb.append(", eventInheritance=");
        sb.append(this.eventInheritance);
        sb.append("]");
        return sb.toString();
    }
}
