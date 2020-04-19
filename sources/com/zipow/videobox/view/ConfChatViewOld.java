package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatViewOld extends LinearLayout implements OnClickListener, OnScrollListener {
    private Button mBtnBack;
    private Button mBtnSend;
    private ConfChatListViewOld mChatListView;
    private EditText mEdtMessage;
    private Listener mListener;
    private TextView mTxtTitle;
    private long mUserId;

    public interface Listener {
        void onClickBack();
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public ConfChatViewOld(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfChatViewOld(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), C4558R.layout.zm_conf_chat_view, this);
        this.mTxtTitle = (TextView) findViewById(C4558R.C4560id.txtTitle);
        this.mChatListView = (ConfChatListViewOld) findViewById(C4558R.C4560id.chatListView);
        this.mEdtMessage = (EditText) findViewById(C4558R.C4560id.edtMessage);
        this.mBtnSend = (Button) findViewById(C4558R.C4560id.btnSend);
        this.mBtnBack = (Button) findViewById(C4558R.C4560id.btnBack);
        this.mBtnSend.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mChatListView.setOnScrollListener(this);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public long getUserId() {
        return this.mUserId;
    }

    public void loadData(long j) {
        String str;
        this.mUserId = j;
        if (this.mUserId == 0) {
            str = getContext().getString(C4558R.string.zm_lbl_everyone);
        } else {
            str = "";
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById != null) {
                str = userById.getScreenName();
            }
        }
        this.mTxtTitle.setText(str);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (!(confContext == null || !confContext.isPrivateChatOFF() || j == 0)) {
            this.mBtnSend.setEnabled(false);
            this.mEdtMessage.setHint(C4558R.string.zm_hint_private_chat_disabled);
        }
        loadMessages(this.mUserId);
    }

    public void reloadData() {
        loadData(this.mUserId);
    }

    private void loadMessages(long j) {
        this.mChatListView.loadMessages(j);
    }

    public void onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
        long j4 = this.mUserId;
        if (j == j4 || j2 == j4 || j4 == 0) {
            this.mChatListView.onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
            String str5 = str;
            ConfMgr.getInstance().setChatMessageAsReaded(str);
        }
    }

    public void scrollToBottom(boolean z) {
        this.mChatListView.scrollToBottom(z);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnSend) {
            onClickBtnSend();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnSend() {
        String trim = this.mEdtMessage.getText().toString().trim();
        if (trim.length() != 0) {
            scrollToBottom(true);
            ConfMgr.getInstance().sendChatMessageTo(this.mUserId, trim, false, false, 0);
            this.mEdtMessage.setText("");
        }
    }

    private void onClickBtnBack() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onClickBack();
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 1) {
            UIUtil.closeSoftKeyboard(getContext(), this.mEdtMessage);
        }
    }
}
