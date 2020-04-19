package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import java.util.Collections;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ContentFileDeleteDialogFragment */
public class ContentFileDeleteDialogFragment extends ZMDialogFragment {
    private static final String ARG_FILE_ID = "fileId";
    private static final String ARG_SESSION_ID = "sessionId";
    private static final String ARG_SESSION_NAME = "sessionName";
    private static final String TAG = "ContentFileDeleteDialogFragment";
    public static final String UNSHARE_FILE_RESULT_ID_CODE = "unshare_file_result_id";
    /* access modifiers changed from: private */
    @Nullable
    public String mFileId;
    /* access modifiers changed from: private */
    @Nullable
    public String mSessionId;
    @Nullable
    private String mSessionName;

    public static void showUnshareAlertDialog(@Nullable FragmentManager fragmentManager, @Nullable Fragment fragment, int i, String str, String str2, String str3) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            ContentFileDeleteDialogFragment contentFileDeleteDialogFragment = new ContentFileDeleteDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_FILE_ID, str);
            bundle.putString("sessionId", str2);
            bundle.putString(ARG_SESSION_NAME, str3);
            contentFileDeleteDialogFragment.setArguments(bundle);
            if (fragment != null) {
                contentFileDeleteDialogFragment.setTargetFragment(fragment, i);
            }
            contentFileDeleteDialogFragment.show(fragmentManager, ContentFileDeleteDialogFragment.class.getName());
        }
    }

    public ContentFileDeleteDialogFragment() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFileId = arguments.getString(ARG_FILE_ID);
            this.mSessionId = arguments.getString("sessionId");
            this.mSessionName = arguments.getString(ARG_SESSION_NAME);
        }
        return new Builder(getActivity()).setTitle((CharSequence) getResources().getString(C4558R.string.zm_msg_delete_file_confirm_89710)).setMessage(getResources().getString(C4558R.string.zm_msg_delete_file_in_chats_warning_89710, new Object[]{this.mSessionName})).setPositiveButton(C4558R.string.zm_btn_delete, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ContentFileDeleteDialogFragment contentFileDeleteDialogFragment = ContentFileDeleteDialogFragment.this;
                contentFileDeleteDialogFragment.unshareZoomFile(contentFileDeleteDialogFragment.mFileId, ContentFileDeleteDialogFragment.this.mSessionId);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public void unshareZoomFile(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                String unshareFile = zoomFileContentMgr.unshareFile(str, Collections.singletonList(str2));
                Intent intent = new Intent();
                intent.putExtra(UNSHARE_FILE_RESULT_ID_CODE, unshareFile);
                Fragment targetFragment = getTargetFragment();
                if (targetFragment != null) {
                    targetFragment.onActivityResult(getTargetRequestCode(), -1, intent);
                }
            }
        }
    }
}
