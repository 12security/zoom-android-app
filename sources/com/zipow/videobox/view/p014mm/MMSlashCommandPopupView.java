package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.zipow.videobox.ptapp.IMProtos.RobotCommand;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSlashCommandPopupView */
public class MMSlashCommandPopupView extends PopupWindow {
    private static final String TAG = "MMSlashCommandPopupView";
    public static final int TYPE_LAST_SLASH = 1;
    public static final int TYPE_NORMAL_SLASH = 0;
    private SlashCommandAdapter commandAdapter;
    private int delta;
    /* access modifiers changed from: private */
    @NonNull
    public List<SlashItem> filterList = new ArrayList();
    @NonNull
    private List<SlashItem> list = new ArrayList();
    /* access modifiers changed from: private */
    public Context mContext;
    private String mFilter;
    /* access modifiers changed from: private */
    public ListView mListView;
    private int mMaxHeight;
    private View mParent;
    private String mSessionID;
    private int measurePopupHeight;
    private int minHeight;
    /* access modifiers changed from: private */
    public OnSlashCommandClickListener onSlashCommandClickListener;
    private View parentView;

    /* renamed from: com.zipow.videobox.view.mm.MMSlashCommandPopupView$OnSlashCommandClickListener */
    public interface OnSlashCommandClickListener {
        void onSlashCommandClick(SlashItem slashItem);
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSlashCommandPopupView$SlashCommandAdapter */
    private class SlashCommandAdapter extends BaseAdapter {
        private Context context;
        private List<SlashItem> mList = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public SlashCommandAdapter(Context context2, List<SlashItem> list) {
            this.mList = list;
            this.context = context2;
        }

        public int getCount() {
            return this.mList.size();
        }

        public Object getItem(int i) {
            return this.mList.get(i);
        }

