package com.zipow.videobox.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.ABContactsHelper;
import com.zipow.videobox.ptapp.ContactsMatchHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MMLocalHelper;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.MMSelectContactsListItemSpan;
import com.zipow.videobox.view.ZMReplaceSpanMovementMethod;
import com.zipow.videobox.view.p014mm.MMSelectContactsListItem;
import com.zipow.videobox.view.p014mm.MMSelectContactsListView;
import com.zipow.videobox.view.p014mm.MMSelectContactsListView.Listener;
import com.zipow.videobox.view.p014mm.MMSelectContactsListView.OnBlockedByIBListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMEditText;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.videomeetings.C4558R;

public class MMSelectContactsFragment extends ZMDialogFragment implements OnClickListener, Listener, KeyboardListener, IABContactsCacheListener, OnBlockedByIBListener {
    public static final String ARG_PARAMTERS = "paramters";
    public static final String ARG_RESULT_DATA = "resultData";
    public static final String JID_SELECTED_EVERYONE = "jid_select_everyone";
    private static final String TAG = "MMSelectContactsFragment";
    private boolean mAcceptNoSestion = false;
    @NonNull
    private MemCache<String, Bitmap> mAvatarCache = new MemCache<>(20);
    private Button mBtnBack;
    private Button mBtnOK;
    /* access modifiers changed from: private */
    public ZMEditText mEdtSelected;
    private boolean mFilterZoomRooms = false;
    /* access modifiers changed from: private */
    @Nullable
    public GestureDetector mGestureDetector;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private boolean mIncludeMe = false;
    private boolean mIncludeRobot = true;
    /* access modifiers changed from: private */
    public boolean mIsAlternativeHost;
    private boolean mIsSingleChoice = false;
    /* access modifiers changed from: private */
    public MMSelectContactsListView mListView;
    private int mMaxSelectCount = -1;
    @Nullable
    private Dialog mMessageDialog;
    private int mMinSelectCount = -1;
    private boolean mOnlyRobot = false;
    private boolean mOnlySameOrganizion = false;
    @Nullable
    private ProgressDialog mProgressDialog;
    @NonNull
    private SearchFilterRunnable mRunnableFilter = new SearchFilterRunnable();
    private TextView mTxtTitle;
    private IZoomMessengerUIListener mZoomMessengerUIListener;

    public static class GuestureListener extends SimpleOnGestureListener {
        private View mHolderView;
        private View mKeyboardView;

        public GuestureListener(View view, View view2) {
            this.mHolderView = view;
            this.mKeyboardView = view2;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            View view = this.mHolderView;
            if (view != null) {
                view.requestFocus();
                UIUtil.closeSoftKeyboard(this.mHolderView.getContext(), this.mKeyboardView);
            }
            return super.onScroll(motionEvent, motionEvent2, f, f2);
        }
    }

    public class SearchFilterRunnable implements Runnable {
        @NonNull
        private String mKey = "";

        public SearchFilterRunnable() {
        }

        public void setKey(@Nullable String str) {
            if (str == null) {
                str = "";
            }
            this.mKey = str;
        }

        @NonNull
        public String getKey() {
            return this.mKey;
        }

        public void run() {
            MMSelectContactsFragment.this.mListView.filter(this.mKey);
        }
    }

    public class SelectedListAdapter extends BaseAdapter {
        private static final int ITEM_TYPE_NORMAL = 0;
        private MemCache<String, Bitmap> mAvatarCache;
        private Context mContext;
        private List<MMSelectContactsListItem> mItems;

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public SelectedListAdapter(Context context) {
            this.mContext = context;
        }

        public void setAvatarMemCache(MemCache<String, Bitmap> memCache) {
            this.mAvatarCache = memCache;
        }

        public void update(List<MMSelectContactsListItem> list) {
            this.mItems = list;
            notifyDataSetChanged();
        }

        public int getCount() {
            List<MMSelectContactsListItem> list = this.mItems;
            if (list == null) {
                return 1;
            }
            return list.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (this.mItems != null && i >= 0 && i < getCount()) {
                return this.mItems.get(i);
            }
            return null;
        }

