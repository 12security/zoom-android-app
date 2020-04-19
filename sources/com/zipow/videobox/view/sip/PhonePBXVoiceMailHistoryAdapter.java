package com.zipow.videobox.view.sip;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItemBean;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailItemBean;
import com.zipow.videobox.view.sip.BasePBXHistoryAdapter.IPBXListView;
import com.zipow.videobox.view.sip.BasePBXHistoryAdapter.ViewHolder;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXVoiceMailHistoryAdapter extends BasePBXHistoryAdapter<CmmSIPVoiceMailItemBean> implements OnClickListener {
    public PhonePBXVoiceMailHistoryAdapter(@NonNull Context context, IPBXListView iPBXListView) {
        super(context, iPBXListView);
    }

    /* access modifiers changed from: protected */
    public void bind(int i, View view, @NonNull ViewHolder viewHolder, ViewGroup viewGroup) {
        CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) getItem(i);
        if (cmmSIPVoiceMailItemBean != null) {
            viewHolder.checkDeleteItem.setVisibility(this.mIsSelectMode ? 0 : 8);
            viewHolder.txtBuddyName.setTextColor(this.mContext.getResources().getColor(C4558R.color.zm_call_history_name));
            if (cmmSIPVoiceMailItemBean.isUnread()) {
                viewHolder.imgOutCall.setImageResource(C4558R.C4559drawable.zm_unread_voicemail);
                viewHolder.imgOutCall.setVisibility(0);
            } else {
                viewHolder.imgOutCall.setVisibility(4);
            }
            viewHolder.txtBuddyName.setText(cmmSIPVoiceMailItemBean.getDisplayName());
            viewHolder.txtBuddyName.setContentDescription(cmmSIPVoiceMailItemBean.getFromUserName());
            viewHolder.txtCallNo.setText(cmmSIPVoiceMailItemBean.getDisplayPhoneNumber());
            if (TextUtils.isEmpty(cmmSIPVoiceMailItemBean.getPhoneNumberContentDescription())) {
                cmmSIPVoiceMailItemBean.setPhoneNumberContentDescription(StringUtil.digitJoin(cmmSIPVoiceMailItemBean.getFromPhoneNumber().split(""), OAuth.SCOPE_DELIMITER));
            }
            viewHolder.txtCallNo.setContentDescription(cmmSIPVoiceMailItemBean.getPhoneNumberContentDescription());
            viewHolder.txtDate.setText(formatTime(this.mContext, cmmSIPVoiceMailItemBean.getCreateTime()));
            if (isSelectMode()) {
                viewHolder.checkDeleteItem.setTag(cmmSIPVoiceMailItemBean.getId());
                viewHolder.checkDeleteItem.setChecked(this.mSelectItem.contains(cmmSIPVoiceMailItemBean.getId()));
            }
            if (cmmSIPVoiceMailItemBean.getAudioFileList() == null || cmmSIPVoiceMailItemBean.getAudioFileList().isEmpty()) {
                viewHolder.txtTime.setVisibility(8);
            } else {
                viewHolder.txtTime.setVisibility(0);
                viewHolder.txtTime.setText(TimeUtil.formateDuration((long) ((CmmSIPAudioFileItemBean) cmmSIPVoiceMailItemBean.getAudioFileList().get(0)).getFileDuration()));
            }
            viewHolder.showDialog.setVisibility(isSelectMode() ? 8 : 0);
            if (!isSelectMode()) {
                viewHolder.showDialog.setTag(Integer.valueOf(i));
                viewHolder.showDialog.setOnClickListener(this);
            }
            String forwardExtensionName = cmmSIPVoiceMailItemBean.getForwardExtensionName();
            int forwardExtensionLevel = cmmSIPVoiceMailItemBean.getForwardExtensionLevel();
            if (forwardExtensionLevel == -1 || forwardExtensionLevel == 0 || TextUtils.isEmpty(forwardExtensionName)) {
                viewHolder.txtSlaInfo.setVisibility(8);
            } else {
                viewHolder.txtSlaInfo.setText(this.mContext.getString(C4558R.string.zm_pbx_voicemail_for_100064, new Object[]{forwardExtensionName}));
                viewHolder.txtSlaInfo.setVisibility(0);
            }
        }
    }

    public boolean removeCall(String str) {
        CmmSIPVoiceMailItemBean itemById = getItemById(str);
        if (itemById == null) {
            return false;
        }
        this.mHistoryList.remove(itemById);
        return true;
    }

    @Nullable
    public CmmSIPVoiceMailItemBean getItemById(String str) {
        List list = this.mHistoryList;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = (CmmSIPVoiceMailItemBean) list.get(i);
            if (StringUtil.isSameString(str, cmmSIPVoiceMailItemBean.getId())) {
                return cmmSIPVoiceMailItemBean;
            }
        }
        return null;
    }

    public int getIndexById(String str) {
        List list = this.mHistoryList;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (StringUtil.isSameString(str, ((CmmSIPVoiceMailItemBean) list.get(i)).getId())) {
                return i;
            }
        }
        return -1;
    }

    public boolean changeVoiceMailStatus(String str, boolean z) {
        CmmSIPVoiceMailItemBean itemById = getItemById(str);
        if (itemById == null || z == itemById.isUnread()) {
            return false;
        }
        itemById.setUnread(z);
        return true;
    }

    public void onClick(@NonNull View view) {
        if (C4558R.C4560id.showDialog == view.getId()) {
            ((PhonePBXVoiceMailListView) this.mListView).itemLongClick(((Integer) view.getTag()).intValue());
        }
    }

    private String getPhoneNumberContentDescription(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return OAuth.SCOPE_DELIMITER;
        }
        if (str.length() == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(OAuth.SCOPE_DELIMITER);
            return sb.toString();
        } else if (str.length() == 2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getPhoneNumberContentDescription(str.substring(0, 1)));
            sb2.append(getPhoneNumberContentDescription(str.substring(1, 2)));
            return sb2.toString();
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(getPhoneNumberContentDescription(str.substring(0, 2)));
            sb3.append(str.substring(2));
            return sb3.toString();
        }
    }
}
