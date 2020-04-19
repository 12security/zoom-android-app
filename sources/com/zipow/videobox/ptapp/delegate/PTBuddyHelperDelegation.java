package com.zipow.videobox.ptapp.delegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTBuddyHelper;
import com.zipow.videobox.ptapp.PTUI.IIMListener;
import java.util.ArrayList;
import java.util.Iterator;

public class PTBuddyHelperDelegation implements IIMListener {
    private static final String TAG = "PTBuddyHelperDelegation";
    private PTBuddyHelper mBuddyHelper;
    @NonNull
    private ArrayList<BuddyItem> mBuddyItems = new ArrayList<>();

    public void onIMBuddySort() {
    }

    public void onIMReceived(IMMessage iMMessage) {
    }

    public void onQueryIPLocation(int i, IPLocationInfo iPLocationInfo) {
    }

    public void onSubscriptionRequest() {
    }

    public void onSubscriptionUpdate() {
    }

    protected PTBuddyHelperDelegation() {
        reloadAllBuddyItems();
        PTUIDelegation.getInstance().addIMListener(this);
    }

    protected PTBuddyHelperDelegation(PTBuddyHelper pTBuddyHelper) {
        this.mBuddyHelper = pTBuddyHelper;
    }

    @Nullable
    public BuddyItem getBuddyItem(int i) {
        BuddyItem buddyItem;
        PTBuddyHelper pTBuddyHelper = this.mBuddyHelper;
        if (pTBuddyHelper != null) {
            return pTBuddyHelper.getBuddyItem(i);
        }
        synchronized (this.mBuddyItems) {
            buddyItem = (BuddyItem) this.mBuddyItems.get(i);
        }
        return buddyItem;
    }

    public int getBuddyItemCount() {
        int size;
        PTBuddyHelper pTBuddyHelper = this.mBuddyHelper;
        if (pTBuddyHelper != null) {
            return pTBuddyHelper.getBuddyItemCount();
        }
        synchronized (this.mBuddyItems) {
            size = this.mBuddyItems.size();
        }
        return size;
    }

    @Nullable
    public BuddyItem getBuddyItemByJid(@NonNull String str) {
        PTBuddyHelper pTBuddyHelper = this.mBuddyHelper;
        if (pTBuddyHelper != null) {
            return pTBuddyHelper.getBuddyItemByJid(str);
        }
        synchronized (this.mBuddyItems) {
            Iterator it = this.mBuddyItems.iterator();
            while (it.hasNext()) {
                BuddyItem buddyItem = (BuddyItem) it.next();
                if (str.equals(buddyItem.getJid())) {
                    return buddyItem;
                }
            }
            return null;
        }
    }

    @Nullable
    public String[] filterBuddyWithInput(@Nullable String str) {
        String[] strArr;
        if (str == null) {
            return null;
        }
        PTBuddyHelper pTBuddyHelper = this.mBuddyHelper;
        if (pTBuddyHelper != null) {
            return pTBuddyHelper.filterBuddyWithInput(str);
        }
        synchronized (this.mBuddyItems) {
            ArrayList arrayList = new ArrayList();
            Iterator it = this.mBuddyItems.iterator();
            while (it.hasNext()) {
                BuddyItem buddyItem = (BuddyItem) it.next();
                String screenName = buddyItem.getScreenName();
                if (screenName != null) {
                    if (screenName.toLowerCase().indexOf(str.toLowerCase()) >= 0) {
                        arrayList.add(buddyItem.getJid());
                    }
                }
            }
            strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        return strArr;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:7|8|9|10|11) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0017 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void reloadAllBuddyItems() {
        /*
            r2 = this;
            java.util.ArrayList<com.zipow.videobox.ptapp.IMProtos$BuddyItem> r0 = r2.mBuddyItems
            monitor-enter(r0)
            java.util.ArrayList<com.zipow.videobox.ptapp.IMProtos$BuddyItem> r1 = r2.mBuddyItems     // Catch:{ all -> 0x0019 }
            r1.clear()     // Catch:{ all -> 0x0019 }
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getInstance()     // Catch:{ all -> 0x0019 }
            com.zipow.videobox.IPTService r1 = r1.getPTService()     // Catch:{ all -> 0x0019 }
            if (r1 != 0) goto L_0x0014
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            return
        L_0x0014:
            r1.reloadAllBuddyItems()     // Catch:{ RemoteException -> 0x0017 }
        L_0x0017:
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            return
        L_0x0019:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.ptapp.delegate.PTBuddyHelperDelegation.reloadAllBuddyItems():void");
    }

    private void clearBuddyItems() {
        synchronized (this.mBuddyItems) {
            this.mBuddyItems.clear();
        }
    }

    public void onIMLocalStatusChanged(int i) {
        if (i == 0) {
            clearBuddyItems();
        } else if (i == 4) {
            reloadAllBuddyItems();
        }
    }

    public void onIMBuddyPresence(@NonNull BuddyItem buddyItem) {
        updateBuddyItem(buddyItem);
    }

    public void onIMBuddyPic(@NonNull BuddyItem buddyItem) {
        updateBuddyItem(buddyItem);
    }

    private void updateBuddyItem(@NonNull BuddyItem buddyItem) {
        synchronized (this.mBuddyItems) {
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= this.mBuddyItems.size()) {
                    break;
                }
                BuddyItem buddyItem2 = (BuddyItem) this.mBuddyItems.get(i);
                if (buddyItem2 != null) {
                    if (buddyItem.getJid() != null && buddyItem.getJid().equals(buddyItem2.getJid())) {
                        this.mBuddyItems.set(i, buddyItem);
                        z = true;
                        break;
                    }
                }
                i++;
            }
            if (!z) {
                this.mBuddyItems.add(buddyItem);
            }
        }
    }
}
