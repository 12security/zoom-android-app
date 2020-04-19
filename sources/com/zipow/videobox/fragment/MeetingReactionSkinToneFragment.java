package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.util.ZMSettingHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MeetingReactionSkinToneFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final String SELECTED_TYPE = "select_type";
    private static final String TAG = "MeetingReactionSkinToneFragment";
    private int selectedType;
    private ImageView tickImgDark;
    private ImageView tickImgDefault;
    private ImageView tickImgLight;
    private ImageView tickImgMedium;
    private ImageView tickImgMediumDark;
    private ImageView tickImgMediumLight;

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, MeetingReactionSkinToneFragment.class.getName(), bundle, i, false, 1);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.selectedType = bundle.getInt("select_type", 0);
        } else {
            this.selectedType = ZMSettingHelper.getMeetingReactionSkinToneType();
        }
        if (this.selectedType == 0) {
            this.selectedType = 1;
        }
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_meeting_reaction_skin_tone, viewGroup, false);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_default).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_light).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_medium_light).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_medium).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_medium_dark).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_dark).setOnClickListener(this);
        this.tickImgDefault = (ImageView) inflate.findViewById(C4558R.C4560id.img_default);
        this.tickImgLight = (ImageView) inflate.findViewById(C4558R.C4560id.img_light);
        this.tickImgMediumLight = (ImageView) inflate.findViewById(C4558R.C4560id.img_medium_light);
        this.tickImgMedium = (ImageView) inflate.findViewById(C4558R.C4560id.img_medium);
        this.tickImgMediumDark = (ImageView) inflate.findViewById(C4558R.C4560id.img_medium_dark);
        this.tickImgDark = (ImageView) inflate.findViewById(C4558R.C4560id.img_dark);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("select_type", this.selectedType);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            dismiss();
            return;
        }
        if (id == C4558R.C4560id.panel_default) {
            this.selectedType = 1;
        } else if (id == C4558R.C4560id.panel_light) {
            this.selectedType = 2;
        } else if (id == C4558R.C4560id.panel_medium_light) {
            this.selectedType = 3;
        } else if (id == C4558R.C4560id.panel_medium) {
            this.selectedType = 4;
        } else if (id == C4558R.C4560id.panel_medium_dark) {
            this.selectedType = 5;
        } else if (id == C4558R.C4560id.panel_dark) {
            this.selectedType = 6;
        }
        updateUI();
    }

    public void dismiss() {
        ZMSettingHelper.setMeetingReactionSkinToneType(this.selectedType);
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    private void updateUI() {
        int i = 0;
        this.tickImgDefault.setVisibility(this.selectedType == 1 ? 0 : 8);
        this.tickImgLight.setVisibility(this.selectedType == 2 ? 0 : 8);
        this.tickImgMediumLight.setVisibility(this.selectedType == 3 ? 0 : 8);
        this.tickImgMedium.setVisibility(this.selectedType == 4 ? 0 : 8);
        this.tickImgMediumDark.setVisibility(this.selectedType == 5 ? 0 : 8);
        ImageView imageView = this.tickImgDark;
        if (this.selectedType != 6) {
            i = 8;
        }
        imageView.setVisibility(i);
    }

    public boolean onBackPressed() {
        dismiss();
        return true;
    }
}
