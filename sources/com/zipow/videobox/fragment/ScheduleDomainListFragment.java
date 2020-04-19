package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.util.ZMScheduleUtil;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMChildListView;
import p021us.zoom.videomeetings.C4558R;

public class ScheduleDomainListFragment extends ZMDialogFragment implements OnClickListener {
    private DomainListAdapter mAdapter;
    private Button mBtnAddNewDomain;
    /* access modifiers changed from: private */
    public Button mBtnSave;
    /* access modifiers changed from: private */
    public boolean mIsLockEditDomain = false;
    private ZMChildListView mLvDomains;
    private View mPanelAddDomain;
    private RefreshUIListener mRefreshUIListener;
    /* access modifiers changed from: private */
    @Nullable
    public ArrayList<CharSequence> mSpecifiedDomains = new ArrayList<>();

    public static class DomainListAdapter extends BaseAdapter {
        private Context context;
        /* access modifiers changed from: private */
        public ArrayList<CharSequence> mList;
        private boolean mLockDomain;
        private RefreshUIListener mRefreshUIListener;

        public long getItemId(int i) {
            return (long) i;
        }

        public DomainListAdapter(@NonNull Context context2, @NonNull ArrayList<CharSequence> arrayList, @Nullable RefreshUIListener refreshUIListener, boolean z) {
            this.context = context2;
            this.mList = arrayList;
            this.mRefreshUIListener = refreshUIListener;
            this.mLockDomain = z;
        }

        public int getCount() {
            ArrayList<CharSequence> arrayList = this.mList;
            if (arrayList == null) {
                return 0;
            }
            return arrayList.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i >= 0) {
                return this.mList.get(i);
            }
            return null;
        }

