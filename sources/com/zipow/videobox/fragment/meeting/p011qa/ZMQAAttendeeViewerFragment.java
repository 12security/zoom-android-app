package com.zipow.videobox.fragment.meeting.p011qa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.fragment.meeting.p011qa.dialog.ZMQAAskDialog;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.androidlib.widget.segement.OnTabSelectListener;
import p021us.zoom.androidlib.widget.segement.ZMSegmentTabLayout;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAAttendeeViewerFragment */
public class ZMQAAttendeeViewerFragment extends ZMDialogFragment implements OnClickListener {
    @NonNull
    private static int[] NO_ITEM_TXT_MSG = {C4558R.string.zm_qa_msg_no_question, C4558R.string.zm_qa_msg_no_question};
    private static final String TAG = "ZMQAAttendeeViewerFragment";
    /* access modifiers changed from: private */
    @NonNull
    public static int[] TITLE_IDS = {C4558R.string.zm_qa_tab_all_question_41047, C4558R.string.zm_qa_tab_my_question_41047};
    private View llContent;
    private View mBtnAsk;
    private View mBtnBack;
    private View mPanelNoItemMsg;
    private IZoomQAUIListener mQAUIListener;
    private TabViewPagerAdapter mTabViewPagerAdapter;
    private TextView mTxtNoItemMsg;
    private TextView mTxtNoTitle;
    private ZMSegmentTabLayout mZMSegmentTabLayout;
    /* access modifiers changed from: private */
    public ZMViewPager mZMViewPager;

    /* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAAttendeeViewerFragment$TabViewPagerAdapter */
    static class TabViewPagerAdapter extends FragmentPagerAdapter {
        @NonNull
        private List<Fragment> mCacheFragments = new ArrayList();

        public TabViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public int getCount() {
            return ZMQAAttendeeViewerFragment.TITLE_IDS.length;
        }

        @Nullable
        public Fragment getItem(int i) {
            if (i < this.mCacheFragments.size()) {
                return (Fragment) this.mCacheFragments.get(i);
            }
            ZMQAAttendeeTabFragment zMQAAttendeeTabFragment = null;
            if (i == 0) {
                zMQAAttendeeTabFragment = ZMQAAttendeeTabFragment.newInstance(ZMQuestionsMode.MODE_ATTENDEE_ALL_QUESTIONS.ordinal());
            } else if (i == 1) {
                zMQAAttendeeTabFragment = ZMQAAttendeeTabFragment.newInstance(ZMQuestionsMode.MODE_ATTENDEE_MY_QUESTIONS.ordinal());
            }
            if (zMQAAttendeeTabFragment != null) {
                this.mCacheFragments.add(zMQAAttendeeTabFragment);
            }
            return zMQAAttendeeTabFragment;
        }

        public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
            super.destroyItem(viewGroup, i, obj);
            if (i < this.mCacheFragments.size()) {
                this.mCacheFragments.remove(i);
            }
        }
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        SimpleActivity.show(zMActivity, ZMQAAttendeeViewerFragment.class.getName(), new Bundle(), 0);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_qa_attendee_viewer, viewGroup, false);
        this.llContent = inflate.findViewById(C4558R.C4560id.llContent);
        this.mZMSegmentTabLayout = (ZMSegmentTabLayout) inflate.findViewById(C4558R.C4560id.zmSegmentTabLayout);
        this.mZMSegmentTabLayout.setTabWidth((float) ZMQAHelper.getTabWidth(getContext(), TITLE_IDS.length));
        this.mZMViewPager = (ZMViewPager) inflate.findViewById(C4558R.C4560id.viewPager);
        this.mZMViewPager.setDisableScroll(true);
        this.mTabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager());
        this.mZMViewPager.setAdapter(this.mTabViewPagerAdapter);
        this.mZMSegmentTabLayout.setTabData(getTabTitles());
        this.mZMSegmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            public void onTabReselect(int i) {
            }

            public void onTabSelect(int i) {
                ZMQAAttendeeViewerFragment.this.mZMViewPager.setCurrentItem(i);
            }
        });
        this.mBtnAsk = inflate.findViewById(C4558R.C4560id.btnAsk);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mTxtNoTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtNoMessageTitle);
        this.mTxtNoItemMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtNoItemMsg);
        this.mBtnAsk.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onRefreshQAUI() {
                    ZMQAAttendeeViewerFragment.this.updateData();
                }

                public void onReceiveQuestion(String str) {
                    ZMQAAttendeeViewerFragment.this.updateData();
                }

                public void onAddQuestion(String str, boolean z) {
                    if (ZMQAHelper.isQuestionSent(str)) {
                        ZMQAAttendeeViewerFragment.this.updateData();
                    }
                }

                public void onReceiveAnswer(String str) {
                    ZMQAAttendeeViewerFragment.this.updateData();
                }

                public void notifyConnectResult(boolean z) {
                    ZMQAAttendeeViewerFragment.this.updateData();
                }

                public void onQuestionMarkedAsDismissed(String str) {
                    ZMQAAttendeeViewerFragment.this.updateData();
                }

                public void onReopenQuestion(String str) {
                    ZMQAAttendeeViewerFragment.this.updateData();
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        updateData();
    }

    /* access modifiers changed from: private */
    public void updateData() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null || !qAComponent.isStreamConflict()) {
            this.mBtnAsk.setVisibility(0);
            if (qAComponent == null || qAComponent.getQuestionCount() <= 0) {
                this.llContent.setVisibility(8);
                this.mPanelNoItemMsg.setVisibility(0);
                this.mTxtNoTitle.setVisibility(0);
                this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_no_question_41047);
            } else {
                this.llContent.setVisibility(0);
                this.mPanelNoItemMsg.setVisibility(8);
            }
            this.mZMSegmentTabLayout.updateTabData(getTabTitles());
            return;
        }
        this.llContent.setVisibility(8);
        this.mPanelNoItemMsg.setVisibility(0);
        this.mTxtNoTitle.setVisibility(8);
        this.mTxtNoItemMsg.setText(C4558R.string.zm_qa_msg_stream_conflict);
        this.mBtnAsk.setVisibility(8);
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnAsk) {
            onClickBtnAsk();
        }
    }

    private void onClickBtnAsk() {
        ZMQAAskDialog.show((ZMActivity) getActivity());
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        finishFragment(true);
    }

    @NonNull
    private String[] getTabTitles() {
        String[] strArr = new String[TITLE_IDS.length];
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        int i = 0;
        if (qAComponent == null) {
            while (true) {
                int[] iArr = TITLE_IDS;
                if (i >= iArr.length) {
                    break;
                }
                strArr[i] = getString(iArr[i]);
                i++;
            }
        } else {
            int i2 = 0;
            while (i < TITLE_IDS.length) {
                if (i == 0) {
                    i2 = qAComponent.getQuestionCount();
                } else if (i == 1) {
                    i2 = qAComponent.getMyQuestionCount();
                }
                if (i2 == 0) {
                    strArr[i] = getString(TITLE_IDS[i]);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(TITLE_IDS[i]));
                    sb.append("(");
                    sb.append(i2 > 99 ? "99+" : String.valueOf(i2));
                    sb.append(")");
                    strArr[i] = sb.toString();
                }
                i++;
            }
        }
        return strArr;
    }
}
