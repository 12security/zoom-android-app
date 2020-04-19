package p021us.zoom.androidlib.widget.segement;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: us.zoom.androidlib.widget.segement.FragmentChangeManager */
public class FragmentChangeManager {
    private int mContainerViewId;
    private int mCurrentTab;
    private FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;

    public FragmentChangeManager(FragmentManager fragmentManager, int i, ArrayList<Fragment> arrayList) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = i;
        this.mFragments = arrayList;
        initFragments();
    }

    private void initFragments() {
        Iterator it = this.mFragments.iterator();
        while (it.hasNext()) {
            Fragment fragment = (Fragment) it.next();
            this.mFragmentManager.beginTransaction().add(this.mContainerViewId, fragment).hide(fragment).commit();
        }
        setFragments(0);
    }

    public void setFragments(int i) {
        for (int i2 = 0; i2 < this.mFragments.size(); i2++) {
            FragmentTransaction beginTransaction = this.mFragmentManager.beginTransaction();
            Fragment fragment = (Fragment) this.mFragments.get(i2);
            if (i2 == i) {
                beginTransaction.show(fragment);
            } else {
                beginTransaction.hide(fragment);
            }
            beginTransaction.commit();
        }
        this.mCurrentTab = i;
    }

    public int getCurrentTab() {
        return this.mCurrentTab;
    }

    public Fragment getCurrentFragment() {
        return (Fragment) this.mFragments.get(this.mCurrentTab);
    }
}
