package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;

public interface IZMListItemView {

    public interface IAction {
        public static final int ACTION_HANGUP = 2;
        public static final int ACTION_JOIN_MEETING = 4;
        public static final int ACTION_MERGE_CALL = 1;
        public static final int ACTION_RESUME = 3;
    }

    public interface IActionClickListener {
        void onAction(String str, int i);
    }

    @Nullable
    View getView(Context context, int i, View view, ViewGroup viewGroup, IActionClickListener iActionClickListener);
}
