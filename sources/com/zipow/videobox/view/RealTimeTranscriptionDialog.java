package com.zipow.videobox.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CCMessage;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.util.ZMRecyclerView;
import p021us.zoom.videomeetings.C4558R;

public class RealTimeTranscriptionDialog extends ZMDialogFragment implements OnClickListener {
    private static final int MSG_INIT_DATA = 1;
    private static final int MSG_LOAD_LATER_DATA = 3;
    private static final int MSG_LOAD_PRE_DATA = 2;
    public static final int PAGE_SIZE = 20;
    /* access modifiers changed from: private */
    public static final String TAG = "RealTimeTranscriptionDialog";
    boolean isLoadItemsTasking;
    @NonNull
    Adapter<ViewHolder> mAdapter = new Adapter<ViewHolder>() {
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_live_transcript_item, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            CCMessage cCMessage = (CCMessage) RealTimeTranscriptionDialog.this.mDataList.get(i);
            if (!StringUtil.isEmptyOrNull(cCMessage.getContent())) {
                viewHolder.mMessage.setText(cCMessage.getContent());
            }
            viewHolder.mTime.setText(TimeUtil.formatTimeMin(cCMessage.getTime() * 1000));
            CmmUser userById = ConfMgr.getInstance().getUserById(cCMessage.getSpeakerId());
            if (userById != null) {
                viewHolder.mAvatarView.show(new ParamsBuilder().setPath(userById.getSmallPicPath()));
                viewHolder.mScreenName.setText(userById.getScreenName());
            } else {
                viewHolder.mAvatarView.show(null);
                viewHolder.mScreenName.setText("");
            }
            viewHolder.mAvatarView.setVisibility(0);
            viewHolder.mScreenName.setVisibility(0);
            viewHolder.mTime.setVisibility(0);
            if (i >= 1 && ((CCMessage) RealTimeTranscriptionDialog.this.mDataList.get(i - 1)).getSpeakerId() == cCMessage.getSpeakerId()) {
                viewHolder.mAvatarView.setVisibility(8);
                viewHolder.mScreenName.setVisibility(8);
            }
        }

        public int getItemCount() {
            return RealTimeTranscriptionDialog.this.mDataList.size();
        }

        @Nullable
        public Object getDataAtPosition(int i) {
            if (i < 0 || i >= RealTimeTranscriptionDialog.this.mDataList.size()) {
                return null;
            }
            return RealTimeTranscriptionDialog.this.mDataList.get(i);
        }

