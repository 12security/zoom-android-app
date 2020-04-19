package com.zipow.videobox.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.IMAddrBookItem;
import java.io.File;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class AvatarPreviewFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final String ARG_AVATAR_BY_CONTACT = "avatarIsFromContact";
    private static final String ARG_CONTACT = "contact";
    private boolean mAvatarFromContact = false;
    @Nullable
    private IMAddrBookItem mContact = null;
    private ImageView mImageView;
    private IZoomMessengerUIListener mZoomMessengerUIListener;

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showContactAvatar(@Nullable Fragment fragment, @Nullable IMAddrBookItem iMAddrBookItem) {
        if (fragment != null && iMAddrBookItem != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_CONTACT, iMAddrBookItem);
            bundle.putBoolean(ARG_AVATAR_BY_CONTACT, true);
            SimpleActivity.show(fragment, AvatarPreviewFragment.class.getName(), bundle, 0);
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                zMActivity.overridePendingTransition(0, 0);
            }
        }
    }

    public static void showMyAvatar(@Nullable Fragment fragment) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(ARG_AVATAR_BY_CONTACT, false);
            SimpleActivity.show(fragment, AvatarPreviewFragment.class.getName(), bundle, 0);
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                zMActivity.overridePendingTransition(0, 0);
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_avatar_original, viewGroup, false);
        if (inflate == null) {
            return null;
        }
        inflate.setOnClickListener(this);
        this.mImageView = (ImageView) inflate.findViewById(C4558R.C4560id.imgAvator);
        if (this.mImageView == null) {
            return null;
        }
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mAvatarFromContact = arguments.getBoolean(ARG_AVATAR_BY_CONTACT);
            if (this.mAvatarFromContact) {
                this.mContact = (IMAddrBookItem) arguments.getSerializable(ARG_CONTACT);
            }
        }
        return inflate;
    }

    public void onPause() {
        super.onPause();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mZoomMessengerUIListener == null) {
            this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                public void onIndicateInfoUpdatedWithJID(String str) {
                    AvatarPreviewFragment.this.onIndicateInfoUpdatedWithJID(str);
                }

                public void onIndicate_BuddyBigPictureDownloaded(String str, int i) {
                    AvatarPreviewFragment.this.onIndicateBuddyBigPicUpdated(str);
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onIndicateInfoUpdatedWithJID(String str) {
        if (this.mAvatarFromContact) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem != null && StringUtil.isSameString(str, iMAddrBookItem.getJid())) {
                updateUI();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyBigPicUpdated(String str) {
        if (this.mAvatarFromContact) {
            IMAddrBookItem iMAddrBookItem = this.mContact;
            if (iMAddrBookItem != null && StringUtil.isSameString(str, iMAddrBookItem.getJid())) {
                updateUI();
            }
        }
    }

    private void updateUI() {
        Bitmap bitmap;
        if (this.mAvatarFromContact) {
            bitmap = getAvatarBitmapFromContact();
        } else {
            String str = null;
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                str = currentUserProfile.getPictureLocalPath();
            }
            bitmap = ZMBitmapFactory.decodeFile(str);
        }
        if (bitmap != null) {
            this.mImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap getAvatarBitmapFromContact() {
        if (this.mContact == null) {
            return null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.mContact.getJid());
            String localBigPicturePath = buddyWithJID != null ? buddyWithJID.getLocalBigPicturePath() : null;
            if (ImageUtil.isValidImageFile(localBigPicturePath)) {
                Bitmap decodeFile = ZMBitmapFactory.decodeFile(localBigPicturePath);
                if (decodeFile != null) {
                    return decodeFile;
                }
            } else {
                if (!StringUtil.isEmptyOrNull(localBigPicturePath)) {
                    File file = new File(localBigPicturePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                if (buddyWithJID != null) {
                    localBigPicturePath = buddyWithJID.getLocalPicturePath();
                }
                if (ImageUtil.isValidImageFile(localBigPicturePath)) {
                    Bitmap decodeFile2 = ZMBitmapFactory.decodeFile(localBigPicturePath);
                    if (decodeFile2 != null) {
                        return decodeFile2;
                    }
                }
            }
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return this.mContact.getAvatarBitmap(activity);
        }
        return null;
    }

    public boolean onBackPressed() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return false;
        }
        activity.finish();
        activity.overridePendingTransition(0, 0);
        return true;
    }

    public void onClick(View view) {
        dismiss();
    }

    public void dismiss() {
        finishFragment(false);
    }
}
