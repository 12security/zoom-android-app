package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.videomeetings.C4558R;

public class MMAddBuddyNewFragment extends ZMDialogFragment implements OnClickListener {
    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(fragment, MMAddBuddyNewFragment.class.getName(), bundle, 0, true, 1);
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment, boolean z) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(fragment, MMAddBuddyNewFragment.class.getName(), bundle, 0, z, 1);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_add_buddy_new, viewGroup, false);
        inflate.findViewById(C4558R.C4560id.btnCancel).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.edtSearch).setOnClickListener(this);
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnCancel) {
            onClickBtnCancel();
        } else if (id == C4558R.C4560id.edtSearch) {
            onClickEdtSearch();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    private void onClickBtnCancel() {
        dismiss();
    }

    private void onClickEdtSearch() {
        MMAddBuddySearchFragment.showAsActivity(this);
        finishFragment(false);
    }
}