        private void displayAvatar(@Nullable AvatarView avatarView, String str) {
            if (avatarView != null && !TextUtils.isEmpty(str)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomMessenger.getBuddyWithJID(str));
                    if (fromZoomBuddy != null) {
                        avatarView.show(fromZoomBuddy.getAvatarParamsBuilder());
                    }
                }
            }
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            int itemViewType = getItemViewType(i);
            if (view == null) {
                if (itemViewType == 0) {
                    view = LayoutInflater.from(this.context).inflate(C4558R.layout.zm_mm_slash_command_item, viewGroup, false);
                } else {
                    view = LayoutInflater.from(this.context).inflate(C4558R.layout.zm_mm_slash_command_last_item, viewGroup, false);
                }
            }
            SlashItem slashItem = (SlashItem) getItem(i);
            if (itemViewType == 0) {
                View findViewById = view.findViewById(C4558R.C4560id.slash_command_item_top_blank);
                AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.slash_command_item_owner_avatar);
                LinearLayout linearLayout = (LinearLayout) view.findViewById(C4558R.C4560id.slash_command_item_owner_linear);
                TextView textView = (TextView) view.findViewById(C4558R.C4560id.slash_command_item_owner);
                TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.slash_command_item_command_profix);
                TextView textView3 = (TextView) view.findViewById(C4558R.C4560id.slash_command_item_command);
                TextView textView4 = (TextView) view.findViewById(C4558R.C4560id.slash_command_item_dec);
                avatarView.setBorderColor(ContextCompat.getColor(MMSlashCommandPopupView.this.mContext, C4558R.color.zm_mm_slash_popup_avatar_border_color));
                avatarView.setBorderSize(UIUtil.dip2px(MMSlashCommandPopupView.this.mContext, 0.5f));
                if (i == 0) {
                    findViewById.setVisibility(8);
                    linearLayout.setVisibility(0);
                    textView.setText(slashItem.getOwner());
                    displayAvatar(avatarView, slashItem.getJid());
                } else if (i > 0) {
                    if (slashItem.getOwner().equals(((SlashItem) getItem(i - 1)).getOwner())) {
                        linearLayout.setVisibility(8);
                        findViewById.setVisibility(8);
                    } else {
                        linearLayout.setVisibility(0);
                        textView.setText(slashItem.getOwner());
                        findViewById.setVisibility(0);
                        displayAvatar(avatarView, slashItem.getJid());
                    }
                } else {
                    findViewById.setVisibility(8);
                    linearLayout.setVisibility(8);
                }
                textView2.setText(slashItem.getProfix());
                if (slashItem.getCommand() != null) {
                    textView3.setText(slashItem.getCommand().getCommand());
                    if (TextUtils.isEmpty(slashItem.getCommand().getShortDescription())) {
                        textView4.setVisibility(8);
                    } else {
                        textView4.setVisibility(0);
                        textView4.setText(slashItem.getCommand().getShortDescription());
                    }
                }
            } else {
                TextView textView5 = (TextView) view.findViewById(C4558R.C4560id.slash_command_item_last_used_profix);
                TextView textView6 = (TextView) view.findViewById(C4558R.C4560id.slash_command_item_last_used);
                String profix = slashItem.getProfix();
                textView5.setText(profix);
                String command = slashItem.getCommand().getCommand();
                if (TextUtils.equals(profix.trim(), command.trim())) {
                    command = "";
                } else if (command.startsWith(profix)) {
                    command = command.replace(profix, "");
                }
                textView6.setText(command);
            }
            return view;
        }

        public int getItemViewType(int i) {
            return ((SlashItem) this.mList.get(i)).type;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSlashCommandPopupView$SlashCommandItemComparator */
    public class SlashCommandItemComparator implements Comparator<SlashItem> {
        private Collator mCollator;

        public SlashCommandItemComparator(Locale locale) {
            this.mCollator = Collator.getInstance(locale);
            this.mCollator.setStrength(0);
        }

        public int compare(@NonNull SlashItem slashItem, @NonNull SlashItem slashItem2) {
            if (slashItem == slashItem2) {
                return 0;
            }
            return this.mCollator.compare(getItemSortKey(slashItem), getItemSortKey(slashItem2));
        }

        @NonNull
        private String getItemSortKey(SlashItem slashItem) {
            String owner = slashItem.getOwner();
            return owner == null ? "" : owner;
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSlashCommandPopupView$SlashItem */
    public class SlashItem {
        private RobotCommand command;
        private String jid;
        @Nullable
        private String owner;
        private String profix;
        /* access modifiers changed from: private */
        public int type;

        public SlashItem(MMSlashCommandPopupView mMSlashCommandPopupView, IMAddrBookItem iMAddrBookItem, RobotCommand robotCommand) {
            this(iMAddrBookItem, robotCommand, 0);
        }

        public SlashItem(@Nullable IMAddrBookItem iMAddrBookItem, RobotCommand robotCommand, int i) {
            this.owner = "";
            this.jid = "";
            this.type = 0;
            this.command = robotCommand;
            this.type = i;
            if (iMAddrBookItem != null) {
                this.owner = iMAddrBookItem.getScreenName();
                this.jid = iMAddrBookItem.getJid();
                this.profix = iMAddrBookItem.getRobotCmdPrefix();
            }
        }

        public String getJid() {
            return this.jid;
        }

        public void setJid(String str) {
            this.jid = str;
        }

        @Nullable
        public String getOwner() {
            return this.owner;
        }

        public void setOwner(@Nullable String str) {
            this.owner = str;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int i) {
            this.type = i;
        }

        public RobotCommand getCommand() {
            return this.command;
        }

        public void setCommand(RobotCommand robotCommand) {
            this.command = robotCommand;
        }

        public String getProfix() {
            return this.profix;
        }

        public void setProfix(String str) {
            this.profix = str;
        }
    }

    public void setOnSlashCommandClickListener(OnSlashCommandClickListener onSlashCommandClickListener2) {
        this.onSlashCommandClickListener = onSlashCommandClickListener2;
    }

    public void setmFilter(@NonNull String str) {
        this.mFilter = str;
        filterListByString(str);
        if (this.filterList.isEmpty()) {
            dismiss();
            return;
        }
        SlashCommandAdapter slashCommandAdapter = this.commandAdapter;
        if (slashCommandAdapter != null) {
            slashCommandAdapter.notifyDataSetChanged();
            measurePopupHeight();
            int i = this.measurePopupHeight;
            int i2 = this.mMaxHeight;
            if (i > i2) {
                setHeight(i2);
            } else {
                int i3 = this.minHeight;
                if (i < i3) {
                    setHeight(i3);
                } else {
                    setHeight(i);
                }
            }
            show();
            this.mListView.setSelection(this.commandAdapter.getCount() - 1);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
                getContentView().post(new Runnable() {
                    public void run() {
                        AccessibilityUtil.announceForAccessibilityCompat(MMSlashCommandPopupView.this.getContentView(), (CharSequence) MMSlashCommandPopupView.this.mContext.getString(C4558R.string.zm_accessibility_slash_cmd_77835, new Object[]{Integer.valueOf(MMSlashCommandPopupView.this.mListView.getChildCount()), Integer.valueOf(MMSlashCommandPopupView.this.filterList.size())}));
                    }
                });
            }
        }
    }

    public MMSlashCommandPopupView(@NonNull Context context, View view, String str) {
        this.mContext = context;
        this.mParent = view;
        this.mSessionID = str;
        this.parentView = View.inflate(context, C4558R.layout.zm_mm_slash_command_popup, null);
        this.mListView = (ListView) this.parentView.findViewById(C4558R.C4560id.slash_command_listView);
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        this.delta = UIUtil.dip2px(this.mContext, 20.0f);
        this.mMaxHeight = (rect.top - UIUtil.getStatusBarHeight(context)) + this.delta;
        this.minHeight = UIUtil.dip2px(this.mContext, 100.0f);
        setContentView(this.parentView);
        setWidth(-1);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(false);
        setAnimationStyle(C4558R.style.zm_popwindow_anim_style);
        this.list = loadAndSortData();
        if (!this.list.isEmpty()) {
            this.filterList.addAll(this.list);
            this.commandAdapter = new SlashCommandAdapter(context, this.filterList);
            this.mListView.setAdapter(this.commandAdapter);
            this.mListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (MMSlashCommandPopupView.this.onSlashCommandClickListener != null) {
                        MMSlashCommandPopupView.this.onSlashCommandClickListener.onSlashCommandClick((SlashItem) MMSlashCommandPopupView.this.filterList.get(i));
                        MMSlashCommandPopupView.this.dismiss();
                    }
                }
            });
            this.mListView.setSelection(this.commandAdapter.getCount() - 1);
        }
        measurePopupHeight();
        int i = this.measurePopupHeight;
        int i2 = this.mMaxHeight;
        if (i > i2) {
            setHeight(i2);
            return;
        }
        int i3 = this.minHeight;
        if (i < i3) {
            setHeight(i3);
        } else {
            setHeight(i);
        }
    }

    private void measurePopupHeight() {
        SlashCommandAdapter slashCommandAdapter = this.commandAdapter;
        if (slashCommandAdapter != null && this.mListView != null) {
            this.measurePopupHeight = 0;
            int count = slashCommandAdapter.getCount();
            for (int i = 0; i < count; i++) {
                View view = this.commandAdapter.getView(i, null, null);
                if (view != null) {
                    view.measure(0, 0);
                    this.measurePopupHeight += view.getMeasuredHeight();
                }
            }
            this.measurePopupHeight += this.delta * 2;
        }
    }

    @NonNull
    private List<SlashItem> filterListByString(@Nullable String str) {
        if (TextUtils.isEmpty(str)) {
            this.filterList.clear();
        } else {
            this.filterList.clear();
            if (!this.list.isEmpty()) {
                for (SlashItem slashItem : this.list) {
                    String profix = slashItem.getProfix();
                    if (!TextUtils.isEmpty(profix)) {
                        if (str.length() > profix.length()) {
                            if (str.toLowerCase().startsWith(profix.toLowerCase())) {
                                String trim = str.substring(profix.length()).trim();
                                if (TextUtils.isEmpty(trim)) {
                                    this.filterList.add(slashItem);
                                } else {
                                    RobotCommand command = slashItem.getCommand();
                                    if (command != null) {
                                        String command2 = command.getCommand();
                                        if (TextUtils.equals(profix.trim(), command2.trim())) {
                                            command2 = "";
                                        } else if (command2.startsWith(profix)) {
                                            command2 = command2.replace(profix, "").trim();
                                        }
                                        if (!TextUtils.isEmpty(command2) && command2.toLowerCase().startsWith(trim.toLowerCase())) {
                                            this.filterList.add(slashItem);
                                        }
                                    }
                                }
                            }
                        } else if (profix.toLowerCase().startsWith(str.toLowerCase())) {
                            this.filterList.add(slashItem);
                        }
                    }
                }
            }
        }
        return this.filterList;
    }

    @NonNull
    private List<SlashItem> loadAndSortData() {
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return arrayList;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionID);
        if (sessionById == null) {
            return arrayList;
        }
        List arrayList2 = new ArrayList();
        if (sessionById.isGroup()) {
            arrayList2 = zoomMessenger.getAllRobotBuddies(this.mSessionID);
        } else {
            ZoomBuddyGroup buddyGroupByType = zoomMessenger.getBuddyGroupByType(61);
            if (buddyGroupByType != null) {
                int buddyCount = buddyGroupByType.getBuddyCount();
                for (int i = 0; i < buddyCount; i++) {
                    ZoomBuddy buddyAt = buddyGroupByType.getBuddyAt(i);
                    if (buddyAt != null) {
                        String jid = buddyAt.getJid();
                        if (jid != null) {
                            arrayList2.add(jid);
                        }
                    }
                }
            }
        }
        if (arrayList2 != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself == null) {
                return arrayList;
            }
            String jid2 = myself.getJid();
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID((String) arrayList2.get(i2));
                if (buddyWithJID != null && !StringUtil.isSameString(buddyWithJID.getJid(), jid2) && !buddyWithJID.isZoomRoom() && buddyWithJID.isRobot()) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (fromZoomBuddy != null) {
                        List<RobotCommand> robotCommands = fromZoomBuddy.getRobotCommands();
                        if (!CollectionsUtil.isListEmpty(robotCommands)) {
                            for (RobotCommand slashItem : robotCommands) {
                                arrayList.add(new SlashItem(this, fromZoomBuddy, slashItem));
                            }
                        }
                    }
                }
            }
        }
        if (arrayList.size() > 1) {
            Collections.sort(arrayList, new SlashCommandItemComparator(CompatUtils.getLocalDefault()));
        }
        RobotCommand lastUsedRobotCommand = zoomMessenger.getLastUsedRobotCommand();
        if (lastUsedRobotCommand != null && !TextUtils.isEmpty(lastUsedRobotCommand.getCommand())) {
            String jid3 = lastUsedRobotCommand.getJid();
            if (!TextUtils.isEmpty(jid3)) {
                ZoomBuddy buddyWithJID2 = zoomMessenger.getBuddyWithJID(jid3);
                if (buddyWithJID2 != null) {
                    arrayList.add(new SlashItem(IMAddrBookItem.fromZoomBuddy(buddyWithJID2), lastUsedRobotCommand, 1));
                }
            }
        }
        return arrayList;
    }

    public void updateWindowSize() {
        if (this.mParent != null && this.mContext != null) {
            Rect rect = new Rect();
            this.mParent.getGlobalVisibleRect(rect);
            this.mMaxHeight = (rect.top - UIUtil.getStatusBarHeight(this.mContext)) + this.delta;
            measurePopupHeight();
            int i = this.measurePopupHeight;
            int i2 = this.mMaxHeight;
            if (i > i2) {
                setHeight(i2);
            } else {
                int i3 = this.minHeight;
                if (i < i3) {
                    setHeight(i3);
                } else {
                    setHeight(i);
                }
            }
        }
    }

    public void show() {
        if (!(this.mParent == null || this.mContext == null || this.filterList.isEmpty())) {
            int[] iArr = new int[2];
            this.mParent.getLocationOnScreen(iArr);
            int height = (iArr[1] - getHeight()) + this.delta;
            if (isShowing()) {
                update(0, height, getWidth(), getHeight());
            } else {
                showAtLocation(this.mParent, 0, 0, height);
            }
            if (AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
                getContentView().post(new Runnable() {
                    public void run() {
                        AccessibilityUtil.announceForAccessibilityCompat(MMSlashCommandPopupView.this.getContentView(), (CharSequence) MMSlashCommandPopupView.this.mContext.getString(C4558R.string.zm_accessibility_slash_cmd_77835, new Object[]{Integer.valueOf(MMSlashCommandPopupView.this.mListView.getChildCount()), Integer.valueOf(MMSlashCommandPopupView.this.filterList.size())}));
                    }
                });
            }
        }
    }
}
