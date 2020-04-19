package com.zipow.videobox.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class NewGroupChatFragment extends ZMDialogFragment implements OnClickListener {
    private static final int MAX_NAME_LENGTH = 64;
    public static final int REQUEST_MM_SELECT_BUDDIES = 100;
    public static final String RESULT_ARG_SELECTED_ITEMS = "selectedItems";
    public static final String RESULT_ARG_SUBJECT = "group.subject";
    private View mBtnCancel;
    private View mBtnNext;
    private EditText mEdtSubject;
    private TextView mTxtCharactersLeft;

    public static void showAsActivity(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            SimpleActivity.show(fragment, NewGroupChatFragment.class.getName(), new Bundle(), i, true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_new_group_chat, null);
        this.mBtnCancel = inflate.findViewById(C4558R.C4560id.btnCancel);
        this.mEdtSubject = (EditText) inflate.findViewById(C4558R.C4560id.edtSubject);
        this.mTxtCharactersLeft = (TextView) inflate.findViewById(C4558R.C4560id.txtCharatersLeft);
        this.mBtnNext = inflate.findViewById(C4558R.C4560id.btnNext);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnNext.setOnClickListener(this);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            if (window != null) {
                window.setSoftInputMode(16);
            }
        }
        checkInput();
        this.mEdtSubject.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                NewGroupChatFragment.this.checkInput();
            }
        });
        return inflate;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null && !getShowsDialog()) {
            intent.putExtra(RESULT_ARG_SUBJECT, getSubject());
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                zMActivity.setResult(-1, intent);
                zMActivity.finish();
                zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkInput() {
        String subject = getSubject();
        Resources resources = getResources();
        int integer = resources.getInteger(C4558R.integer.zm_group_chat_topic_max_length) - subject.length();
        if (integer < 0) {
            integer = 0;
        }
        this.mTxtCharactersLeft.setText(resources.getQuantityString(C4558R.plurals.zm_msg_charactors_left, integer, new Object[]{Integer.valueOf(integer)}));
        this.mBtnNext.setEnabled(!StringUtil.isEmptyOrNull(subject));
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSubject);
        finishFragment(true);
    }

    public void onClick(View view) {
        if (view == this.mBtnCancel) {
            onClickBtnCancel();
        } else if (view == this.mBtnNext) {
            onClickBtnNext();
        }
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickBtnNext() {
        String subject = getSubject();
        if (StringUtil.isEmptyOrNull(subject)) {
            EditText editText = this.mEdtSubject;
            if (editText != null) {
                editText.requestFocus();
            }
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            Bundle bundle = new Bundle();
            bundle.putString(RESULT_ARG_SUBJECT, subject);
            MMSelectContactsActivity.show((Fragment) this, subject, null, getString(C4558R.string.zm_btn_create), getString(C4558R.string.zm_msg_select_buddies_to_join_group_instructions_59554), false, bundle, false, 100, false, (String) null, false, zoomMessenger.getGroupLimitCount(false) - 1);
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }

    private String getSubject() {
        EditText editText = this.mEdtSubject;
        return editText != null ? editText.getText().toString() : "";
    }
}
