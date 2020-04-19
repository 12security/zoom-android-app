package com.box.androidsdk.content.auth;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.androidsdk.content.utils.BoxLogUtils;
import com.box.androidsdk.content.utils.SdkUtils;
import com.box.sdk.android.C0469R;
import java.util.List;

public class AuthenticatedAccountsAdapter extends ArrayAdapter<BoxAuthenticationInfo> {
    private static final int CREATE_NEW_TYPE_ID = 2;
    private static final int[] THUMB_COLORS = {-6381922, -10234140, -41121, -8465078, -5299724, -25001, -1752253, -10631001, -888412, -13733450, -1937604, -9007174, -11091626, -1061074, -11680004, -11528315, -1152974, -20195, -2195471};

    public static class DifferentAuthenticationInfo extends BoxAuthenticationInfo {
    }

    public static class ViewHolder {
        public TextView descriptionView;
        public TextView initialsView;
        public TextView titleView;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public AuthenticatedAccountsAdapter(Context context, int i, List<BoxAuthenticationInfo> list) {
        super(context, i, list);
    }

    public BoxAuthenticationInfo getItem(int i) {
        if (i == getCount() - 1) {
            return new DifferentAuthenticationInfo();
        }
        return (BoxAuthenticationInfo) super.getItem(i);
    }

    public int getItemViewType(int i) {
        if (i == getCount() - 1) {
            return 2;
        }
        return super.getItemViewType(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == 2) {
            return LayoutInflater.from(getContext()).inflate(C0469R.layout.boxsdk_list_item_new_account, viewGroup, false);
        }
        View inflate = LayoutInflater.from(getContext()).inflate(C0469R.layout.boxsdk_list_item_account, viewGroup, false);
        ViewHolder viewHolder = (ViewHolder) inflate.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) inflate.findViewById(C0469R.C0471id.box_account_title);
            viewHolder.descriptionView = (TextView) inflate.findViewById(C0469R.C0471id.box_account_description);
            viewHolder.initialsView = (TextView) inflate.findViewById(C0469R.C0471id.box_account_initials);
            inflate.setTag(viewHolder);
        }
        BoxAuthenticationInfo item = getItem(i);
        if (item != null && item.getUser() != null) {
            boolean z = !SdkUtils.isEmptyString(item.getUser().getName());
            viewHolder.titleView.setText(z ? item.getUser().getName() : item.getUser().getLogin());
            if (z) {
                viewHolder.descriptionView.setText(item.getUser().getLogin());
            }
            setColorsThumb(viewHolder.initialsView, i);
        } else if (item != null) {
            BoxLogUtils.m11e("invalid account info", item.toJson());
        }
        return inflate;
    }

    public int getCount() {
        return super.getCount() + 1;
    }

    @TargetApi(16)
    public void setColorsThumb(TextView textView, int i) {
        Drawable drawable = textView.getResources().getDrawable(C0469R.C0470drawable.boxsdk_thumb_background);
        int[] iArr = THUMB_COLORS;
        drawable.setColorFilter(iArr[i % iArr.length], Mode.MULTIPLY);
        textView.setBackground(drawable);
    }
}
