package com.zipow.videobox.fragment.meeting.p011qa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.IConfUIListener;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.IZoomQAUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQAUI.SimpleZoomQAUIListener;
import com.zipow.videobox.fragment.meeting.p011qa.dialog.ZMQAMoreDialog;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.androidlib.widget.segement.OnTabSelectListener;
import p021us.zoom.androidlib.widget.segement.ZMSegmentTabLayout;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAPanelistViewerFragment */
public class ZMQAPanelistViewerFragment extends ZMDialogFragment implements OnClickListener {
    /* access modifiers changed from: private */
    @NonNull
    public static int[] TITLE_IDS = {C4558R.string.zm_qa_tab_open, C4558R.string.zm_qa_tab_answered, C4558R.string.zm_qa_tab_dismissed_34305};
    private View llContent;
    private ImageView mBtnMore;
    private IConfUIListener mConfUIListener;
    private View mPanelNoItemMsg;
    private IZoomQAUIListener mQAUIListener;
    private TabViewPagerAdapter mTabViewPagerAdapter;
    private ZMSegmentTabLayout mZMSegmentTabLayout;
    /* access modifiers changed from: private */
    public ZMViewPager mZMViewPager;

    /* renamed from: com.zipow.videobox.fragment.meeting.qa.ZMQAPanelistViewerFragment$TabViewPagerAdapter */
    static class TabViewPagerAdapter extends FragmentPagerAdapter {
        @NonNull
        private List<Fragment> mCacheFragments = new ArrayList();

        public TabViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public int getCount() {
            return ZMQAPanelistViewerFragment.TITLE_IDS.length;
        }

        @Nullable
        public Fragment getItem(int i) {
            if (i < this.mCacheFragments.size()) {
                return (Fragment) this.mCacheFragments.get(i);
            }
            ZMQAPanelistTabFragment zMQAPanelistTabFragment = null;
            if (i == 0) {
                zMQAPanelistTabFragment = ZMQAPanelistTabFragment.newInstance(ZMQuestionsMode.MODE_OPEN_QUESTIONS.ordinal());
            } else if (i == 1) {
                zMQAPanelistTabFragment = ZMQAPanelistTabFragment.newInstance(ZMQuestionsMode.MODE_ANSWERED_QUESTIONS.ordinal());
            } else if (i == 2) {
                zMQAPanelistTabFragment = ZMQAPanelistTabFragment.newInstance(ZMQuestionsMode.MODE_DISMISSED_QUESTIONS.ordinal());
            }
            if (zMQAPanelistTabFragment != null) {
                this.mCacheFragments.add(zMQAPanelistTabFragment);
            }
            return zMQAPanelistTabFragment;
        }

        public void destroyItem(@NonNull ViewGroup viewGroup, int i, @NonNull Object obj) {
            super.destroyItem(viewGroup, i, obj);
            if (i < this.mCacheFragments.size()) {
                this.mCacheFragments.remove(i);
            }
        }
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        SimpleActivity.show(zMActivity, ZMQAPanelistViewerFragment.class.getName(), new Bundle(), 0);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_qa_panelist_viewer, viewGroup, false);
        this.llContent = inflate.findViewById(C4558R.C4560id.llContent);
        this.mBtnMore = (ImageView) inflate.findViewById(C4558R.C4560id.btnMore);
        this.mZMSegmentTabLayout = (ZMSegmentTabLayout) inflate.findViewById(C4558R.C4560id.zmSegmentTabLayout);
        this.mZMSegmentTabLayout.setTabWidth((float) ZMQAHelper.getTabWidth(getContext(), TITLE_IDS.length));
        this.mZMViewPager = (ZMViewPager) inflate.findViewById(C4558R.C4560id.viewPager);
        this.mZMViewPager.setOffscreenPageLimit(TITLE_IDS.length);
        this.mZMViewPager.setDisableScroll(true);
        this.mTabViewPagerAdapter = new TabViewPagerAdapter(getChildFragmentManager());
        this.mZMViewPager.setAdapter(this.mTabViewPagerAdapter);
        this.mZMSegmentTabLayout.setTabData(getTabTitles());
        this.mZMSegmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            public void onTabReselect(int i) {
            }

