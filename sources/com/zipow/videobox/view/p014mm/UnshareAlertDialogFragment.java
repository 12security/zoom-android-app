package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.UnshareAlertDialogFragment */
public class UnshareAlertDialogFragment extends ZMDialogFragment {
    static final String ARG_FILE_ID = "fileId";
    static final String ARG_IS_SHARE_MUTI_CHAT = "isSharedMutiChat";
    static final String ARG_SHARE_ACTION = "shareAction";
    static final String TAG = "UnshareAlertDialogFragment";
    /* access modifiers changed from: private */
    public List<MMZoomShareAction> mActions;
    /* access modifiers changed from: private */
    @Nullable
    public String mFileId;
    private boolean mIsShareMutiChat;

    public static void showUnshareAlertDialog(FragmentManager fragmentManager, String str, @Nullable MMZoomShareAction mMZoomShareAction, boolean z) {
        if (mMZoomShareAction != null) {
            showUnshareAlertDialog(fragmentManager, str, Arrays.asList(new MMZoomShareAction[]{mMZoomShareAction}), z);
        }
    }

    public static void showUnshareAlertDialog(@Nullable FragmentManager fragmentManager, String str, @NonNull List<MMZoomShareAction> list, boolean z) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str) && !CollectionsUtil.isCollectionEmpty(list)) {
            UnshareAlertDialogFragment unshareAlertDialogFragment = new UnshareAlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_FILE_ID, str);
            bundle.putString(ARG_SHARE_ACTION, new Gson().toJson((Object) list));
            bundle.putBoolean(ARG_IS_SHARE_MUTI_CHAT, z);
            unshareAlertDialogFragment.setArguments(bundle);
            unshareAlertDialogFragment.show(fragmentManager, UnshareAlertDialogFragment.class.getName());
        }
    }

    public UnshareAlertDialogFragment() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        String str;
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mFileId = arguments.getString(ARG_FILE_ID);
            String string = arguments.getString(ARG_SHARE_ACTION);
            if (!TextUtils.isEmpty(string)) {
                try {
                    this.mActions = (List) new Gson().fromJson(string, new TypeToken<List<MMZoomShareAction>>() {
                    }.getType());
                } catch (Exception unused) {
                }
            }
            this.mIsShareMutiChat = arguments.getBoolean(ARG_IS_SHARE_MUTI_CHAT);
        }
        String string2 = getResources().getString(C4558R.string.zm_msg_delete_file_confirm_89710);
        if (this.mIsShareMutiChat) {
            CharSequence shareeName = !CollectionsUtil.isListEmpty(this.mActions) ? ((MMZoomShareAction) this.mActions.get(0)).getShareeName(getActivity()) : null;
            if (!TextUtils.isEmpty(shareeName)) {
                str = getResources().getString(C4558R.string.zm_msg_delete_file_in_chats_warning_89710, new Object[]{shareeName});
            } else {
                str = getResources().getString(C4558R.string.zm_msg_delete_file_warning_89710);
            }
        } else {
            str = getResources().getString(C4558R.string.zm_msg_delete_file_warning_89710);
        }
        return new Builder(getActivity()).setTitle((CharSequence) string2).setMessage(str).setPositiveButton(C4558R.string.zm_btn_delete, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UnshareAlertDialogFragment unshareAlertDialogFragment = UnshareAlertDialogFragment.this;
                unshareAlertDialogFragment.unshareZoomFile(unshareAlertDialogFragment.mFileId, UnshareAlertDialogFragment.this.mActions);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) null).create();
    }

    /* access modifiers changed from: private */
    public void unshareZoomFile(String str, @Nullable List<MMZoomShareAction> list) {
        if (!StringUtil.isEmptyOrNull(str) && list != null && !list.isEmpty()) {
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr != null) {
                ArrayList arrayList = new ArrayList();
                for (MMZoomShareAction sharee : list) {
                    arrayList.add(sharee.getSharee());
                }
                if (StringUtil.isEmptyOrNull(zoomFileContentMgr.unshareFile(str, arrayList))) {
                    ErrorMsgDialog.newInstance(getString(C4558R.string.zm_alert_unshare_file_failed), -1).show(getFragmentManager(), ErrorMsgDialog.class.getName());
                }
            }
        }
    }
}
