package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.zipow.videobox.fragment.MMSelectContactsFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import java.io.Serializable;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class MMSelectContactsActivity extends ZMActivity {
    public static final String RESULT_ARG_SELECTED_ITEMS = "selectedItems";
    private boolean mIsOK = false;

    public static class SelectContactsParamter implements Serializable {
        private static final long serialVersionUID = 1;
        public String btnOkText;
        public boolean canSelectNothing;
        @Nullable
        public String groupId;
        public boolean includeMe;
        public boolean includeRobot = true;
        @Nullable
        public String instructionMessage;
        public boolean isAcceptNoSestion;
        public boolean isAlternativeHost;
        public boolean isAnimBottomTop;
        public boolean isContainsAllInGroup = true;
        public boolean isForwardResult;
        public boolean isOnlySameOrganization;
        public boolean isSingleChoice;
        public boolean mFilterZoomRooms;
        public String maxCountMessage;
        public int maxSelectCount;
        public int minSelectCount;
        public boolean onlyRobot;
        @Nullable
        public ArrayList<String> preSelectedItems;
        public String title;
    }

    public static void show(Activity activity, String str, ArrayList<String> arrayList, String str2, String str3, int i) {
        show(activity, str, arrayList, str2, str3, false, (Bundle) null, false, i, true, (String) null, false);
    }

    public static void show(Activity activity, String str, ArrayList<String> arrayList, String str2, String str3, int i, int i2) {
        show(activity, str, arrayList, str2, str3, false, (Bundle) null, false, i, true, (String) null, false, i2, false, false);
    }

    public static void show(Activity activity, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3) {
        show(activity, str, arrayList, str2, str3, z, bundle, z2, i, z3, (String) null, false);
    }

    public static void show(Activity activity, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4, boolean z4) {
        show(activity, str, arrayList, str2, str3, z, bundle, z2, i, z3, str4, z4, -1, false, false);
    }

    public static void show(Activity activity, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4, boolean z4, int i2) {
        show(activity, str, arrayList, str2, str3, z, bundle, z2, i, z3, str4, z4, i2, false, false);
    }

    public static void showOnlyRobot(Activity activity, String str, String str2, int i, boolean z, String str3) {
        show(activity, str, null, str2, (String) null, false, (Bundle) null, true, i, z, str3, false, -1, false, true);
    }

    public static void showOnlyRobot(Fragment fragment, String str, String str2, int i, boolean z, String str3) {
        show(fragment, str, null, str2, (String) null, false, (Bundle) null, true, i, z, str3, false, -1, false, true);
    }

    public static void show(Activity activity, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4, boolean z4, int i2, boolean z5, boolean z6) {
        SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
        selectContactsParamter.title = str;
        selectContactsParamter.preSelectedItems = arrayList;
        selectContactsParamter.btnOkText = str2;
        selectContactsParamter.instructionMessage = str3;
        selectContactsParamter.isForwardResult = z;
        selectContactsParamter.isSingleChoice = z2;
        selectContactsParamter.isAnimBottomTop = z3;
        selectContactsParamter.groupId = str4;
        selectContactsParamter.isOnlySameOrganization = z4;
        selectContactsParamter.maxSelectCount = i2;
        selectContactsParamter.isAcceptNoSestion = z5;
        selectContactsParamter.onlyRobot = z6;
        show(activity, selectContactsParamter, i, bundle);
    }

    public static void show(@Nullable Activity activity, @Nullable SelectContactsParamter selectContactsParamter, int i, @Nullable Bundle bundle) {
        if (activity != null && selectContactsParamter != null) {
            Intent intent = new Intent(activity, MMSelectContactsActivity.class);
            intent.putExtra(MMSelectContactsFragment.ARG_PARAMTERS, selectContactsParamter);
            if (bundle != null) {
                intent.putExtra("resultData", bundle);
            }
            if (selectContactsParamter.isForwardResult) {
                intent.addFlags(MediaHttpDownloader.MAXIMUM_CHUNK_SIZE);
                ActivityStartHelper.startActivityForeground(activity, intent);
            } else {
                ActivityStartHelper.startActivityForResult(activity, intent, i);
            }
            if (selectContactsParamter.isAnimBottomTop) {
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
            } else {
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }

    public static void show(Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, int i) {
        show(fragment, str, arrayList, str2, str3, false, (Bundle) null, false, i, true, (String) null, false);
    }

    public static void show(Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, int i, int i2) {
        show(fragment, str, arrayList, str2, str3, false, (Bundle) null, false, i, true, (String) null, false, i2, false, false);
    }

    public static void show(Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3) {
        show(fragment, str, arrayList, str2, str3, z, bundle, z2, i, z3, (String) null, false);
    }

    public static void show(Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4) {
        show(fragment, str, arrayList, str2, str3, z, bundle, z2, i, z3, str4, false, -1, false, false);
    }

    public static void show(Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4, boolean z4) {
        show(fragment, str, arrayList, str2, str3, z, bundle, z2, i, z3, str4, z4, -1, false, false);
    }

    public static void show(Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4, boolean z4, int i2) {
        show(fragment, str, arrayList, str2, str3, z, bundle, z2, i, z3, str4, z4, i2, false, false);
    }

    public static void show(@Nullable Fragment fragment, String str, ArrayList<String> arrayList, String str2, String str3, boolean z, Bundle bundle, boolean z2, int i, boolean z3, String str4, boolean z4, int i2, boolean z5, boolean z6) {
        if (fragment != null) {
            SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
            selectContactsParamter.title = str;
            selectContactsParamter.preSelectedItems = arrayList;
            selectContactsParamter.btnOkText = str2;
            selectContactsParamter.instructionMessage = str3;
            selectContactsParamter.isForwardResult = z;
            selectContactsParamter.isSingleChoice = z2;
            selectContactsParamter.isAnimBottomTop = z3;
            selectContactsParamter.groupId = str4;
            selectContactsParamter.isOnlySameOrganization = z4;
            selectContactsParamter.maxSelectCount = i2;
            selectContactsParamter.isAcceptNoSestion = z5;
            selectContactsParamter.onlyRobot = z6;
            if (z6) {
                selectContactsParamter.isContainsAllInGroup = false;
            }
            show(fragment, selectContactsParamter, i, bundle);
        }
    }

    public static void show(@Nullable Fragment fragment, @Nullable SelectContactsParamter selectContactsParamter, int i, @Nullable Bundle bundle) {
        if (fragment != null && selectContactsParamter != null) {
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent(zMActivity, MMSelectContactsActivity.class);
                intent.putExtra(MMSelectContactsFragment.ARG_PARAMTERS, selectContactsParamter);
                if (bundle != null) {
                    intent.putExtra("resultData", bundle);
                }
                if (selectContactsParamter.isForwardResult) {
                    intent.addFlags(MediaHttpDownloader.MAXIMUM_CHUNK_SIZE);
                    ActivityStartHelper.startActivityForeground(zMActivity, intent);
                } else {
                    ActivityStartHelper.startActivityForResult(fragment, intent, i);
                }
                if (selectContactsParamter.isAnimBottomTop) {
                    zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
                } else {
                    zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
                }
            }
        }
    }

    public void finish() {
        super.finish();
        Intent intent = getIntent();
        if (intent != null) {
            SelectContactsParamter selectContactsParamter = (SelectContactsParamter) intent.getSerializableExtra(MMSelectContactsFragment.ARG_PARAMTERS);
            if (selectContactsParamter == null || selectContactsParamter.isAnimBottomTop) {
                overridePendingTransition(0, C4558R.anim.zm_slide_out_bottom);
            } else if (this.mIsOK) {
                overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            } else {
                overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
            }
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        getWindow().addFlags(2097280);
        if (bundle == null) {
            Intent intent = getIntent();
            if (intent != null) {
                SelectContactsParamter selectContactsParamter = (SelectContactsParamter) intent.getSerializableExtra(MMSelectContactsFragment.ARG_PARAMTERS);
                MMSelectContactsFragment.showInActivity(this, selectContactsParamter, intent.getBundleExtra("resultData"));
                if (selectContactsParamter != null && selectContactsParamter.isAnimBottomTop) {
                    disableFinishActivityByGesture(true);
                }
            }
        }
    }

    public void onSelectContactsDone(ArrayList<IMAddrBookItem> arrayList, @Nullable Bundle bundle) {
        this.mIsOK = true;
        Intent intent = new Intent();
        intent.putExtra("selectedItems", arrayList);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(-1, intent);
        finish();
    }

    public boolean onSearchRequested() {
        MMSelectContactsFragment mMSelectContactsFragment = (MMSelectContactsFragment) getSupportFragmentManager().findFragmentByTag(MMSelectContactsFragment.class.getName());
        if (mMSelectContactsFragment != null) {
            return mMSelectContactsFragment.onSearchRequested();
        }
        return true;
    }
}
