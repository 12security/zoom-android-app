package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.QuickSearchListView.QuickSearchListDataAdapter;
import p021us.zoom.videomeetings.C4558R;

public class SelectCustomGroupFragment extends ZMDialogFragment implements OnClickListener, OnItemClickListener {
    private static final String EXTRA_BUDDY_JID = "EXTRA_BUDDY_JID";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String RESULT_GROUP = "RESULT_GROUP";
    private GroupAdapter mAdapter;
    @Nullable
    private String mBuddyJid;
    private QuickSearchListView mListCustomGroups;
    private TextView mTxtTitle;
    @NonNull
    private IZoomMessengerUIListener messengerUIListener = new SimpleZoomMessengerUIListener() {
        public void OnPersonalGroupResponse(byte[] bArr) {
            SelectCustomGroupFragment.this.loadAllCustomGroups();
        }

        public void NotifyPersonalGroupSync(int i, String str, List<String> list, String str2, String str3) {
            SelectCustomGroupFragment.this.loadAllCustomGroups();
        }
    };

    static class GroupAdapter extends QuickSearchListDataAdapter {
        private Context context;
        private List<GroupItem> groups;

        public long getItemId(int i) {
            return 0;
        }

        GroupAdapter(Context context2) {
            this.context = context2;
        }

        public void updateGroups(List<GroupItem> list) {
            this.groups = list;
            notifyDataSetChanged();
        }

        @Nullable
        public String getItemSortKey(Object obj) {
            if (obj instanceof GroupItem) {
                return ((GroupItem) obj).group.getSortKey();
            }
            return null;
        }

        public int getCount() {
            List<GroupItem> list = this.groups;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Nullable
        public GroupItem getItem(int i) {
            List<GroupItem> list = this.groups;
            if (list != null && i >= 0 && i < list.size()) {
                return (GroupItem) this.groups.get(i);
            }
            return null;
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(this.context, C4558R.layout.zm_select_custom_group_item, null);
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.groupName);
            TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtAdded);
            GroupItem item = getItem(i);
            if (item == null || item.group == null) {
                textView.setText("");
                textView2.setVisibility(8);
                return view;
            }
            textView.setText(item.group.getName());
            if (item.added) {
                textView2.setVisibility(0);
                textView.setTextColor(this.context.getResources().getColor(C4558R.color.zm_ui_kit_color_gray_747487));
            } else {
                textView.setTextColor(this.context.getResources().getColor(C4558R.color.zm_ui_kit_color_black_232333));
                textView2.setVisibility(8);
            }
            return view;
        }
    }

    static class GroupItem {
        boolean added;
        MMZoomBuddyGroup group;

        GroupItem(MMZoomBuddyGroup mMZoomBuddyGroup) {
            this.group = mMZoomBuddyGroup;
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment, String str, @Nullable Bundle bundle, int i, String str2) {
        if (fragment != null && !TextUtils.isEmpty(str)) {
            Bundle bundle2 = new Bundle();
            if (bundle != null) {
                bundle2.putBundle(EXTRA_DATA, bundle);
            }
            bundle2.putString(EXTRA_TITLE, str);
            bundle2.putString(EXTRA_BUDDY_JID, str2);
            SimpleActivity.show(fragment, SelectCustomGroupFragment.class.getName(), bundle2, i, true, 1);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_select_custom_group, viewGroup, false);
        this.mListCustomGroups = (QuickSearchListView) inflate.findViewById(C4558R.C4560id.listCustomGroups);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        this.mAdapter = new GroupAdapter(getContext());
        this.mListCustomGroups.setAdapter(this.mAdapter);
        this.mListCustomGroups.getListView().setEmptyView(inflate.findViewById(C4558R.C4560id.emptyView));
        this.mListCustomGroups.setOnItemClickListener(this);
        return inflate;
    }

    /* access modifiers changed from: private */
    public void loadAllCustomGroups() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < zoomMessenger.getBuddyGroupCount(); i++) {
                ZoomBuddyGroup buddyGroupAt = zoomMessenger.getBuddyGroupAt(i);
                if (buddyGroupAt != null && buddyGroupAt.getGroupType() == 500) {
                    GroupItem groupItem = new GroupItem(MMZoomBuddyGroup.fromZoomBuddyGroup(buddyGroupAt));
                    String str = this.mBuddyJid;
                    if (str != null && buddyGroupAt.containsBuddy(str)) {
                        groupItem.added = true;
                    }
                    arrayList.add(groupItem);
                }
            }
            this.mAdapter.updateGroups(arrayList);
        }
    }

    public void onStart() {
        super.onStart();
        loadAllCustomGroups();
        ZoomMessengerUI.getInstance().addListener(this.messengerUIListener);
    }

    public void onStop() {
        ZoomMessengerUI.getInstance().removeListener(this.messengerUIListener);
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        this.mListCustomGroups.onResume();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mTxtTitle.setText(arguments.getString(EXTRA_TITLE));
            this.mBuddyJid = arguments.getString(EXTRA_BUDDY_JID);
        }
    }

    private void selectDone(@Nullable MMZoomBuddyGroup mMZoomBuddyGroup) {
        Intent intent;
        if (mMZoomBuddyGroup != null) {
            intent = new Intent();
            Bundle arguments = getArguments();
            if (arguments != null) {
                Bundle bundle = arguments.getBundle(EXTRA_DATA);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
            }
            intent.putExtra(RESULT_GROUP, mMZoomBuddyGroup);
        } else {
            intent = null;
        }
        finishFragment(intent == null ? 0 : -1, intent);
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnCancel) {
            finishFragment(true);
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = this.mListCustomGroups.getItemAtPosition(i);
        if (itemAtPosition instanceof GroupItem) {
            GroupItem groupItem = (GroupItem) itemAtPosition;
            if (!groupItem.added) {
                selectDone(groupItem.group);
            }
        }
    }
}
