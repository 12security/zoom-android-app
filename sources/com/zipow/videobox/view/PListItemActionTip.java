package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.fragment.ExpelUserAlertDialog;
import com.zipow.videobox.fragment.MakeHostAlertDialog;
import com.zipow.videobox.fragment.PListFragment;
import com.zipow.videobox.util.ConfLocalHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class PListItemActionTip extends ZMTip implements OnClickListener {
    private PListItem mPListItem;

    public PListItemActionTip(Context context, AttributeSet attributeSet, PListItem pListItem) {
        super(context, attributeSet);
        this.mPListItem = pListItem;
        initView();
    }

    public PListItemActionTip(Context context, PListItem pListItem) {
        super(context);
        this.mPListItem = pListItem;
        initView();
    }

    private void initView() {
        View.inflate(getContext(), C4558R.layout.zm_plistitem_action_tip, this);
        Button button = (Button) findViewById(C4558R.C4560id.btnMakeHost);
        Button button2 = (Button) findViewById(C4558R.C4560id.btnChat);
        Button button3 = (Button) findViewById(C4558R.C4560id.btnMute);
        Button button4 = (Button) findViewById(C4558R.C4560id.btnUnmute);
        Button button5 = (Button) findViewById(C4558R.C4560id.btnExpel);
        if (ConfMgr.getInstance().getUserById(this.mPListItem.userId).getAudioStatusObj().getIsMuted()) {
            button3.setVisibility(8);
        } else {
            button4.setVisibility(8);
        }
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnMakeHost) {
            onClickBtnMakeHost();
        } else if (id == C4558R.C4560id.btnChat) {
            onClickBtnChat();
        } else if (id == C4558R.C4560id.btnMute) {
            onClickBtnMute();
        } else if (id == C4558R.C4560id.btnUnmute) {
            onClickBtnUnmute();
        } else if (id == C4558R.C4560id.btnExpel) {
            onClickBtnExpel();
        }
    }

    private void onClickBtnMakeHost() {
        MakeHostAlertDialog.showMakeHostAlertDialog((ZMActivity) getContext(), this.mPListItem.userId);
        dismiss();
    }

    private void onClickBtnChat() {
        showChatUI(this.mPListItem);
        dismiss();
    }

    private void onClickBtnMute() {
        ConfMgr.getInstance().handleUserCmd(47, this.mPListItem.userId);
        dismiss();
    }

    private void onClickBtnUnmute() {
        ConfMgr.getInstance().handleUserCmd(48, this.mPListItem.userId);
        dismiss();
    }

    private void onClickBtnExpel() {
        ExpelUserAlertDialog.showExpelUserAlertDialog((ZMActivity) getContext(), this.mPListItem.userId);
        dismiss();
    }

    private void showChatUI(PListItem pListItem) {
        ConfLocalHelper.showChatUI((ZMFragment) PListFragment.getPListFragment(((ZMActivity) getContext()).getSupportFragmentManager()), pListItem.userId);
    }
}
