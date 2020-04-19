package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettingItem;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMNotificationExceptionGroupSettingsListView */
public class MMNotificationExceptionGroupSettingsListView extends ListView {
    private MMNotificationExceptionGroupAdapter mAdapter;

    /* renamed from: com.zipow.videobox.view.mm.MMNotificationExceptionGroupSettingsListView$MMNotificationExceptionGroupAdapter */
    static class MMNotificationExceptionGroupAdapter extends BaseAdapter {
        private static final String TAG_GROUPS_ITEM = "item";
        private static final String TAG_GROUPS_LABEL = "label";
        private Context mContext;
        private int mDefaultNotificationType = 1;
        @NonNull
        private List<Object> mDisplayData = new ArrayList();
        @NonNull
        private List<MMZoomGroup> mExceptionGroups = new ArrayList();
        private String mKey;
        @NonNull
        private List<MMZoomGroup> mNormalGroups = new ArrayList();
        @Nullable
        private Map<String, Integer> mySettings;

        public long getItemId(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public MMNotificationExceptionGroupAdapter(Context context) {
            this.mContext = context;
        }

        public void setDefaultNotificationType(int i) {
            this.mDefaultNotificationType = i;
        }

        public void setNormalGroups(@Nullable List<MMZoomGroup> list) {
            if (list != null) {
                this.mNormalGroups.clear();
                for (MMZoomGroup add : list) {
                    this.mNormalGroups.add(add);
                }
                Collections.sort(this.mNormalGroups, new MMZoomGroupComparator(CompatUtils.getLocalDefault()));
                removeExceptionGroupInNormal();
            }
        }

        public void cleanData() {
            this.mNormalGroups.clear();
            this.mExceptionGroups.clear();
            this.mDisplayData.clear();
            this.mySettings = null;
        }

        public void setSettings(Map<String, Integer> map) {
            this.mySettings = map;
        }

        public void updateGroup(String str) {
            boolean z;
            if (!StringUtil.isEmptyOrNull(str)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomGroup groupById = zoomMessenger.getGroupById(str);
                    if (groupById != null) {
                        int i = 0;
                        while (true) {
                            if (i >= this.mExceptionGroups.size()) {
                                z = false;
                                break;
                            } else if (StringUtil.isSameString(str, ((MMZoomGroup) this.mExceptionGroups.get(i)).getGroupId())) {
                                this.mExceptionGroups.set(i, MMZoomGroup.initWithZoomGroup(groupById));
                                z = true;
                                break;
                            } else {
                                i++;
                            }
                        }
                        if (!z) {
                            boolean z2 = false;
                            for (int i2 = 0; i2 < this.mNormalGroups.size(); i2++) {
                                if (StringUtil.isSameString(str, ((MMZoomGroup) this.mNormalGroups.get(i2)).getGroupId())) {
                                    this.mNormalGroups.set(i2, MMZoomGroup.initWithZoomGroup(groupById));
                                    z2 = true;
                                }
                            }
                            if (!z2) {
                                this.mNormalGroups.add(MMZoomGroup.initWithZoomGroup(groupById));
                            }
                        }
                    }
                }
            }
        }

