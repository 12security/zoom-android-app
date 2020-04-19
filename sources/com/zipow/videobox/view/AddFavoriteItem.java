package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ZoomContact;

public class AddFavoriteItem extends FavoriteItem {
    private static final long serialVersionUID = 1;
    private boolean mChecked = false;

    public AddFavoriteItem() {
    }

    public AddFavoriteItem(ZoomContact zoomContact) {
        super(zoomContact);
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean z) {
        this.mChecked = z;
    }

    @Nullable
    public View getView(Context context, View view) {
        AddFavoriteItemView addFavoriteItemView;
        if (view instanceof AddFavoriteItemView) {
            addFavoriteItemView = (AddFavoriteItemView) view;
        } else {
            addFavoriteItemView = new AddFavoriteItemView(context);
        }
        bindView(addFavoriteItemView);
        return addFavoriteItemView;
    }

    private void bindView(AddFavoriteItemView addFavoriteItemView) {
        addFavoriteItemView.setFavoriteItem(this);
    }
}
