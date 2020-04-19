package com.zipow.videobox.login.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;
import com.zipow.videobox.login.model.ZMLoginPanelPageAdapter;
import com.zipow.videobox.util.ZmPtUtils;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.ZMViewPager;
import p021us.zoom.videomeetings.C4558R;

public class ZmChinaLoginPanel extends AbstractLoginPanel implements OnClickListener {
    private ZMLoginPanelPageAdapter mPagerAdapter;
    /* access modifiers changed from: private */
    public TabHost mTabHost;
    /* access modifiers changed from: private */
    public ZMViewPager mViewPager;

    public void initVendorOptions(int i) {
    }

    public boolean isEnableLoginType(int i) {
        return i == 101;
    }

    public void onClick(View view) {
    }

    public ZmChinaLoginPanel(Context context) {
        this(context, null);
    }

    public ZmChinaLoginPanel(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public ZmChinaLoginPanel(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View inflate = View.inflate(getContext(), C4558R.layout.zm_layout_china_login, this);
        this.mTabHost = (TabHost) findViewById(16908306);
        this.mPagerAdapter = new ZMLoginPanelPageAdapter(((ZMActivity) getContext()).getSupportFragmentManager());
        this.mPagerAdapter.initTab(this.mTabHost, getContext());
        this.mViewPager = (ZMViewPager) inflate.findViewById(C4558R.C4560id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        showTab(this.mPagerAdapter.getDefaultTabTag());
        this.mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String str) {
                ZMActivity zMActivity = (ZMActivity) ZmChinaLoginPanel.this.getContext();
                if (zMActivity != null && zMActivity.isActive()) {
                    ZmChinaLoginPanel.this.mViewPager.setCurrentItem(ZmChinaLoginPanel.this.mTabHost.getCurrentTab(), false);
                }
            }
        });
        this.mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
            public void onPageSelected(int i) {
                ZmChinaLoginPanel.this.mTabHost.setCurrentTab(i);
            }
        });
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txtCnPrivacy);
        textView.setVisibility(0);
        ZmPtUtils.initPrivacyAndTerms((ZMActivity) getContext(), textView);
    }

    private void showTab(String str) {
        TabHost tabHost = this.mTabHost;
        if (tabHost != null) {
            tabHost.setCurrentTabByTag(str);
            this.mViewPager.setCurrentItem(this.mTabHost.getCurrentTab(), false);
        }
    }
}