        public void removeGroup(String str) {
            if (!StringUtil.isEmptyOrNull(str)) {
                int i = 0;
                int i2 = 0;
                while (true) {
                    if (i2 >= this.mNormalGroups.size()) {
                        break;
                    } else if (StringUtil.isSameString(str, ((MMZoomGroup) this.mNormalGroups.get(i2)).getGroupId())) {
                        this.mNormalGroups.remove(i2);
                        break;
                    } else {
                        i2++;
                    }
                }
                while (true) {
                    if (i >= this.mExceptionGroups.size()) {
                        break;
                    } else if (StringUtil.isSameString(str, ((MMZoomGroup) this.mExceptionGroups.get(i)).getGroupId())) {
                        this.mExceptionGroups.remove(i);
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }

        public void setFilter(String str) {
            this.mKey = str;
        }

        public void setExceptionGroups(@Nullable List<MMZoomGroup> list) {
            if (list != null) {
                this.mExceptionGroups.clear();
                for (MMZoomGroup add : list) {
                    this.mExceptionGroups.add(add);
                }
                Collections.sort(this.mExceptionGroups, new MMZoomGroupComparator(CompatUtils.getLocalDefault()));
                removeExceptionGroupInNormal();
            }
        }

        private void removeExceptionGroupInNormal() {
            if (!CollectionsUtil.isListEmpty(this.mExceptionGroups) && !CollectionsUtil.isListEmpty(this.mNormalGroups)) {
                for (MMZoomGroup mMZoomGroup : this.mExceptionGroups) {
                    int i = 0;
                    while (true) {
                        if (i >= this.mNormalGroups.size()) {
                            break;
                        } else if (StringUtil.isSameString(((MMZoomGroup) this.mNormalGroups.get(i)).getGroupId(), mMZoomGroup.getGroupId())) {
                            this.mNormalGroups.remove(i);
                            break;
                        } else {
                            i++;
                        }
                    }
                }
            }
        }

        public void notifyDataSetChanged() {
            sortAll();
            super.notifyDataSetChanged();
        }

        public int getItemViewType(int i) {
            return getItem(i) instanceof ZoomGroup ? 0 : 1;
        }

        public int getCount() {
            return this.mDisplayData.size();
        }

        @Nullable
        public Object getItem(int i) {
            return this.mDisplayData.get(i);
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (item instanceof MMZoomGroup) {
                return getGroupItemView((MMZoomGroup) item, view, viewGroup);
            }
            return getLabelItemView((String) item, view, viewGroup);
        }

        @NonNull
        private View getLabelItemView(String str, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !TAG_GROUPS_LABEL.equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_listview_label_item, null);
                view.setTag(TAG_GROUPS_LABEL);
            }
            ((TextView) view.findViewById(C4558R.C4560id.txtHeaderLabel)).setText(str);
            return view;
        }

        @NonNull
        private View getGroupItemView(@NonNull MMZoomGroup mMZoomGroup, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"item".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_contacts_group_item, null);
                view.setTag("item");
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtGroupName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtMemberNo);
            TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.txtGroupdes);
            CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(C4558R.C4560id.check);
            ((AvatarView) view.findViewById(C4558R.C4560id.avatarView)).show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_avatar_group, null));
            textView.setText(mMZoomGroup.getGroupName());
            textView2.setText(String.format("(%s)", new Object[]{Integer.valueOf(mMZoomGroup.getMemberCount())}));
            checkedTextView.setVisibility(8);
            int notifyType = mMZoomGroup.getNotifyType();
            Map<String, Integer> map = this.mySettings;
            if (map != null) {
                Integer num = (Integer) map.get(mMZoomGroup.getGroupId());
                if (num != null) {
                    notifyType = num.intValue();
                }
            }
            switch (notifyType) {
                case 1:
                    textView3.setText(C4558R.string.zm_lbl_notification_all_msg_19898);
                    break;
                case 2:
                    textView3.setText(C4558R.string.zm_lbl_notification_private_msg_19898);
                    break;
                case 3:
                    textView3.setText(C4558R.string.zm_lbl_notification_nothing_19898);
                    break;
                default:
                    switch (this.mDefaultNotificationType) {
                        case 1:
                            textView3.setText(C4558R.string.zm_lbl_notification_all_msg_19898);
                            break;
                        case 2:
                            textView3.setText(C4558R.string.zm_lbl_notification_private_msg_19898);
                            break;
                        case 3:
                            textView3.setText(C4558R.string.zm_lbl_notification_nothing_19898);
                            break;
                        default:
                            textView3.setText("");
                            break;
                    }
            }
            return view;
        }

        private void sortAll() {
            this.mDisplayData.clear();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            Iterator it = this.mExceptionGroups.iterator();
            while (true) {
                Integer num = null;
                if (!it.hasNext()) {
                    break;
                }
                MMZoomGroup mMZoomGroup = (MMZoomGroup) it.next();
                if (!StringUtil.isEmptyOrNull(mMZoomGroup.getGroupName()) && (StringUtil.isEmptyOrNull(this.mKey) || mMZoomGroup.getGroupName().contains(this.mKey))) {
                    Map<String, Integer> map = this.mySettings;
                    if (map != null) {
                        num = (Integer) map.get(mMZoomGroup.getGroupId());
                    }
                    if (num == null || !(num.intValue() == this.mDefaultNotificationType || num.intValue() == 0)) {
                        arrayList.add(mMZoomGroup);
                    } else {
                        arrayList2.add(mMZoomGroup);
                    }
                }
            }
            for (MMZoomGroup mMZoomGroup2 : this.mNormalGroups) {
                if (!StringUtil.isEmptyOrNull(mMZoomGroup2.getGroupName()) && (StringUtil.isEmptyOrNull(this.mKey) || mMZoomGroup2.getGroupName().contains(this.mKey))) {
                    Map<String, Integer> map2 = this.mySettings;
                    Integer num2 = map2 == null ? null : (Integer) map2.get(mMZoomGroup2.getGroupId());
                    if (num2 == null || num2.intValue() == this.mDefaultNotificationType) {
                        arrayList2.add(mMZoomGroup2);
                    } else {
                        arrayList.add(mMZoomGroup2);
                    }
                }
            }
            MMZoomGroupComparator mMZoomGroupComparator = new MMZoomGroupComparator(CompatUtils.getLocalDefault());
            Collections.sort(arrayList, mMZoomGroupComparator);
            Collections.sort(arrayList2, mMZoomGroupComparator);
            if (!CollectionsUtil.isListEmpty(arrayList)) {
                List<Object> list = this.mDisplayData;
                StringBuilder sb = new StringBuilder();
                sb.append(this.mContext.getString(C4558R.string.zm_title_notification_exception_group_59554));
                sb.append(String.format("(%d)", new Object[]{Integer.valueOf(arrayList.size())}));
                list.add(sb.toString());
                this.mDisplayData.addAll(arrayList);
            }
            if (!CollectionsUtil.isListEmpty(arrayList2)) {
                this.mDisplayData.add(this.mContext.getString(C4558R.string.zm_lbl_group_59554, new Object[]{Integer.valueOf(arrayList2.size())}));
                this.mDisplayData.addAll(arrayList2);
            }
        }
    }

    public MMNotificationExceptionGroupSettingsListView(Context context) {
        super(context);
        init();
    }

    public MMNotificationExceptionGroupSettingsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMNotificationExceptionGroupSettingsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mAdapter = new MMNotificationExceptionGroupAdapter(getContext());
        setAdapter(this.mAdapter);
        updateDefaultNotificationSettings();
    }

    private void updateDefaultNotificationSettings() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
            if (blockAllSettings != null) {
                int i = blockAllSettings[0];
                int i2 = blockAllSettings[1];
                if (i == 1 && i2 == 1) {
                    this.mAdapter.setDefaultNotificationType(1);
                } else if (i == 2) {
                    this.mAdapter.setDefaultNotificationType(3);
                } else if (i == 1 && i2 == 4) {
                    this.mAdapter.setDefaultNotificationType(2);
                }
            }
        }
    }

    public void updateData(Map<String, Integer> map) {
        this.mAdapter.cleanData();
        this.mAdapter.setNormalGroups(loadAllGroups());
        updateDefaultNotificationSettings();
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            MUCNotifySettings mUCDiffFromGeneralSetting = notificationSettingMgr.getMUCDiffFromGeneralSetting();
            if (mUCDiffFromGeneralSetting != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ArrayList arrayList = new ArrayList();
                    for (MUCNotifySettingItem mUCNotifySettingItem : mUCDiffFromGeneralSetting.getItemsList()) {
                        ZoomGroup groupById = zoomMessenger.getGroupById(mUCNotifySettingItem.getSessionId());
                        if (groupById != null) {
                            MMZoomGroup initWithZoomGroup = MMZoomGroup.initWithZoomGroup(groupById);
                            initWithZoomGroup.setNotifyType(mUCNotifySettingItem.getType());
                            arrayList.add(initWithZoomGroup);
                        }
                    }
                    this.mAdapter.setExceptionGroups(arrayList);
                } else {
                    return;
                }
            }
            this.mAdapter.setSettings(map);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    private List<MMZoomGroup> loadAllGroups() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < zoomMessenger.getGroupCount(); i++) {
            ZoomGroup groupAt = zoomMessenger.getGroupAt(i);
            if (groupAt != null) {
                arrayList.add(MMZoomGroup.initWithZoomGroup(groupAt));
            }
        }
        return arrayList;
    }

    public void setFilter(String str) {
        this.mAdapter.setFilter(str);
        this.mAdapter.notifyDataSetChanged();
    }

    public void updateGroup(String str) {
        this.mAdapter.updateGroup(str);
        this.mAdapter.notifyDataSetChanged();
    }

    public void removeGroup(String str) {
        this.mAdapter.removeGroup(str);
        this.mAdapter.notifyDataSetChanged();
    }

    @Nullable
    public MMZoomGroup getItem(int i) {
        Object item = this.mAdapter.getItem(i - getHeaderViewsCount());
        if (item instanceof MMZoomGroup) {
            return (MMZoomGroup) item;
        }
        return null;
    }
}