        public View getView(final int i, View view, ViewGroup viewGroup) {
            int i2 = 0;
            if (view == null || !"domainItem".equals(view.getTag())) {
                view = LayoutInflater.from(this.context).inflate(C4558R.layout.zm_domain_item, viewGroup, false);
                view.setTag("domainItem");
            }
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.ivDelete);
            ((TextView) view.findViewById(C4558R.C4560id.txtDomain)).setText((CharSequence) this.mList.get(i));
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    DomainListAdapter.this.mList.remove(i);
                    DomainListAdapter.this.notifyDataSetChanged();
                }
            });
            if (this.mLockDomain) {
                i2 = 8;
            }
            imageView.setVisibility(i2);
            return view;
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            RefreshUIListener refreshUIListener = this.mRefreshUIListener;
            if (refreshUIListener != null) {
                refreshUIListener.refreshUi();
            }
        }
    }

    interface RefreshUIListener {
        void refreshUi();
    }

    public static void showInActivity(@Nullable Fragment fragment, int i, String str, boolean z) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(ZMScheduleUtil.ARG_SPECIFIED_DOMAINS, str);
            bundle.putBoolean(ZMScheduleUtil.ARG_DOMAIN_EDIT_LOCK, z);
            SimpleActivity.show(fragment, ScheduleDomainListFragment.class.getName(), bundle, i, false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_schedule_domain_list, viewGroup, false);
        this.mBtnAddNewDomain = (Button) inflate.findViewById(C4558R.C4560id.btnAddNewDomain);
        this.mLvDomains = (ZMChildListView) inflate.findViewById(C4558R.C4560id.lvDomains);
        this.mBtnSave = (Button) inflate.findViewById(C4558R.C4560id.btnSave);
        this.mPanelAddDomain = inflate.findViewById(C4558R.C4560id.llAdd);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        this.mBtnSave.setOnClickListener(this);
        this.mBtnAddNewDomain.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mIsLockEditDomain = arguments.getBoolean(ZMScheduleUtil.ARG_DOMAIN_EDIT_LOCK, false);
            initSpecifiedDomains(arguments.getString(ZMScheduleUtil.ARG_SPECIFIED_DOMAINS));
        }
        if (bundle != null) {
            this.mSpecifiedDomains = bundle.getCharSequenceArrayList("mJoinSpecifiedDomains");
        }
        this.mRefreshUIListener = new RefreshUIListener() {
            public void refreshUi() {
                if (ScheduleDomainListFragment.this.mSpecifiedDomains == null || ScheduleDomainListFragment.this.mSpecifiedDomains.size() <= 0) {
                    ScheduleDomainListFragment.this.mBtnSave.setEnabled(false);
                } else {
                    ScheduleDomainListFragment.this.mBtnSave.setEnabled(true);
                }
            }
        };
        Context context = getContext();
        if (context != null) {
            ArrayList<CharSequence> arrayList = this.mSpecifiedDomains;
            if (arrayList != null) {
                this.mAdapter = new DomainListAdapter(context, arrayList, this.mRefreshUIListener, this.mIsLockEditDomain);
                this.mLvDomains.setAdapter(this.mAdapter);
            }
        }
        this.mLvDomains.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (!ScheduleDomainListFragment.this.mIsLockEditDomain) {
                    ScheduleDomainListFragment.this.editDomain(i);
                }
            }
        });
        if (this.mIsLockEditDomain) {
            this.mBtnSave.setVisibility(4);
            this.mPanelAddDomain.setVisibility(8);
        }
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequenceArrayList("mJoinSpecifiedDomains", this.mSpecifiedDomains);
    }

    private void initSpecifiedDomains(@Nullable String str) {
        if (!TextUtils.isEmpty(str)) {
            ArrayList<CharSequence> arrayList = this.mSpecifiedDomains;
            if (arrayList != null) {
                arrayList.addAll(ZMScheduleUtil.getDomainListFromStr(str));
            }
            refreshDomainsPanel();
        }
    }

    /* access modifiers changed from: private */
    public void refreshDomainsPanel() {
        DomainListAdapter domainListAdapter = this.mAdapter;
        if (domainListAdapter != null) {
            domainListAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void editDomain(final int i) {
        final boolean z = i >= 0;
        ArrayList<CharSequence> arrayList = this.mSpecifiedDomains;
        if (arrayList != null && i < arrayList.size()) {
            View inflate = View.inflate(getActivity(), C4558R.layout.zm_schedule_input_domain, null);
            final EditText editText = (EditText) inflate.findViewById(C4558R.C4560id.edtDomainName);
            if (z) {
                editText.setText((CharSequence) this.mSpecifiedDomains.get(i));
                editText.setSelection(editText.length());
            }
            editText.setHint(C4558R.string.zm_hint_allow_join_input_domains);
            final ZMAlertDialog create = new Builder(getActivity()).setView(inflate).setPositiveButton(C4558R.string.zm_btn_save, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    String obj = editText.getText().toString();
                    if (z) {
                        ScheduleDomainListFragment.this.mSpecifiedDomains.set(i, obj);
                    } else {
                        ScheduleDomainListFragment.this.mSpecifiedDomains.add(obj);
                    }
                    ScheduleDomainListFragment.this.refreshDomainsPanel();
                }
            }).setNeutralButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    UIUtil.closeSoftKeyboardInActivity((ZMActivity) ScheduleDomainListFragment.this.getActivity());
                }
            }).create();
            editText.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                public void afterTextChanged(@NonNull Editable editable) {
                    create.getButton(-1).setEnabled(StringUtil.isValidDomain(editable.toString()));
                }
            });
            create.show();
            create.getButton(-1).setEnabled(StringUtil.isValidDomain(editText.getText().toString()));
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnAddNewDomain) {
                onclickBtnAddNewDomain();
            } else if (id == C4558R.C4560id.btnBack) {
                onclickBtnBack();
            } else if (id == C4558R.C4560id.btnSave) {
                onclickBtnSave();
            }
        }
    }

    private void onclickBtnSave() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent();
            StringBuilder sb = new StringBuilder();
            if (this.mSpecifiedDomains != null) {
                for (int i = 0; i < this.mSpecifiedDomains.size(); i++) {
                    sb.append((CharSequence) this.mSpecifiedDomains.get(i));
                    if (i != this.mSpecifiedDomains.size() - 1) {
                        sb.append(PreferencesConstants.COOKIE_DELIMITER);
                    }
                }
                intent.putExtra(ZMScheduleUtil.ARG_SPECIFIED_DOMAINS, sb.toString());
                activity.setResult(-1, intent);
                dismiss();
            }
        }
    }

    private void onclickBtnBack() {
        dismiss();
    }

    private void onclickBtnAddNewDomain() {
        editDomain(-1);
    }

    public void dismiss() {
        finishFragment(true);
    }
}
