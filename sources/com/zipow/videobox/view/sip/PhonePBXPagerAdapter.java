package com.zipow.videobox.view.sip;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.zipow.videobox.sip.server.CmmSIPMessageManager;
import java.util.ArrayList;

public class PhonePBXPagerAdapter extends FragmentStatePagerAdapter {
    public static final int ITEM_HISTORY = 0;
    public static final int ITEM_SHARE_LINE = 2;
    public static final int ITEM_SMS = 3;
    public static final int ITEM_VOICEMAIL = 1;
    @NonNull
    private ArrayList<Fragment> mPages = new ArrayList<>(4);

    public PhonePBXPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        int[] iArr = {0, 1, 2, 3};
        for (int i : iArr) {
            switch (i) {
                case 0:
                    this.mPages.add(new PhonePBXHistoryFragment());
                    break;
                case 1:
                    this.mPages.add(new PhonePBXVoiceMailFragment());
                    break;
                case 2:
                    this.mPages.add(new PhonePBXLinesFragment());
                    break;
                case 3:
                    if (!CmmSIPMessageManager.getInstance().isMessageEnabled()) {
                        break;
                    } else {
                        this.mPages.add(new PhonePBXSmsFragment());
                        break;
                    }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0026  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean checkPages(boolean r5) {
        /*
            r4 = this;
            r0 = 3
            androidx.fragment.app.Fragment r1 = r4.getItem(r0)
            r2 = 0
            r3 = 1
            if (r1 == 0) goto L_0x000b
            r1 = 1
            goto L_0x000c
        L_0x000b:
            r1 = 0
        L_0x000c:
            if (r1 == 0) goto L_0x0016
            if (r5 != 0) goto L_0x0023
            java.util.ArrayList<androidx.fragment.app.Fragment> r5 = r4.mPages
            r5.remove(r0)
            goto L_0x0024
        L_0x0016:
            if (r5 == 0) goto L_0x0023
            java.util.ArrayList<androidx.fragment.app.Fragment> r5 = r4.mPages
            com.zipow.videobox.view.sip.PhonePBXSmsFragment r0 = new com.zipow.videobox.view.sip.PhonePBXSmsFragment
            r0.<init>()
            r5.add(r0)
            goto L_0x0024
        L_0x0023:
            r3 = 0
        L_0x0024:
            if (r3 == 0) goto L_0x0029
            r4.notifyDataSetChanged()
        L_0x0029:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.sip.PhonePBXPagerAdapter.checkPages(boolean):boolean");
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

    public int getCount() {
        return this.mPages.size();
    }

    public void clear() {
        this.mPages.clear();
    }
}
