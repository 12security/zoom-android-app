package com.zipow.videobox.view.sip;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMAddrBookItemView;
import java.util.ArrayList;
import java.util.regex.Pattern;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMAddrPbxSearchItemView extends IMAddrBookItemView {
    public IMAddrPbxSearchItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public IMAddrPbxSearchItemView(Context context) {
        super(context);
    }

    public void setAddrBookItem(IMAddrBookItem iMAddrBookItem, String str, boolean z, boolean z2, boolean z3, boolean z4, int i) {
        super.setAddrBookItem(iMAddrBookItem, z, z2, z3, i);
        if (this.mItem != null) {
            String accountEmail = this.mItem.getAccountEmail();
            int contactType = this.mItem.getContactType();
            boolean z5 = false;
            if (StringUtil.isEmptyOrNull(accountEmail) || contactType != 8) {
                this.mEmail.setVisibility(8);
            } else {
                this.mEmail.setVisibility(0);
                this.mEmail.setText(getHighLightWords(accountEmail, str));
            }
            IMAddrBookItem iMAddrBookItem2 = this.mItem;
            if (i == 2) {
                z5 = true;
            }
            String cloudDefaultPhoneNo = iMAddrBookItem2.getCloudDefaultPhoneNo(z5);
            if (!StringUtil.isEmptyOrNull(cloudDefaultPhoneNo)) {
                if (!StringUtil.isEmptyOrNull(this.mItem.getContactTypeString())) {
                    this.mPresenceStateView.setVisibility(8);
                    TextView textView = this.mTxtCustomMessage;
                    StringBuilder sb = new StringBuilder();
                    sb.append(getHighLightWords(cloudDefaultPhoneNo, str));
                    sb.append(this.mItem.getContactTypeString());
                    textView.setText(sb.toString());
                } else {
                    this.mTxtCustomMessage.setText(getHighLightWords(cloudDefaultPhoneNo, str));
                }
            }
            if (this.mItem.isFromPhoneContacts()) {
                this.mTxtCustomMessage.setText(getHighLightWords(this.mTxtCustomMessage.getText().toString(), str));
            }
            if (contactType == 8 || i == 2) {
                this.mPresenceStateView.setVisibility(8);
            }
        }
    }

    public static boolean isInteger(String str) {
        return Pattern.compile("^[-+]?[\\d]*$").matcher(str).matches();
    }

    private CharSequence getHighLightWords(String str, String str2) {
        if (StringUtil.isEmptyOrNull(str2)) {
            return str;
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        SpannableString spannableString = new SpannableString(str);
        int indexOf = str.indexOf(str2);
        while (indexOf != -1) {
            arrayList.add(Integer.valueOf(indexOf));
            indexOf = str.indexOf(str2, indexOf + str2.length());
        }
        for (Integer num : arrayList) {
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(C4558R.color.zm_bg_blue)), num.intValue(), num.intValue() + str2.length(), 17);
        }
        return spannableString;
    }
}
