package p021us.zoom.androidlib.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.ClipboardManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;

/* renamed from: us.zoom.androidlib.app.ZMSendMessageFragment */
public class ZMSendMessageFragment extends ZMDialogFragment {
    public static final int APP_TYPE_ALL = -1;
    public static final int APP_TYPE_CLIPBOARD = 4;
    public static final int APP_TYPE_EMAIL = 1;
    public static final int APP_TYPE_SMS = 2;
    private static final String ARG_APP_TYPES = "appTypes";
    private static final String ARG_CHOOSER_TITLE = "chooserTitle";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_EMAIL = "email";
    private static final String ARG_EXT_ITEMS = "extItems";
    private static final String ARG_PHONE_NUMBERS = "phoneNumbers";
    private static final String ARG_SMS_CONTENT = "smsContent";
    private static final String ARG_STREAM = "stream";
    private static final String ARG_TOPIC = "topic";
    private static final String TAG = "ZMSendMessageFragment";
    /* access modifiers changed from: private */
    public AppListAdapter mAdapter;

    /* renamed from: us.zoom.androidlib.app.ZMSendMessageFragment$AppItem */
    static class AppItem {
        ResolveInfo appInfo = null;
        int appType = 0;

        AppItem(int i, ResolveInfo resolveInfo) {
            this.appType = i;
            this.appInfo = resolveInfo;
        }
    }

    /* renamed from: us.zoom.androidlib.app.ZMSendMessageFragment$AppListAdapter */
    static class AppListAdapter extends BaseAdapter {
        private ZMActivity mActivity;
        private ExtAppItem[] mExtItems;
        private List<AppItem> mList = new ArrayList();

        public long getItemId(int i) {
            return (long) i;
        }

        public AppListAdapter(ZMActivity zMActivity, int i, ExtAppItem[] extAppItemArr) {
            this.mActivity = zMActivity;
            this.mExtItems = extAppItemArr;
            if ((i & 1) != 0) {
                for (ResolveInfo appItem : AndroidAppUtil.queryEmailActivities(zMActivity)) {
                    this.mList.add(new AppItem(1, appItem));
                }
            }
            if ((i & 2) != 0) {
                for (ResolveInfo appItem2 : AndroidAppUtil.querySMSActivities(zMActivity)) {
                    this.mList.add(new AppItem(2, appItem2));
                }
            }
            if ((i & 4) != 0) {
                this.mList.add(new AppItem(4, null));
            }
        }

