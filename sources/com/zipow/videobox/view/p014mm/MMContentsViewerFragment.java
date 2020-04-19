package com.zipow.videobox.view.p014mm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.zipow.videobox.SimpleActivity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContentsViewerFragment */
public class MMContentsViewerFragment extends ZMDialogFragment {
    private static final String ARG_CURRENT_FILE_ID = "arg_current_file_id";
    private static final String ARG_FILE_IDS = "arg_file_ids";
    private static final String ARG_SESSION_ID = "arg_session_id";
    private ContentViewPagerAdapter mAdapter;
    @Nullable
    private String mCurrentFileId;
    @Nullable
    private List<String> mFileIds;
    /* access modifiers changed from: private */
    @NonNull
    public List<Fragment> mFragments = new ArrayList();
    @Nullable
    private String mSessionId;
    private ViewPager mViewPager;

    /* renamed from: com.zipow.videobox.view.mm.MMContentsViewerFragment$ContentViewPagerAdapter */
    class ContentViewPagerAdapter extends FragmentPagerAdapter {
        public ContentViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            return (Fragment) MMContentsViewerFragment.this.mFragments.get(i);
        }

        public int getCount() {
            return MMContentsViewerFragment.this.mFragments.size();
        }
    }

    public static void showAsActivity(Fragment fragment, String str, List<String> list, int i) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CURRENT_FILE_ID, str);
        bundle.putSerializable(ARG_FILE_IDS, (Serializable) list);
        SimpleActivity.show(fragment, MMContentsViewerFragment.class.getName(), bundle, i);
    }

    public static void showAsActivity(Fragment fragment, String str, String str2, List<String> list, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("arg_session_id", str);
        bundle.putString(ARG_CURRENT_FILE_ID, str2);
        bundle.putSerializable(ARG_FILE_IDS, (Serializable) list);
        SimpleActivity.show(fragment, MMContentsViewerFragment.class.getName(), bundle, i);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_contents_viewer, viewGroup, false);
        this.mViewPager = (ViewPager) inflate.findViewById(C4558R.C4560id.zm_mm_content_view_pager);
        this.mAdapter = new ContentViewPagerAdapter(getFragmentManager());
        this.mViewPager.setAdapter(this.mAdapter);
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            zMActivity.disableFinishActivityByGesture(true);
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionId = arguments.getString("arg_session_id");
            this.mCurrentFileId = arguments.getString(ARG_CURRENT_FILE_ID);
            this.mFileIds = (List) arguments.getSerializable(ARG_FILE_IDS);
        }
        buildFragments();
        return inflate;
    }

    private void buildFragments() {
        List<String> list = this.mFileIds;
        if (list == null || list.isEmpty()) {
            finishFragment(false);
            return;
        }
        for (String str : this.mFileIds) {
            Bundle bundle = new Bundle();
            if (!StringUtil.isEmptyOrNull(this.mSessionId)) {
                bundle.putString("sessionId", this.mSessionId);
            }
            bundle.putString(MMContentFileViewerFragment.RESULT_FILE_WEB_ID, str);
            MMContentFileViewerFragment mMContentFileViewerFragment = new MMContentFileViewerFragment();
            mMContentFileViewerFragment.setArguments(bundle);
            this.mFragments.add(mMContentFileViewerFragment);
        }
        this.mAdapter.notifyDataSetChanged();
        this.mViewPager.setCurrentItem(this.mFileIds.indexOf(this.mCurrentFileId));
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString(ARG_CURRENT_FILE_ID, this.mCurrentFileId);
        bundle.putSerializable(ARG_FILE_IDS, (Serializable) this.mFileIds);
    }

    public void onViewStateRestored(@Nullable Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (bundle != null) {
            this.mCurrentFileId = bundle.getString(ARG_CURRENT_FILE_ID);
            this.mFileIds = (List) bundle.getSerializable(ARG_FILE_IDS);
        }
    }
}
