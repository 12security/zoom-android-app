package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.videomeetings.C4558R;

public class JbhTimeSelectFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    public static final String SELECTED_JBH_TIME = "selected_jbh_time";
    private ImageView mImg10Min;
    private ImageView mImg15Min;
    private ImageView mImg5Min;
    private ImageView mImgUnlimited;
    private View mPanel10Min;
    private View mPanel15Min;
    private View mPanel5Min;
    private View mPanelUnlimited;
    private int mSelectJoinTime = 5;
    private TextView mTxt10Min;
    private TextView mTxt15Min;
    private TextView mTxt5Min;

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

    public static void show(@NonNull ZMActivity zMActivity, @Nullable Bundle bundle, int i) {
        if (bundle == null) {
            bundle = new Bundle();
        }
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager != null) {
            SimpleActivity.show(supportFragmentManager.findFragmentByTag(ScheduleFragment.class.getName()), JbhTimeSelectFragment.class.getName(), bundle, i);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_jbh_time_select, viewGroup, false);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mPanel5Min = inflate.findViewById(C4558R.C4560id.panel5Min);
        this.mPanel10Min = inflate.findViewById(C4558R.C4560id.panel10Min);
        this.mPanel15Min = inflate.findViewById(C4558R.C4560id.panel15Min);
        this.mPanelUnlimited = inflate.findViewById(C4558R.C4560id.panelUnlimited);
        this.mTxt5Min = (TextView) inflate.findViewById(C4558R.C4560id.txt5Min);
        this.mTxt10Min = (TextView) inflate.findViewById(C4558R.C4560id.txt10Min);
        this.mTxt15Min = (TextView) inflate.findViewById(C4558R.C4560id.txt15Min);
        this.mImg5Min = (ImageView) inflate.findViewById(C4558R.C4560id.img5Min);
        this.mImg10Min = (ImageView) inflate.findViewById(C4558R.C4560id.img10Min);
        this.mImg15Min = (ImageView) inflate.findViewById(C4558R.C4560id.img15Min);
        this.mImgUnlimited = (ImageView) inflate.findViewById(C4558R.C4560id.imgUnlimited);
        this.mTxt5Min.setText(getString(C4558R.string.zm_lbl_min_115416, Integer.valueOf(5)));
        this.mTxt10Min.setText(getString(C4558R.string.zm_lbl_min_115416, Integer.valueOf(10)));
        this.mTxt15Min.setText(getString(C4558R.string.zm_lbl_min_115416, Integer.valueOf(15)));
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSelectJoinTime = arguments.getInt(SELECTED_JBH_TIME, 5);
        }
        updateSelectJbhTime(this.mSelectJoinTime);
        this.mPanel5Min.setOnClickListener(this);
        this.mPanel10Min.setOnClickListener(this);
        this.mPanel15Min.setOnClickListener(this);
        this.mPanelUnlimited.setOnClickListener(this);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("mSelectJoinTime", this.mSelectJoinTime);
    }

    public void onViewStateRestored(@Nullable Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle != null) {
            this.mSelectJoinTime = bundle.getInt("mSelectJoinTime", 5);
            updateSelectJbhTime(this.mSelectJoinTime);
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.panel5Min) {
            this.mSelectJoinTime = 5;
            updateSelectJbhTime(this.mSelectJoinTime);
        } else if (id == C4558R.C4560id.panel10Min) {
            this.mSelectJoinTime = 10;
            updateSelectJbhTime(this.mSelectJoinTime);
        } else if (id == C4558R.C4560id.panel15Min) {
            this.mSelectJoinTime = 15;
            updateSelectJbhTime(this.mSelectJoinTime);
        } else if (id == C4558R.C4560id.panelUnlimited) {
            this.mSelectJoinTime = 0;
            updateSelectJbhTime(this.mSelectJoinTime);
        }
    }

    private void updateSelectJbhTime(int i) {
        this.mImg5Min.setVisibility(8);
        this.mImg10Min.setVisibility(8);
        this.mImg15Min.setVisibility(8);
        this.mImgUnlimited.setVisibility(8);
        if (i == 5) {
            this.mImg5Min.setVisibility(0);
        } else if (i == 10) {
            this.mImg10Min.setVisibility(0);
        } else if (i == 15) {
            this.mImg15Min.setVisibility(0);
        } else if (i == 0) {
            this.mImgUnlimited.setVisibility(0);
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_JBH_TIME, this.mSelectJoinTime);
        finishFragment(-1, intent);
    }

    public boolean onBackPressed() {
        dismiss();
        return false;
    }
}
