package com.zipow.videobox.view.bookmark;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class BookmarkAddViewFragment extends ZMDialogFragment implements OnClickListener {
    private BookmarkMgr mBookmarMgr = new BookmarkMgr();
    private View mBtnBack;
    private View mBtnStore;
    /* access modifiers changed from: private */
    @Nullable
    public String mCurPageTitle;
    /* access modifiers changed from: private */
    @Nullable
    public String mCurPageUrl;
    private EditText mTitle;
    private TextView mURL;

    public static void showAsActivity(@Nullable Fragment fragment, @Nullable Bundle bundle) {
        if (fragment != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            SimpleActivity.show(fragment, BookmarkAddViewFragment.class.getName(), bundle, 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_bookmark_add_view, viewGroup, false);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnStore = inflate.findViewById(C4558R.C4560id.btnStore);
        this.mTitle = (EditText) inflate.findViewById(C4558R.C4560id.edtTitle);
        this.mURL = (TextView) inflate.findViewById(C4558R.C4560id.txtURL);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnStore.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mCurPageTitle = arguments.getString(BookmarkMgr.BOOKMARK_TITLE);
            this.mCurPageUrl = arguments.getString(BookmarkMgr.BOOKMARK_URL);
        }
        if (this.mCurPageTitle == null) {
            this.mCurPageUrl = "";
        }
        if (this.mCurPageUrl == null) {
            this.mCurPageUrl = "";
        }
        this.mTitle.setText(this.mCurPageTitle);
        this.mURL.setText(this.mCurPageUrl);
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
                        BookmarkAddViewFragment.this.mCurPageTitle = "";
                    } else {
                        BookmarkAddViewFragment.this.mCurPageTitle = trim;
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
                        BookmarkAddViewFragment.this.mCurPageUrl = "";
                    } else {
                        BookmarkAddViewFragment.this.mCurPageUrl = trim;
                    }
                    BookmarkAddViewFragment.this.checkInput();
                }
            }
        });
        return inflate;
    }

    /* access modifiers changed from: private */
    public void checkInput() {
        this.mBtnStore.setEnabled(!StringUtil.isEmptyOrNull(this.mCurPageUrl));
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
    }

    public void onClick(View view) {
        if (view == this.mBtnStore) {
            onClickBtnStore();
        } else if (view == this.mBtnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickBtnStore() {
        if (!StringUtil.isEmptyOrNull(this.mCurPageUrl)) {
            this.mBookmarMgr.add(new BookmarkItem(this.mCurPageTitle, this.mCurPageUrl));
        }
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }
}