        public long getItemId(int i) {
            if (i < 0 || i >= getCount()) {
                return -1;
            }
            Object item = getItem(i);
            if (!(item instanceof MMSelectContactsListItem)) {
                return 0;
            }
            return (long) ((MMSelectContactsListItem) item).getItemId().hashCode();
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            LayoutInflater from = LayoutInflater.from(this.mContext);
            if (!(item instanceof MMSelectContactsListItem)) {
                return null;
            }
            if (view == null || !"normalItem".equals(view.getTag())) {
                view = from.inflate(C4558R.layout.zm_invite_selected_listview_item, viewGroup, false);
                view.setTag("normalItem");
            }
            AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            if (avatarView == null) {
                view = from.inflate(C4558R.layout.zm_invite_selected_listview_item, viewGroup, false);
                avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            }
            MMSelectContactsListItem mMSelectContactsListItem = (MMSelectContactsListItem) item;
            if (!mMSelectContactsListItem.isAddrBookItem() || mMSelectContactsListItem.getAddrBookItem() == null) {
                avatarView.show(new ParamsBuilder().setName(mMSelectContactsListItem.getScreenName(), mMSelectContactsListItem.getBuddyJid()).setPath(mMSelectContactsListItem.getAvatar()));
            } else {
                avatarView.show(mMSelectContactsListItem.getAddrBookItem().getAvatarParamsBuilder());
            }
            return view;
        }
    }

    public void onBlockedByIB() {
        this.mListView.setEmptyViewText(C4558R.string.zm_mm_information_barries_search_contact_115072);
    }

