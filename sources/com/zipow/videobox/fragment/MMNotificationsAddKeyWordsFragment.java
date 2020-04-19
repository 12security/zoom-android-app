package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationsAddKeyWordsFragment extends ZMDialogFragment implements OnClickListener {
    private Button mBackBtn;
    private EditText mContent;
    @NonNull
    private SimpleNotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnKeyWordSettingUpdated() {
            super.OnKeyWordSettingUpdated();
            MMNotificationsAddKeyWordsFragment.this.dismiss();
        }
    };
    private TextView mSaveBtn;
    private TextView mTitle;
    @Nullable
    private List<String> originalKeywords;

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, MMNotificationsAddKeyWordsFragment.class.getName(), new Bundle(), 0);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification_add_keywords, viewGroup, false);
        this.mBackBtn = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mSaveBtn = (TextView) inflate.findViewById(C4558R.C4560id.viewRight);
        this.mTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mContent = (EditText) inflate.findViewById(C4558R.C4560id.zm_notification_keywords_editText);
        this.mBackBtn.setOnClickListener(this);
        this.mSaveBtn.setOnClickListener(this);
        return inflate;
    }

    public void onStart() {
        super.onStart();
        NotificationSettingUI.getInstance().addListener(this.mListener);
    }

    public void onStop() {
        super.onStop();
        NotificationSettingUI.getInstance().removeListener(this.mListener);
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null) {
            this.originalKeywords = notificationSettingMgr.getKeywordSetting();
        }
        List<String> list = this.originalKeywords;
        if (list != null && !list.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < this.originalKeywords.size(); i++) {
                if (i == 0) {
                    stringBuffer.append((String) this.originalKeywords.get(i));
                } else {
                    stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                    stringBuffer.append((String) this.originalKeywords.get(i));
                }
            }
            this.mContent.setText(stringBuffer.toString());
            this.mContent.setSelection(stringBuffer.length());
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            dismiss();
        } else if (id == C4558R.C4560id.viewRight) {
            String obj = this.mContent.getText().toString();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.isConnectionGood()) {
                List<String> asList = Arrays.asList(obj.split(PreferencesConstants.COOKIE_DELIMITER));
                if (!asList.isEmpty()) {
                    LinkedHashMap linkedHashMap = new LinkedHashMap();
                    for (String str : asList) {
                        if (!TextUtils.isEmpty(str) && !linkedHashMap.containsKey(str.toLowerCase())) {
                            linkedHashMap.put(str.toLowerCase(), str);
                        }
                    }
                    if (linkedHashMap.isEmpty()) {
                        asList = new ArrayList<>();
                    } else {
                        asList = new ArrayList<>(linkedHashMap.values());
                    }
                }
                NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                if (notificationSettingMgr != null) {
                    List<String> list = this.originalKeywords;
                    if (list == null || list.isEmpty()) {
                        notificationSettingMgr.applyKeyword(asList, null);
                    } else {
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        for (String str2 : asList) {
                            if (!this.originalKeywords.contains(str2)) {
                                arrayList.add(str2);
                            }
                        }
                        for (String str3 : this.originalKeywords) {
                            if (!asList.contains(str3)) {
                                arrayList2.add(str3);
                            }
                        }
                        notificationSettingMgr.applyKeyword(arrayList, arrayList2);
                    }
                }
                dismiss();
            }
        }
    }
}