        public long getItemId(int i) {
            if (hasStableIds()) {
                Object dataAtPosition = getDataAtPosition(i);
                if (dataAtPosition == null) {
                    return super.getItemId(i);
                }
                if (dataAtPosition instanceof CCMessage) {
                    return (long) ((CCMessage) dataAtPosition).getId().hashCode();
                }
            }
            return super.getItemId(i);
        }
    };
    private View mBack;
    @NonNull
    private IConfUIListener mConfUIListener = new SimpleConfUIListener() {
        public void onClosedCaptionMessageReceived(String str, String str2, long j) {
            String access$200 = RealTimeTranscriptionDialog.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onClosedCaptionMessageReceived, msgID = ");
            sb.append(str);
            sb.append(", content = ");
            sb.append(str2);
            sb.append(", time = ");
            sb.append(j);
            ZMLog.m278d(access$200, sb.toString(), new Object[0]);
            RealTimeTranscriptionDialog.this.update(CCMessage.newBuilder().setId(str).setSource(0).setSpeakerId(0).setContent(str2).setTime(j).build(), 1);
        }

        public void onLiveTranscriptionClosedCaptionMessageReceived(CCMessage cCMessage, int i) {
            RealTimeTranscriptionDialog.this.update(cCMessage, i);
        }
    };
    @NonNull
    List<CCMessage> mDataList = new ArrayList();
    int mEnd = -1;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler() {
        public void dispatchMessage(Message message) {
            if (RealTimeTranscriptionDialog.this.isLoadItemsTasking) {
                sendMessageDelayed(Message.obtain(message), 100);
                return;
            }
            switch (message.what) {
                case 1:
                    RealTimeTranscriptionDialog.this.runLoadPreItemsTask(true);
                    break;
                case 2:
                    RealTimeTranscriptionDialog.this.runLoadPreItemsTask(false);
                    break;
                case 3:
                    CCMessage cCMessage = (CCMessage) message.obj;
                    switch (message.arg1) {
                        case 1:
                            RealTimeTranscriptionDialog.this.runLoadLaterItemsTask();
                            break;
                        case 2:
                            int size = RealTimeTranscriptionDialog.this.mDataList.size() - 1;
                            while (true) {
                                if (size >= 0) {
                                    CCMessage cCMessage2 = (CCMessage) RealTimeTranscriptionDialog.this.mDataList.get(size);
                                    if (cCMessage.getId().equalsIgnoreCase(cCMessage2.getId())) {
                                        RealTimeTranscriptionDialog.this.mDataList.remove(cCMessage2);
                                        RealTimeTranscriptionDialog.this.mDataList.add(size, cCMessage);
                                    } else {
                                        size--;
                                    }
                                }
                            }
                            if (size < 0) {
                                RealTimeTranscriptionDialog.this.mHandler.sendEmptyMessage(3);
                                break;
                            }
                            break;
                        case 3:
                            int size2 = RealTimeTranscriptionDialog.this.mDataList.size() - 1;
                            while (true) {
                                if (size2 < 0) {
                                    break;
                                } else {
                                    CCMessage cCMessage3 = (CCMessage) RealTimeTranscriptionDialog.this.mDataList.get(size2);
                                    if (cCMessage.getId().equalsIgnoreCase(cCMessage3.getId())) {
                                        RealTimeTranscriptionDialog.this.mDataList.remove(cCMessage3);
                                        RealTimeTranscriptionDialog.this.mEnd--;
                                        break;
                                    } else {
                                        size2--;
                                    }
                                }
                            }
                    }
                    RealTimeTranscriptionDialog.this.mAdapter.notifyDataSetChanged();
                    if (RealTimeTranscriptionDialog.this.mShowList != null) {
                        RealTimeTranscriptionDialog.this.mShowList.scrollToBottom(false);
                        break;
                    }
                    break;
                default:
                    super.dispatchMessage(message);
                    break;
            }
        }
    };
    private View mShowEmptyTipView;
    /* access modifiers changed from: private */
    public ZMRecyclerView mShowList;
    int mStart = -1;
    /* access modifiers changed from: private */
    public SwipeRefreshLayout mSwipeRefreshLayout;

    private class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        AvatarView mAvatarView;
        TextView mMessage;
        TextView mScreenName;
        TextView mTime;

        public ViewHolder(View view) {
            super(view);
            this.mAvatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            this.mScreenName = (TextView) view.findViewById(C4558R.C4560id.txtScreenName);
            this.mTime = (TextView) view.findViewById(C4558R.C4560id.txtTime);
            this.mMessage = (TextView) view.findViewById(C4558R.C4560id.txtMessage);
        }
    }

    /* access modifiers changed from: private */
    public void update(CCMessage cCMessage, int i) {
        Message obtain = Message.obtain();
        obtain.what = 3;
        obtain.obj = cCMessage;
        obtain.arg1 = i;
        this.mHandler.sendMessage(obtain);
    }

    public static void show(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            RealTimeTranscriptionDialog realTimeTranscriptionDialog = new RealTimeTranscriptionDialog();
            realTimeTranscriptionDialog.setArguments(bundle);
            realTimeTranscriptionDialog.show(fragmentManager, RealTimeTranscriptionDialog.class.getName());
        }
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(zMActivity, RealTimeTranscriptionDialog.class.getName(), bundle, 0, false, true);
        }
    }

    public static void hide(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            RealTimeTranscriptionDialog realTimeTranscriptionDialog = (RealTimeTranscriptionDialog) fragmentManager.findFragmentByTag(RealTimeTranscriptionDialog.class.getName());
            if (realTimeTranscriptionDialog != null) {
                realTimeTranscriptionDialog.dismiss();
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        setCancelable(true);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_live_transcript, null);
        this.mShowList = (ZMRecyclerView) inflate.findViewById(C4558R.C4560id.show_transcript);
        this.mShowEmptyTipView = inflate.findViewById(C4558R.C4560id.showEmptyTipView);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(C4558R.C4560id.swipeRefreshLayout);
        this.mBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBack.setOnClickListener(this);
        this.mShowList.setLayoutManager(new LinearLayoutManager(getContext()));
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            this.mShowList.setItemAnimator(null);
            this.mAdapter.setHasStableIds(true);
        }
        this.mShowList.setAdapter(this.mAdapter);
        this.mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                if (RealTimeTranscriptionDialog.this.mStart == 0) {
                    RealTimeTranscriptionDialog.this.mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    RealTimeTranscriptionDialog.this.mHandler.sendEmptyMessage(2);
                }
            }
        });
        this.mSwipeRefreshLayout.setRefreshing(true);
        this.mHandler.sendEmptyMessage(1);
        ConfUI.getInstance().addListener(this.mConfUIListener);
        return inflate;
    }

    public void onClick(View view) {
        if (view == this.mBack) {
            finishFragment(false);
        }
    }

    public void runLoadPreItemsTask(boolean z) {
        int i;
        int closedCaptionMessageCount = ConfMgr.getInstance().getClosedCaptionMessageCount();
        int i2 = this.mStart;
        if (i2 < 0) {
            i = Math.max(0, closedCaptionMessageCount - 20);
        } else {
            i = Math.max(0, i2 - 20);
        }
        int i3 = this.mEnd;
        if (i3 >= 0) {
            closedCaptionMessageCount = Math.max(0, i3 - 20);
        }
        runLoadItemsTask(i, closedCaptionMessageCount, true, z);
    }

    public void runLoadLaterItemsTask() {
        if (this.mEnd >= 0) {
            runLoadItemsTask(this.mEnd, ConfMgr.getInstance().getClosedCaptionMessageCount(), false, false);
        }
    }

    private synchronized void runLoadItemsTask(int i, int i2, boolean z, boolean z2) {
        List loadItems = loadItems(i, i2);
        if (z) {
            this.mDataList.addAll(0, loadItems);
            this.mStart = i;
            if (this.mEnd < 0) {
                this.mEnd = i2;
            }
        } else {
            this.mDataList.addAll(loadItems);
            this.mEnd = i2;
        }
        if (this.mDataList.size() > 0) {
            this.mShowEmptyTipView.setVisibility(8);
        } else {
            this.mShowEmptyTipView.setVisibility(0);
        }
        this.mAdapter.notifyDataSetChanged();
        if (z2) {
            this.mShowList.scrollToBottom(true);
        } else {
            this.mShowList.scrollToBottom(false);
        }
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    public List<CCMessage> loadItems(int i, int i2) {
        this.isLoadItemsTasking = true;
        ConfMgr instance = ConfMgr.getInstance();
        ArrayList arrayList = new ArrayList();
        while (i < i2) {
            CCMessage cCMessageAt = instance.getCCMessageAt(i);
            if (cCMessageAt != null) {
                arrayList.add(cCMessageAt);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("ccMessage = ");
                sb.append(getCCMessageDesc(cCMessageAt));
                ZMLog.m278d(str, sb.toString(), new Object[0]);
            }
            i++;
        }
        this.isLoadItemsTasking = false;
        return arrayList;
    }

    /* access modifiers changed from: 0000 */
    public String getCCMessageDesc(CCMessage cCMessage) {
        if (cCMessage == null) {
            return "null";
        }
        return String.format("ccMessage-id:%s,source=%d,speakerId=%d,content=%s,time=%s", new Object[]{cCMessage.getId(), Long.valueOf(cCMessage.getSource()), Long.valueOf(cCMessage.getSpeakerId()), cCMessage.getContent(), TimeUtil.formatTimeMin(cCMessage.getTime() * 1000)});
    }

    public void onDestroyView() {
        super.onDestroyView();
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        this.mHandler.removeCallbacksAndMessages(null);
    }
}
