package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.eventbus.ZMContactsBuddLongClickyEvent;
import com.zipow.videobox.eventbus.ZMContactsBuddyEvent;
import com.zipow.videobox.eventbus.ZMContactsGroupLongClickEvent;
import com.zipow.videobox.eventbus.ZMContactsPhoneAddressEvent;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.IPinnedSectionAdapter;
import p021us.zoom.androidlib.widget.pinnedsectionrecyclerview.PinnedSectionRecyclerView;
import p021us.zoom.videomeetings.C4558R;

public class IMDirectoryAdapter extends Adapter<DirectoryHolder> implements IPinnedSectionAdapter {
    private static final int DELAY_HINT = 1200;
    private static final String TAG = "IMDirectoryAdapter";
    private static final int TYPE_BUDDY = 2;
    private static final int TYPE_CATE = 0;
    private static final int TYPE_GROUP = 1;
    private static final int TYPE_PHONE_INVITE = 3;
    private ItemData mAllCompanyContacts;
    private ItemData mAutoAnswerContacts;
    private ItemData mCompanyLabel;
    private Context mContext = VideoBoxApplication.getGlobalContext();
    /* access modifiers changed from: private */
    @NonNull
    public List<Object> mDisplayDatas = new ArrayList();
    private final boolean mEnableStableIds;
    private ItemData mExternalContacts;
    @NonNull
    private List<ItemData> mGroupDatas = new ArrayList();
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsGroupOn = PTApp.getInstance().isSyncUserGroupON();
    private boolean mIsKeepCompanyContact = PTApp.getInstance().isKeepCompanyContacts();
    private ItemData mMyGroupLabel;
    @NonNull
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(Object obj, DirectoryHolder directoryHolder) {
            IMDirectoryAdapter.this.onItemClick(obj, directoryHolder);
        }
    };
    private RecyclerView mParentView;
    private ItemData mPhoneAddressContacts;
    private ItemData mRoomContacts;
    private ItemData mRoomSystems;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mRreshTask;
    @NonNull
    private List<String> mShowedBuddies = new ArrayList();
    private ItemData mStarContacts;
    private ItemData mTBD;
    @NonNull
    private List<WeakReference<DirectoryHolder>> mViewHolderInCache = new ArrayList();

    static class BuddyComparator implements Comparator<ItemData> {
        private Collator mCollator = Collator.getInstance(CompatUtils.getLocalDefault());

        BuddyComparator() {
            this.mCollator.setStrength(0);
        }

        public int compare(@Nullable ItemData itemData, @Nullable ItemData itemData2) {
            if ((itemData == null || itemData.buddy == null) && (itemData2 == null || itemData2.buddy == null)) {
                return 0;
            }
            if (itemData == null || itemData.buddy == null) {
                return 1;
            }
            if (itemData2 == null || itemData2.buddy == null) {
                return -1;
            }
            String sortKey = itemData.buddy.getSortKey();
            String sortKey2 = itemData2.buddy.getSortKey();
            if (sortKey == null) {
                sortKey = "";
            }
            if (sortKey2 == null) {
                sortKey2 = "";
            }
            return this.mCollator.compare(sortKey, sortKey2);
        }
    }

    static class BuddyHolder extends DirectoryHolder implements OnClickListener, OnLongClickListener {
        public ItemData item;
        public IMAddrBookItemView itemView;

        BuddyHolder(@NonNull IMAddrBookItemView iMAddrBookItemView) {
            super(iMAddrBookItemView);
            this.itemView = iMAddrBookItemView;
            iMAddrBookItemView.setOnClickListener(this);
            iMAddrBookItemView.setOnLongClickListener(this);
        }

        public void updateUI(Object obj) {
            if (obj instanceof ItemData) {
                this.item = (ItemData) obj;
                this.itemView.setAddrBookItem(this.item.buddy, false, true, false);
            }
        }

        public void onClick(View view) {
            if (this.itemView != null && this.clickListener != null) {
                this.clickListener.onItemClick(this.item.buddy, this);
            }
        }

        public boolean onLongClick(View view) {
            if (this.item == null) {
                return false;
            }
            EventBus.getDefault().post(new ZMContactsBuddLongClickyEvent(this.item.buddy, this.item.group));
            return true;
        }
    }

    static class CategoryHolder extends DirectoryHolder {
        private String mCateName;
        @NonNull
        private final String mFormatString;
        private TextView txtCateName;

        CategoryHolder(@NonNull View view, @NonNull String str) {
            super(view);
            this.mFormatString = str;
            this.txtCateName = (TextView) view.findViewById(C4558R.C4560id.txtCateName);
        }

        /* access modifiers changed from: 0000 */
        public void updateUI(Object obj) {
            if (obj instanceof ItemData) {
                this.mCateName = ((ItemData) obj).cateName;
                this.txtCateName.setText(this.mCateName);
                this.txtCateName.setContentDescription(String.format(this.mFormatString, new Object[]{StringUtil.safeString(this.mCateName)}));
            }
            getitemView().setBackgroundColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_white));
        }
    }

    static abstract class DirectoryHolder extends ViewHolder {
        protected OnItemClickListener clickListener;
        /* access modifiers changed from: private */
        public View itemView;

        /* access modifiers changed from: 0000 */
        public abstract void updateUI(Object obj);

        public void setClickListener(OnItemClickListener onItemClickListener) {
            this.clickListener = onItemClickListener;
        }

        public DirectoryHolder(@NonNull View view) {
            super(view);
            this.itemView = view;
        }

        public void update(Object obj) {
            if ((obj instanceof ItemData) && this.itemView != null) {
                if (System.currentTimeMillis() - ((ItemData) obj).actionTime <= 1200) {
                    this.itemView.setBackgroundColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_ui_kit_color_light_blue_E0F0FE));
                } else {
                    this.itemView.setBackgroundResource(C4558R.C4559drawable.zm_list_selector_white_bg);
                }
            }
            updateUI(obj);
        }

        public View getitemView() {
            return this.itemView;
        }
    }

    private static class ExternalContactItemData extends ItemData {
        ExternalContactItemData() {
            this.buddiesInGroup = new ArrayList();
            this.buddiesInGroup.add(new InvitePhoneAddress());
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            this.buddiesInGroup = new ArrayList();
            this.buddiesInGroup.add(new InvitePhoneAddress());
        }

        /* access modifiers changed from: 0000 */
        public int getBuddySize() {
            int size = this.buddiesInGroup == null ? 0 : this.buddiesInGroup.size();
            return (this.group == null || this.group.getBuddyCount() <= size) ? size - 1 : this.group.getBuddyCount();
        }

        /* access modifiers changed from: 0000 */
        public void addBuddies(Collection<IMAddrBookItem> collection) {
            if (!CollectionsUtil.isCollectionEmpty(this.buddiesInGroup) && (this.buddiesInGroup.get(this.buddiesInGroup.size() - 1) instanceof InvitePhoneAddress)) {
                this.buddiesInGroup.remove(this.buddiesInGroup.size() - 1);
            }
            super.addBuddies(collection);
            this.buddiesInGroup.add(new InvitePhoneAddress());
        }

        /* access modifiers changed from: 0000 */
        public void sort() {
            super.sort();
            if (!CollectionsUtil.isCollectionEmpty(this.buddiesInGroup)) {
                int size = this.buddiesInGroup.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        size = -1;
                        break;
                    } else if (this.buddiesInGroup.get(size) instanceof InvitePhoneAddress) {
                        break;
                    }
                }
                if (size != -1) {
                    this.buddiesInGroup.add(0, (ItemData) this.buddiesInGroup.remove(size));
                }
            }
        }
    }

    static class GroupComparatorByName implements Comparator<ItemData> {
        private Collator mCollator = Collator.getInstance(CompatUtils.getLocalDefault());

        GroupComparatorByName() {
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull ItemData itemData, @NonNull ItemData itemData2) {
            if (itemData.group == null && itemData2.group == null) {
                return 0;
            }
            if (itemData.group == null) {
                return 1;
            }
            if (itemData2.group == null) {
                return -1;
            }
            return this.mCollator.compare(itemData.group.getName(), itemData2.group.getName());
        }
    }

    static class GroupHolder extends DirectoryHolder implements OnClickListener, OnLongClickListener {
        private ItemData data;
        private ImageView imgCateExpand;
        @NonNull
        private final String mFormatCollapsedString;
        @NonNull
        private final String mFormatExpandedString;
        private View rlGroup;
        private TextView txtCateName;
        private TextView txtCount;

        GroupHolder(@NonNull View view, @NonNull String str, @NonNull String str2) {
            super(view);
            this.rlGroup = view.findViewById(C4558R.C4560id.rlGroup);
            this.txtCateName = (TextView) view.findViewById(C4558R.C4560id.txtCateName);
            this.imgCateExpand = (ImageView) view.findViewById(C4558R.C4560id.imgCateExpand);
            this.txtCount = (TextView) view.findViewById(C4558R.C4560id.txtCount);
            this.mFormatExpandedString = str;
            this.mFormatCollapsedString = str2;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        /* access modifiers changed from: 0000 */
        public void updateUI(Object obj) {
            if (obj instanceof ItemData) {
                ItemData itemData = (ItemData) obj;
                this.data = itemData;
                MMZoomBuddyGroup mMZoomBuddyGroup = itemData.group;
                if (mMZoomBuddyGroup != null) {
                    this.txtCateName.setText(mMZoomBuddyGroup.getName());
                    TextView textView = this.txtCount;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(itemData.getBuddySize());
                    textView.setText(sb.toString());
                    if (itemData.isExpanded) {
                        this.rlGroup.setContentDescription(String.format(this.mFormatExpandedString, new Object[]{StringUtil.safeString(mMZoomBuddyGroup.getName()), Integer.valueOf(itemData.getBuddySize())}));
                    } else {
                        this.rlGroup.setContentDescription(String.format(this.mFormatCollapsedString, new Object[]{StringUtil.safeString(mMZoomBuddyGroup.getName()), Integer.valueOf(itemData.getBuddySize())}));
                    }
                }
                this.imgCateExpand.setImageResource(itemData.isExpanded ? C4558R.C4559drawable.zm_directory_group_expand : C4558R.C4559drawable.zm_directory_group_unexpand);
            }
        }

        public void onClick(View view) {
            if (this.data != null && this.clickListener != null) {
                this.clickListener.onItemClick(this.data, this);
            }
        }

        public boolean onLongClick(View view) {
            ItemData itemData = this.data;
            if (itemData == null || itemData.group == null || this.data.group.getType() != 500) {
                return false;
            }
            EventBus.getDefault().post(new ZMContactsGroupLongClickEvent(this.data.group));
            return true;
        }
    }

    private static class InvitePhoneAddress extends ItemData {
        private InvitePhoneAddress() {
        }
    }

    static class InvitePhoneAddressHolder extends DirectoryHolder implements OnClickListener {
        private InvitePhoneAddress data;
        private View itemView;
        private TextView txtScreenName;

        InvitePhoneAddressHolder(@NonNull View view) {
            super(view);
            this.txtScreenName = (TextView) view.findViewById(C4558R.C4560id.txtScreenName);
            this.itemView = view;
            view.setOnClickListener(this);
        }

        /* access modifiers changed from: 0000 */
        public void updateUI(Object obj) {
            if (obj instanceof InvitePhoneAddress) {
                this.data = (InvitePhoneAddress) obj;
                if (PTApp.getInstance().isPhoneNumberRegistered()) {
                    this.txtScreenName.setText(this.itemView.getResources().getString(C4558R.string.zm_lbl_invite_zoom_105180));
                } else {
                    this.txtScreenName.setText(this.itemView.getResources().getString(C4558R.string.zm_lbl_invite_connect_phone_contacts_105180));
                }
            }
        }

        public void onClick(View view) {
            if (this.data != null && this.clickListener != null) {
                this.clickListener.onItemClick(this.data, this);
            }
        }
    }

    static class ItemData {
        long actionTime;
        List<ItemData> buddiesInGroup;
        IMAddrBookItem buddy;
        @Nullable
        String cateName;
        @Nullable
        MMZoomBuddyGroup group;
        boolean isExpanded;
        boolean isSorted;
        int type;

        ItemData() {
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            List<ItemData> list = this.buddiesInGroup;
            if (list != null) {
                list.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public int getBuddySize() {
            List<ItemData> list = this.buddiesInGroup;
            int size = list == null ? 0 : list.size();
            MMZoomBuddyGroup mMZoomBuddyGroup = this.group;
            return (mMZoomBuddyGroup == null || mMZoomBuddyGroup.getBuddyCount() <= size) ? size : this.group.getBuddyCount();
        }

        /* access modifiers changed from: 0000 */
        public void addBuddies(@Nullable Collection<IMAddrBookItem> collection, boolean z) {
            if (collection != null) {
                this.isSorted = false;
                if (this.buddiesInGroup == null) {
                    this.buddiesInGroup = new ArrayList();
                }
                HashSet hashSet = new HashSet(this.buddiesInGroup);
                for (IMAddrBookItem iMAddrBookItem : collection) {
                    ItemData itemData = new ItemData();
                    itemData.buddy = iMAddrBookItem;
                    itemData.group = this.group;
                    itemData.type = 2;
                    if (z) {
                        itemData.actionTime = System.currentTimeMillis();
                    }
                    hashSet.add(itemData);
                }
                this.buddiesInGroup = new ArrayList(hashSet);
            }
        }

        /* access modifiers changed from: 0000 */
        public void addBuddies(Collection<IMAddrBookItem> collection) {
            addBuddies(collection, false);
        }

        /* access modifiers changed from: 0000 */
        public void sort() {
            if (!this.isSorted) {
                Collections.sort(this.buddiesInGroup, new BuddyComparator());
                this.isSorted = true;
            }
        }

        public int hashCode() {
            IMAddrBookItem iMAddrBookItem = this.buddy;
            if (iMAddrBookItem != null) {
                return iMAddrBookItem.hashCode();
            }
            return super.hashCode();
        }

        public boolean equals(@Nullable Object obj) {
            if (!(obj instanceof ItemData)) {
                return super.equals(obj);
            }
            IMAddrBookItem iMAddrBookItem = ((ItemData) obj).buddy;
            if (iMAddrBookItem != null) {
                IMAddrBookItem iMAddrBookItem2 = this.buddy;
                if (iMAddrBookItem2 != null) {
                    return iMAddrBookItem.equals(iMAddrBookItem2);
                }
            }
            return super.equals(obj);
        }

        /* access modifiers changed from: 0000 */
        public boolean isBuddyEmpty() {
            return getBuddySize() == 0;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Object obj, DirectoryHolder directoryHolder);
    }

    private static class StarItemData extends ItemData {
        private StarItemData() {
        }

        /* access modifiers changed from: 0000 */
        public void sort() {
            super.sort();
            if (!CollectionsUtil.isCollectionEmpty(this.buddiesInGroup)) {
                int i = 0;
                while (true) {
                    if (i >= this.buddiesInGroup.size()) {
                        i = -1;
                        break;
                    }
                    ItemData itemData = (ItemData) this.buddiesInGroup.get(i);
                    if (itemData.buddy != null && itemData.buddy.isMyNote()) {
                        break;
                    }
                    i++;
                }
                if (i != -1) {
                    this.buddiesInGroup.add(0, (ItemData) this.buddiesInGroup.remove(i));
                }
            }
        }
    }

    public void onChanged() {
    }

    public IMDirectoryAdapter(boolean z) {
        this.mEnableStableIds = z;
        initData();
    }

    private void initData() {
        this.mMyGroupLabel = new ItemData();
        ItemData itemData = this.mMyGroupLabel;
        itemData.type = 0;
        itemData.cateName = this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_my_contacts_149054);
        this.mCompanyLabel = new ItemData();
        ItemData itemData2 = this.mCompanyLabel;
        itemData2.type = 0;
        itemData2.cateName = this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_all_contacts_149054);
        this.mAllCompanyContacts = new ItemData();
        ItemData itemData3 = this.mAllCompanyContacts;
        itemData3.type = 1;
        itemData3.group = new MMZoomBuddyGroup();
        this.mAllCompanyContacts.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_company_contacts_68451));
        this.mTBD = new ItemData();
        ItemData itemData4 = this.mTBD;
        itemData4.type = 1;
        itemData4.group = new MMZoomBuddyGroup();
        this.mTBD.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_tbd_68451));
        this.mAutoAnswerContacts = new ItemData();
        ItemData itemData5 = this.mAutoAnswerContacts;
        itemData5.type = 1;
        itemData5.group = new MMZoomBuddyGroup();
        this.mAutoAnswerContacts.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_auto_answer_contacts_68451));
        this.mStarContacts = new StarItemData();
        ItemData itemData6 = this.mStarContacts;
        itemData6.type = 1;
        itemData6.group = new MMZoomBuddyGroup();
        this.mStarContacts.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_star_contacts_68451));
        this.mPhoneAddressContacts = new ItemData();
        ItemData itemData7 = this.mPhoneAddressContacts;
        itemData7.type = 1;
        itemData7.group = new MMZoomBuddyGroup();
        this.mPhoneAddressContacts.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_phone_contacts_105180));
        this.mExternalContacts = new ExternalContactItemData();
        ItemData itemData8 = this.mExternalContacts;
        itemData8.type = 1;
        itemData8.group = new MMZoomBuddyGroup();
        this.mExternalContacts.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_external_contacts_68451));
        this.mRoomContacts = new ItemData();
        ItemData itemData9 = this.mRoomContacts;
        itemData9.type = 1;
        itemData9.group = new MMZoomBuddyGroup();
        this.mRoomContacts.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_room_contacts_68451));
        this.mRoomSystems = new ItemData();
        ItemData itemData10 = this.mRoomSystems;
        itemData10.type = 1;
        itemData10.group = new MMZoomBuddyGroup();
        this.mRoomSystems.group.setName(this.mContext.getResources().getString(C4558R.string.zm_mm_lbl_room_systems_82945));
        if (!CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            this.mStarContacts.isExpanded = true;
            this.mExternalContacts.isExpanded = true;
        }
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mParentView = recyclerView;
    }

    @NonNull
    public List<String> getShowedBuddies(boolean z) {
        ArrayList arrayList = new ArrayList(this.mShowedBuddies);
        if (z) {
            this.mShowedBuddies.clear();
        }
        return arrayList;
    }

    public void updateBuddyInfo(@Nullable List<String> list) {
        if (!CollectionsUtil.isCollectionEmpty(list)) {
            int i = 0;
            ZMLog.m278d(TAG, "updateBuddyInfo", new Object[0]);
            while (i < this.mViewHolderInCache.size()) {
                ViewHolder viewHolder = (ViewHolder) ((WeakReference) this.mViewHolderInCache.get(i)).get();
                if (viewHolder == null) {
                    this.mViewHolderInCache.remove(i);
                    i--;
                }
                if (viewHolder instanceof BuddyHolder) {
                    BuddyHolder buddyHolder = (BuddyHolder) viewHolder;
                    if (!(buddyHolder.item == null || buddyHolder.item.buddy == null || !list.contains(buddyHolder.item.buddy.getJid()))) {
                        buddyHolder.update(buddyHolder.item);
                    }
                }
                i++;
            }
        }
    }

    public void removeGroup(String str) {
        if (!TextUtils.isEmpty(str)) {
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= this.mGroupDatas.size()) {
                    break;
                }
                ItemData itemData = (ItemData) this.mGroupDatas.get(i);
                if (itemData != null && itemData.group != null && TextUtils.equals(str, itemData.group.getXmppGroupID())) {
                    this.mGroupDatas.remove(i);
                    z = true;
                    break;
                }
                i++;
            }
            if (z) {
                refresh();
            }
        }
    }

    public void expandGroup(String str) {
        for (Object next : this.mDisplayDatas) {
            if (next instanceof ItemData) {
                ItemData itemData = (ItemData) next;
                MMZoomBuddyGroup mMZoomBuddyGroup = itemData.group;
                if (mMZoomBuddyGroup != null && TextUtils.equals(str, mMZoomBuddyGroup.getXmppGroupID())) {
                    itemData.isExpanded = true;
                }
            }
        }
    }

    public int getIndexByGroupId(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            Object obj = this.mDisplayDatas.get(i);
            if (obj instanceof ItemData) {
                MMZoomBuddyGroup mMZoomBuddyGroup = ((ItemData) obj).group;
                if (mMZoomBuddyGroup != null && TextUtils.equals(str, mMZoomBuddyGroup.getXmppGroupID())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getIndexByBuddyId(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        for (int i = 0; i < this.mDisplayDatas.size(); i++) {
            Object obj = this.mDisplayDatas.get(i);
            if (obj instanceof ItemData) {
                IMAddrBookItem iMAddrBookItem = ((ItemData) obj).buddy;
                if (iMAddrBookItem != null && TextUtils.equals(str, iMAddrBookItem.getJid())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void updateData(String str) {
        if (!TextUtils.isEmpty(str)) {
            ZMLog.m278d(TAG, "updateData(String groupId)", new Object[0]);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddyGroup buddyGroupByXMPPId = zoomMessenger.getBuddyGroupByXMPPId(str);
                if (buddyGroupByXMPPId != null) {
                    updateData(MMZoomBuddyGroup.fromZoomBuddyGroup(buddyGroupByXMPPId));
                }
            }
        }
    }

    public void updateData(@NonNull final MMZoomBuddyGroup mMZoomBuddyGroup) {
        if (!this.mIsGroupOn || this.mIsKeepCompanyContact || !mMZoomBuddyGroup.isInCompanyContacts()) {
            boolean z = false;
            ZMLog.m278d(TAG, "updateData(MMZoomBuddyGroup group)", new Object[0]);
            Iterator it = this.mGroupDatas.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ItemData itemData = (ItemData) it.next();
                if (itemData.group != null && TextUtils.equals(mMZoomBuddyGroup.getXmppGroupID(), itemData.group.getXmppGroupID())) {
                    itemData.group = mMZoomBuddyGroup;
                    itemData.actionTime = System.currentTimeMillis();
                    z = true;
                    break;
                }
            }
            if (!z) {
                ZMBuddySyncInstance insatance = ZMBuddySyncInstance.getInsatance();
                ItemData itemData2 = new ItemData();
                itemData2.type = 1;
                itemData2.group = mMZoomBuddyGroup;
                itemData2.actionTime = System.currentTimeMillis();
                Set<String> buddiesInBuddyGroup = insatance.getBuddiesInBuddyGroup(mMZoomBuddyGroup.getId());
                ArrayList arrayList = new ArrayList();
                if (!CollectionsUtil.isCollectionEmpty(buddiesInBuddyGroup)) {
                    for (String buddyByJid : buddiesInBuddyGroup) {
                        arrayList.add(insatance.getBuddyByJid(buddyByJid, true));
                    }
                }
                itemData2.addBuddies(arrayList);
                this.mGroupDatas.add(itemData2);
            }
            refresh(true);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    for (int i = 0; i < IMDirectoryAdapter.this.mDisplayDatas.size(); i++) {
                        Object obj = IMDirectoryAdapter.this.mDisplayDatas.get(i);
                        if (obj instanceof ItemData) {
                            ItemData itemData = (ItemData) obj;
                            if (itemData.group != null && TextUtils.equals(mMZoomBuddyGroup.getId(), itemData.group.getId())) {
                                itemData.actionTime = 0;
                                IMDirectoryAdapter.this.notifyItemChanged(i);
                                return;
                            }
                        }
                    }
                }
            }, 1500);
        }
    }

    public void updateStarsBuddiesInBuddyGroup(Collection<IMAddrBookItem> collection, boolean z) {
        this.mStarContacts.clear();
        this.mStarContacts.addBuddies(collection);
        if (z) {
            refresh();
        }
    }

    public void updateRoomSystemsInBuddyGroup(Collection<IMAddrBookItem> collection, boolean z) {
        this.mRoomSystems.clear();
        this.mRoomSystems.addBuddies(collection);
        if (z) {
            refresh();
        }
    }

    public void removeBuddiesInBuddyGroup(String str, @Nullable Collection<String> collection, boolean z) {
        ItemData itemData;
        if (!TextUtils.isEmpty(str) && !CollectionsUtil.isCollectionEmpty(collection)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.getBuddyGroupByXMPPId(str) != null) {
                Iterator it = this.mGroupDatas.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    itemData = (ItemData) it.next();
                    if (itemData.group == null || (!TextUtils.equals(str, itemData.group.getXmppGroupID()) && !TextUtils.equals(str, itemData.group.getId()))) {
                    }
                }
                if (!CollectionsUtil.isCollectionEmpty(itemData.buddiesInGroup)) {
                    int i = 0;
                    while (i < itemData.buddiesInGroup.size()) {
                        ItemData itemData2 = (ItemData) itemData.buddiesInGroup.get(i);
                        if (itemData2.buddy != null && collection.remove(itemData2.buddy.getJid())) {
                            itemData.buddiesInGroup.remove(i);
                            i--;
                        }
                        i++;
                    }
                    itemData.group.setBuddyCount(itemData.buddiesInGroup.size());
                    if (itemData.isExpanded) {
                        refresh();
                    } else {
                        notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0054, code lost:
        if (r1.buddiesInGroup != null) goto L_0x005d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0056, code lost:
        r1.buddiesInGroup = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005d, code lost:
        r7 = new java.util.ArrayList();
        r0 = r8.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0066, code lost:
        r3 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006b, code lost:
        if (r0.hasNext() == false) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006d, code lost:
        r2 = (java.lang.String) r0.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0079, code lost:
        if (r8.size() > 50) goto L_0x007c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007c, code lost:
        r3 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007d, code lost:
        r2 = r9.getBuddyByJid(r2, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0081, code lost:
        if (r2 == null) goto L_0x0066;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0083, code lost:
        r7.add(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0087, code lost:
        r1.addBuddies(r7, true);
        r1.group.setBuddyCount(r1.buddiesInGroup.size());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0097, code lost:
        if (r1.isExpanded == false) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0099, code lost:
        refresh(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009d, code lost:
        notifyDataSetChanged();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addBuddiesInBuddyGroup(java.lang.String r7, @androidx.annotation.Nullable java.util.Collection<java.lang.String> r8, boolean r9) {
        /*
            r6 = this;
            boolean r9 = android.text.TextUtils.isEmpty(r7)
            if (r9 != 0) goto L_0x00ad
            boolean r9 = p021us.zoom.androidlib.util.CollectionsUtil.isCollectionEmpty(r8)
            if (r9 == 0) goto L_0x000e
            goto L_0x00ad
        L_0x000e:
            com.zipow.videobox.ptapp.PTApp r9 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.ptapp.mm.ZoomMessenger r9 = r9.getZoomMessenger()
            if (r9 != 0) goto L_0x0019
            return
        L_0x0019:
            com.zipow.videobox.ptapp.mm.ZoomBuddyGroup r9 = r9.getBuddyGroupByXMPPId(r7)
            if (r9 != 0) goto L_0x0020
            return
        L_0x0020:
            com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance r9 = com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.getInsatance()
            java.util.List<com.zipow.videobox.view.IMDirectoryAdapter$ItemData> r0 = r6.mGroupDatas
            java.util.Iterator r0 = r0.iterator()
        L_0x002a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x00a0
            java.lang.Object r1 = r0.next()
            com.zipow.videobox.view.IMDirectoryAdapter$ItemData r1 = (com.zipow.videobox.view.IMDirectoryAdapter.ItemData) r1
            com.zipow.videobox.ptapp.mm.MMZoomBuddyGroup r2 = r1.group
            if (r2 == 0) goto L_0x002a
            com.zipow.videobox.ptapp.mm.MMZoomBuddyGroup r2 = r1.group
            java.lang.String r2 = r2.getXmppGroupID()
            boolean r2 = android.text.TextUtils.equals(r7, r2)
            if (r2 != 0) goto L_0x0052
            com.zipow.videobox.ptapp.mm.MMZoomBuddyGroup r2 = r1.group
            java.lang.String r2 = r2.getId()
            boolean r2 = android.text.TextUtils.equals(r7, r2)
            if (r2 == 0) goto L_0x002a
        L_0x0052:
            java.util.List<com.zipow.videobox.view.IMDirectoryAdapter$ItemData> r7 = r1.buddiesInGroup
            if (r7 != 0) goto L_0x005d
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            r1.buddiesInGroup = r7
        L_0x005d:
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
            java.util.Iterator r0 = r8.iterator()
        L_0x0066:
            boolean r2 = r0.hasNext()
            r3 = 1
            if (r2 == 0) goto L_0x0087
            java.lang.Object r2 = r0.next()
            java.lang.String r2 = (java.lang.String) r2
            int r4 = r8.size()
            r5 = 50
            if (r4 > r5) goto L_0x007c
            goto L_0x007d
        L_0x007c:
            r3 = 0
        L_0x007d:
            com.zipow.videobox.view.IMAddrBookItem r2 = r9.getBuddyByJid(r2, r3)
            if (r2 == 0) goto L_0x0066
            r7.add(r2)
            goto L_0x0066
        L_0x0087:
            r1.addBuddies(r7, r3)
            com.zipow.videobox.ptapp.mm.MMZoomBuddyGroup r7 = r1.group
            java.util.List<com.zipow.videobox.view.IMDirectoryAdapter$ItemData> r8 = r1.buddiesInGroup
            int r8 = r8.size()
            r7.setBuddyCount(r8)
            boolean r7 = r1.isExpanded
            if (r7 == 0) goto L_0x009d
            r6.refresh(r3)
            goto L_0x00a0
        L_0x009d:
            r6.notifyDataSetChanged()
        L_0x00a0:
            android.os.Handler r7 = r6.mHandler
            com.zipow.videobox.view.IMDirectoryAdapter$3 r8 = new com.zipow.videobox.view.IMDirectoryAdapter$3
            r8.<init>()
            r0 = 1500(0x5dc, double:7.41E-321)
            r7.postDelayed(r8, r0)
            return
        L_0x00ad:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.IMDirectoryAdapter.addBuddiesInBuddyGroup(java.lang.String, java.util.Collection, boolean):void");
    }

    public void updateBuddiesInBuddyGroup(@Nullable MMZoomBuddyGroup mMZoomBuddyGroup, @Nullable Collection<IMAddrBookItem> collection, boolean z) {
        if (mMZoomBuddyGroup != null && collection != null) {
            if (!this.mIsGroupOn || this.mIsKeepCompanyContact || !mMZoomBuddyGroup.isInCompanyContacts()) {
                if (this.mIsGroupOn || !mMZoomBuddyGroup.isInCompanyContacts()) {
                    int type = mMZoomBuddyGroup.getType();
                    if (type != 4) {
                        if (type == 10) {
                            this.mPhoneAddressContacts.addBuddies(collection);
                        } else if (type == 50) {
                            this.mAutoAnswerContacts.addBuddies(collection);
                        } else if (type != 61) {
                            if (type != 74) {
                                switch (type) {
                                    case 0:
                                        this.mExternalContacts.addBuddies(collection);
                                        break;
                                    case 1:
                                    case 2:
                                        if (this.mIsKeepCompanyContact) {
                                            this.mAllCompanyContacts.addBuddies(collection);
                                            this.mAllCompanyContacts.group.setBuddyCount(mMZoomBuddyGroup.getBuddyCount());
                                            break;
                                        }
                                        break;
                                    default:
                                        if (!mMZoomBuddyGroup.isZoomRoomGroup()) {
                                            boolean z2 = false;
                                            Iterator it = this.mGroupDatas.iterator();
                                            while (true) {
                                                if (it.hasNext()) {
                                                    ItemData itemData = (ItemData) it.next();
                                                    if (itemData.group != null && TextUtils.equals(mMZoomBuddyGroup.getXmppGroupID(), itemData.group.getXmppGroupID())) {
                                                        itemData.addBuddies(collection);
                                                        z2 = true;
                                                    }
                                                }
                                            }
                                            if (!z2) {
                                                ItemData itemData2 = new ItemData();
                                                itemData2.type = 1;
                                                itemData2.group = mMZoomBuddyGroup;
                                                itemData2.cateName = mMZoomBuddyGroup.getName();
                                                itemData2.addBuddies(collection);
                                                this.mGroupDatas.add(itemData2);
                                                break;
                                            }
                                        } else {
                                            this.mRoomContacts.addBuddies(collection);
                                            break;
                                        }
                                        break;
                                }
                            } else {
                                this.mTBD.addBuddies(collection);
                            }
                        }
                    }
                } else {
                    this.mAllCompanyContacts.addBuddies(collection);
                    this.mAllCompanyContacts.group.setBuddyCount(mMZoomBuddyGroup.getBuddyCount());
                }
                if (z) {
                    refresh();
                }
            }
        }
    }

    public void onPinnedSectionLongClick(int i) {
        if (getDataAtPosition(i) != null) {
            DirectoryHolder directoryHolder = null;
            RecyclerView recyclerView = this.mParentView;
            if (recyclerView != null) {
                directoryHolder = (DirectoryHolder) recyclerView.findViewHolderForAdapterPosition(i);
            }
            if (!(directoryHolder == null || directoryHolder.itemView == null)) {
                directoryHolder.itemView.performLongClick();
            }
        }
    }

    public void onPinnedSectionClick(int i) {
        Object dataAtPosition = getDataAtPosition(i);
        if (dataAtPosition != null) {
            DirectoryHolder directoryHolder = null;
            RecyclerView recyclerView = this.mParentView;
            if (recyclerView != null) {
                directoryHolder = (DirectoryHolder) recyclerView.findViewHolderForAdapterPosition(i);
            }
            onItemClick(dataAtPosition, directoryHolder);
        }
    }

    private void announceExpandOrCollapsed(@NonNull ItemData itemData) {
        String str;
        Context context = this.mContext;
        if (context != null && AccessibilityUtil.isSpokenFeedbackEnabled(context)) {
            MMZoomBuddyGroup mMZoomBuddyGroup = itemData.group;
            if (!(mMZoomBuddyGroup == null || this.mParentView == null)) {
                Resources resources = this.mContext.getResources();
                RecyclerView recyclerView = this.mParentView;
                if (itemData.isExpanded) {
                    str = resources.getString(C4558R.string.zm_accessibility_contacts_group_expanded_103023, new Object[]{StringUtil.safeString(mMZoomBuddyGroup.getName()), Integer.valueOf(itemData.getBuddySize())});
                } else {
                    str = resources.getString(C4558R.string.zm_accessibility_contacts_group_collapsed_103023, new Object[]{StringUtil.safeString(mMZoomBuddyGroup.getName()), Integer.valueOf(itemData.getBuddySize())});
                }
                AccessibilityUtil.announceForAccessibilityCompat((View) recyclerView, (CharSequence) str);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onItemClick(Object obj, DirectoryHolder directoryHolder) {
        if (obj instanceof InvitePhoneAddress) {
            EventBus.getDefault().post(new ZMContactsPhoneAddressEvent());
        } else if (obj instanceof ItemData) {
            ItemData itemData = (ItemData) obj;
            if (itemData.group != null) {
                int i = 0;
                while (i < this.mDisplayDatas.size() && this.mDisplayDatas.get(i) != itemData) {
                    i++;
                }
                if (i != this.mDisplayDatas.size()) {
                    itemData.isExpanded = !itemData.isExpanded;
                    RecyclerView recyclerView = this.mParentView;
                    if (recyclerView instanceof PinnedSectionRecyclerView) {
                        PinnedSectionRecyclerView pinnedSectionRecyclerView = (PinnedSectionRecyclerView) recyclerView;
                        int currentPinnedSection = pinnedSectionRecyclerView.getCurrentPinnedSection();
                        if (currentPinnedSection >= 0 && getDataAtPosition(currentPinnedSection) == obj) {
                            pinnedSectionRecyclerView.updatePinnedSection();
                        }
                    }
                    if (!CollectionsUtil.isCollectionEmpty(itemData.buddiesInGroup)) {
                        if (itemData.isExpanded) {
                            int size = itemData.buddiesInGroup.size();
                            itemData.sort();
                            int i2 = i + 1;
                            this.mDisplayDatas.addAll(i2, itemData.buddiesInGroup);
                            notifyItemRangeInserted(i2, size);
                        } else {
                            int i3 = i + 1;
                            if (i3 < this.mDisplayDatas.size()) {
                                int i4 = i3;
                                int i5 = 0;
                                while (i4 < this.mDisplayDatas.size() && (this.mDisplayDatas.get(i4) instanceof ItemData)) {
                                    ItemData itemData2 = (ItemData) this.mDisplayDatas.get(i4);
                                    if (itemData2.buddy == null && !(itemData2 instanceof InvitePhoneAddress)) {
                                        break;
                                    }
                                    i5++;
                                    if (i5 > 5000) {
                                        break;
                                    }
                                    i4++;
                                }
                                if (i5 > 5000) {
                                    refresh();
                                } else if (i5 > 0) {
                                    this.mDisplayDatas.subList(i3, i3 + i5).clear();
                                    notifyItemRangeRemoved(i3, i5);
                                }
                            }
                        }
                    }
                    announceExpandOrCollapsed(itemData);
                    notifyItemChanged(i);
                }
            }
        } else if (obj instanceof IMAddrBookItem) {
            EventBus.getDefault().post(new ZMContactsBuddyEvent((IMAddrBookItem) obj));
        }
    }

    public void refresh() {
        refresh(false);
    }

    public void refresh(boolean z) {
        if (z) {
            this.mDisplayDatas.clear();
            this.mDisplayDatas.addAll(sort());
            notifyDataSetChanged();
            Runnable runnable = this.mRreshTask;
            if (runnable != null) {
                this.mHandler.removeCallbacks(runnable);
                this.mRreshTask = null;
            }
        } else if (this.mRreshTask == null) {
            this.mRreshTask = new Runnable() {
                public void run() {
                    IMDirectoryAdapter.this.mDisplayDatas.clear();
                    IMDirectoryAdapter.this.mDisplayDatas.addAll(IMDirectoryAdapter.this.sort());
                    IMDirectoryAdapter.this.notifyDataSetChanged();
                    IMDirectoryAdapter.this.mRreshTask = null;
                }
            };
            this.mHandler.postDelayed(this.mRreshTask, 2000);
        }
    }

    @Nullable
    public Object getDataAtPosition(int i) {
        if (i < 0 || i >= this.mDisplayDatas.size()) {
            return null;
        }
        return this.mDisplayDatas.get(i);
    }

    @NonNull
    public DirectoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DirectoryHolder directoryHolder;
        switch (i) {
            case 1:
                directoryHolder = new GroupHolder(View.inflate(this.mContext, C4558R.layout.zm_directory_cate_expand_item, null), this.mContext.getResources().getString(C4558R.string.zm_accessibility_contacts_group_expanded_103023), this.mContext.getResources().getString(C4558R.string.zm_accessibility_contacts_group_collapsed_103023));
                break;
            case 2:
                directoryHolder = new BuddyHolder(new IMAddrBookItemView(this.mContext));
                break;
            case 3:
                directoryHolder = new InvitePhoneAddressHolder(View.inflate(this.mContext, C4558R.layout.zm_item_invite_phone_address, null));
                break;
            default:
                directoryHolder = new CategoryHolder(View.inflate(this.mContext, C4558R.layout.zm_directory_cate_item, null), this.mContext.getResources().getString(C4558R.string.zm_accessibility_contacts_category_head_103023));
                break;
        }
        directoryHolder.setClickListener(this.mOnItemClickListener);
        this.mViewHolderInCache.add(new WeakReference(directoryHolder));
        return directoryHolder;
    }

    public void onBindViewHolder(@NonNull DirectoryHolder directoryHolder, int i) {
        Object dataAtPosition = getDataAtPosition(i);
        if (dataAtPosition != null) {
            directoryHolder.update(dataAtPosition);
            if (dataAtPosition instanceof ItemData) {
                ItemData itemData = (ItemData) dataAtPosition;
                if (itemData.buddy != null) {
                    this.mShowedBuddies.add(itemData.buddy.getJid());
                }
            }
        }
    }

    public int getItemViewType(int i) {
        if (i < 0 || i >= this.mDisplayDatas.size()) {
            return 0;
        }
        Object obj = this.mDisplayDatas.get(i);
        if (obj == null) {
            return 0;
        }
        if (obj instanceof InvitePhoneAddress) {
            return 3;
        }
        if (obj instanceof ItemData) {
            return ((ItemData) obj).type;
        }
        return 0;
    }

    public int getItemCount() {
        return this.mDisplayDatas.size();
    }

    public long getItemId(int i) {
        if (this.mEnableStableIds) {
            Object dataAtPosition = getDataAtPosition(i);
            if (dataAtPosition == null) {
                return super.getItemId(i);
            }
            if (dataAtPosition instanceof ItemData) {
                return (long) ((ItemData) dataAtPosition).hashCode();
            }
        }
        return super.getItemId(i);
    }

    public void clearData(boolean z) {
        for (Object next : this.mDisplayDatas) {
            if (next instanceof ItemData) {
                ((ItemData) next).clear();
            }
        }
        this.mGroupDatas.clear();
        if (z) {
            this.mDisplayDatas.clear();
            notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    @NonNull
    public List<Object> sort() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return arrayList;
        }
        int personalGroupGetOption = zoomMessenger.personalGroupGetOption();
        arrayList.add(this.mMyGroupLabel);
        ArrayList<ItemData> arrayList2 = new ArrayList<>();
        ArrayList<ItemData> arrayList3 = new ArrayList<>();
        for (ItemData itemData : this.mGroupDatas) {
            MMZoomBuddyGroup mMZoomBuddyGroup = itemData.group;
            if (mMZoomBuddyGroup != null) {
                if (mMZoomBuddyGroup.getType() == 500) {
                    if (personalGroupGetOption == 1) {
                        arrayList2.add(itemData);
                    }
                } else if (mMZoomBuddyGroup.getType() != 61 && this.mIsGroupOn) {
                    arrayList3.add(itemData);
                }
            }
        }
        GroupComparatorByName groupComparatorByName = new GroupComparatorByName();
        Collections.sort(arrayList2, groupComparatorByName);
        Collections.sort(arrayList3, groupComparatorByName);
        addDataItem(this.mStarContacts, arrayList);
        if ((PTApp.getInstance().isPhoneNumberRegistered() || (CmmSIPCallManager.getInstance().isCloudPBXEnabled() && CmmSIPCallManager.getInstance().isPBXActive())) && !this.mPhoneAddressContacts.isBuddyEmpty()) {
            addDataItem(this.mPhoneAddressContacts, arrayList);
        }
        for (ItemData addDataItem : arrayList2) {
            addDataItem(addDataItem, arrayList);
        }
        addDataItem(this.mExternalContacts, arrayList);
        if (!this.mAutoAnswerContacts.isBuddyEmpty()) {
            addDataItem(this.mAutoAnswerContacts, arrayList);
        }
        ArrayList arrayList4 = new ArrayList();
        if (!this.mRoomContacts.isBuddyEmpty() || !this.mAllCompanyContacts.isBuddyEmpty() || !CollectionsUtil.isListEmpty(arrayList3) || !this.mRoomSystems.isBuddyEmpty() || !this.mTBD.isBuddyEmpty()) {
            arrayList4.add(this.mCompanyLabel);
            if (!this.mRoomContacts.isBuddyEmpty()) {
                addDataItem(this.mRoomContacts, arrayList4);
            }
            if (!this.mRoomSystems.isBuddyEmpty()) {
                addDataItem(this.mRoomSystems, arrayList4);
            }
            if (this.mIsGroupOn || this.mAllCompanyContacts.isBuddyEmpty()) {
                if (this.mIsKeepCompanyContact) {
                    addDataItem(this.mAllCompanyContacts, arrayList4);
                }
                for (ItemData addDataItem2 : arrayList3) {
                    addDataItem(addDataItem2, arrayList4);
                }
            } else {
                addDataItem(this.mAllCompanyContacts, arrayList4);
            }
            if (!this.mTBD.isBuddyEmpty()) {
                addDataItem(this.mTBD, arrayList4);
            }
        }
        if (arrayList4.size() > 0) {
            arrayList.addAll(arrayList4);
        }
        return arrayList;
    }

    private void addDataItem(@Nullable ItemData itemData, @Nullable List<Object> list) {
        if (itemData != null && list != null) {
            list.add(itemData);
            if (itemData.isExpanded && itemData.buddiesInGroup != null) {
                itemData.sort();
                list.addAll(itemData.buddiesInGroup);
            }
        }
    }

    public boolean isDataEmpty() {
        return CollectionsUtil.isCollectionEmpty(this.mGroupDatas);
    }

    public boolean isStarEmpty() {
        return this.mStarContacts.getBuddySize() == 0;
    }

    public boolean isPhoneAddrEmpty() {
        return this.mPhoneAddressContacts.getBuddySize() == 0;
    }

    public boolean isRoomSystemsEmpty() {
        return this.mRoomSystems.getBuddySize() == 0;
    }

    public boolean isPinnedSection(int i) {
        int itemViewType = getItemViewType(i);
        return itemViewType == 1 || itemViewType == 0;
    }
}
