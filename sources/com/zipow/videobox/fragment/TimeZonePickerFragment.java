package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.TimeZoneListView;
import p021us.zoom.androidlib.widget.TimeZoneListView.Listener;
import p021us.zoom.videomeetings.C4558R;

public class TimeZonePickerFragment extends ZMDialogFragment implements Listener, OnClickListener {
    public static final String TIME_ZONE_SELECTED_ID = "time_zone_selected_name";
    private View mBtnBack;
    private TimeZoneListView mTimeZoneListView;

    public static void show(@Nullable Fragment fragment, @Nullable Bundle bundle, int i) {
        if (fragment != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            SimpleActivity.show(fragment, TimeZonePickerFragment.class.getName(), bundle, i);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_time_zone_picker_layout, viewGroup, false);
        this.mTimeZoneListView = (TimeZoneListView) inflate.findViewById(C4558R.C4560id.timeZoneListView);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnBack.setOnClickListener(this);
        this.mTimeZoneListView.setListener(this);
        return inflate;
    }

    public void onSelected(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Intent intent = new Intent();
            intent.putExtra(TIME_ZONE_SELECTED_ID, str);
            dismiss(intent);
        }
    }

    public void dismiss(@Nullable Intent intent) {
        FragmentActivity activity = getActivity();
        if (getShowsDialog()) {
            super.dismiss();
        } else if (activity != null) {
            if (intent == null) {
                activity.setResult(-1);
            } else {
                activity.setResult(-1, intent);
            }
            activity.finish();
        }
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnBack) {
            onClickBackBtn();
        }
    }

    private void onClickBackBtn() {
        dismiss(null);
    }
}