            public void onTabSelect(int i) {
                ZMQAPanelistViewerFragment.this.mZMViewPager.setCurrentItem(i);
            }
        });
        this.mPanelNoItemMsg = inflate.findViewById(C4558R.C4560id.panelNoItemMsg);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mBtnMore.setOnClickListener(this);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        ZoomQAUI.getInstance().removeListener(this.mQAUIListener);
        ConfUI.getInstance().removeListener(this.mConfUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mQAUIListener == null) {
            this.mQAUIListener = new SimpleZoomQAUIListener() {
                public void onRefreshQAUI() {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onReceiveQuestion(String str) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onReceiveAnswer(String str) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onAddAnswer(String str, boolean z) {
                    if (ZMQAHelper.isAnswerSent(str)) {
                        ZMQAPanelistViewerFragment.this.updateData();
                    }
                }

                public void onQuestionMarkedAsAnswered(String str) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onUserLivingReply(String str) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void notifyConnectResult(boolean z) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onAnswerSenderNameChanged(String str, String str2) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onQuestionMarkedAsDismissed(String str) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onReopenQuestion(String str) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onUpvoteQuestion(String str, boolean z) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }

                public void onRevokeUpvoteQuestion(String str, boolean z) {
                    ZMQAPanelistViewerFragment.this.updateData();
                }
            };
        }
        ZoomQAUI.getInstance().addListener(this.mQAUIListener);
        if (this.mConfUIListener == null) {
            this.mConfUIListener = new SimpleConfUIListener() {
                public boolean onUserStatusChanged(int i, long j, int i2) {
                    if (i != 1) {
                        switch (i) {
                            case 44:
                            case 45:
                                break;
                        }
                    }
                    ZMQAPanelistViewerFragment.this.processOnHostOrCoHostChanged(j);
                    return true;
                }
            };
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        this.mBtnMore.setVisibility(ConfLocalHelper.isHostCoHost() ? 0 : 8);
        updateData();
    }

    /* access modifiers changed from: private */
    public void processOnHostOrCoHostChanged(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            if (ConfLocalHelper.isHostCoHost()) {
                this.mBtnMore.setVisibility(0);
                return;
            }
            this.mBtnMore.setVisibility(8);
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                ZMQAMoreDialog.dismiss(zMActivity.getSupportFragmentManager());
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateData() {
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        if (qAComponent == null || !qAComponent.isStreamConflict()) {
            this.llContent.setVisibility(0);
            this.mPanelNoItemMsg.setVisibility(8);
            this.mZMSegmentTabLayout.updateTabData(getTabTitles());
            return;
        }
        this.llContent.setVisibility(8);
        this.mPanelNoItemMsg.setVisibility(0);
    }

    @NonNull
    private String[] getTabTitles() {
        int i;
        String[] strArr = new String[TITLE_IDS.length];
        ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
        int i2 = 0;
        if (qAComponent == null) {
            while (true) {
                int[] iArr = TITLE_IDS;
                if (i2 >= iArr.length) {
                    break;
                }
                strArr[i2] = getString(iArr[i2]);
                i2++;
            }
        } else {
            while (i2 < TITLE_IDS.length) {
                if (i2 == 0) {
                    i = qAComponent.getOpenQuestionCount();
                } else if (i2 == 1) {
                    i = qAComponent.getAnsweredQuestionCount();
                } else {
                    i = qAComponent.getDismissedQuestionCount();
                }
                if (i == 0) {
                    strArr[i2] = getString(TITLE_IDS[i2]);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(TITLE_IDS[i2]));
                    sb.append("(");
                    sb.append(i > 99 ? "99+" : String.valueOf(i));
                    sb.append(")");
                    strArr[i2] = sb.toString();
                }
                i2++;
            }
        }
        return strArr;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnMore) {
            onClickBtnMore();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickBtnMore() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ZMQAMoreDialog.showAsActivity(zMActivity);
        }
    }

    public void dismiss() {
        finishFragment(true);
    }
}
