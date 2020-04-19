package com.zipow.videobox.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.Arrays;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class DialogUtils {
    public static boolean isCanShowDialog(@Nullable ZMActivity zMActivity) {
        return zMActivity != null && !zMActivity.isFinishing();
    }

    @Nullable
    public static View createAvatarDialogTitleView(@Nullable Context context, CharSequence charSequence, @Nullable Object obj) {
        if (context == null || TextUtils.isEmpty(charSequence)) {
            return null;
        }
        View inflate = View.inflate(context, C4558R.layout.zm_avatar_dialog_header, null);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtName)).setText(charSequence);
        AvatarView avatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        if (obj != null) {
            ParamsBuilder paramsBuilder = new ParamsBuilder();
            if (obj instanceof Integer) {
                paramsBuilder.setResource(((Integer) obj).intValue(), null);
            } else {
                paramsBuilder.setPath(obj.toString());
            }
            avatarView.show(paramsBuilder);
        } else {
            avatarView.setVisibility(8);
        }
        return inflate;
    }

    @Nullable
    public static View createListViewDialogTitleView(@Nullable Context context, String[] strArr, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        View inflate = View.inflate(context, C4558R.layout.zm_listview_dialog_header, null);
        ListView listView = (ListView) inflate.findViewById(C4558R.C4560id.headSmallTitles);
        ((TextView) inflate.findViewById(C4558R.C4560id.txtName)).setText(str);
        listView.setAdapter(new ArrayAdapter(context, C4558R.layout.zm_sip_dialog_title_list_item, Arrays.asList(strArr)));
        return inflate;
    }

    public static void showAlertDialog(ZMActivity zMActivity, int i, int i2, int i3) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle(i).setMessage(i2).setPositiveButton(i3, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, String str, String str2, int i, int i2, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle((CharSequence) str).setMessage(str2).setPositiveButton(i, onClickListener).setNegativeButton(i2, (OnClickListener) null).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, int i, int i2) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle(i).setPositiveButton(i2, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, String str, int i) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle((CharSequence) str).setPositiveButton(i, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, String str, String str2, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle((CharSequence) str).setPositiveButton(str2, onClickListener).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, int i, int i2, int i3, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle(i).setPositiveButton(i2, onClickListener).setNegativeButton(i3, (OnClickListener) null).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, int i, int i2, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle(i).setPositiveButton(i2, onClickListener).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, int i, int i2, boolean z, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle(i).setPositiveButton(i2, onClickListener).setCancelable(z).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, int i, int i2, int i3, boolean z, OnClickListener onClickListener, OnClickListener onClickListener2) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle(i).setPositiveButton(i2, onClickListener).setNegativeButton(i3, onClickListener2).setCancelable(z).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, String str, String str2, int i, int i2, boolean z, OnClickListener onClickListener, OnClickListener onClickListener2) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle((CharSequence) str).setMessage(str2).setPositiveButton(i, onClickListener).setNegativeButton(i2, onClickListener2).setCancelable(z).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, String str, String str2, int i, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle((CharSequence) str).setMessage(str2).setPositiveButton(i, onClickListener).create().show();
        }
    }

    public static void showAlertDialog(ZMActivity zMActivity, String str, CharSequence[] charSequenceArr, OnClickListener onClickListener) {
        if (isCanShowDialog(zMActivity)) {
            new Builder(zMActivity).setTitle((CharSequence) str).setSingleChoiceItems(charSequenceArr, -1, onClickListener).create().show();
        }
    }

    public static ZMAlertDialog createVerticalActionDialog(Activity activity, String str, String str2, String str3, View.OnClickListener onClickListener, String str4, View.OnClickListener onClickListener2, String str5, View.OnClickListener onClickListener3) {
        View inflate = activity.getLayoutInflater().inflate(C4558R.layout.zm_vertical_action_dialog, null);
        ((TextView) inflate.findViewById(C4558R.C4560id.title)).setText(str);
        ((TextView) inflate.findViewById(C4558R.C4560id.msg)).setText(str2);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.btnPositive);
        textView.setText(str3);
        textView.setOnClickListener(onClickListener);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.btnNeutral);
        textView2.setText(str4);
        textView2.setOnClickListener(onClickListener2);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.btnNegative);
        textView3.setText(str5);
        textView3.setOnClickListener(onClickListener3);
        Builder builder = new Builder(activity);
        builder.setView(inflate);
        return builder.create();
    }
}
