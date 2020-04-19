package com.zipow.videobox.view.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.view.bookmark.BookmarkListView.onChangeListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class BookmarkListViewFragment extends ZMDialogFragment implements OnClickListener, onChangeListener {
    private static final String BOOKMARK_EDIT_MODE = "bk_edit";
    public static final int REQUEST_BOOKMARK_EDIT = 1200;
    private boolean bEditMode = false;
    private BookmarkListView mBookmarkListView;
    private View mBtnAdd;
    private View mBtnDone;
    private View mBtnEdit;
    @Nullable
    private String mCurPageTitle;
    @Nullable
    private String mCurPageUrl;
    private View mNoBookmarkPromt;

    public static void showAsActivity(@Nullable ZMActivity zMActivity, @Nullable Bundle bundle, int i) {
        if (zMActivity != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            SimpleActivity.show(zMActivity, BookmarkListViewFragment.class.getName(), bundle, i);
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putBoolean(BOOKMARK_EDIT_MODE, this.bEditMode);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.bEditMode = bundle.getBoolean(BOOKMARK_EDIT_MODE, false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_bookmark_list_view, viewGroup, false);
        this.mNoBookmarkPromt = inflate.findViewById(C4558R.C4560id.txtNoBookmark);
        this.mBtnAdd = inflate.findViewById(C4558R.C4560id.btnAdd);
        this.mBtnDone = inflate.findViewById(C4558R.C4560id.btnDone);
        this.mBtnEdit = inflate.findViewById(C4558R.C4560id.btnEdit);
        this.mBookmarkListView = (BookmarkListView) inflate.findViewById(C4558R.C4560id.bookmarkListView);
        this.mNoBookmarkPromt.setVisibility(8);
        this.mBtnAdd.setOnClickListener(this);
        this.mBtnDone.setOnClickListener(this);
        this.mBtnEdit.setOnClickListener(this);
        this.mBookmarkListView.registerListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mCurPageTitle = arguments.getString(BookmarkMgr.BOOKMARK_TITLE);
            this.mCurPageUrl = arguments.getString(BookmarkMgr.BOOKMARK_URL);
        }
        return inflate;
    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        this.mBookmarkListView.reloadAllItems();
        updateUI();
    }

    public void onClick(View view) {
        if (view == this.mBtnAdd) {
            onClickBtnAdd();
        } else if (view == this.mBtnDone) {
            onClickBtnCancel();
        } else if (view == this.mBtnEdit) {
            onClickBtnEdit();
        }
    }

    private void onClickBtnAdd() {
        Bundle bundle = new Bundle();
        if (!StringUtil.isEmptyOrNull(this.mCurPageTitle)) {
            bundle.putString(BookmarkMgr.BOOKMARK_TITLE, this.mCurPageTitle);
        }
        if (!StringUtil.isEmptyOrNull(this.mCurPageUrl)) {
            bundle.putString(BookmarkMgr.BOOKMARK_URL, this.mCurPageUrl);
        }
        BookmarkAddViewFragment.showAsActivity(this, bundle);
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void showEditStatus() {
        ((Button) this.mBtnEdit).setText(C4558R.string.zm_btn_done);
        this.mBtnAdd.setVisibility(8);
        this.mBtnDone.setVisibility(8);
    }

    private void showListStatus() {
        ((Button) this.mBtnEdit).setText(C4558R.string.zm_btn_edit);
        this.mBtnAdd.setVisibility(0);
        this.mBtnDone.setVisibility(0);
    }

    private void onClickBtnEdit() {
        if (this.mBookmarkListView.getItemCount() > 0) {
            this.bEditMode = !this.bEditMode;
        } else {
            this.bEditMode = false;
        }
        updateUI();
    }

    private void updateUI() {
        if (this.mBookmarkListView.getItemCount() <= 0) {
            this.mNoBookmarkPromt.setVisibility(0);
            this.mBtnEdit.setVisibility(8);
        } else {
            this.mNoBookmarkPromt.setVisibility(8);
            this.mBtnEdit.setVisibility(0);
        }
        if (!this.bEditMode) {
            showListStatus();
        } else {
            showEditStatus();
        }
        this.mBookmarkListView.setMode(this.bEditMode);
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onSelectItem(@Nullable BookmarkItem bookmarkItem) {
        if (getShowsDialog()) {
            super.dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent();
            if (bookmarkItem != null) {
                intent.putExtra(BookmarkMgr.BOOKMARK_URL, bookmarkItem.getItemUrl());
            }
            activity.setResult(-1, intent);
            activity.finish();
        }
    }

    public void onEditItem(int i) {
        Bundle bundle = new Bundle();
        if (i >= 0) {
            bundle.putInt(BookmarkMgr.BOOKMARK_ITEM_POS, i);
        }
        BookmarkEditViewFragment.showAsActivity(this, bundle, REQUEST_BOOKMARK_EDIT);
    }

    public void onDataChange() {
        if (this.mBookmarkListView.getItemCount() <= 0) {
            this.bEditMode = false;
        }
        updateUI();
    }
}
