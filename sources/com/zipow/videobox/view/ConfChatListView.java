package com.zipow.videobox.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfAppProtos.ChatMessage;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.util.ZMWebLinkFilter;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatListView extends ListView {
    private static final int MSG_SCROLL_BOTTOM = 1;
    private static final int TIME_NEW_MESSAGE_REFRESH_DELAY = 2000;
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                ConfChatListView.this.scrollToBottom(false);
            }
        }
    };
    private boolean mHasNewMessageDuringPaused = true;
    @Nullable
    private Runnable mNewMsgRefreshTask;
    @NonNull
    private List<String> mPendingItems = new ArrayList();
    private WebinarChatAdapter mWebinarChatAdapter;

    public interface OnClickMessageListener {
        void onClickMessage(ConfChatItem confChatItem);

        void onLongClickMessage(ConfChatItem confChatItem);
    }

    static class WebinarChatAdapter extends BaseAdapter {
        private Context mContext;
        /* access modifiers changed from: private */
        public OnClickMessageListener mOnClickMessageListener;
        @NonNull
        private List<ConfChatItem> mitems = new ArrayList();

        public long getItemId(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        public void setOnClickMessageListener(OnClickMessageListener onClickMessageListener) {
            this.mOnClickMessageListener = onClickMessageListener;
        }

        WebinarChatAdapter(Context context) {
            this.mContext = context;
        }

        public boolean isLastItem(@Nullable String str) {
            boolean z = false;
            if (str == null) {
                return false;
            }
            for (int i = 0; i < this.mitems.size(); i++) {
                if (TextUtils.equals(str, ((ConfChatItem) this.mitems.get(i)).f337id)) {
                    if (i == this.mitems.size() - 1) {
                        z = true;
                    }
                    return z;
                }
            }
            return false;
        }

        public int getCount() {
            return this.mitems.size();
        }

        public void addItem(@Nullable ConfChatItem confChatItem) {
            if (confChatItem != null) {
                this.mitems.add(confChatItem);
            }
        }

        public void addItemAtFirst(@Nullable ConfChatItem confChatItem) {
            if (confChatItem != null) {
                this.mitems.add(0, confChatItem);
            }
        }

        public Object getItem(int i) {
            return this.mitems.get(i);
        }

        public int getItemViewType(int i) {
            ConfChatItem confChatItem = (ConfChatItem) getItem(i);
            return (confChatItem == null || !confChatItem.isSelfSend) ? 1 : 0;
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            View view2;
            int itemViewType = getItemViewType(i);
            String str = itemViewType == 0 ? "messageto" : "messagefrom";
            int i2 = itemViewType == 0 ? C4558R.layout.zm_webinar_chat_to : C4558R.layout.zm_webinar_chat_from;
            int i3 = 0;
            if (view == null || !str.equals(view.getTag())) {
                view2 = LayoutInflater.from(this.mContext).inflate(i2, viewGroup, false);
                view2.setTag(str);
            } else {
                view2 = view;
            }
            final ConfChatItem confChatItem = (ConfChatItem) getItem(i);
            if (confChatItem != null) {
                TextView textView = (TextView) view2.findViewById(C4558R.C4560id.txtMsgLabel);
                TextView textView2 = (TextView) view2.findViewById(C4558R.C4560id.txtPrivateStatus);
                TextView textView3 = (TextView) view2.findViewById(C4558R.C4560id.txtMsgValue);
                View findViewById = view2.findViewById(C4558R.C4560id.layoutMsgHead);
                String str2 = confChatItem.receiverName;
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null && confStatusObj.isMyself(confChatItem.receiver)) {
                    str2 = this.mContext.getString(C4558R.string.zm_webinar_txt_me);
                }
                int i4 = confChatItem.msgType;
                if (i4 != 4) {
                    switch (i4) {
                        case 0:
                            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
                            if (confContext != null && confContext.isWebinar()) {
                                str2 = this.mContext.getString(C4558R.string.zm_mi_panelists_and_attendees_11380);
                                break;
                            } else {
                                str2 = this.mContext.getString(C4558R.string.zm_mi_everyone_122046);
                                break;
                            }
                            break;
                        case 1:
                            str2 = this.mContext.getString(C4558R.string.zm_webinar_txt_all_panelists);
                            break;
                        case 2:
                            str2 = this.mContext.getString(C4558R.string.zm_webinar_txt_label_ccPanelist, new Object[]{str2, this.mContext.getString(C4558R.string.zm_webinar_txt_all_panelists)});
                            break;
                    }
                } else {
                    str2 = this.mContext.getString(C4558R.string.zm_mi_everyone_in_waiting_room_122046);
                }
                if (itemViewType == 0) {
                    textView.setText(this.mContext.getString(C4558R.string.zm_webinar_txt_label_to, new Object[]{str2}));
                } else {
                    textView.setText(this.mContext.getString(C4558R.string.zm_webinar_txt_label_from, new Object[]{confChatItem.senderName, str2}));
                }
                if (i > 0) {
                    ConfChatItem confChatItem2 = (ConfChatItem) getItem(i - 1);
                    if (confChatItem2.msgType == confChatItem.msgType && confChatItem2.receiver == confChatItem.receiver && confChatItem2.sender == confChatItem.sender) {
                        findViewById.setVisibility(8);
                        textView3.setBackgroundResource(itemViewType == 0 ? C4558R.C4559drawable.zm_webinar_message_out_notitle : C4558R.C4559drawable.zm_webinar_message_in_notitle);
                    } else {
                        findViewById.setVisibility(0);
                        textView3.setBackgroundResource(itemViewType == 0 ? C4558R.C4559drawable.zm_webinar_message_out : C4558R.C4559drawable.zm_webinar_message_in);
                    }
                } else {
                    findViewById.setVisibility(0);
                    textView3.setBackgroundResource(itemViewType == 0 ? C4558R.C4559drawable.zm_webinar_message_out : C4558R.C4559drawable.zm_webinar_message_in);
                }
                if (confChatItem.msgType != 3) {
                    i3 = 8;
                }
                textView2.setVisibility(i3);
                textView3.setText(confChatItem.content);
                textView3.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (WebinarChatAdapter.this.mOnClickMessageListener != null) {
                            WebinarChatAdapter.this.mOnClickMessageListener.onClickMessage(confChatItem);
                        }
                    }
                });
                textView3.setOnLongClickListener(new OnLongClickListener() {
                    public boolean onLongClick(View view) {
                        if (WebinarChatAdapter.this.mOnClickMessageListener != null) {
                            WebinarChatAdapter.this.mOnClickMessageListener.onLongClickMessage(confChatItem);
                        }
                        return false;
                    }
                });
                ZMWebLinkFilter.filter(textView3);
            }
            return view2;
        }
    }

    public boolean onUserEvent(int i, long j, int i2) {
        return false;
    }

    public ConfChatListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ConfChatListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ConfChatListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mWebinarChatAdapter = new WebinarChatAdapter(getContext());
        setAdapter(this.mWebinarChatAdapter);
        loadAllItems();
    }

    public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        addChatMessage(str, confStatusObj != null && confStatusObj.isMyself(j));
        return true;
    }

    private void addChatMessage(String str, boolean z) {
        this.mPendingItems.add(str);
        Runnable runnable = this.mNewMsgRefreshTask;
        if (runnable == null) {
            this.mNewMsgRefreshTask = new Runnable() {
                public void run() {
                    ConfChatListView.this.refreshMsg();
                }
            };
            this.mHandler.post(this.mNewMsgRefreshTask);
        } else if (z) {
            this.mHandler.removeCallbacks(runnable);
            this.mNewMsgRefreshTask.run();
            this.mHandler.postDelayed(this.mNewMsgRefreshTask, 2000);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        final int lastVisiblePosition = getLastVisiblePosition();
        super.onLayout(z, i, i2, i3, i4);
        if (z && lastVisiblePosition >= 0) {
            post(new Runnable() {
                public void run() {
                    ConfChatListView.this.setSelection(lastVisiblePosition);
                }
            });
        }
    }

    @Nullable
    public ConfChatItem addChatMsgToAdapter(int i, String str) {
        ConfChatItem confChatItemFromMsgID = ConfChatItem.getConfChatItemFromMsgID(str, true);
        if (confChatItemFromMsgID == null) {
            return null;
        }
        if (i < 0) {
            this.mWebinarChatAdapter.addItem(confChatItemFromMsgID);
        } else if (i == 0) {
            this.mWebinarChatAdapter.addItemAtFirst(confChatItemFromMsgID);
        }
        return confChatItemFromMsgID;
    }

    public void loadAllItems() {
        ConfMgr instance = ConfMgr.getInstance();
        int chatMessageCount = instance.getChatMessageCount();
        if (chatMessageCount > 0) {
            for (int i = 0; i < chatMessageCount; i++) {
                ChatMessage chatMessageAt = instance.getChatMessageAt(i);
                if (chatMessageAt != null) {
                    addChatMsgToAdapter(-1, chatMessageAt.getId());
                }
            }
        }
        this.mWebinarChatAdapter.notifyDataSetChanged();
        this.mHasNewMessageDuringPaused = true;
    }

    public void updateUI() {
        this.mWebinarChatAdapter.notifyDataSetChanged();
        if (this.mHasNewMessageDuringPaused) {
            scrollToBottom(true);
        }
    }

    public void onParentFragmentPause() {
        this.mHasNewMessageDuringPaused = false;
    }

    public void delayScrollToBottom() {
        if (this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
        this.mHandler.sendEmptyMessageDelayed(1, 200);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeMessages(1);
        Runnable runnable = this.mNewMsgRefreshTask;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
            this.mNewMsgRefreshTask = null;
        }
        super.onDetachedFromWindow();
    }

    public void scrollToBottom(boolean z) {
        int lastVisiblePosition = getLastVisiblePosition();
        int count = getCount() - 1;
        if (z) {
            setSelection(count);
        } else if (count - lastVisiblePosition >= 5) {
        } else {
            if (VERSION.SDK_INT >= 16) {
                smoothScrollToPosition(count);
            } else {
                setSelection(count);
            }
        }
    }

    public void setOnClickMessageListener(OnClickMessageListener onClickMessageListener) {
        this.mWebinarChatAdapter.setOnClickMessageListener(onClickMessageListener);
    }

    /* access modifiers changed from: private */
    public void refreshMsg() {
        if (!this.mPendingItems.isEmpty()) {
            ConfChatItem confChatItem = null;
            for (String addChatMsgToAdapter : this.mPendingItems) {
                ConfChatItem addChatMsgToAdapter2 = addChatMsgToAdapter(-1, addChatMsgToAdapter);
                if (addChatMsgToAdapter2 != null && !addChatMsgToAdapter2.isSelfSend) {
                    confChatItem = addChatMsgToAdapter2;
                }
            }
            if (confChatItem != null && AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
                AccessibilityUtil.announceForAccessibilityCompat(this, ConfActivityNormal.getConfChatAccessibilityDescription(getContext(), confChatItem), true);
            }
            WebinarChatAdapter webinarChatAdapter = this.mWebinarChatAdapter;
            List<String> list = this.mPendingItems;
            if (webinarChatAdapter.isLastItem((String) list.get(list.size() - 1))) {
                delayScrollToBottom();
            }
            this.mWebinarChatAdapter.notifyDataSetChanged();
            this.mHasNewMessageDuringPaused = true;
        }
        this.mPendingItems.clear();
        Runnable runnable = this.mNewMsgRefreshTask;
        if (runnable != null) {
            this.mHandler.postDelayed(runnable, 2000);
        }
    }
}
