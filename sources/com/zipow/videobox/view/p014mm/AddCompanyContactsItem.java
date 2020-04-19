package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import java.io.Serializable;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;

/* renamed from: com.zipow.videobox.view.mm.AddCompanyContactsItem */
public class AddCompanyContactsItem implements Serializable {
    private static final long serialVersionUID = 1;
    private String mAvatar;
    private boolean mChecked = false;
    private String mEmail;
    private String mJid;
    private String mScreenName;
    private String mSortKey;

    public AddCompanyContactsItem() {
    }

    public AddCompanyContactsItem(@Nullable ZoomBuddy zoomBuddy) {
        if (zoomBuddy != null) {
            setScreenName(zoomBuddy.getScreenName());
            setEmail(zoomBuddy.getEmail());
            setAvatar(zoomBuddy.getLocalPicturePath());
            setJid(zoomBuddy.getJid());
        }
    }

    public String getScreenName() {
        return this.mScreenName;
    }

    public void setScreenName(String str) {
        this.mScreenName = str;
        this.mSortKey = SortUtil.getSortKey(this.mScreenName, CompatUtils.getLocalDefault());
    }

    public String getEmail() {
        return this.mEmail;
    }

    public void setEmail(String str) {
        this.mEmail = str;
    }

    public String getAvatar() {
        return this.mAvatar;
    }

    public void setAvatar(String str) {
        this.mAvatar = str;
    }

    public String getJid() {
        return this.mJid;
    }

    public void setJid(String str) {
        this.mJid = str;
    }

    public String getSortKey() {
        return this.mSortKey;
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean z) {
        this.mChecked = z;
    }

    @Nullable
    public View getView(Context context, View view) {
        AddCompanyContactsItemView addCompanyContactsItemView;
        if (view instanceof AddCompanyContactsItemView) {
            addCompanyContactsItemView = (AddCompanyContactsItemView) view;
        } else {
            addCompanyContactsItemView = new AddCompanyContactsItemView(context);
        }
        bindView(addCompanyContactsItemView);
        return addCompanyContactsItemView;
    }

    private void bindView(AddCompanyContactsItemView addCompanyContactsItemView) {
        addCompanyContactsItemView.setFavoriteItem(this);
    }
}