        public int getCount() {
            int size = this.mList.size();
            ExtAppItem[] extAppItemArr = this.mExtItems;
            return size + (extAppItemArr != null ? extAppItemArr.length : 0);
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0) {
                return null;
            }
            int i2 = 0;
            ExtAppItem[] extAppItemArr = this.mExtItems;
            if (extAppItemArr != null) {
                i2 = extAppItemArr.length;
                if (i < i2) {
                    return extAppItemArr[i];
                }
            }
            return this.mList.get(i - i2);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            if (view == null) {
                view = View.inflate(this.mActivity, C4409R.layout.zm_app_item, null);
            }
            ImageView imageView = (ImageView) view.findViewById(C4409R.C4411id.imgIcon);
            TextView textView = (TextView) view.findViewById(C4409R.C4411id.txtLabel);
            if (item instanceof AppItem) {
                AppItem appItem = (AppItem) item;
                if (appItem.appInfo != null) {
                    imageView.setVisibility(0);
                    imageView.setImageDrawable(AndroidAppUtil.getApplicationIcon((Context) this.mActivity, appItem.appInfo));
                    textView.setText(AndroidAppUtil.getApplicationLabel((Context) this.mActivity, appItem.appInfo));
                } else if (appItem.appType == 4) {
                    imageView.setVisibility(0);
                    imageView.setImageResource(C4409R.C4410drawable.zm_copy);
                    textView.setText(C4409R.string.zm_lbl_copy_to_clipboard);
                }
            } else if (item instanceof ExtAppItem) {
                ApplicationInfo applicationInfo = AndroidAppUtil.getApplicationInfo(this.mActivity, ((ExtAppItem) item).packageName);
                String applicationLabel = AndroidAppUtil.getApplicationLabel((Context) this.mActivity, applicationInfo);
                Drawable applicationIcon = AndroidAppUtil.getApplicationIcon((Context) this.mActivity, applicationInfo);
                imageView.setVisibility(0);
                imageView.setImageDrawable(applicationIcon);
                textView.setText(applicationLabel);
            }
            return view;
        }
    }

    /* renamed from: us.zoom.androidlib.app.ZMSendMessageFragment$ExtAppItem */
    public static class ExtAppItem implements Parcelable {
        public static final Creator<ExtAppItem> CREATOR = new Creator<ExtAppItem>() {
            public ExtAppItem createFromParcel(Parcel parcel) {
                return new ExtAppItem((Intent) Intent.CREATOR.createFromParcel(parcel), parcel.readString());
            }

            public ExtAppItem[] newArray(int i) {
                return new ExtAppItem[i];
            }
        };
        Intent intent;
        String packageName;

        public ExtAppItem(Intent intent2, String str) {
            this.intent = intent2;
            this.packageName = str;
        }

        public int describeContents() {
            Intent intent2 = this.intent;
            if (intent2 != null) {
                return intent2.describeContents();
            }
            return 0;
        }

        public void writeToParcel(Parcel parcel, int i) {
            if (parcel != null) {
                this.intent.writeToParcel(parcel, i);
                parcel.writeString(this.packageName);
            }
        }
    }

    public static void show(Context context, FragmentManager fragmentManager, String[] strArr, String[] strArr2, String str, String str2, String str3, String str4, String str5) {
        show(context, fragmentManager, strArr, strArr2, str, str2, str3, str4, str5, -1);
    }

    public static void show(Context context, FragmentManager fragmentManager, String[] strArr, String[] strArr2, String str, String str2, String str3, String str4, String str5, int i) {
        show(context, fragmentManager, strArr, strArr2, str, str2, str3, str4, str5, i, null);
    }

    public static void show(Context context, FragmentManager fragmentManager, String[] strArr, String[] strArr2, String str, String str2, String str3, String str4, String str5, int i, ExtAppItem[] extAppItemArr) {
        String[] strArr3 = strArr2;
        String str6 = str2;
        int i2 = i;
        List queryEmailActivities = AndroidAppUtil.queryEmailActivities(context);
        List querySMSActivities = AndroidAppUtil.querySMSActivities(context);
        int i3 = i2 & 1;
        int size = i3 != 0 ? queryEmailActivities.size() + 0 : 0;
        int i4 = i2 & 2;
        if (i4 != 0) {
            size += querySMSActivities.size();
        }
        if ((i2 & 4) == 0) {
            if (size != 0) {
                if (size == 1) {
                    if (i3 != 0 && queryEmailActivities.size() > 0) {
                        AndroidAppUtil.sendEmailVia((ResolveInfo) queryEmailActivities.get(0), context, strArr, str, str2, str4);
                    } else if (i4 != 0 && querySMSActivities.size() > 0) {
                        Context context2 = context;
                        AndroidAppUtil.sendSMSVia((ResolveInfo) querySMSActivities.get(0), context, strArr2, str2);
                    }
                    return;
                }
            } else {
                return;
            }
        }
        String str7 = StringUtil.isEmptyOrNull(str3) ? str6 : str3;
        Bundle bundle = new Bundle();
        String[] strArr4 = strArr;
        bundle.putStringArray("email", strArr);
        bundle.putStringArray(ARG_PHONE_NUMBERS, strArr2);
        String str8 = str;
        bundle.putString(ARG_TOPIC, str);
        bundle.putString("content", str2);
        bundle.putString(ARG_SMS_CONTENT, str7);
        bundle.putString(ARG_STREAM, str4);
        bundle.putString(ARG_CHOOSER_TITLE, str5);
        bundle.putInt(ARG_APP_TYPES, i2);
        bundle.putParcelableArray(ARG_EXT_ITEMS, extAppItemArr);
        ZMSendMessageFragment zMSendMessageFragment = new ZMSendMessageFragment();
        zMSendMessageFragment.setArguments(bundle);
        FragmentManager fragmentManager2 = fragmentManager;
        zMSendMessageFragment.show(fragmentManager, ZMSendMessageFragment.class.getName());
    }

    public ZMSendMessageFragment() {
        setCancelable(true);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return createEmptyDialog();
        }
        String string = arguments.getString(ARG_CHOOSER_TITLE);
        this.mAdapter = new AppListAdapter((ZMActivity) getActivity(), arguments.getInt(ARG_APP_TYPES), (ExtAppItem[]) arguments.getParcelableArray(ARG_EXT_ITEMS));
        ZMAlertDialog create = new Builder(getActivity()).setTitle((CharSequence) string).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ZMSendMessageFragment zMSendMessageFragment = ZMSendMessageFragment.this;
                zMSendMessageFragment.onClickItem(zMSendMessageFragment.mAdapter, i);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onStart() {
        super.onStart();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
    }

    /* access modifiers changed from: private */
    public void onClickItem(AppListAdapter appListAdapter, int i) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] stringArray = arguments.getStringArray("email");
            String[] stringArray2 = arguments.getStringArray(ARG_PHONE_NUMBERS);
            String string = arguments.getString(ARG_TOPIC);
            String string2 = arguments.getString("content");
            String string3 = arguments.getString(ARG_SMS_CONTENT);
            String string4 = arguments.getString(ARG_STREAM);
            Object item = this.mAdapter.getItem(i);
            FragmentActivity activity = getActivity();
            if (activity != null) {
                if (item instanceof AppItem) {
                    AppItem appItem = (AppItem) item;
                    if (appItem.appType == 1) {
                        AndroidAppUtil.sendEmailVia(appItem.appInfo, activity, stringArray, string, string2, string4);
                    } else if (appItem.appType == 2) {
                        AndroidAppUtil.sendSMSVia(appItem.appInfo, activity, stringArray2, string3);
                    } else if (appItem.appType == 4) {
                        copyToClipboard(string2);
                    }
                } else if (item instanceof ExtAppItem) {
                    try {
                        activity.startActivity(((ExtAppItem) item).intent);
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    private void copyToClipboard(String str) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService("clipboard");
            if (clipboardManager != null) {
                clipboardManager.setText(str);
            }
        }
    }
}
