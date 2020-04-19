package com.zipow.videobox.ptapp.p013mm;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.PersonalGroupAtcionResponse;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.AllBuddyInfo;
import com.zipow.videobox.ptapp.PTAppProtos.BuddyGroupMemberChangeList;
import com.zipow.videobox.ptapp.PTAppProtos.ChangedBuddyGroups;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.PinyinUtil;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance */
public class ZMBuddySyncInstance {
    private static final int BUDDY_COUNT_PER_PAGE = 2000;
    private static final int MSG_BUDDY_INFO_UPDATE_CHECKER = 2;
    private static final int MSG_REFRESH_ALL_BUDDIES = 1;
    private static final String TAG = "ZMBuddySyncInstance";
    private static final int TYPE_BUDDIES_SIZE_CHANGE = 2;
    private static final int TYPE_BUDDY_INFO_CHANGE = 1;
    private static final int TYPE_BUDDY_NO_CHANGE = 0;
    private static ZMBuddySyncInstance mInstance;
    /* access modifiers changed from: private */
    @NonNull
    public Map<String, IMAddrBookItem> mBuddies = new HashMap();
    @NonNull
    private Map<String, Set<String>> mBuddiesInBuddyGroup = new HashMap();
    @NonNull
    private List<MMZoomBuddyGroup> mBuddyGroups = new ArrayList();
    /* access modifiers changed from: private */
    public int mChangeType = 0;
    /* access modifiers changed from: private */
    @NonNull
    public List<String> mDeletedJids = new ArrayList();
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    ZMBuddySyncInstance.this.clearAllBuddies();
                    ZMBuddySyncInstance.this.loadAllBuddies();
                    return;
                case 2:
                    if (ZMBuddySyncInstance.this.mChangeType != 0) {
                        for (IListener iListener : ZMBuddySyncInstance.this.mListenerList.getAll()) {
                            ZMBuddyListListener zMBuddyListListener = (ZMBuddyListListener) iListener;
                            switch (ZMBuddySyncInstance.this.mChangeType) {
                                case 1:
                                    zMBuddyListListener.onBuddyInfoUpdate(new ArrayList(ZMBuddySyncInstance.this.mPendingPresenceUpdateJids), new ArrayList(ZMBuddySyncInstance.this.mPendingInfoUpdateJids));
                                    break;
                                case 2:
                                    zMBuddyListListener.onBuddyListUpdate();
                                    break;
                            }
                        }
                        ZMBuddySyncInstance.this.mPendingPresenceUpdateJids.clear();
                        ZMBuddySyncInstance.this.mPendingInfoUpdateJids.clear();
                    }
                    ZMBuddySyncInstance.this.mChangeType = 0;
                    sendEmptyMessageDelayed(2, 1000);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public ListenerList mListenerList = new ListenerList();
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mLoadCaptureBuddiesRunnable = new Runnable() {
        public void run() {
            if (PTApp.getInstance().getZoomMessenger() != null) {
                byte[] access$500 = ZMBuddySyncInstance.this.loadCaptureBuddies(2000);
                if (access$500 != null) {
                    try {
                        AllBuddyInfo parseFrom = AllBuddyInfo.parseFrom(access$500);
                        if (parseFrom != null) {
                            if (parseFrom.getJidsCount() != 0) {
                                boolean isEmpty = ZMBuddySyncInstance.this.mBuddies.isEmpty();
                                for (int i = 0; i < parseFrom.getJidsCount(); i++) {
                                    String jids = parseFrom.getJids(i);
                                    if (!ZMBuddySyncInstance.this.mDeletedJids.contains(jids)) {
                                        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) ZMBuddySyncInstance.this.mBuddies.get(jids);
                                        if (iMAddrBookItem == null || !iMAddrBookItem.isPropertyInit()) {
                                            IMAddrBookItem iMAddrBookItem2 = r8;
                                            IMAddrBookItem iMAddrBookItem3 = new IMAddrBookItem(jids, parseFrom.getScreenName(i), parseFrom.getPhoneNumber(i), parseFrom.getIsBuddy(i), parseFrom.getIsDesktopOnLine(i), parseFrom.getIsMobileOnLine(i), parseFrom.getEmail(i), parseFrom.getIsZoomRoom(i), parseFrom.getSipPhoneNumber(i));
                                            ZMBuddySyncInstance.this.mBuddies.put(jids, iMAddrBookItem2);
                                        }
                                    }
                                }
                                if (isEmpty) {
                                    ZMBuddySyncInstance.this.mChangeType = 2;
                                }
                                ZMBuddySyncInstance.this.mHandler.postDelayed(this, 2000);
                            }
                        }
                        ZMBuddySyncInstance.this.mDeletedJids.clear();
                        ZMBuddySyncInstance.this.mChangeType = 2;
                    } catch (InvalidProtocolBufferException unused) {
                        ZMBuddySyncInstance.this.mHandler.postDelayed(this, 10000);
                    }
                } else {
                    ZMBuddySyncInstance.this.mHandler.postDelayed(this, 10000);
                }
            }
        }
    };
    @NonNull
    private Object mNaviteBuddyVetctLocker = new Object();
    /* access modifiers changed from: private */
    @NonNull
    public Set<String> mPendingInfoUpdateJids = new HashSet();
    /* access modifiers changed from: private */
    @NonNull
    public Set<String> mPendingPresenceUpdateJids = new HashSet();
    /* access modifiers changed from: private */
    @NonNull
    public Object mRefreshAllBuddyLocker = new Object();
    /* access modifiers changed from: private */
    public boolean mRefreshAllBuddyThread = false;
    /* access modifiers changed from: private */
    @NonNull
    public Object mSortBuddiesLocker = new Object();
    @Nullable
    private MMZoomBuddyGroup mUserBuddyGroup;

    /* renamed from: com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance$ZMBuddyListListener */
    public interface ZMBuddyListListener extends IListener {
        void onBuddyInfoUpdate(List<String> list, List<String> list2);

        void onBuddyListUpdate();
    }

    private native int captureAllBuddiesImpl();

    private native void clearAllCaptureBuddiesImpl();

    @Nullable
    private native byte[] loadCaptureBuddiesImpl(int i);

    private native void sortAllBuddiesImpl();

    public static synchronized ZMBuddySyncInstance getInsatance() {
        ZMBuddySyncInstance zMBuddySyncInstance;
        synchronized (ZMBuddySyncInstance.class) {
            if (mInstance == null) {
                mInstance = new ZMBuddySyncInstance();
            }
            zMBuddySyncInstance = mInstance;
        }
        return zMBuddySyncInstance;
    }

    private ZMBuddySyncInstance() {
        init();
    }

    public void addListener(@Nullable ZMBuddyListListener zMBuddyListListener) {
        if (zMBuddyListListener != null) {
            IListener[] all = this.mListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i] == zMBuddyListListener) {
                    removeListener((ZMBuddyListListener) all[i]);
                }
            }
            this.mListenerList.add(zMBuddyListListener);
        }
    }

    public void removeListener(ZMBuddyListListener zMBuddyListListener) {
        this.mListenerList.remove(zMBuddyListListener);
    }

    @NonNull
    public List<IMAddrBookItem> getAllBuddies() {
        return new ArrayList(this.mBuddies.values());
    }

    @NonNull
    public List<IMAddrBookItem> getAllSipBuddies() {
        ArrayList arrayList = new ArrayList();
        for (IMAddrBookItem iMAddrBookItem : this.mBuddies.values()) {
            if (iMAddrBookItem.isSIPAccount()) {
                arrayList.add(iMAddrBookItem);
            }
        }
        return arrayList;
    }

    @NonNull
    public List<IMAddrBookItem> getAllPbxBuddies() {
        ArrayList arrayList = new ArrayList();
        for (IMAddrBookItem iMAddrBookItem : this.mBuddies.values()) {
            if (iMAddrBookItem.isPBXAccount()) {
                arrayList.add(iMAddrBookItem);
            }
        }
        return arrayList;
    }

    @Nullable
    public IMAddrBookItem getBuddyByJid(@NonNull String str) {
        return getBuddyByJid(str, true);
    }

    @Nullable
    public IMAddrBookItem getBuddyByJid(@NonNull String str, boolean z) {
        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
        if (iMAddrBookItem != null) {
            return iMAddrBookItem;
        }
        if (z) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                if (buddyWithJID != null) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (fromZoomBuddy != null) {
                        this.mBuddies.put(str, fromZoomBuddy);
                        return fromZoomBuddy;
                    }
                }
            }
        }
        return null;
    }

    private void init() {
        loadAllBuddies();
        this.mHandler.sendEmptyMessageDelayed(2, 1000);
    }

    public void clearAllBuddies() {
        Runnable runnable = this.mLoadCaptureBuddiesRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        clearAllCaptureBuddies();
        this.mBuddies.clear();
        this.mBuddyGroups.clear();
        this.mBuddiesInBuddyGroup.clear();
        this.mDeletedJids.clear();
        this.mPendingInfoUpdateJids.clear();
        this.mPendingPresenceUpdateJids.clear();
    }

    /* access modifiers changed from: private */
    public void loadAllBuddies() {
        if (PTApp.getInstance().getZoomMessenger() != null) {
            if (captureAllBuddies() < 2000) {
                Runnable runnable = this.mLoadCaptureBuddiesRunnable;
                if (runnable != null) {
                    this.mHandler.post(runnable);
                }
            } else {
                new Thread("sortAllBuddy") {
                    public void run() {
                        synchronized (ZMBuddySyncInstance.this.mSortBuddiesLocker) {
                            ZMBuddySyncInstance.this.sortAllBuddies();
                        }
                        if (ZMBuddySyncInstance.this.mLoadCaptureBuddiesRunnable != null) {
                            ZMBuddySyncInstance.this.mHandler.post(ZMBuddySyncInstance.this.mLoadCaptureBuddiesRunnable);
                        }
                    }
                }.start();
            }
            loadAllBuddyGroups();
        }
    }

    private void loadAllBuddyGroups() {
        this.mBuddyGroups.clear();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            for (int i = 0; i < zoomMessenger.getBuddyGroupCount(); i++) {
                ZoomBuddyGroup buddyGroupAt = zoomMessenger.getBuddyGroupAt(i);
                if (buddyGroupAt != null) {
                    MMZoomBuddyGroup fromZoomBuddyGroup = MMZoomBuddyGroup.fromZoomBuddyGroup(buddyGroupAt);
                    this.mBuddyGroups.add(fromZoomBuddyGroup);
                    if (fromZoomBuddyGroup.getType() == 0) {
                        this.mUserBuddyGroup = fromZoomBuddyGroup;
                    }
                }
            }
            loadAddressBookContactBuddyGroup();
        }
    }

    @NonNull
    public List<MMZoomBuddyGroup> getAllBuddyGroup() {
        return new ArrayList(this.mBuddyGroups);
    }

    private void addBuddiesInBuddyGroup(@Nullable String str, @NonNull List<String> list) {
        if (!TextUtils.isEmpty(str) && !CollectionsUtil.isCollectionEmpty(list)) {
            Set set = (Set) this.mBuddiesInBuddyGroup.get(str);
            if (set == null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddyGroup buddyGroupByJid = zoomMessenger.getBuddyGroupByJid(str);
                    if (buddyGroupByJid != null) {
                        Set hashSet = new HashSet();
                        this.mBuddiesInBuddyGroup.put(str, hashSet);
                        boolean z = false;
                        for (int i = 0; i < buddyGroupByJid.getBuddyCount(); i++) {
                            ZoomBuddy buddyAt = buddyGroupByJid.getBuddyAt(i);
                            if (buddyAt != null) {
                                hashSet.add(buddyAt.getJid());
                            }
                        }
                        Iterator it = this.mBuddyGroups.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                if (TextUtils.equals(str, ((MMZoomBuddyGroup) it.next()).getId())) {
                                    z = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (!z) {
                            this.mBuddyGroups.add(MMZoomBuddyGroup.fromZoomBuddyGroup(buddyGroupByJid));
                        }
                        set = hashSet;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            set.addAll(list);
        }
    }

    private void addBuddiesInBuddyGroup(@Nullable String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str2);
            addBuddiesInBuddyGroup(str, (List<String>) arrayList);
        }
    }

    private void removeBuddiesInBuddyGroup(String str, @NonNull List<String> list) {
        if (!TextUtils.isEmpty(str) && !CollectionsUtil.isCollectionEmpty(list)) {
            Set set = (Set) this.mBuddiesInBuddyGroup.get(str);
            if (set != null) {
                set.removeAll(list);
            }
        }
    }

    private void removeBuddiesInBuddyGroup(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str2);
            removeBuddiesInBuddyGroup(str, (List<String>) arrayList);
        }
    }

    @Nullable
    public Set<String> getBuddiesInBuddyGroup(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Set<String> set = (Set) this.mBuddiesInBuddyGroup.get(str);
        if (set != null) {
            return set;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddyGroup buddyGroupByJid = zoomMessenger.getBuddyGroupByJid(str);
        if (buddyGroupByJid == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
        for (int i = 0; i < buddyGroupByJid.getBuddyCount(); i++) {
            String buddyJidAt = buddyGroupByJid.getBuddyJidAt(i);
            if (!TextUtils.isEmpty(buddyJidAt)) {
                hashSet.add(buddyJidAt);
            }
        }
        this.mBuddiesInBuddyGroup.put(str, hashSet);
        return hashSet;
    }

    public void onPhoneABEvent(int i, long j, Object obj) {
        loadAddressBookContactBuddyGroup();
        this.mChangeType = 2;
    }

    private void loadAddressBookContactBuddyGroup() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddyGroup addressbookContactBuddyGroup = zoomMessenger.getAddressbookContactBuddyGroup();
            if (addressbookContactBuddyGroup != null) {
                MMZoomBuddyGroup fromZoomBuddyGroup = MMZoomBuddyGroup.fromZoomBuddyGroup(addressbookContactBuddyGroup);
                int i = 0;
                while (true) {
                    if (i >= this.mBuddyGroups.size()) {
                        i = -1;
                        break;
                    } else if (TextUtils.equals(((MMZoomBuddyGroup) this.mBuddyGroups.get(i)).getId(), fromZoomBuddyGroup.getId())) {
                        break;
                    } else {
                        i++;
                    }
                }
                if (i == -1) {
                    this.mBuddyGroups.add(fromZoomBuddyGroup);
                } else {
                    this.mBuddyGroups.set(i, fromZoomBuddyGroup);
                }
                HashSet hashSet = new HashSet();
                for (int i2 = 0; i2 < addressbookContactBuddyGroup.getBuddyCount(); i2++) {
                    String buddyJidAt = addressbookContactBuddyGroup.getBuddyJidAt(i2);
                    if (!TextUtils.isEmpty(buddyJidAt)) {
                        hashSet.add(buddyJidAt);
                    }
                }
                String id = fromZoomBuddyGroup.getId();
                if (id != null) {
                    this.mBuddiesInBuddyGroup.put(id, hashSet);
                }
            }
        }
    }

    public void onPersonalGroupResponse(byte[] bArr) {
        try {
            PersonalGroupAtcionResponse parseFrom = PersonalGroupAtcionResponse.parseFrom(bArr);
            if (parseFrom != null && parseFrom.getResult() == 0) {
                notifyPersonalGroupSync(parseFrom.getType(), parseFrom.getGroupId(), new ArrayList(parseFrom.getChangeListList()), parseFrom.getFromGroupId(), parseFrom.getToGroupId());
            }
        } catch (InvalidProtocolBufferException unused) {
        }
    }

    public void notifyPersonalGroupSync(int i, String str, List<String> list, String str2, String str3) {
        if (!TextUtils.isEmpty(str) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str3)) {
            notifyPersonalGroupSync(i, str, list);
        }
        if (TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            notifyPersonalGroupSync(i, str2, list);
            notifyPersonalGroupSync(i, str3, list);
        }
    }

    public void notifyPersonalGroupSync(int i, String str, List<String> list) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddyGroup buddyGroupByXMPPId = zoomMessenger.getBuddyGroupByXMPPId(str);
            int i2 = 0;
            while (true) {
                if (i2 >= this.mBuddyGroups.size()) {
                    i2 = -1;
                    break;
                } else if (TextUtils.equals(((MMZoomBuddyGroup) this.mBuddyGroups.get(i2)).getXmppGroupID(), str)) {
                    break;
                } else {
                    i2++;
                }
            }
            if (buddyGroupByXMPPId != null) {
                if (i2 == -1) {
                    this.mBuddyGroups.add(MMZoomBuddyGroup.fromZoomBuddyGroup(buddyGroupByXMPPId));
                } else {
                    this.mBuddyGroups.set(i2, MMZoomBuddyGroup.fromZoomBuddyGroup(buddyGroupByXMPPId));
                    this.mBuddiesInBuddyGroup.remove(buddyGroupByXMPPId.getID());
                }
                getBuddiesInBuddyGroup(buddyGroupByXMPPId.getID());
            } else if (i2 != -1) {
                this.mBuddiesInBuddyGroup.remove(((MMZoomBuddyGroup) this.mBuddyGroups.remove(i2)).getId());
            }
        }
    }

    public void Indicate_BlockedUsersUpdated() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            List blockUserGetAll = zoomMessenger.blockUserGetAll();
            Iterator it = this.mBuddies.entrySet().iterator();
            while (true) {
                boolean z = true;
                if (!it.hasNext()) {
                    break;
                }
                Entry entry = (Entry) it.next();
                String str = (String) entry.getKey();
                boolean isBlocked = ((IMAddrBookItem) entry.getValue()).isBlocked();
                if (blockUserGetAll == null || !blockUserGetAll.contains(str)) {
                    z = false;
                }
                if (isBlocked != z) {
                    this.mPendingPresenceUpdateJids.add(str);
                }
                ((IMAddrBookItem) entry.getValue()).setBlocked(z);
            }
            if (this.mChangeType != 2) {
                this.mChangeType = 1;
            }
        }
    }

    public void Indicate_BlockedUsersAdded(@Nullable List<String> list) {
        if (list != null) {
            for (String str : list) {
                IMAddrBookItem buddyByJid = getBuddyByJid(str);
                if (buddyByJid != null) {
                    buddyByJid.setBlocked(true);
                }
                this.mPendingPresenceUpdateJids.add(str);
            }
            if (this.mChangeType != 2) {
                this.mChangeType = 1;
            }
        }
    }

    public void Indicate_BlockedUsersRemoved(@Nullable List<String> list) {
        if (list != null) {
            for (String str : list) {
                IMAddrBookItem buddyByJid = getBuddyByJid(str);
                if (buddyByJid != null) {
                    buddyByJid.setBlocked(false);
                }
                this.mPendingPresenceUpdateJids.add(str);
            }
            if (this.mChangeType != 2) {
                this.mChangeType = 1;
            }
        }
    }

    public void onIndicateBuddyListUpdated() {
        refreshAllBuddy();
    }

    public void Indicate_VCardInfoReady(@NonNull String str) {
        indicate_BuddyInfoUpdatedWithJID(str);
    }

    public void indicate_BuddyInfoUpdatedWithJID(@NonNull String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
                if (iMAddrBookItem != null) {
                    boolean refreshBuddy = iMAddrBookItem.refreshBuddy();
                    if (this.mChangeType != 2) {
                        if (refreshBuddy) {
                            this.mPendingInfoUpdateJids.add(str);
                        } else {
                            this.mPendingPresenceUpdateJids.add(str);
                        }
                        this.mChangeType = 1;
                    }
                } else {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (fromZoomBuddy != null) {
                        this.mBuddies.put(str, fromZoomBuddy);
                        this.mChangeType = 2;
                    }
                }
            }
        }
    }

    public void on_RemoveBuddy(String str, int i) {
        if (i == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                this.mDeletedJids.add(str);
                for (Entry entry : this.mBuddiesInBuddyGroup.entrySet()) {
                    if (((Set) entry.getValue()).contains(str)) {
                        ((Set) entry.getValue()).remove(str);
                        for (MMZoomBuddyGroup mMZoomBuddyGroup : this.mBuddyGroups) {
                            String id = mMZoomBuddyGroup.getId();
                            if (id != null && id.equals(entry.getKey())) {
                                zoomMessenger.removeBuddyToPersonalBuddyGroup(Collections.singletonList(str), mMZoomBuddyGroup.getXmppGroupID());
                            }
                        }
                    }
                }
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.remove(str);
                if (iMAddrBookItem != null) {
                    iMAddrBookItem.checkIsMyContact(zoomMessenger);
                    this.mChangeType = 2;
                }
            }
        }
    }

    public void onSearchBuddyPicDownloaded(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
            if (iMAddrBookItem != null) {
                iMAddrBookItem.updatePicture(zoomMessenger);
                if (this.mChangeType != 2) {
                    this.mPendingPresenceUpdateJids.add(str);
                    this.mChangeType = 1;
                }
            }
        }
    }

    public boolean onNotifySubscriptionAccepted(@NonNull String str) {
        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
        MMZoomBuddyGroup mMZoomBuddyGroup = this.mUserBuddyGroup;
        if (mMZoomBuddyGroup != null) {
            addBuddiesInBuddyGroup(mMZoomBuddyGroup.getId(), str);
        }
        if (iMAddrBookItem == null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null) {
                return false;
            }
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID == null) {
                return false;
            }
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
            if (fromZoomBuddy == null) {
                return false;
            }
            this.mBuddies.put(str, fromZoomBuddy);
        } else {
            iMAddrBookItem.refreshBuddy();
        }
        this.mChangeType = 2;
        return false;
    }

    public void onNotifySubscribeRequestUpdated(@NonNull String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID == null || !zoomMessenger.isMyContact(str)) {
                MMZoomBuddyGroup mMZoomBuddyGroup = this.mUserBuddyGroup;
                if (mMZoomBuddyGroup != null) {
                    removeBuddiesInBuddyGroup(mMZoomBuddyGroup.getId(), str);
                }
                if (this.mBuddies.containsKey(str)) {
                    this.mBuddies.remove(str);
                    this.mChangeType = 2;
                }
                return;
            }
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
            if (fromZoomBuddy != null) {
                this.mBuddies.put(str, fromZoomBuddy);
                MMZoomBuddyGroup mMZoomBuddyGroup2 = this.mUserBuddyGroup;
                if (mMZoomBuddyGroup2 != null) {
                    addBuddiesInBuddyGroup(mMZoomBuddyGroup2.getId(), str);
                }
                this.mChangeType = 2;
            }
        }
    }

    public boolean onNotifySubscriptionDenied(String str) {
        MMZoomBuddyGroup mMZoomBuddyGroup = this.mUserBuddyGroup;
        if (mMZoomBuddyGroup != null) {
            removeBuddiesInBuddyGroup(mMZoomBuddyGroup.getId(), str);
        }
        if (this.mBuddies.remove(str) != null) {
            this.mChangeType = 2;
        }
        return false;
    }

    public void Indicate_OnlineBuddies(@NonNull List<String> list) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            for (String str : list) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
                if (iMAddrBookItem != null) {
                    iMAddrBookItem.updatePresence(zoomMessenger);
                    if (this.mChangeType != 2) {
                        this.mPendingPresenceUpdateJids.add(str);
                        this.mChangeType = 1;
                    }
                }
            }
        }
    }

    public void Indicate_GetContactsPresence(@NonNull List<String> list, @NonNull List<String> list2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            for (String str : list) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
                if (iMAddrBookItem != null) {
                    iMAddrBookItem.updatePresence(zoomMessenger);
                    if (this.mChangeType != 2) {
                        this.mPendingPresenceUpdateJids.add(str);
                        this.mChangeType = 1;
                    }
                }
            }
            for (String str2 : list2) {
                IMAddrBookItem iMAddrBookItem2 = (IMAddrBookItem) this.mBuddies.get(str2);
                if (iMAddrBookItem2 != null) {
                    iMAddrBookItem2.updatePresence(zoomMessenger);
                    if (this.mChangeType != 2) {
                        this.mPendingPresenceUpdateJids.add(str2);
                        this.mChangeType = 1;
                    }
                }
            }
        }
    }

    public void Indicate_BuddyGroupsRemoved(List<String> list) {
        refreshAllBuddy();
    }

    public void requestBuddyListUpdate() {
        this.mChangeType = 2;
    }

    public void refreshAllBuddy() {
        synchronized (this.mRefreshAllBuddyLocker) {
            if (!this.mRefreshAllBuddyThread) {
                this.mRefreshAllBuddyThread = true;
                new Thread("IndicateBuddyListUpdated") {
                    public void run() {
                        synchronized (ZMBuddySyncInstance.this.mSortBuddiesLocker) {
                            if (!ZMBuddySyncInstance.this.mHandler.hasMessages(1)) {
                                ZMBuddySyncInstance.this.mHandler.sendEmptyMessage(1);
                            }
                        }
                        synchronized (ZMBuddySyncInstance.this.mRefreshAllBuddyLocker) {
                            ZMBuddySyncInstance.this.mRefreshAllBuddyThread = false;
                        }
                    }
                }.start();
            }
        }
    }

    public void Indicate_BuddyPresenceChanged(@NonNull String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
            if (iMAddrBookItem != null) {
                iMAddrBookItem.updatePresence(zoomMessenger);
                if (this.mChangeType != 2) {
                    this.mPendingPresenceUpdateJids.add(str);
                    this.mChangeType = 1;
                }
            } else {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                if (buddyWithJID != null) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (fromZoomBuddy != null) {
                        this.mBuddies.put(str, fromZoomBuddy);
                        this.mChangeType = 2;
                    }
                }
            }
        }
    }

    public void Indicate_BuddyGroupMembersUpdated(String str, @NonNull List<String> list) {
        if (!CollectionsUtil.isListEmpty(list)) {
            for (String indicate_BuddyInfoUpdatedWithJID : list) {
                indicate_BuddyInfoUpdatedWithJID(indicate_BuddyInfoUpdatedWithJID);
            }
        }
    }

    public void Indicate_BuddyGroupMembersAdded(@NonNull String str, @NonNull List<String> list) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !CollectionsUtil.isCollectionEmpty(list)) {
            addBuddiesInBuddyGroup(str, list);
            for (String str2 : list) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str2);
                if (buddyWithJID != null) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (fromZoomBuddy != null) {
                        this.mBuddies.put(str2, fromZoomBuddy);
                    }
                }
            }
            this.mChangeType = 2;
        }
    }

    public void Indicate_BuddyGroupMembersRemoved(String str, @NonNull List<String> list) {
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                removeBuddiesInBuddyGroup(str, list);
                for (String str2 : list) {
                    IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str2);
                    if (iMAddrBookItem != null) {
                        iMAddrBookItem.checkIsMyContact(zoomMessenger);
                    }
                }
                this.mChangeType = 2;
            }
        }
    }

    public void Indicate_BuddyGroupAdded(String str) {
        refreshAllBuddy();
    }

    public void Indicate_BuddyGroupMembersChanged(@Nullable ChangedBuddyGroups changedBuddyGroups, boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && changedBuddyGroups != null) {
            for (int i = 0; i < changedBuddyGroups.getBuddyGroupCount(); i++) {
                BuddyGroupMemberChangeList buddyGroup = changedBuddyGroups.getBuddyGroup(i);
                if (buddyGroup != null) {
                    String groupID = buddyGroup.getGroupID();
                    removeBuddiesInBuddyGroup(groupID, buddyGroup.getRemovedMemberJIDsList());
                    addBuddiesInBuddyGroup(groupID, buddyGroup.getAddedMemberJIDsList());
                }
            }
            if (z) {
                refreshAllBuddy();
            } else if (this.mBuddies.size() < 1000) {
                HashSet<String> hashSet = new HashSet<>();
                for (int i2 = 0; i2 < changedBuddyGroups.getBuddyGroupCount(); i2++) {
                    BuddyGroupMemberChangeList buddyGroup2 = changedBuddyGroups.getBuddyGroup(i2);
                    if (buddyGroup2 != null) {
                        hashSet.addAll(buddyGroup2.getAddedMemberJIDsList());
                    }
                }
                for (String str : hashSet) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                        if (fromZoomBuddy != null) {
                            fromZoomBuddy.setIsMyContact(true);
                            this.mBuddies.put(str, fromZoomBuddy);
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                this.mChangeType = 2;
            }
        }
    }

    public void onAddBuddyByJid(@NonNull String str) {
        IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
        if (iMAddrBookItem != null) {
            iMAddrBookItem.refreshBuddy();
            MMZoomBuddyGroup mMZoomBuddyGroup = this.mUserBuddyGroup;
            if (mMZoomBuddyGroup != null) {
                addBuddiesInBuddyGroup(mMZoomBuddyGroup.getId(), str);
            }
            this.mChangeType = 2;
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
            if (buddyWithJID != null) {
                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                if (fromZoomBuddy != null) {
                    this.mBuddies.put(str, fromZoomBuddy);
                    this.mDeletedJids.remove(str);
                    MMZoomBuddyGroup mMZoomBuddyGroup2 = this.mUserBuddyGroup;
                    if (mMZoomBuddyGroup2 != null) {
                        addBuddiesInBuddyGroup(mMZoomBuddyGroup2.getId(), str);
                    }
                    this.mChangeType = 2;
                }
            }
        }
    }

    public void Indicate_BuddyAdded(@NonNull String str, @NonNull List<String> list) {
        if (!TextUtils.isEmpty(str) && !CollectionsUtil.isCollectionEmpty(list)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                for (String addBuddiesInBuddyGroup : list) {
                    addBuddiesInBuddyGroup(addBuddiesInBuddyGroup, str);
                }
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) this.mBuddies.get(str);
                if (iMAddrBookItem == null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                    if (buddyWithJID != null) {
                        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                        if (fromZoomBuddy != null) {
                            this.mBuddies.put(str, fromZoomBuddy);
                            this.mDeletedJids.remove(str);
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    iMAddrBookItem.refreshBuddy();
                }
                this.mChangeType = 2;
            }
        }
    }

    /* access modifiers changed from: protected */
    @Nullable
    public char[] getSortKey(@Nullable int[] iArr) {
        if (iArr == null || iArr.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i : iArr) {
            String sortKey = PinyinUtil.getSortKey(new String(Character.toChars(i)));
            if (StringUtil.isEmptyOrNull(sortKey)) {
                stringBuffer.append(Character.toChars(i));
            } else if (sortKey.length() == 1) {
                stringBuffer.append(sortKey);
            } else {
                stringBuffer.append(sortKey.charAt(0));
            }
        }
        return stringBuffer.toString().toCharArray();
    }

    private void clearAllCaptureBuddies() {
        synchronized (this.mNaviteBuddyVetctLocker) {
            clearAllCaptureBuddiesImpl();
        }
    }

    private int captureAllBuddies() {
        int captureAllBuddiesImpl;
        synchronized (this.mNaviteBuddyVetctLocker) {
            captureAllBuddiesImpl = captureAllBuddiesImpl();
        }
        return captureAllBuddiesImpl;
    }

    /* access modifiers changed from: private */
    public void sortAllBuddies() {
        synchronized (this.mNaviteBuddyVetctLocker) {
            sortAllBuddiesImpl();
        }
    }

    /* access modifiers changed from: private */
    @Nullable
    public byte[] loadCaptureBuddies(int i) {
        byte[] loadCaptureBuddiesImpl;
        synchronized (this.mNaviteBuddyVetctLocker) {
            loadCaptureBuddiesImpl = loadCaptureBuddiesImpl(i);
        }
        return loadCaptureBuddiesImpl;
    }
}
