package com.zipow.videobox.view.bookmark;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class BookmarkEditViewFragment extends ZMDialogFragment implements OnClickListener {
    private BookmarkMgr mBookmarMgr = new BookmarkMgr();
    private View mBtnBack;
    private View mBtnSave;
    /* access modifiers changed from: private */
    public String mCurPageTitle;
    /* access modifiers changed from: private */
    public String mCurPageUrl;
    private int mIndex = -1;
    private EditText mTitle;
    private EditText mURL;

    public static void showAsActivity(@Nullable Fragment fragment, @Nullable Bundle bundle, int i) {
        if (fragment != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            SimpleActivity.show(fragment, BookmarkEditViewFragment.class.getName(), bundle, i);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_bookmark_edit_view, viewGroup, false);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSave = inflate.findViewById(C4558R.C4560id.btnSave);
        this.mTitle = (EditText) inflate.findViewById(C4558R.C4560id.edtTitle);
        this.mURL = (EditText) inflate.findViewById(C4558R.C4560id.txtURL);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSave.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mIndex = arguments.getInt(BookmarkMgr.BOOKMARK_ITEM_POS, -1);
            BookmarkItem bookmarkItem = this.mBookmarMgr.get(this.mIndex);
            if (this.mIndex >= 0 && bookmarkItem != null) {
                this.mCurPageTitle = bookmarkItem.getItemName();
                this.mCurPageUrl = bookmarkItem.getItemUrl();
            }
        }
        if (StringUtil.isEmptyOrNull(this.mCurPageUrl)) {
            this.mCurPageUrl = "";
        }
        this.mURL.setText(this.mCurPageUrl);
        if (StringUtil.isEmptyOrNull(this.mCurPageTitle)) {
            this.mCurPageTitle = "";
        }
        this.mTitle.setText(this.mCurPageTitle);
        checkInput();
        this.mTitle.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@Nullable Editable editable) {
                if (editable != null) {
                    String trim = editable.toString().trim();
                    if (StringUtil.isEmptyOrNull(trim)) {
                        BookmarkEditViewFragment.this.mCurPageTitle = "";
                    } else {
                        BookmarkEditViewFragment.this.mCurPageTitle = trim;
                    }
                }
            }
        });
        this.mURL.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@Nullable Editable editable) {
                if (editable != null) {
                    String trim = editable.toString().trim();
                    if (StringUtil.isEmptyOrNull(trim)) {
                        BookmarkEditViewFragment.this.mCurPageUrl = "";
                    } else {
                        BookmarkEditViewFragment.this.mCurPageUrl = trim;
                    }
                    BookmarkEditViewFragment.this.checkInput();
                }
            }
        });
        return inflate;
    }

    /* access modifiers changed from: private */
    public void checkInput() {
        this.mBtnSave.setEnabled(!StringUtil.isEmptyOrNull(this.mCurPageUrl));
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnSave) {
            onClickBtnSave();
        }
    }

    private void onClickBtnSave() {
        if (!StringUtil.isEmptyOrNull(this.mCurPageUrl)) {
            this.mBookmarMgr.set(this.mIndex, new BookmarkItem(this.mCurPageTitle, this.mCurPageUrl));
        }
        dismiss();
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }
}