    public static void showInActivity(@Nullable ZMActivity zMActivity, @Nullable SelectContactsParamter selectContactsParamter, @Nullable Bundle bundle) {
        if (zMActivity != null && selectContactsParamter != null) {
            MMSelectContactsFragment mMSelectContactsFragment = new MMSelectContactsFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putSerializable(ARG_PARAMTERS, selectContactsParamter);
            if (bundle != null) {
                bundle2.putBundle("resultData", bundle);
            }
            mMSelectContactsFragment.setArguments(bundle2);
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, mMSelectContactsFragment, MMSelectContactsFragment.class.getName()).commit();
        }
    }

    public static void showAsDialog(@NonNull FragmentManager fragmentManager, SelectContactsParamter selectContactsParamter) {
        if (getSelectContactsFragment(fragmentManager) == null) {
            MMSelectContactsFragment mMSelectContactsFragment = new MMSelectContactsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_PARAMTERS, selectContactsParamter);
            mMSelectContactsFragment.setArguments(bundle);
            mMSelectContactsFragment.show(fragmentManager, MMSelectContactsFragment.class.getName());
        }
    }

    @Nullable
    public static MMSelectContactsFragment getSelectContactsFragment(FragmentManager fragmentManager) {
        return (MMSelectContactsFragment) fragmentManager.findFragmentByTag(MMSelectContactsFragment.class.getName());
    }

    public static boolean dismiss(@NonNull FragmentManager fragmentManager) {
        MMSelectContactsFragment selectContactsFragment = getSelectContactsFragment(fragmentManager);
        if (selectContactsFragment == null) {
            return false;
        }
        selectContactsFragment.dismiss();
        return true;
    }

    public void onDestroy() {
        MMSelectContactsListView mMSelectContactsListView = this.mListView;
        if (mMSelectContactsListView != null) {
            mMSelectContactsListView.stop();
        }
        this.mHandler.removeCallbacks(this.mRunnableFilter);
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        Dialog dialog = this.mMessageDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mMessageDialog.dismiss();
        }
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null) {
            SelectContactsParamter selectContactsParamter = (SelectContactsParamter) arguments.getSerializable(ARG_PARAMTERS);
            if (selectContactsParamter != null) {
                String str = selectContactsParamter.title;
                if (str != null) {
                    this.mTxtTitle.setText(str);
                }
                if (selectContactsParamter.canSelectNothing) {
                    this.mBtnBack.setText(C4558R.string.zm_mm_lbl_skip_68451);
                    this.mBtnBack.setBackgroundColor(0);
                }
                if (this.mListView != null) {
                    ArrayList<String> arrayList = selectContactsParamter.preSelectedItems;
                    this.mListView.setGroupId(selectContactsParamter.groupId, selectContactsParamter.isContainsAllInGroup);
                    if (selectContactsParamter.isAlternativeHost) {
                        this.mListView.setPreSelectedItems(arrayList, true);
                    } else {
                        this.mListView.setPreSelectedItems(arrayList);
                    }
                }
            }
            MMSelectContactsListView mMSelectContactsListView = this.mListView;
            if (mMSelectContactsListView != null) {
                mMSelectContactsListView.setFilter(getFilter());
                this.mListView.reloadAllBuddyItems();
                if (this.mIsAlternativeHost) {
                    this.mListView.loadEmailForVisibleItems();
                }
                this.mListView.onResume();
            }
            updateButtonOK(getSelectedBuddiesCount());
            ABContactsCache.getInstance().addListener(this);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (MMSelectContactsFragment.this.isResumed()) {
                        MMSelectContactsFragment.this.mEdtSelected.requestFocus();
                        UIUtil.openSoftKeyboard(MMSelectContactsFragment.this.getActivity(), MMSelectContactsFragment.this.mEdtSelected);
                    }
                }
            }, 100);
        }
    }

    public void onPause() {
        super.onPause();
        ABContactsCache.getInstance().removeListener(this);
        this.mAvatarCache.clear();
    }

    public void onDestroyView() {
        if (this.mZoomMessengerUIListener != null) {
            ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        }
        super.onDestroyView();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view;
        if (UIMgr.isLargeMode(getActivity())) {
            view = layoutInflater.inflate(C4558R.layout.zm_mm_select_contacts, null);
        } else {
            view = layoutInflater.inflate(C4558R.layout.zm_mm_select_contacts_main_screen, null);
            ((ZMKeyboardDetector) view.findViewById(C4558R.C4560id.keyboardDetector)).setKeyboardListener(this);
        }
        this.mListView = (MMSelectContactsListView) view.findViewById(C4558R.C4560id.buddyListView);
        this.mEdtSelected = (ZMEditText) view.findViewById(C4558R.C4560id.edtSelected);
        this.mBtnOK = (Button) view.findViewById(C4558R.C4560id.btnOK);
        this.mTxtTitle = (TextView) view.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = (Button) view.findViewById(C4558R.C4560id.btnBack);
        this.mEdtSelected.setOnClickListener(this);
        this.mEdtSelected.setSelected(true);
        this.mEdtSelected.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (i3 < i2) {
                    final MMSelectContactsListItemSpan[] mMSelectContactsListItemSpanArr = (MMSelectContactsListItemSpan[]) MMSelectContactsFragment.this.mEdtSelected.getText().getSpans(i3 + i, i + i2, MMSelectContactsListItemSpan.class);
                    if (mMSelectContactsListItemSpanArr.length > 0) {
                        MMSelectContactsFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                if (MMSelectContactsFragment.this.isResumed()) {
                                    for (MMSelectContactsListItemSpan item : mMSelectContactsListItemSpanArr) {
                                        MMSelectContactsListItem item2 = item.getItem();
                                        if (item2 != null) {
                                            MMSelectContactsFragment.this.mListView.unselectBuddy(item2);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }

            public void afterTextChanged(Editable editable) {
                MMSelectContactsFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (MMSelectContactsFragment.this.isResumed()) {
                            MMSelectContactsFragment.this.formatSearchEditText();
                            MMSelectContactsFragment.this.mListView.setEmptyViewText("");
                            MMSelectContactsFragment.this.startFilter(MMSelectContactsFragment.this.getFilter());
                        }
                    }
                });
            }
        });
        this.mEdtSelected.setMovementMethod(ZMReplaceSpanMovementMethod.getInstance());
        this.mEdtSelected.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, @Nullable KeyEvent keyEvent) {
                if (keyEvent == null) {
                    return false;
                }
                if (i != 6 && (keyEvent.getKeyCode() != 66 || keyEvent.getAction() != 0)) {
                    return false;
                }
                if (MMSelectContactsFragment.this.mIsAlternativeHost) {
                    MMSelectContactsFragment.this.handEnterKey();
                }
                return true;
            }
        });
        this.mBtnOK.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mListView.setListener(this);
        this.mListView.setParentFragment(this);
        this.mListView.setAvatarMemCache(this.mAvatarCache);
        this.mListView.setOnBlockedByIBListener(this);
        this.mGestureDetector = new GestureDetector(getActivity(), new GuestureListener(this.mListView, this.mEdtSelected));
        this.mListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return MMSelectContactsFragment.this.mGestureDetector.onTouchEvent(motionEvent);
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            SelectContactsParamter selectContactsParamter = (SelectContactsParamter) arguments.getSerializable(ARG_PARAMTERS);
            if (selectContactsParamter != null) {
                this.mIsSingleChoice = selectContactsParamter.isSingleChoice;
                this.mOnlySameOrganizion = selectContactsParamter.isOnlySameOrganization;
                this.mMaxSelectCount = selectContactsParamter.maxSelectCount;
                this.mMinSelectCount = selectContactsParamter.minSelectCount;
                this.mAcceptNoSestion = selectContactsParamter.isAcceptNoSestion;
                this.mIncludeRobot = selectContactsParamter.includeRobot;
                this.mOnlyRobot = selectContactsParamter.onlyRobot;
                this.mIncludeMe = selectContactsParamter.includeMe;
                this.mFilterZoomRooms = selectContactsParamter.mFilterZoomRooms;
                this.mIsAlternativeHost = selectContactsParamter.isAlternativeHost;
            }
        }
        if (this.mIsSingleChoice) {
            this.mListView.setChoiceMode(1);
            this.mBtnBack.setVisibility(8);
        }
        this.mListView.setMaxSelectCount(this.mMaxSelectCount);
        this.mListView.setOnlySameOrganization(this.mOnlySameOrganizion);
        this.mListView.setIncludeRobot(this.mIncludeRobot);
        this.mListView.setmOnlyRobot(this.mOnlyRobot);
        this.mListView.setmIncludeMe(this.mIncludeMe);
        this.mListView.setmIsShowEmail(this.mIsAlternativeHost);
        this.mListView.setmIsDisabledForPreSelected(!this.mIsAlternativeHost);
        this.mListView.setmIsNeedHaveEmail(this.mIsAlternativeHost);
        this.mListView.setmIsNeedSortSelectedItems(true ^ this.mIsAlternativeHost);
        this.mListView.setmIsAutoWebSearch(this.mIsAlternativeHost);
        this.mListView.setmFilterZoomRooms(this.mFilterZoomRooms);
        if (this.mZoomMessengerUIListener == null) {
            this.mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
                public void onIndicateBuddyInfoUpdated(String str) {
                    MMSelectContactsFragment.this.onIndicateZoomMessengerBuddyInfoUpdated(str);
                }

                public void Indicate_BuddyPresenceChanged(String str) {
                    MMSelectContactsFragment.this.onIndicateBuddyPresenceChanged(str);
                }

                public void onIndicateBuddyListUpdated() {
                    MMSelectContactsFragment.this.onIndicateZoomMessengerBuddyListUpdated();
                }

                public void onIndicateInfoUpdatedWithJID(String str) {
                    MMSelectContactsFragment.this.onIndicateZoomMessengerInfoUpdatedWithJID(str);
                }

                public void onConnectReturn(int i) {
                    MMSelectContactsFragment.this.onIndicationZoomMessengerConnectReturn(i);
                }

                public void onSearchBuddyByKey(String str, int i) {
                    MMSelectContactsFragment.this.onIndicationZoomMessengerSearchBuddyByKey(str, i);
                }
            };
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        if (VERSION.SDK_INT >= 23 && checkSelfPermission("android.permission.READ_CONTACTS") != 0 && PTApp.getInstance().isPhoneNumberRegistered() && AppUtil.canRequestContactPermission()) {
            zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 0);
            AppUtil.saveRequestContactPermissionTime();
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (MMSelectContactsFragment.this.isResumed()) {
                    MMSelectContactsFragment.this.mEdtSelected.requestFocus();
                    UIUtil.openSoftKeyboard(MMSelectContactsFragment.this.getActivity(), MMSelectContactsFragment.this.mEdtSelected);
                }
            }
        }, 100);
        return view;
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.push(new EventAction() {
                public void run(@NonNull IUIElement iUIElement) {
                    ((MMSelectContactsFragment) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_CONTACTS".equals(strArr[i2]) && iArr[i2] == 0) {
                    ABContactsCache.getInstance().reloadAllContacts();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void formatSearchEditText() {
        int i;
        Editable editableText = this.mEdtSelected.getEditableText();
        MMSelectContactsListItemSpan[] mMSelectContactsListItemSpanArr = (MMSelectContactsListItemSpan[]) StringUtil.getSortedSpans(editableText, MMSelectContactsListItemSpan.class);
        if (mMSelectContactsListItemSpanArr != null && mMSelectContactsListItemSpanArr.length > 0) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editableText);
            boolean z = false;
            for (int i2 = 0; i2 < mMSelectContactsListItemSpanArr.length; i2++) {
                int spanStart = spannableStringBuilder.getSpanStart(mMSelectContactsListItemSpanArr[i2]);
                if (i2 == 0) {
                    i = 0;
                } else {
                    i = spannableStringBuilder.getSpanEnd(mMSelectContactsListItemSpanArr[i2 - 1]);
                }
                if (spanStart != i) {
                    CharSequence subSequence = spannableStringBuilder.subSequence(i, spanStart);
                    spannableStringBuilder.replace(i, spanStart, "");
                    int spanEnd = spannableStringBuilder.getSpanEnd(mMSelectContactsListItemSpanArr[mMSelectContactsListItemSpanArr.length - 1]);
                    spannableStringBuilder.replace(spanEnd, spanEnd, subSequence);
                    z = true;
                }
            }
            if (z) {
                this.mEdtSelected.setText(spannableStringBuilder);
                this.mEdtSelected.setSelection(spannableStringBuilder.length());
            }
        }
    }

    /* access modifiers changed from: private */
    @NonNull
    public String getFilter() {
        String str = "";
        Editable text = this.mEdtSelected.getText();
        MMSelectContactsListItemSpan[] mMSelectContactsListItemSpanArr = (MMSelectContactsListItemSpan[]) text.getSpans(0, text.length(), MMSelectContactsListItemSpan.class);
        if (mMSelectContactsListItemSpanArr.length <= 0) {
            return text.toString();
        }
        int spanEnd = text.getSpanEnd(mMSelectContactsListItemSpanArr[mMSelectContactsListItemSpanArr.length - 1]);
        int length = text.length();
        return spanEnd < length ? text.subSequence(spanEnd, length).toString() : str;
    }

    /* access modifiers changed from: private */
    public void startFilter(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        if (!str.equals(this.mRunnableFilter.getKey())) {
            this.mRunnableFilter.setKey(str);
            this.mHandler.removeCallbacks(this.mRunnableFilter);
            this.mHandler.postDelayed(this.mRunnableFilter, 300);
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnOK) {
            onClickBtnOK();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.edtSelected) {
            onClickEditSelectedView();
        }
    }

    private void onClickEditSelectedView() {
        this.mEdtSelected.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSelected);
    }

    public void onClickEveryone() {
        MMSelectContactsActivity mMSelectContactsActivity = (MMSelectContactsActivity) getActivity();
        if (mMSelectContactsActivity != null) {
            Bundle arguments = getArguments();
            if (arguments != null) {
                ArrayList arrayList = new ArrayList();
                IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
                iMAddrBookItem.setJid(JID_SELECTED_EVERYONE);
                iMAddrBookItem.setScreenName(getString(C4558R.string.zm_lbl_select_everyone));
                arrayList.add(iMAddrBookItem);
                mMSelectContactsActivity.onSelectContactsDone(arrayList, arguments.getBundle("resultData"));
            }
        }
    }

    private void onClickBtnBack() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            SelectContactsParamter selectContactsParamter = (SelectContactsParamter) arguments.getSerializable(ARG_PARAMTERS);
            if (selectContactsParamter != null && selectContactsParamter.canSelectNothing) {
                finishFragment(-1);
                return;
            }
        }
        dismiss();
    }

    private void onClickBtnOK() {
        if (this.mIsSingleChoice) {
            dismiss();
        } else {
            onSelectionConfirmed();
        }
    }

    private void onSelectionConfirmed() {
        List<MMSelectContactsListItem> selectedBuddies = this.mListView.getSelectedBuddies();
        if (this.mIsAlternativeHost || this.mAcceptNoSestion || selectedBuddies.size() != 0) {
            MMSelectContactsActivity mMSelectContactsActivity = (MMSelectContactsActivity) getActivity();
            if (mMSelectContactsActivity != null) {
                Bundle arguments = getArguments();
                if (arguments != null) {
                    ArrayList arrayList = new ArrayList();
                    ArrayList arrayList2 = new ArrayList();
                    for (MMSelectContactsListItem mMSelectContactsListItem : selectedBuddies) {
                        IMAddrBookItem addrBookItem = mMSelectContactsListItem.getAddrBookItem();
                        if (addrBookItem != null) {
                            addrBookItem.setManualInput(mMSelectContactsListItem.isManualInput());
                            arrayList.add(addrBookItem);
                            arrayList2.add(addrBookItem.getJid());
                        }
                    }
                    ZoomMessenger goodConnectedZoomMessenger = MMLocalHelper.getGoodConnectedZoomMessenger();
                    if (goodConnectedZoomMessenger != null) {
                        goodConnectedZoomMessenger.refreshBuddyVCards(arrayList2, true);
                    }
                    UIUtil.closeSoftKeyboard(mMSelectContactsActivity, getView());
                    mMSelectContactsActivity.onSelectContactsDone(arrayList, arguments.getBundle("resultData"));
                    return;
                }
                return;
            }
            return;
        }
        onClickBtnBack();
    }

    public void clearSelection() {
        this.mListView.clearSelection();
    }

    public void onSelectionChanged() {
        int selectedBuddiesCount = getSelectedBuddiesCount();
        updateButtonOK(selectedBuddiesCount);
        if (this.mIsAlternativeHost) {
            if (this.mListView.getSelectedBuddies().isEmpty()) {
                this.mTxtTitle.setText(getString(C4558R.string.zm_lbl_schedule_alter_host_127873));
            } else {
                this.mTxtTitle.setText(getString(C4558R.string.zm_title_select_alternative_host_127873, Integer.valueOf(this.mListView.getSelectedBuddies().size())));
            }
        }
        if (this.mIsSingleChoice && selectedBuddiesCount == 1) {
            onSelectionConfirmed();
        }
    }

    private void updateButtonOK(int i) {
        if (this.mAcceptNoSestion || this.mIsSingleChoice || i > 0) {
            if (this.mIsAlternativeHost || i >= this.mMinSelectCount) {
                this.mBtnOK.setEnabled(true);
            } else {
                this.mBtnOK.setEnabled(false);
            }
        } else if (this.mIsAlternativeHost) {
            this.mBtnOK.setEnabled(true);
        } else {
            this.mBtnOK.setEnabled(false);
        }
    }

    private int getSelectedBuddiesCount() {
        return this.mListView.getSelectedBuddies().size();
    }

    public void onKeyboardOpen() {
        this.mEdtSelected.setCursorVisible(true);
    }

    public void onKeyboardClosed() {
        this.mEdtSelected.setCursorVisible(false);
        this.mListView.setForeground(null);
        this.mHandler.post(new Runnable() {
            public void run() {
                MMSelectContactsFragment.this.mListView.requestLayout();
            }
        });
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public boolean onSearchRequested() {
        this.mEdtSelected.requestFocus();
        UIUtil.openSoftKeyboard(getActivity(), this.mEdtSelected);
        return true;
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerBuddyInfoUpdated(String str) {
        if (this.mListView != null && isResumed()) {
            this.mListView.notifyDataSetChanged(true);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyPresenceChanged(String str) {
        if (this.mListView != null && isResumed()) {
            this.mListView.updateBuddyInfoWithJid(str);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerBuddyListUpdated() {
        if (this.mListView != null && isResumed()) {
            this.mListView.reloadAllBuddyItems();
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateZoomMessengerInfoUpdatedWithJID(String str) {
        if (this.mListView != null && isResumed()) {
            this.mListView.updateBuddyInfoWithJid(str);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicationZoomMessengerConnectReturn(int i) {
        if (this.mListView != null && isResumed()) {
            this.mListView.notifyDataSetChanged(true);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicationZoomMessengerSearchBuddyByKey(String str, int i) {
        if (this.mListView != null) {
            ProgressDialog progressDialog = this.mProgressDialog;
            if (progressDialog == null || !progressDialog.isShowing()) {
                this.mListView.onIndicationZoomMessengerSearchBuddyByKeyForNoProgressDialog(str, i);
                return;
            }
            this.mProgressDialog.dismiss();
            this.mListView.onIndicationZoomMessengerSearchBuddyByKey(str, i);
        }
    }

    public void onViewMoreClick() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood()) {
            ProgressDialog progressDialog = this.mProgressDialog;
            if (progressDialog != null && progressDialog.isShowing()) {
                this.mProgressDialog.dismiss();
            }
            if (zoomMessenger.searchBuddyByKey(getFilter())) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    this.mProgressDialog = UIUtil.showSimpleWaitingDialog((Activity) activity, C4558R.string.zm_msg_waiting);
                }
            }
        }
    }

    public void onSelectCountReachMax() {
        String string = getString(C4558R.string.zm_alert_select_count_reach_max_59554);
        Bundle arguments = getArguments();
        if (arguments != null) {
            SelectContactsParamter selectContactsParamter = (SelectContactsParamter) arguments.getSerializable(ARG_PARAMTERS);
            if (selectContactsParamter != null && !StringUtil.isEmptyOrNull(selectContactsParamter.maxCountMessage)) {
                string = selectContactsParamter.maxCountMessage;
            }
        }
        this.mMessageDialog = UIUtil.showSimpleMessageDialog((Activity) getActivity(), (String) null, string);
    }

    public void onSelected(boolean z, MMSelectContactsListItem mMSelectContactsListItem) {
        MMLocalHelper.onSelected(getActivity(), this.mEdtSelected, z, mMSelectContactsListItem);
    }

    public void onContactsCacheUpdated() {
        ABContactsCache.getInstance().removeListener(this);
        this.mListView.reloadAllBuddyItems();
        updateButtonOK(getSelectedBuddiesCount());
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            if (PTApp.getInstance().isWebSignedOn() && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber()) && ABContactsHelper.isTimeToMatchPhoneNumbers()) {
                startABMatching();
            } else if (!StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
                matchNewNumbers();
            }
        }
    }

    private int startABMatching() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return 11;
        }
        if (!PTApp.getInstance().isWebSignedOn()) {
            return 9;
        }
        return ContactsMatchHelper.getInstance().matchAllNumbers(activity);
    }

    private void matchNewNumbers() {
        if (PTApp.getInstance().isWebSignedOn()) {
            ContactsMatchHelper.getInstance().matchNewNumbers(getActivity());
        }
    }

    /* access modifiers changed from: private */
    public void handEnterKey() {
        String trim = StringUtil.safeString(getFilter()).trim();
        if (StringUtil.isValidEmailAddress(trim)) {
            MMSelectContactsListItem transformEmailToMMSelectContactsListItem = MMLocalHelper.transformEmailToMMSelectContactsListItem(trim);
            transformEmailToMMSelectContactsListItem.setManualInput(true);
            this.mListView.addSelectedItem(transformEmailToMMSelectContactsListItem);
        }
    }
}
