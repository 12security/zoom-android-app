package com.zipow.videobox.view.sip;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallHistoryItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.view.sip.BasePBXHistoryAdapter.IPBXListView;
import com.zipow.videobox.view.sip.BasePBXHistoryAdapter.ViewHolder;
import java.io.File;
import java.util.List;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXCallHistoryAdapter extends BasePBXHistoryAdapter<CmmSIPCallHistoryItemBean> implements OnClickListener {
    private static final String TAG = "PhonePBXCallHistoryAdapter";

    public PhonePBXCallHistoryAdapter(Context context, IPBXListView iPBXListView) {
        super(context, iPBXListView);
    }

    /* access modifiers changed from: protected */
    public void bind(int i, View view, ViewHolder viewHolder, ViewGroup viewGroup) {
        viewHolder.checkDeleteItem.setVisibility(this.mIsSelectMode ? 0 : 8);
        CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) getItem(i);
        if (cmmSIPCallHistoryItemBean != null) {
            if (cmmSIPCallHistoryItemBean.isMissedCall()) {
                viewHolder.txtBuddyName.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_call_history_name_miss));
            } else {
                viewHolder.txtBuddyName.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_call_history_name));
            }
            if (cmmSIPCallHistoryItemBean.isInBound()) {
                viewHolder.imgOutCall.setVisibility(4);
                viewHolder.txtBuddyName.setText(cmmSIPCallHistoryItemBean.getDisplayName());
                TextView textView = viewHolder.txtBuddyName;
                StringBuilder sb = new StringBuilder();
                sb.append(cmmSIPCallHistoryItemBean.getFromUserName());
                sb.append(this.mContext.getString(C4558R.string.zm_accessibility_sip_call_history_in_calling_62592));
                textView.setContentDescription(sb.toString());
                if (TextUtils.isEmpty(cmmSIPCallHistoryItemBean.getPhoneNumberContentDescription())) {
                    cmmSIPCallHistoryItemBean.setPhoneNumberContentDescription(StringUtil.digitJoin(cmmSIPCallHistoryItemBean.getFromPhoneNumber().split(""), OAuth.SCOPE_DELIMITER));
                }
            } else {
                viewHolder.imgOutCall.setVisibility(0);
                viewHolder.txtBuddyName.setText(cmmSIPCallHistoryItemBean.getDisplayName());
                TextView textView2 = viewHolder.txtBuddyName;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(cmmSIPCallHistoryItemBean.getToUserName());
                sb2.append(this.mContext.getString(C4558R.string.zm_accessibility_sip_call_history_out_calling_62592));
                textView2.setContentDescription(sb2.toString());
                if (TextUtils.isEmpty(cmmSIPCallHistoryItemBean.getPhoneNumberContentDescription())) {
                    cmmSIPCallHistoryItemBean.setPhoneNumberContentDescription(StringUtil.digitJoin(cmmSIPCallHistoryItemBean.getToPhoneNumber().split(""), OAuth.SCOPE_DELIMITER));
                }
            }
            viewHolder.txtCallNo.setText(cmmSIPCallHistoryItemBean.getDisplayPhoneNumber());
            viewHolder.txtCallNo.setContentDescription(cmmSIPCallHistoryItemBean.getPhoneNumberContentDescription());
            viewHolder.recordingPanel.setVisibility(cmmSIPCallHistoryItemBean.isRecordingExist() ? 0 : 8);
            if (cmmSIPCallHistoryItemBean.isRecordingExist()) {
                String string = this.mContext.getResources().getString(C4558R.string.zm_sip_call_duration1_104213, new Object[]{TimeUtil.formateDuration((long) cmmSIPCallHistoryItemBean.getRecordingAudioFile().getFileDuration())});
                viewHolder.recordingPanel.setContentDescription(getTimeContentdescription(string));
                viewHolder.txtRecording.setText(string);
                viewHolder.recordingPanel.setTag(Integer.valueOf(i));
                if (NetworkUtil.hasDataNetwork(this.mContext)) {
                    viewHolder.txtRecording.setEnabled(true);
                } else {
                    viewHolder.txtRecording.setEnabled(isFileExist(cmmSIPCallHistoryItemBean.getRecordingAudioFile()));
                }
                if (!isSelectMode()) {
                    viewHolder.recordingPanel.setOnClickListener(this);
                }
                viewHolder.txtSlaInfo.setPadding(0, 0, 0, 0);
            } else {
                viewHolder.recordingPanel.setClickable(false);
                viewHolder.txtSlaInfo.setPadding(0, 0, 0, UIUtil.dip2px(this.mContext, 6.0f));
            }
            viewHolder.txtDate.setText(getDate(cmmSIPCallHistoryItemBean.getCreateTime() * 1000));
            viewHolder.txtTime.setText(getTime(cmmSIPCallHistoryItemBean.getCreateTime() * 1000));
            if (viewHolder.checkDeleteItem.getVisibility() == 0) {
                viewHolder.checkDeleteItem.setTag(cmmSIPCallHistoryItemBean.getId());
                viewHolder.checkDeleteItem.setChecked(this.mSelectItem.contains(cmmSIPCallHistoryItemBean.getId()));
            }
            viewHolder.showDialog.setVisibility(isSelectMode() ? 8 : 0);
            if (!isSelectMode()) {
                viewHolder.showDialog.setTag(Integer.valueOf(i));
                viewHolder.showDialog.setOnClickListener(this);
            }
            boolean z = (cmmSIPCallHistoryItemBean.isSLAType() || cmmSIPCallHistoryItemBean.isSLGLevel() || cmmSIPCallHistoryItemBean.isCQLevel()) && !StringUtil.isEmptyOrNull(cmmSIPCallHistoryItemBean.getOwnerPhoneNumber()) && !StringUtil.isEmptyOrNull(cmmSIPCallHistoryItemBean.getOwnerName());
            String toExtensionID = cmmSIPCallHistoryItemBean.isInBound() ? cmmSIPCallHistoryItemBean.getToExtensionID() : cmmSIPCallHistoryItemBean.getFromExtensionID();
            boolean z2 = !TextUtils.isEmpty(toExtensionID) && !toExtensionID.equals(cmmSIPCallHistoryItemBean.getOwnerExtensionID());
            if (cmmSIPCallHistoryItemBean.isMissedCall()) {
                viewHolder.txtSlaInfo.setVisibility(0);
                if (!z || !z2) {
                    viewHolder.txtSlaInfo.setText(C4558R.string.zm_sip_history_missed_106004);
                } else {
                    viewHolder.txtSlaInfo.setText(this.mContext.getString(C4558R.string.zm_sip_history_missed_for_106004, new Object[]{cmmSIPCallHistoryItemBean.getOwnerName()}));
                }
            } else if (z) {
                String slaInfo = cmmSIPCallHistoryItemBean.getSlaInfo();
                if (slaInfo == null) {
                    if (cmmSIPCallHistoryItemBean.isSLAType()) {
                        String string2 = this.mContext.getString(C4558R.string.zm_sip_history_you_82852);
                        if (!TextUtils.isEmpty(cmmSIPCallHistoryItemBean.getInterceptExtensionID()) && !cmmSIPCallHistoryItemBean.getInterceptExtensionID().equals(cmmSIPCallHistoryItemBean.getOwnerExtensionID())) {
                            String interceptUserName = (TextUtils.isEmpty(toExtensionID) || toExtensionID.equals(cmmSIPCallHistoryItemBean.getInterceptExtensionID())) ? string2 : cmmSIPCallHistoryItemBean.getInterceptUserName();
                            if (z2) {
                                string2 = cmmSIPCallHistoryItemBean.getOwnerName();
                            }
                            slaInfo = cmmSIPCallHistoryItemBean.isInBound() ? this.mContext.getString(C4558R.string.zm_sip_history_answered_by_for_106004, new Object[]{interceptUserName, string2}) : this.mContext.getString(C4558R.string.zm_sip_history_outgoing_by_for_82852, new Object[]{interceptUserName, string2});
                        } else if (cmmSIPCallHistoryItemBean.isInBound() && z2) {
                            slaInfo = this.mContext.getString(C4558R.string.zm_sip_history_answered_by_106004, new Object[]{cmmSIPCallHistoryItemBean.getOwnerName()});
                        }
                    } else if (z2) {
                        slaInfo = this.mContext.getString(C4558R.string.zm_sip_history_for_106004, new Object[]{cmmSIPCallHistoryItemBean.getOwnerName()});
                    }
                }
                if (slaInfo != null) {
                    viewHolder.txtSlaInfo.setVisibility(0);
                    TextView textView3 = viewHolder.txtSlaInfo;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(cmmSIPCallHistoryItemBean.isRecordingExist() ? " - " : "");
                    sb3.append(slaInfo);
                    textView3.setText(sb3.toString());
                } else {
                    viewHolder.txtSlaInfo.setVisibility(8);
                }
            } else {
                viewHolder.txtSlaInfo.setVisibility(8);
            }
        }
    }

    private String getTimeContentdescription(String str) {
        String[] split = str.trim().split(":");
        int length = split.length;
        String str2 = split[length - 1];
        String str3 = split[length - 2];
        if (length == 2) {
            if (str3.equals("00")) {
                return this.mContext.getString(C4558R.string.zm_sip_call_accessibility4_104213, new Object[]{str2});
            }
            return this.mContext.getString(C4558R.string.zm_sip_call_accessibility3_104213, new Object[]{str3, str2});
        } else if (length == 3) {
            return this.mContext.getString(C4558R.string.zm_sip_call_accessibility2_104213, new Object[]{split[0], str3, str2});
        } else {
            return this.mContext.getString(C4558R.string.zm_sip_call_accessibility1_104213, new Object[]{split[0], split[1], str3, str2});
        }
    }

    public boolean removeCall(String str) {
        CmmSIPCallHistoryItemBean itemById = getItemById(str);
        if (itemById == null) {
            return false;
        }
        this.mHistoryList.remove(itemById);
        return true;
    }

    public CmmSIPCallHistoryItemBean getItemById(String str) {
        List list = this.mHistoryList;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) list.get(i);
            if (StringUtil.isSameString(str, cmmSIPCallHistoryItemBean.getId())) {
                return cmmSIPCallHistoryItemBean;
            }
        }
        return null;
    }

    public int getIndexById(String str) {
        List list = this.mHistoryList;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (StringUtil.isSameString(str, ((CmmSIPCallHistoryItemBean) list.get(i)).getId())) {
                return i;
            }
        }
        return -1;
    }

    public void onClick(View view) {
        if (C4558R.C4560id.showDialog == view.getId()) {
            ((PhonePBXHistoryListView) this.mListView).itemLongClick(((Integer) view.getTag()).intValue());
        } else if (C4558R.C4560id.recordingPanel == view.getId() && !isSelectMode()) {
            playRecording(((Integer) view.getTag()).intValue());
        }
    }

    private void playRecording(int i) {
        if (!CmmSIPCallManager.getInstance().isLoginConflict()) {
            PhonePBXHistoryListView phonePBXHistoryListView = (PhonePBXHistoryListView) this.mListView;
            View childAt = phonePBXHistoryListView.getChildAt((phonePBXHistoryListView.getHeaderViewsCount() + i) - phonePBXHistoryListView.getFirstVisiblePosition());
            CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = (CmmSIPCallHistoryItemBean) getItem(i);
            if (cmmSIPCallHistoryItemBean != null) {
                PBXCallHistory pBXCallHistory = new PBXCallHistory(cmmSIPCallHistoryItemBean);
                phonePBXHistoryListView.getParentFragment().displayCoverView(pBXCallHistory, childAt, !isFileExist(pBXCallHistory.audioFile));
            }
        }
    }

    private boolean isFileExist(CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean) {
        boolean z = false;
        if (cmmSIPAudioFileItemBean == null) {
            return false;
        }
        String localFileName = cmmSIPAudioFileItemBean.getLocalFileName();
        if (!cmmSIPAudioFileItemBean.isFileInLocal()) {
            return false;
        }
        File file = new File(localFileName);
        if (file.exists() && file.length() > 0) {
            z = true;
        }
        return z;
    }

    private String getDate(long j) {
        if (DateUtils.isToday(j)) {
            return this.mContext.getString(C4558R.string.zm_today_85318);
        }
        if (TimeUtil.isYesterday(j)) {
            return this.mContext.getString(C4558R.string.zm_yesterday_85318);
        }
        return DateUtils.formatDateTime(this.mContext, j, 131092);
    }

    private String getTime(long j) {
        return TimeUtil.formatTime(this.mContext, j);
    }
}
