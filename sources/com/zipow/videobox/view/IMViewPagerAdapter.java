package com.zipow.videobox.view;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.IMAddrBookListFragment;
import com.zipow.videobox.fragment.IMBuddyListFragment;
import com.zipow.videobox.fragment.IMFavoriteListFragment;
import com.zipow.videobox.fragment.IMMeetingFragment;
import com.zipow.videobox.fragment.IMMyMeetingsFragment;
import com.zipow.videobox.fragment.MMChatsListFragment;
import com.zipow.videobox.fragment.SettingFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.view.p014mm.MMContentFragment;
import com.zipow.videobox.view.sip.PhoneCallFragment;
import com.zipow.videobox.view.sip.PhonePBXTabFragment;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final int ITEM_ADDRBOOK = 0;
    public static final int ITEM_BUDDYLIST = 3;
    public static final int ITEM_CHATS = 6;
    public static final int ITEM_CONTENT = 7;
    public static final int ITEM_FAVORITELIST = 5;
    public static final int ITEM_MEETING = 2;
    public static final int ITEM_PBX = 9;
    public static final int ITEM_SETTINGS = 4;
    public static final int ITEM_SIP = 8;
    @NonNull
    private ArrayList<Fragment> mPages = new ArrayList<>();

    public IMViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        if (ResourcesUtil.getBoolean((Context) VideoBoxApplication.getInstance(), C4558R.bool.zm_config_use_4_pies_meeting_tab, false)) {
            this.mPages.add(new IMMeetingFragment());
        } else {
            this.mPages.add(new MMChatsListFragment());
        }
        if (CmmSIPCallManager.getInstance().isPBXActive()) {
            this.mPages.add(new PhonePBXTabFragment());
        } else if (!CmmSIPCallManager.getInstance().isCloudPBXEnabled() && CmmSIPCallManager.getInstance().isSipCallEnabled()) {
            this.mPages.add(new PhoneCallFragment());
        }
        if (PTApp.getInstance().getZoomProductHelper() != null) {
            this.mPages.add(new IMMyMeetingsFragment());
            if (PTApp.getInstance().hasZoomMessenger()) {
                this.mPages.add(new IMAddrBookListFragment());
            } else if (PTApp.getInstance().isCurrentLoginTypeSupportIM()) {
                this.mPages.add(new IMBuddyListFragment());
            }
            this.mPages.add(SettingFragment.createSettingFragment(true, false));
        }
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int i) {
        Fragment fragment = (Fragment) super.instantiateItem(viewGroup, i);
        if (fragment != getItem(i)) {
            this.mPages.set(i, fragment);
        }
        return fragment;
    }

    @Nullable
    public Fragment getItem(int i) {
        if (i >= this.mPages.size()) {
            return null;
        }
        return (Fragment) this.mPages.get(i);
    }

    @Nullable
    public Fragment getItemById(int i) {
        if (i == 0) {
            return getItemByType(IMAddrBookListFragment.class);
        }
        switch (i) {
            case 2:
                return getItemByType(IMMeetingFragment.class);
            case 3:
                return getItemByType(IMBuddyListFragment.class);
            case 4:
                return getItemByType(SettingFragment.class);
            case 5:
                return getItemByType(IMFavoriteListFragment.class);
            case 6:
                return getItemByType(MMChatsListFragment.class);
            case 7:
                return getItemByType(MMContentFragment.class);
            case 8:
                return getItemByType(PhoneCallFragment.class);
            case 9:
                return getItemByType(PhonePBXTabFragment.class);
            default:
                return null;
        }
    }

    private Fragment getItemByType(Class cls) {
        Iterator it = this.mPages.iterator();
        while (it.hasNext()) {
            Fragment fragment = (Fragment) it.next();
            if (fragment.getClass() == cls) {
                return fragment;
            }
        }
        return null;
    }

    public int getCount() {
        return this.mPages.size();
    }

    public void onConfigurationChanged(Configuration configuration) {
        Fragment itemById = getItemById(2);
        if (itemById != null && itemById.isAdded()) {
            notifyDataSetChanged();
        }
    }

    public int getItemPosition(@NonNull Object obj) {
        if (obj == getItemById(2)) {
            return -2;
        }
        boolean z = false;
        Iterator it = this.mPages.iterator();
        while (true) {
            if (it.hasNext()) {
                if (((Fragment) it.next()) == obj) {
                    z = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!z) {
            return -2;
        }
        return super.getItemPosition(obj);
    }

    public void clear() {
        this.mPages.clear();
    }
}
