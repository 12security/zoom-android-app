package com.zipow.videobox.login.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.zipow.videobox.login.ZMCNLoginPanelFragment;
import com.zipow.videobox.login.ZMInternationalLoginPanelFragment;
import java.util.ArrayList;
import p021us.zoom.videomeetings.C4558R;

public class ZMLoginPanelPageAdapter extends FragmentStatePagerAdapter {
    @NonNull
    private ArrayList<Fragment> mPages = new ArrayList<>();

    public ZMLoginPanelPageAdapter(@NonNull FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mPages.add(new ZMCNLoginPanelFragment());
        this.mPages.add(new ZMInternationalLoginPanelFragment());
    }

    public void initTab(@NonNull TabHost tabHost, @NonNull final Context context) {
        tabHost.setup();
        C31551 r0 = new TabContentFactory() {
            public View createTabContent(String str) {
                return new View(context);
            }
        };
        tabHost.addTab(tabHost.newTabSpec(ZMCNLoginPanelFragment.class.getName()).setIndicator(createTabView(context)).setContent(r0));
        tabHost.addTab(tabHost.newTabSpec(ZMInternationalLoginPanelFragment.class.getName()).setIndicator(createTabView(context)).setContent(r0));
    }

    @NonNull
    public String getDefaultTabTag() {
        return ZMCNLoginPanelFragment.class.getName();
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        Fragment fragment = (Fragment) super.instantiateItem(viewGroup, i);
        if (fragment != getItem(i)) {
            this.mPages.set(i, fragment);
        }
        return fragment;
    }

    @NonNull
    public Fragment getItem(int i) {
        return (Fragment) this.mPages.get(i);
    }

    public int getCount() {
        return this.mPages.size();
    }

    private View createTabView(@NonNull Context context) {
        return View.inflate(context, C4558R.layout.zm_indicator_circle, null);
    }
}
