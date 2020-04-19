package p021us.zoom.thirdparty.login;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.thirdparty.dialog.NoBrowserDialog;
import p021us.zoom.thirdparty.login.util.CustomTabsHelper;
import p021us.zoom.thirdparty.login.util.CustomTabsIntent;

/* renamed from: us.zoom.thirdparty.login.ThirdPartyLogin */
public abstract class ThirdPartyLogin {
    protected final Bundle bundle;

    public abstract void login(@NonNull Activity activity, List<String> list);

    public abstract boolean logout(@NonNull Context context);

    public ThirdPartyLogin(Bundle bundle2) {
        this.bundle = bundle2;
    }

    /* access modifiers changed from: protected */
    public void loginBrowser(@NonNull Activity activity, List<String> list, @NonNull Uri uri) {
        if (list != null && list.size() > 0) {
            List queryAllActivitiesForIntent = ZMIntentUtil.queryAllActivitiesForIntent(activity, new Intent("android.intent.action.VIEW", uri));
            for (String str : list) {
                Iterator it = queryAllActivitiesForIntent.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (str.equals(((ResolveInfo) it.next()).activityInfo.packageName)) {
                            Intent intent = new Intent("android.intent.action.VIEW", uri);
                            intent.addFlags(268435456);
                            intent.addCategory("android.intent.category.BROWSABLE");
                            intent.setPackage(str);
                            try {
                                activity.startActivity(intent);
                                activity.overridePendingTransition(C4538R.anim.zm_slide_in_right, C4538R.anim.zm_slide_out_left);
                            } catch (Exception unused) {
                            }
                            return;
                        }
                    }
                }
            }
        }
        Intent customTabIntentWithNoSession = CustomTabsHelper.getCustomTabIntentWithNoSession(activity, uri);
        int i = 1;
        if (customTabIntentWithNoSession != null) {
            customTabIntentWithNoSession.putExtra(CustomTabsIntent.EXTRA_TOOLBAR_COLOR, activity.getResources().getColor(C4538R.color.zm_customtab_titlebar_bg));
            customTabIntentWithNoSession.putExtra(CustomTabsIntent.EXTRA_CLOSE_BUTTON_ICON, BitmapFactory.decodeResource(activity.getResources(), C4538R.C4539drawable.zm_customtab_arrow_back));
            customTabIntentWithNoSession.putExtra(CustomTabsIntent.EXTRA_TITLE_VISIBILITY_STATE, 1);
            customTabIntentWithNoSession.putExtra(CustomTabsIntent.EXTRA_EXIT_ANIMATION_BUNDLE, ActivityOptionsCompat.makeCustomAnimation(activity, C4538R.anim.zm_slide_in_left, C4538R.anim.zm_slide_out_right).toBundle());
        } else {
            customTabIntentWithNoSession = new Intent("android.intent.action.VIEW", uri);
            customTabIntentWithNoSession.addFlags(268435456);
            customTabIntentWithNoSession.addCategory("android.intent.category.BROWSABLE");
        }
        try {
            activity.startActivity(customTabIntentWithNoSession);
            activity.overridePendingTransition(C4538R.anim.zm_slide_in_right, C4538R.anim.zm_slide_out_left);
        } catch (ActivityNotFoundException unused2) {
            if (activity instanceof FragmentActivity) {
                FragmentManager supportFragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                String name = NoBrowserDialog.class.getName();
                if (ZMIntentUtil.hasAppStore(activity)) {
                    i = 2;
                }
                NoBrowserDialog.showDialog(supportFragmentManager, name, i);
            }
        } catch (Exception unused3) {
        }
    }
}
