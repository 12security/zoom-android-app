package com.zipow.videobox.view.p014mm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.UIMgr;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ContentFileMenuDialogFragment */
public class ContentFileMenuDialogFragment extends ZMDialogFragment {
    private static final String ARG_FILE_ID = "fileId";
    private static final String ARG_IS_SHOW_DELETE_ITEM = "isShowDeleteItem";
    private static final String ARG_SESSION_ID = "sessionId";
    private static final String ARG_SESSION_NAME = "sessionName";
    private static final int REQUEST_CODE_SHOW_DELETE_DIALOG = 1;
    public static final int RESULT_CODE_DELETE_FILE_DIALOG_OK = 2;
    private static final String TAG = "ContentFileMenuDialogFragment";
    @Nullable
    private String mFileId;
    private boolean mIsShowDeleteFile;
    @Nullable
    private String mSessionId;
    @Nullable
    private String mSessionName;

    /* renamed from: com.zipow.videobox.view.mm.ContentFileMenuDialogFragment$ShareChatContextMenuItem */
    private class ShareChatContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_JUMP = 0;
        public static final int ACTION_UNSHARE = 1;
        /* access modifiers changed from: private */
        public String mFileId;
        /* access modifiers changed from: private */
        public String mSessionId;
        /* access modifiers changed from: private */
        public String mSessionName;

        public ShareChatContextMenuItem(String str, int i, String str2, String str3, String str4) {
            super(i, str);
            this.mFileId = str2;
            this.mSessionId = str3;
            this.mSessionName = str4;
        }
    }

    public static void showContentFileMeunDialog(@Nullable FragmentManager fragmentManager, @Nullable Fragment fragment, int i, String str, String str2, String str3, boolean z) {
        if (fragmentManager != null && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2)) {
            ContentFileMenuDialogFragment contentFileMenuDialogFragment = new ContentFileMenuDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ARG_FILE_ID, str);
            bundle.putString("sessionId", str2);
            bundle.putString(ARG_SESSION_NAME, str3);
            bundle.putBoolean(ARG_IS_SHOW_DELETE_ITEM, z);
            contentFileMenuDialogFragment.setArguments(bundle);
            if (fragment != null) {
                contentFileMenuDialogFragment.setTargetFragment(fragment, i);
            }
            contentFileMenuDialogFragment.show(fragmentManager, ContentFileMenuDialogFragment.class.getName());
        }
    }

    public ContentFileMenuDialogFragment() {
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
            this.mIsShowDeleteFile = arguments.getBoolean(ARG_IS_SHOW_DELETE_ITEM);
        }
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getActivity(), false);
        ArrayList arrayList = new ArrayList();
        ShareChatContextMenuItem shareChatContextMenuItem = new ShareChatContextMenuItem(getString(C4558R.string.zm_btn_jump_group_59554), 0, this.mFileId, this.mSessionId, this.mSessionName);
        arrayList.add(shareChatContextMenuItem);
        if (this.mIsShowDeleteFile && !StringUtil.isEmptyOrNull(this.mSessionName)) {
            ShareChatContextMenuItem shareChatContextMenuItem2 = new ShareChatContextMenuItem(getString(C4558R.string.zm_btn_unshare_group_59554), 1, this.mFileId, this.mSessionId, this.mSessionName);
            arrayList.add(shareChatContextMenuItem2);
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        TextView textView = new TextView(getActivity());
        if (VERSION.SDK_INT < 23) {
            textView.setTextAppearance(getActivity(), C4558R.style.ZMTextView_Medium);
        } else {
            textView.setTextAppearance(C4558R.style.ZMTextView_Medium);
        }
        int dip2px = UIUtil.dip2px(getActivity(), 20.0f);
        textView.setPadding(dip2px, dip2px, dip2px, dip2px / 2);
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile fileWithWebFileID = zoomFileContentMgr.getFileWithWebFileID(this.mFileId);
            if (fileWithWebFileID != null) {
                String fileName = fileWithWebFileID.getFileName();
                textView.setText(getString(C4558R.string.zm_title_sharer_action, fileName, this.mSessionName));
                zoomFileContentMgr.destroyFileObject(fileWithWebFileID);
            }
        }
        return new Builder(getActivity()).setTitleView(textView).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ContentFileMenuDialogFragment.this.onSelectSharerActionContextMenuItem((ShareChatContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public void onSelectSharerActionContextMenuItem(@Nullable ShareChatContextMenuItem shareChatContextMenuItem) {
        if (shareChatContextMenuItem != null) {
            switch (shareChatContextMenuItem.getAction()) {
                case 0:
                    jumpToChat(shareChatContextMenuItem.mSessionId);
                    break;
                case 1:
                    ContentFileDeleteDialogFragment.showUnshareAlertDialog(getFragmentManager(), this, 1, shareChatContextMenuItem.mFileId, shareChatContextMenuItem.mSessionId, shareChatContextMenuItem.mSessionName);
                    break;
            }
        }
    }

    private void jumpToChat(String str) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    if (sessionById.isGroup()) {
                        ZoomGroup sessionGroup = sessionById.getSessionGroup();
                        if (sessionGroup != null) {
                            String groupID = sessionGroup.getGroupID();
                            if (!StringUtil.isEmptyOrNull(groupID)) {
                                MMChatActivity.showAsGroupChat(zMActivity, groupID);
                            }
                        }
                    } else {
                        ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                        if (sessionBuddy == null) {
                            if (UIMgr.isMyNotes(str)) {
                                sessionBuddy = zoomMessenger.getMyself();
                            }
                            if (sessionBuddy == null) {
                                return;
                            }
                        }
                        MMChatActivity.showAsOneToOneChat(zMActivity, sessionBuddy);
                    }
                }
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1 && i2 == -1) {
            Fragment targetFragment = getTargetFragment();
            if (targetFragment != null) {
                targetFragment.onActivityResult(getTargetRequestCode(), 2, intent);
            }
        }
    }
}
