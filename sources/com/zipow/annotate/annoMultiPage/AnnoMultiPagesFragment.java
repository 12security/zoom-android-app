package com.zipow.annotate.annoMultiPage;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.ZMGlideUtil;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class AnnoMultiPagesFragment extends ZMDialogFragment implements OnClickListener {
    public static final int APAN_COUNT_PORTRAIT_MOBILE = 2;
    public static final int SPAN_COUNT_LANDSCAPE = 4;
    public static final int SPAN_COUNT_PORTRAIT_TABLET = 3;
    private AnnoMultiPageAdapter adapter;
    private boolean editMode = false;
    private AnnoDataMgr mAnnoDataMgr = AnnoDataMgr.getInstance();
    private View mBtnCloseWhiteBoards;
    private TextView mBtnEditWhiteBoards;
    private boolean mSaveModel = false;
    private RecyclerView mShowWhiteBoards;
    private TextView mTitle;
    private int mTotalPageNum = 1;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(1, C4558R.style.ZMDialog_FullScreen);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_multi_pages_for_whiteboard, viewGroup, false);
        this.mBtnCloseWhiteBoards = inflate.findViewById(C4558R.C4560id.zm_btn_close_whiteboards);
        this.mBtnCloseWhiteBoards.setOnClickListener(this);
        this.mTitle = (TextView) inflate.findViewById(C4558R.C4560id.id_wb_title);
        this.mBtnEditWhiteBoards = (TextView) inflate.findViewById(C4558R.C4560id.zm_btn_edit_whiteboards);
        this.mBtnEditWhiteBoards.setOnClickListener(this);
        this.mShowWhiteBoards = (RecyclerView) inflate.findViewById(C4558R.C4560id.zm_show_whiteboards);
        int i = UIUtil.isTablet(getContext()) ? 3 : 2;
        Context context = getContext();
        if (getResources().getConfiguration().orientation != 1) {
            i = 4;
        }
        this.mShowWhiteBoards.setLayoutManager(new GridLayoutManager(context, i));
        this.adapter = new AnnoMultiPageAdapter(this, ZMGlideUtil.getGlideRequestManager((Fragment) this));
        this.mShowWhiteBoards.setAdapter(this.adapter);
        if (this.mSaveModel) {
            this.mBtnEditWhiteBoards.setText(C4558R.string.zm_btn_save);
            this.mTitle.setText(C4558R.string.zm_anno_select_whiteboard_103374);
        }
        if (!this.mSaveModel) {
            this.editMode = false;
            this.mBtnEditWhiteBoards.setText(C4558R.string.zm_btn_edit_43757);
        }
        AnnoMultiPageAdapter annoMultiPageAdapter = this.adapter;
        if (annoMultiPageAdapter != null) {
            annoMultiPageAdapter.setShowMode(this.editMode, this.mSaveModel);
        }
        return inflate;
    }

    public AnnoMultiPageAdapter getAdapter() {
        return this.adapter;
    }

    public void showNow(FragmentManager fragmentManager, String str, boolean z) {
        checkEditBtnVisible();
        this.mSaveModel = z;
        Glide.get(VideoBoxApplication.getNonNullInstance()).clearMemory();
        super.showNow(fragmentManager, str);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.zm_btn_close_whiteboards) {
            finishFragment(0);
            if (this.mSaveModel) {
                this.mAnnoDataMgr.resetSaveStatus();
            }
        } else if (id != C4558R.C4560id.zm_btn_edit_whiteboards) {
        } else {
            if (this.mSaveModel) {
                this.mAnnoDataMgr.copyPageSnapahotToAlbum();
                finishFragment(0);
                return;
            }
            toggleEditMode();
        }
    }

    private void toggleEditMode() {
        if (this.editMode) {
            this.editMode = false;
            this.mBtnEditWhiteBoards.setText(C4558R.string.zm_btn_edit_43757);
        } else {
            this.editMode = true;
            this.mBtnEditWhiteBoards.setText(C4558R.string.zm_btn_done_43757);
        }
        AnnoMultiPageAdapter annoMultiPageAdapter = this.adapter;
        if (annoMultiPageAdapter != null) {
            annoMultiPageAdapter.setShowMode(this.editMode, this.mSaveModel);
            this.adapter.notifyDataSetChanged();
        }
    }

    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        RecyclerView recyclerView = this.mShowWhiteBoards;
        if (recyclerView != null && recyclerView.getLayoutManager() != null) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) this.mShowWhiteBoards.getLayoutManager();
            int i = 2;
            if (configuration.orientation == 2) {
                gridLayoutManager.setSpanCount(4);
            } else if (configuration.orientation == 1) {
                if (UIUtil.isTablet(getContext())) {
                    i = 3;
                }
                gridLayoutManager.setSpanCount(i);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onWBPageNumChanged(int i) {
        this.mTotalPageNum = i;
        if (i == 1) {
            toggleEditMode();
        }
        checkEditBtnVisible();
    }

    private void checkEditBtnVisible() {
        TextView textView = this.mBtnEditWhiteBoards;
        if (textView != null) {
            textView.setVisibility(this.mTotalPageNum > 1 ? 0 : 8);
        }
    }
}
