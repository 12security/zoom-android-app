package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMShareZoomFileDialogFragment */
public class MMShareZoomFileDialogFragment extends DialogFragment {
    private static final String ARG_SESSION_ID = "sessionid";
    private static final String ARG_SHAREE = "sharee";
    private static final String ARG_XMPP_ID = "xmppid";
    private static final String ARG_fILE_ID = "fileId";
    public static final String RESULT_ARG_REQUEST_ID = "reqId";

    public static void showShareFileDialog(@Nullable FragmentManager fragmentManager, @Nullable ArrayList<String> arrayList, String str, String str2, String str3, @Nullable Fragment fragment, int i) {
        if (fragmentManager != null && arrayList != null && arrayList.size() != 0 && (!StringUtil.isEmptyOrNull(str) || (!StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str3)))) {
            MMShareZoomFileDialogFragment mMShareZoomFileDialogFragment = new MMShareZoomFileDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_fILE_ID, str);
            bundle.putStringArrayList(ARG_SHAREE, arrayList);
            bundle.putString(ARG_XMPP_ID, str2);
            bundle.putString(ARG_SESSION_ID, str3);
            mMShareZoomFileDialogFragment.setArguments(bundle);
            if (fragment != null) {
                mMShareZoomFileDialogFragment.setTargetFragment(fragment, i);
            }
            mMShareZoomFileDialogFragment.show(fragmentManager, MMShareZoomFileDialogFragment.class.getName());
        }
    }

    public static void showShareFileDialog(FragmentManager fragmentManager, ArrayList<String> arrayList, String str, Fragment fragment, int i) {
        showShareFileDialog(fragmentManager, arrayList, str, "", "", fragment, i);
    }

    public static void showShareFileDialog(FragmentManager fragmentManager, ArrayList<String> arrayList, String str) {
        showShareFileDialog(fragmentManager, arrayList, str, null, 0);
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        ArrayList arrayList;
        CharSequence charSequence;
        Bundle arguments = getArguments();
        if (arguments != null) {
            arrayList = arguments.getStringArrayList(ARG_SHAREE);
            str = arguments.getString(ARG_fILE_ID);
        } else {
            str = null;
            arrayList = null;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        StringBuffer stringBuffer = new StringBuffer();
        if (!(arrayList == null || zoomMessenger == null)) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                String str2 = (String) it.next();
                ZoomGroup groupById = zoomMessenger.getGroupById(str2);
                if (groupById != null) {
                    String groupDisplayName = groupById.getGroupDisplayName(getActivity());
                    if (!StringUtil.isEmptyOrNull(groupDisplayName)) {
                        stringBuffer.append(groupDisplayName);
                        stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                } else {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str2);
                    if (buddyWithJID != null) {
                        String screenName = buddyWithJID.getScreenName();
                        if (!StringUtil.isEmptyOrNull(screenName)) {
                            stringBuffer.append(screenName);
                            stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                        }
                    }
                }
            }
        }
        String str3 = "";
        if (stringBuffer.length() > 0) {
            str3 = stringBuffer.substring(0, stringBuffer.length() - 1);
        }
        String string = getString(C4558R.string.zm_lbl_content_send_to, str3);
        if (!StringUtil.isEmptyOrNull(str)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(str);
                if (fileWithWebFileID != null) {
                    charSequence = fileWithWebFileID.getFileName();
                    zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                    return new Builder(getActivity()).setTitle(charSequence).setMessage(string).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MMShareZoomFileDialogFragment.this.doShareFile();
                        }
                    }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
                }
            }
        }
        charSequence = null;
        return new Builder(getActivity()).setTitle(charSequence).setMessage(string).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMShareZoomFileDialogFragment.this.doShareFile();
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public void doShareFile() {
        String str;
        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList stringArrayList = arguments.getStringArrayList(ARG_SHAREE);
            String string = arguments.getString(ARG_fILE_ID);
            String string2 = arguments.getString(ARG_SESSION_ID);
            String string3 = arguments.getString(ARG_XMPP_ID);
            if (stringArrayList != null && stringArrayList.size() != 0) {
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null) {
                    if (!TextUtils.isEmpty(string2) && !TextUtils.isEmpty(string3)) {
                        str = zoomFileContentMgr.forwardFileMessage(string2, string3, (String) stringArrayList.get(0));
                    } else if (!TextUtils.isEmpty(string)) {
                        ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(string);
                        if (fileWithWebFileID != null) {
                            String shareFile = zoomFileContentMgr.shareFile(string, (String) stringArrayList.get(0));
                            zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
                            str = shareFile;
                        } else {
                            return;
                        }
                    } else {
                        str = null;
                    }
                    if (StringUtil.isEmptyOrNull(str)) {
                        Fragment targetFragment = getTargetFragment();
                        if (targetFragment != null) {
                            targetFragment.onActivityResult(getTargetRequestCode(), 0, null);
                        }
                        ErrorMsgDialog.newInstance(getString(C4558R.string.zm_alert_share_file_failed), -1).show(getFragmentManager(), ErrorMsgDialog.class.getName());
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("reqId", str);
                        Fragment targetFragment2 = getTargetFragment();
                        if (targetFragment2 != null) {
                            targetFragment2.onActivityResult(getTargetRequestCode(), -1, intent);
                        }
                    }
                }
            }
        }
    }
}
