package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.MakeGroupResult;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMSelectSessionListView;
import java.io.File;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class MMSelectSessionFragment extends ZMFragment implements OnClickListener, OnEditorActionListener, IABContactsCacheListener, ExtListener {
    private static final String ARG_ACTION_SEND_INTENT = "actionSendIntent";
    public static final int REQUEST_MM_NEW_CHAT = 100;
    public static final int REQUEST_MM_NEW_GROUP_CHAT = 101;
    private final String TAG = MMSelectSessionFragment.class.getSimpleName();
    private Button mBtnClearSearchView;
    private View mBtnNewChat;
    private View mBtnNewGroup;
    /* access modifiers changed from: private */
    public MMSelectSessionListView mChatsListView;
    /* access modifiers changed from: private */
    @Nullable
    public Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    public EditText mEdtSearch;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public FrameLayout mListContainer;
    private IPTUIListener mNetworkStateReceiver;
    private View mPanelSearch;
    /* access modifiers changed from: private */
    public View mPanelTitleBar;
    /* access modifiers changed from: private */
    @NonNull
    public Runnable mRunnableFilter = new Runnable() {
        public void run() {
            String obj = MMSelectSessionFragment.this.mEdtSearch.getText().toString();
            MMSelectSessionFragment.this.mChatsListView.filter(obj);
            if ((obj.length() <= 0 || MMSelectSessionFragment.this.mChatsListView.getCount() <= 0) && MMSelectSessionFragment.this.mPanelTitleBar.getVisibility() != 0) {
                MMSelectSessionFragment.this.mListContainer.setForeground(MMSelectSessionFragment.this.mDimmedForground);
            } else {
                MMSelectSessionFragment.this.mListContainer.setForeground(null);
            }
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public ZMAlertDialog mStreamConflictDialog;
    private TextView mTxtTitle;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onBeginConnect() {
            MMSelectSessionFragment.this.onBeginConnect();
        }

        public void onConnectReturn(int i) {
            MMSelectSessionFragment.this.onConnectReturn(i);
        }

        public void onIndicateBuddyInfoUpdated(String str) {
            MMSelectSessionFragment.this.onIndicateBuddyInfoUpdated(str);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMSelectSessionFragment.this.onIndicateBuddyInfoUpdatedWithJID(str);
        }

        public void onIndicateBuddyListUpdated() {
            MMSelectSessionFragment.this.onIndicateBuddyListUpdated();
        }

        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMSelectSessionFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_ChatSessionListUpdate() {
            MMSelectSessionFragment.this.onNotify_ChatSessionListUpdate();
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(@NonNull String str) {
            MMSelectSessionFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }
    };

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showSendTo(@NonNull ZMActivity zMActivity, Intent intent) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_ACTION_SEND_INTENT, intent);
        SimpleActivity.show(zMActivity, MMSelectSessionFragment.class.getName(), bundle, 0, false, 1);
    }

    private Intent getActionSendIntent() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return (Intent) arguments.getParcelable(ARG_ACTION_SEND_INTENT);
        }
        return null;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_select_session_list, viewGroup, false);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mChatsListView = (MMSelectSessionListView) inflate.findViewById(C4558R.C4560id.chatsListView);
        this.mBtnNewChat = inflate.findViewById(C4558R.C4560id.btnNewChat);
        this.mBtnNewGroup = inflate.findViewById(C4558R.C4560id.btnNewGroup);
        this.mEdtSearch = (EditText) inflate.findViewById(C4558R.C4560id.edtSearch);
        this.mBtnClearSearchView = (Button) inflate.findViewById(C4558R.C4560id.btnClearSearchView);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mPanelSearch = inflate.findViewById(C4558R.C4560id.panelSearch);
        this.mChatsListView.setParentFragment(this);
        this.mBtnNewChat.setOnClickListener(this);
        this.mBtnNewGroup.setOnClickListener(this);
        this.mBtnClearSearchView.setOnClickListener(this);
        this.mEdtSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                MMSelectSessionFragment.this.mHandler.removeCallbacks(MMSelectSessionFragment.this.mRunnableFilter);
                MMSelectSessionFragment.this.mHandler.postDelayed(MMSelectSessionFragment.this.mRunnableFilter, 300);
                MMSelectSessionFragment.this.updateBtnClearSearchView();
            }
        });
        this.mEdtSearch.setOnEditorActionListener(this);
        onKeyboardClosed();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        if (!PTApp.getInstance().hasZoomMessenger()) {
            this.mPanelSearch.setVisibility(8);
        }
        return inflate;
    }

    /* access modifiers changed from: private */
    public void updateBtnClearSearchView() {
        this.mBtnClearSearchView.setVisibility(this.mEdtSearch.getText().length() > 0 ? 0 : 8);
    }

    public boolean onEditorAction(@NonNull TextView textView, int i, KeyEvent keyEvent) {
        if (textView.getId() != C4558R.C4560id.edtSearch) {
            return false;
        }
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onKeyboardOpen() {
        if (getView() != null && this.mEdtSearch.hasFocus()) {
            this.mEdtSearch.setCursorVisible(true);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_focused);
            this.mPanelTitleBar.setVisibility(8);
            this.mListContainer.setForeground(this.mDimmedForground);
        }
    }

    public void onKeyboardClosed() {
        EditText editText = this.mEdtSearch;
        if (editText != null) {
            editText.setCursorVisible(false);
            this.mEdtSearch.setBackgroundResource(C4558R.C4559drawable.zm_search_bg_normal);
            this.mListContainer.setForeground(null);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (MMSelectSessionFragment.this.isResumed()) {
                        MMSelectSessionFragment.this.mPanelTitleBar.setVisibility(0);
                    }
                }
            });
        }
    }

    private void deleteShareCopyFile() {
        if (OsUtil.isAtLeastQ()) {
            Intent actionSendIntent = getActionSendIntent();
            if (actionSendIntent != null && "android.intent.action.SEND".equals(actionSendIntent.getAction())) {
                Uri uri = (Uri) actionSendIntent.getParcelableExtra("android.intent.extra.STREAM");
                if (uri != null) {
                    String pathFromUri = FileUtils.getPathFromUri(getContext(), uri);
                    if (!StringUtil.isEmptyOrNull(pathFromUri)) {
                        File file = new File(pathFromUri);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }
            }
        }
    }

    public boolean onBackPressed() {
        deleteShareCopyFile();
        return false;
    }

    public void onResume() {
        super.onResume();
        if (PTApp.getInstance().hasZoomMessenger()) {
            this.mBtnNewChat.setVisibility(0);
            this.mBtnNewGroup.setVisibility(0);
        } else {
            this.mBtnNewChat.setVisibility(4);
            this.mBtnNewGroup.setVisibility(4);
        }
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.onParentFragmentResume();
        }
        updateTitle();
        updateBtnClearSearchView();
        ABContactsCache.getInstance().addListener(this);
        if (ABContactsCache.getInstance().needReloadAll()) {
            ABContactsCache.getInstance().reloadAllContacts();
        }
    }

    public void onStart() {
        super.onStart();
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.onParentFragmentStart();
        }
    }

    private void updateTitle() {
        switch (ZoomMessengerUI.getInstance().getConnectionStatus()) {
            case -1:
            case 0:
            case 1:
                TextView textView = this.mTxtTitle;
                if (textView != null) {
                    textView.setText(C4558R.string.zm_mm_title_send_to);
                    break;
                }
                break;
            case 2:
                TextView textView2 = this.mTxtTitle;
                if (textView2 != null) {
                    textView2.setText(C4558R.string.zm_mm_title_chats_connecting);
                    break;
                }
                break;
        }
        TextView textView3 = this.mTxtTitle;
        if (textView3 != null) {
            textView3.getParent().requestLayout();
        }
    }

    public void onContactsCacheUpdated() {
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.loadData(false);
            this.mChatsListView.notifyDataSetChanged(true);
        }
    }

    public void onPause() {
        super.onPause();
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.onParentFragmentPause();
        }
        ABContactsCache.getInstance().removeListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void dismiss() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public boolean onSearchRequested() {
        this.mEdtSearch.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSearch);
        return true;
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (arrayList != null && arrayList.size() != 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (arrayList.size() == 1) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(((IMAddrBookItem) arrayList.get(0)).getJid());
                        if (buddyWithJID != null) {
                            startOneToOneChat(buddyWithJID);
                        }
                    } else {
                        makeGroup(zoomMessenger, arrayList, "", 80);
                    }
                }
            }
        } else if (i == 101 && i2 == -1 && intent != null) {
            String stringExtra = intent.getStringExtra(NewGroupChatFragment.RESULT_ARG_SUBJECT);
            if (stringExtra == null) {
                stringExtra = "";
            }
            ArrayList arrayList2 = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (arrayList2 != null && arrayList2.size() != 0) {
                ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger2 != null) {
                    makeGroup(zoomMessenger2, arrayList2, stringExtra, 8);
                }
            }
        }
    }

    public void makeGroup(@NonNull ZoomMessenger zoomMessenger, @NonNull ArrayList<IMAddrBookItem> arrayList, String str, int i) {
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself != null) {
            String jid = myself.getJid();
            if (!StringUtil.isEmptyOrNull(jid)) {
                ArrayList arrayList2 = new ArrayList();
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    String jid2 = ((IMAddrBookItem) arrayList.get(i2)).getJid();
                    if (!StringUtil.isEmptyOrNull(jid2)) {
                        arrayList2.add(jid2);
                    }
                }
                arrayList2.add(jid);
                if (arrayList2.size() != 0) {
                    if (!zoomMessenger.isConnectionGood()) {
                        showConnectionError();
                        return;
                    }
                    MakeGroupResult makeGroup = zoomMessenger.makeGroup(arrayList2, str, (long) i);
                    if (makeGroup == null || !makeGroup.getResult()) {
                        showMakeGroupFailureMessage(1, null);
                    } else if (makeGroup.getValid()) {
                        String reusableGroupId = makeGroup.getReusableGroupId();
                        if (!StringUtil.isEmptyOrNull(reusableGroupId)) {
                            startGroupChat(reusableGroupId);
                        }
                    } else {
                        showWaitingMakeGroupDialog();
                    }
                }
            }
        }
    }

    public void startOneToOneChat(ZoomBuddy zoomBuddy) {
        MMChatActivity.showAsOneToOneChat((ZMActivity) getActivity(), zoomBuddy, getActionSendIntent());
        dismiss();
    }

    public void startGroupChat(String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            MMChatActivity.showAsGroupChat(zMActivity, str, getActionSendIntent());
        }
        dismiss();
    }

    public void onClick(View view) {
        if (view == this.mBtnNewChat) {
            startNewChat();
        } else if (view == this.mBtnNewGroup) {
            startNewGroupChat();
        } else if (view == this.mBtnClearSearchView) {
            onClickBtnClearSearchView();
        }
    }

    private void onClickBtnClearSearchView() {
        this.mEdtSearch.setText("");
        UIUtil.closeSoftKeyboard(getActivity(), this.mEdtSearch);
    }

    private void onClickPanelConnectionAlert() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (!NetworkUtil.hasDataNetwork(activity)) {
                Toast.makeText(activity, C4558R.string.zm_alert_network_disconnected, 1).show();
                return;
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (zoomMessenger.isStreamConflict()) {
                    showStreamConflictMessage();
                } else {
                    zoomMessenger.trySignon();
                }
            }
        }
    }

    private void showStreamConflictMessage() {
        ZMAlertDialog zMAlertDialog = this.mStreamConflictDialog;
        if (zMAlertDialog == null || !zMAlertDialog.isShowing()) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                this.mStreamConflictDialog = new Builder(activity).setTitle(C4558R.string.zm_mm_msg_stream_conflict_msg).setCancelable(true).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setPositiveButton(C4558R.string.zm_btn_sign_in_again, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MMSelectSessionFragment.this.forceConnectMessenger();
                    }
                }).create();
                this.mStreamConflictDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        MMSelectSessionFragment.this.mStreamConflictDialog = null;
                    }
                });
                this.mStreamConflictDialog.setCanceledOnTouchOutside(false);
                this.mStreamConflictDialog.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void forceConnectMessenger() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.forceSignon();
        }
    }

    private void startNewChat() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                MMSelectContactsActivity.show((Fragment) this, zMActivity.getString(C4558R.string.zm_mm_title_new_chat), null, zMActivity.getString(C4558R.string.zm_mm_btn_start_chat), zMActivity.getString(C4558R.string.zm_msg_select_buddies_to_chat_instructions), false, (Bundle) null, false, 100, true, (String) null, false, zoomMessenger.getGroupLimitCount(false) - 1);
            }
        }
    }

    private void startNewGroupChat() {
        if (((ZMActivity) getActivity()) != null) {
            NewGroupChatFragment.showAsActivity(this, 101);
        }
    }

    private void showWaitingMakeGroupDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingMakeGroupDialog");
        }
    }

    private boolean dismissWaitingMakeGroupDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            return false;
        }
        ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingMakeGroupDialog");
        if (zMDialogFragment == null) {
            return false;
        }
        zMDialogFragment.dismissAllowingStateLoss();
        return true;
    }

    private void showMakeGroupFailureMessage(int i, @Nullable GroupAction groupAction) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 8) {
                Toast.makeText(activity, C4558R.string.zm_mm_msg_make_group_failed_too_many_buddies_59554, 1).show();
            } else {
                String string = activity.getString(C4558R.string.zm_mm_msg_make_group_failed_59554, new Object[]{Integer.valueOf(i)});
                if (i == 40 && groupAction != null && groupAction.getMaxAllowed() > 0) {
                    string = activity.getString(C4558R.string.zm_mm_msg_max_allowed_buddies_50731, new Object[]{Integer.valueOf(groupAction.getMaxAllowed())});
                }
                Toast.makeText(activity, string, 1).show();
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void onBeginConnect() {
        if (NetworkUtil.hasDataNetwork(getActivity()) && isResumed()) {
            updateTitle();
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(int i) {
        if (PTApp.getInstance().getZoomMessenger() != null && isResumed()) {
            updateTitle();
            MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
            if (mMSelectSessionListView != null) {
                mMSelectSessionListView.notifyDataSetChanged(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyInfoUpdated(String str) {
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.onIndicateBuddyInfoUpdated(str);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyInfoUpdatedWithJID(String str) {
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.onIndicateBuddyInfoUpdatedWithJID(str);
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, @Nullable final GroupAction groupAction, String str) {
        if (groupAction != null) {
            this.mChatsListView.onGroupAction(i, groupAction, str);
            if (groupAction.getActionType() == 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself == null || StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                            public void run(IUIElement iUIElement) {
                                MMSelectSessionFragment mMSelectSessionFragment = (MMSelectSessionFragment) iUIElement;
                                if (mMSelectSessionFragment != null) {
                                    mMSelectSessionFragment.handleGroupActionMakeGroup(i, groupAction);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionMakeGroup(int i, @NonNull GroupAction groupAction) {
        if (dismissWaitingMakeGroupDialog()) {
            if (i == 0) {
                String groupId = groupAction.getGroupId();
                if (!StringUtil.isEmptyOrNull(groupId)) {
                    startGroupChat(groupId);
                }
            } else {
                showMakeGroupFailureMessage(i, groupAction);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_ChatSessionListUpdate() {
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.loadData(false);
            if (isResumed()) {
                this.mChatsListView.notifyDataSetChanged(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyListUpdated() {
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.loadData(false);
            if (isResumed()) {
                this.mChatsListView.notifyDataSetChanged(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(@NonNull String str) {
        MMSelectSessionListView mMSelectSessionListView = this.mChatsListView;
        if (mMSelectSessionListView != null) {
            mMSelectSessionListView.onNotify_MUCGroupInfoUpdatedImpl(str);
        }
    }
}
