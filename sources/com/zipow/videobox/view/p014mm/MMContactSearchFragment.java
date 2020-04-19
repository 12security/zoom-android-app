package com.zipow.videobox.view.p014mm;

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
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.view.IMSearchView;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.ZMSearchBar.OnSearchBarListener;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContactSearchFragment */
public class MMContactSearchFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final String ARG_SEARCH_FILTER = "search_filter";
    @NonNull
    private IIMCallbackUIListener mIMCallbackUI = new SimpleIMCallbackUIListener() {
        public void Indicate_LocalSearchContactResponse(String str, List<String> list) {
            MMContactSearchFragment.this.Indicate_LocalSearchContactResponse(str, list);
        }
    };
    /* access modifiers changed from: private */
    public IMSearchView mSearchResultListView;
    private TextView mTxtEmptyView;
    private TextView mTxtIBTips;
    private ZMSearchBar mZMSearchBar;
    private IZoomMessengerUIListener mZoomMessengerUIListener;
    private boolean mbKeyboardOpen = false;

    public boolean onBackPressed() {
        return false;
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Object obj) {
        showAsFragment(obj, null);
    }

    public static void showAsFragment(Object obj, String str) {
        showAsFragment(obj, -1, str);
    }

    public static void showAsFragment(Object obj, int i, String str) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putString(ARG_SEARCH_FILTER, str);
        }
        if (obj instanceof Fragment) {
            SimpleActivity.show((Fragment) obj, MMContactSearchFragment.class.getName(), bundle, i, 2);
        } else if (obj instanceof ZMActivity) {
            SimpleActivity.show((ZMActivity) obj, MMContactSearchFragment.class.getName(), bundle, i, true);
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_LocalSearchContactResponse(String str, List<String> list) {
        IMSearchView iMSearchView = this.mSearchResultListView;
        if (iMSearchView != null) {
            iMSearchView.Indicate_LocalSearchContactResponse(str, list);
        }
    }

    public void onActivityCreated(Bundle bundle) {
        getActivity().getWindow().setSoftInputMode(21);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String string = arguments.getString(ARG_SEARCH_FILTER);
            if (!TextUtils.isEmpty(string)) {
                this.mZMSearchBar.setText(string);
            }
        }
        super.onActivityCreated(bundle);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(getActivity(), true, C4558R.color.zm_im_search_bar_bg);
    }

    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_contact_search, viewGroup, false);
        this.mSearchResultListView = (IMSearchView) inflate.findViewById(C4558R.C4560id.searchResultListView);
        this.mSearchResultListView.setFooterType(2);
        this.mZMSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearchBar);
        this.mTxtEmptyView = (TextView) inflate.findViewById(C4558R.C4560id.txtEmptyView);
        this.mTxtIBTips = (TextView) inflate.findViewById(C4558R.C4560id.txtIBTips);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mZMSearchBar.setOnSearchBarListener(new OnSearchBarListener() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onClearSearch() {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                MMContactSearchFragment.this.mSearchResultListView.setFilter(editable.toString());
            }

            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == 6) {
                    MMContactSearchFragment.this.mSearchResultListView.setFilter(textView.getText().toString());
                }
                return false;
            }
        });
        this.mSearchResultListView.setEmptyView(this.mTxtEmptyView);
        EventBus.getDefault().register(this);
        return inflate;
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

    public void onConnectReturn(int i) {
        this.mSearchResultListView.refreshSearchResult(false);
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

    public void onResume() {
        super.onResume();
        this.mSearchResultListView.onResume();
        if (this.mZoomMessengerUIListener == null) {
            this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                public void onSearchBuddyByKey(String str, int i) {
                    MMContactSearchFragment.this.onZoomMessengerSearchBuddyByKey(str, i);
                }

                public void onIndicateBuddyListUpdated() {
                    MMContactSearchFragment.this.onIndicateZoomMessengerBuddyListUpdated();
                }

                public void onIndicateInfoUpdatedWithJID(String str) {
                    MMContactSearchFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                }

                public void onSearchBuddyPicDownloaded(String str) {
                    MMContactSearchFragment.this.onZoomMessengerSearchBuddyPicDownloaded(str);
                }

                public void onRemoveBuddy(String str, int i) {
                    MMContactSearchFragment.this.onZoomMessengerRemoveBuddy(str, i);
                }

                public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
                    MMContactSearchFragment.this.onGroupAction(i, groupAction, str);
                }

                public void onConfirm_MessageSent(String str, String str2, int i) {
                    MMContactSearchFragment.this.onConfirm_MessageSent(str, str2, i);
                }

                public boolean onIndicateMessageReceived(String str, String str2, String str3) {
                    return MMContactSearchFragment.this.onIndicateMessageReceived(str, str2, str3);
                }

                public void onNotify_MUCGroupInfoUpdatedImpl(String str) {
                    MMContactSearchFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
                }

                public void onNotify_ChatSessionListUpdate() {
                    MMContactSearchFragment.this.onNotify_ChatSessionListUpdate();
                }

                public void Indicate_OnlineBuddies(List<String> list) {
                    MMContactSearchFragment.this.Indicate_OnlineBuddies(list);
                }

                public void Indicate_GetContactsPresence(List<String> list, List<String> list2) {
                    MMContactSearchFragment.this.Indicate_GetContactsPresence(list, list2);
                }

                public void Indicate_BuddyPresenceChanged(String str) {
                    MMContactSearchFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                }

                public void onConnectReturn(int i) {
                    MMContactSearchFragment.this.onConnectReturn(i);
                }

                public void indicate_BuddyBlockedByIB(List<String> list) {
                    MMContactSearchFragment.this.mSearchResultListView.indicate_BuddyBlockedByIB(list);
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

    public void onKeyboardOpen() {
        this.mbKeyboardOpen = true;
    }

    public void onKeyboardClosed() {
        if (this.mbKeyboardOpen) {
            this.mbKeyboardOpen = false;
        }
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
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
