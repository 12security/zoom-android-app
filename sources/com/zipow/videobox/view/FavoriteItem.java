package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.FavoriteMgr;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomContact;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.io.Serializable;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class FavoriteItem implements Serializable {
    private static final long serialVersionUID = 1;
    private String mScreenName = "";
    private String mSortKey = "";
    private ZoomContact mZoomContact;

    public FavoriteItem() {
    }

    public FavoriteItem(ZoomContact zoomContact) {
        this.mZoomContact = zoomContact;
        String formatPersonName = StringUtil.formatPersonName(this.mZoomContact.getFirstName(), this.mZoomContact.getLastName(), PTApp.getInstance().getRegionCodeForNameFormating());
        if (formatPersonName.equals(getEmail())) {
            this.mScreenName = "";
        } else {
            this.mScreenName = formatPersonName;
        }
        if (StringUtil.isEmptyOrNull(this.mScreenName)) {
            this.mSortKey = SortUtil.getSortKey(getEmail(), CompatUtils.getLocalDefault());
        } else {
            this.mSortKey = SortUtil.getSortKey(this.mScreenName, CompatUtils.getLocalDefault());
        }
    }

    public ZoomContact getZoomContact() {
        return this.mZoomContact;
    }

    public String getScreenName() {
        return this.mScreenName;
    }

    public String getSortKey() {
        return this.mSortKey;
    }

    public String getEmail() {
        ZoomContact zoomContact = this.mZoomContact;
        if (zoomContact == null) {
            return "";
        }
        return zoomContact.getEmail();
    }

    public String getUserID() {
        ZoomContact zoomContact = this.mZoomContact;
        if (zoomContact == null) {
            return "";
        }
        return zoomContact.getUserID();
    }

    @Nullable
    public ParamsBuilder getAvatarParams() {
        String str = null;
        if (this.mZoomContact == null) {
            return null;
        }
        try {
            FavoriteMgr favoriteMgr = PTApp.getInstance().getFavoriteMgr();
            if (favoriteMgr != null) {
                str = favoriteMgr.getLocalPicturePath(this.mZoomContact.getEmail());
            }
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return new ParamsBuilder().setPath(str);
    }

    @Nullable
    public View getView(Context context, View view) {
        FavoriteItemView favoriteItemView;
        if (view instanceof FavoriteItemView) {
            favoriteItemView = (FavoriteItemView) view;
        } else {
            favoriteItemView = new FavoriteItemView(context);
        }
        bindView(favoriteItemView);
        return favoriteItemView;
    }

    private void bindView(FavoriteItemView favoriteItemView) {
        favoriteItemView.setFavoriteItem(this);
    }
}
