package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.eventbus.ZMTipsIBEvent;
import com.zipow.videobox.ptapp.IMCallbackUI;
import com.zipow.videobox.ptapp.IMCallbackUI.IIMCallbackUIListener;
import com.zipow.videobox.ptapp.IMCallbackUI.SimpleIMCallbackUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.view.IMSearchView;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import com.zipow.videobox.view.p014mm.MMContactSearchFragment;
import com.zipow.videobox.view.p014mm.MMContentSearchFragment;
import com.zipow.videobox.view.p014mm.MMMessageSearchFragment;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMSearchFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final String ARGS_JUMP_CHATS = "jumpChats";
    private View mBtnSearchContacts;
    private View mBtnSearchContents;
    private View mBtnSearchMessages;
    @NonNull
    private IIMCallbackUIListener mIMCallbackUI = new SimpleIMCallbackUIListener() {
        public void Indicate_LocalSearchContactResponse(String str, List<String> list) {
            IMSearchFragment.this.Indicate_LocalSearchContactResponse(str, list);
        }
    };
    /* access modifiers changed from: private */
    public View mPanelSpecifiedContents;
    /* access modifiers changed from: private */
    public IMSearchView mSearchResultListView;
    private TextView mTxtEmptyView;
    private TextView mTxtIBTips;
    private ZMSearchBar mZMSearchBar;
    private IZoomMessengerUIListener mZoomMessengerUIListener;

    public boolean onBackPressed() {
        return false;
    }

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

    public static void showAsFragment(Fragment fragment, int i) {
        showAsFragment(fragment, false, i);
    }

    public static void showAsFragment(Fragment fragment, boolean z, int i) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_JUMP_CHATS, z);
        SimpleActivity.show(fragment, IMSearchFragment.class.getName(), bundle, i, 2);
    }

    /* access modifiers changed from: private */
    public void Indicate_LocalSearchContactResponse(String str, List<String> list) {
        IMSearchView iMSearchView = this.mSearchResultListView;
        if (iMSearchView != null) {
            iMSearchView.Indicate_LocalSearchContactResponse(str, list);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(getActivity(), true, C4558R.color.zm_im_search_bar_bg);
    }

    public void onActivityCreated(Bundle bundle) {
        getActivity().getWindow().setSoftInputMode(21);
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean z = arguments.getBoolean(ARGS_JUMP_CHATS);
            IMSearchView iMSearchView = this.mSearchResultListView;
            if (iMSearchView != null) {
                iMSearchView.setJumpChats(z);
            }
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            boolean z2 = true;
            boolean z3 = zoomMessenger.imChatGetOption() == 2;
            boolean z4 = zoomMessenger.e2eGetMyOption() == 2;
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            if (zoomFileContentMgr == null || zoomFileContentMgr.getFileContentMgmtOption() != 1) {
                z2 = false;
            }
            boolean isFileTransferDisabled = PTApp.getInstance().isFileTransferDisabled();
            if (z3 || z4 || !z2 || isFileTransferDisabled) {
                this.mBtnSearchContents.setVisibility(8);
                this.mSearchResultListView.setFooterType(3);
            } else {
                this.mBtnSearchContents.setVisibility(0);
            }
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_im_search, viewGroup, false);
        this.mSearchResultListView = (IMSearchView) inflate.findViewById(C4558R.C4560id.searchResultListView);
        this.mSearchResultListView.setFooterType(1);
        this.mZMSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mTxtEmptyView = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mTxtIBTips = (TextView) inflate.findViewById(C4558R.C4560id.txtIBTips);
        this.mPanelSpecifiedContents = inflate.findViewById(C4558R.C4560id.panelSpecifiedContents);
        this.mBtnSearchContacts = inflate.findViewById(C4558R.C4560id.btn_search_contacts);
        this.mBtnSearchMessages = inflate.findViewById(C4558R.C4560id.btn_search_messages);
        this.mBtnSearchContents = inflate.findViewById(C4558R.C4560id.btn_search_contents);
        this.mBtnSearchContacts.setOnClickListener(this);
        this.mBtnSearchContents.setOnClickListener(this);
        this.mBtnSearchMessages.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mZMSearchBar.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onClearSearch() {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                IMSearchFragment.this.mSearchResultListView.setFilter(editable.toString());
                IMSearchFragment.this.mPanelSpecifiedContents.setVisibility(TextUtils.isEmpty(editable) ? 0 : 8);
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 6) {
                    IMSearchFragment.this.mSearchResultListView.setFilter(textView.getText().toString());
                }
                return false;
            }
        });
        this.mSearchResultListView.setEmptyView(this.mTxtEmptyView);
        EventBus.getDefault().register(this);
        return inflate;
    }

    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public void onIndicateZoomMessengerBuddyListUpdated() {
        this.mSearchResultListView.onIndicateZoomMessengerBuddyListUpdated();
    }

    public void onIndicateZoomMessengerInfoUpdatedWithJID(String str) {
        this.mSearchResultListView.updateBuddyInfoWithJid(str);
    }

    public void onZoomMessengerSearchBuddyPicDownloaded(String str) {
        this.mSearchResultListView.updateBuddyInfoWithJid(str);
    }

    public void onZoomMessengerRemoveBuddy(String str, int i) {
        this.mSearchResultListView.onZoomMessengerRemoveBuddy(str, i);
    }

    public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
        this.mSearchResultListView.onGroupAction(i, groupAction, str);
    }

    public void onConfirm_MessageSent(String str, String str2, int i) {
        this.mSearchResultListView.onConfirm_MessageSent(str, str2, i);
    }

    public boolean onIndicateMessageReceived(String str, String str2, String str3) {
        this.mSearchResultListView.onReceiveMessage(str, str2, str3);
        return false;
    }

    public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        this.mSearchResultListView.onNotify_MUCGroupInfoUpdatedImpl(str);
    }

    public void onNotify_ChatSessionListUpdate() {
        this.mSearchResultListView.onNotify_ChatSessionListUpdate();
    }

    public void onZoomMessengerSearchBuddyByKey(String str, int i) {
        this.mSearchResultListView.onSearchBuddyByKey(str, i);
    }

    public void Indicate_OnlineBuddies(@Nullable List<String> list) {
        if (list == null) {
            return;
        }
        if (list.size() > 10) {
            this.mSearchResultListView.refreshSearchResult(false);
            return;
        }
        for (String updateBuddyInfoWithJid : list) {
            this.mSearchResultListView.updateBuddyInfoWithJid(updateBuddyInfoWithJid);
        }
    }

    public void Indicate_GetContactsPresence(@Nullable List<String> list, @Nullable List<String> list2) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (list != null) {
            arrayList.addAll(list);
        }
        if (list2 != null) {
            arrayList.addAll(list2);
        }
        if (arrayList.size() > 10) {
            this.mSearchResultListView.refreshSearchResult(false);
            return;
        }
        for (String updateBuddyInfoWithJid : arrayList) {
            this.mSearchResultListView.updateBuddyInfoWithJid(updateBuddyInfoWithJid);
        }
    }

    public void indicate_BuddyBlockedByIB(List<String> list) {
        this.mSearchResultListView.indicate_BuddyBlockedByIB(list);
    }

    public void onResume() {
        super.onResume();
        this.mSearchResultListView.onResume();
        if (this.mZoomMessengerUIListener == null) {
            this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                public void onSearchBuddyByKey(String str, int i) {
                    IMSearchFragment.this.onZoomMessengerSearchBuddyByKey(str, i);
                }

                public void onIndicateBuddyListUpdated() {
                    IMSearchFragment.this.onIndicateZoomMessengerBuddyListUpdated();
                }

                public void onIndicateInfoUpdatedWithJID(String str) {
                    IMSearchFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                }

                public void onSearchBuddyPicDownloaded(String str) {
                    IMSearchFragment.this.onZoomMessengerSearchBuddyPicDownloaded(str);
                }

                public void onRemoveBuddy(String str, int i) {
                    IMSearchFragment.this.onZoomMessengerRemoveBuddy(str, i);
                }

                public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
                    IMSearchFragment.this.onGroupAction(i, groupAction, str);
                }

                public void onConfirm_MessageSent(String str, String str2, int i) {
                    IMSearchFragment.this.onConfirm_MessageSent(str, str2, i);
                }

                public boolean onIndicateMessageReceived(String str, String str2, String str3) {
                    return IMSearchFragment.this.onIndicateMessageReceived(str, str2, str3);
                }

                public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
                    IMSearchFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
                }

                public void onNotify_ChatSessionListUpdate() {
                    IMSearchFragment.this.onNotify_ChatSessionListUpdate();
                }

                public void Indicate_OnlineBuddies(List<String> list) {
                    IMSearchFragment.this.Indicate_OnlineBuddies(list);
                }

                public void Indicate_GetContactsPresence(List<String> list, List<String> list2) {
                    IMSearchFragment.this.Indicate_GetContactsPresence(list, list2);
                }

                public void Indicate_BuddyPresenceChanged(String str) {
                    IMSearchFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                }

                public void indicate_BuddyBlockedByIB(List<String> list) {
                    IMSearchFragment.this.indicate_BuddyBlockedByIB(list);
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        IMCallbackUI.getInstance().addListener(this.mIMCallbackUI);
    }

    public void onPause() {
        if (this.mZoomMessengerUIListener != null) {
            ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        }
        IMCallbackUI.getInstance().removeListener(this.mIMCallbackUI);
        super.onPause();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btn_search_contacts) {
            onClickBtnSearchContacts();
        } else if (id == C4558R.C4560id.btn_search_contents) {
            onClickBtnSearchContents();
        } else if (id == C4558R.C4560id.btn_search_messages) {
            onClickBtnSearchMessages();
        }
    }

    private void onClickBtnSearchContacts() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
            MMContactSearchFragment.showAsFragment(this);
        }
    }

    private void onClickBtnSearchContents() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
            MMContentSearchFragment.showAsFragment(this, false);
        }
    }

    private void onClickBtnSearchMessages() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
            MMMessageSearchFragment.showAsFragment(this);
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        if (getActivity() != null) {
            UIUtil.closeSoftKeyboard(getActivity(), getView());
        }
        finishFragment(0);
    }

    private boolean isResultEmpty() {
        IMSearchView iMSearchView = this.mSearchResultListView;
        if (iMSearchView == null) {
            return true;
        }
        return iMSearchView.isResultEmpty();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMTipsIBEvent zMTipsIBEvent) {
        if (isAdded()) {
            this.mTxtIBTips.setVisibility(zMTipsIBEvent.showTips ? 0 : 8);
        }
    }
}
