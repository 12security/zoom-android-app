package com.zipow.videobox.kubi;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.nydus.KUBIDeviceController;
import com.zipow.nydus.KUBIDeviceController.IKubiListener;
import com.zipow.nydus.KUBIDeviceController.SimpleKubiListener;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.meeting.confhelper.KubiComponent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class KubiChoiceFragment extends ZMDialogFragment {
    /* access modifiers changed from: private */
    @Nullable
    public KubiListAdapter mAdapter;
    @Nullable
    private BroadcastReceiver mBluetoothStatusReceiver;
    private IKubiListener mKubiListener;

    static class EmptyListItem {
        EmptyListItem() {
        }
    }

    static class KubiItem {
        boolean connected;
        KubiDevice device;

        public KubiItem(KubiDevice kubiDevice, boolean z) {
            this.device = kubiDevice;
            this.connected = z;
        }
    }

    static class KubiListAdapter extends BaseAdapter {
        private static final int ITEM_TYPE_EMPTY_LIST = 3;
        private static final int ITEM_TYPE_KUBI_CONNECTED = 2;
        private static final int ITEM_TYPE_KUBI_NORMAL = 1;
        private static final int ITEM_TYPE_LOADING = 0;
        private ZMActivity mActivity;
        @NonNull
        private List<Object> mList = new ArrayList();

        public boolean areAllItemsEnabled() {
            return false;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 4;
        }

        public KubiListAdapter(ZMActivity zMActivity) {
            this.mActivity = zMActivity;
            this.mList.add(new LoadingItem());
            KubiDevice currentKubi = getCurrentKubi();
            if (currentKubi != null) {
                this.mList.add(new KubiItem(currentKubi, true));
            }
        }

        private KubiDevice getCurrentKubi() {
            KUBIDeviceController instance = KUBIDeviceController.getInstance();
            if (instance == null) {
                return null;
            }
            return instance.getCurrentKubi();
        }

        public void loadSearchResult(@NonNull ArrayList<KubiDevice> arrayList) {
            this.mList.clear();
            KubiDevice currentKubi = getCurrentKubi();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                KubiDevice kubiDevice = (KubiDevice) it.next();
                if (kubiDevice != null && (currentKubi == null || !StringUtil.isSameString(currentKubi.getMac(), kubiDevice.getMac()))) {
                    this.mList.add(new KubiItem(kubiDevice, false));
                }
            }
            if (currentKubi != null) {
                this.mList.add(new KubiItem(currentKubi, true));
            }
            if (this.mList.isEmpty()) {
                this.mList.add(new EmptyListItem());
            }
        }

        public int getCount() {
            return this.mList.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= this.mList.size()) {
                return null;
            }
            return this.mList.get(i);
        }

        public int getItemViewType(int i) {
            Object item = getItem(i);
            int i2 = 1;
            if (item == null) {
                return 1;
            }
            if (item instanceof LoadingItem) {
                i2 = 0;
            } else if (item instanceof EmptyListItem) {
                i2 = 3;
            } else if ((item instanceof KubiItem) && ((KubiItem) item).connected) {
                i2 = 2;
            }
            return i2;
        }

        public boolean isEnabled(int i) {
            int itemViewType = getItemViewType(i);
            return itemViewType == 2 || itemViewType == 1;
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            Object item = getItem(i);
            LayoutInflater from = LayoutInflater.from(this.mActivity);
            if (from == null) {
                return null;
            }
            if (item instanceof LoadingItem) {
                if (view == null || !"LoadingItem".equals(view.getTag())) {
                    view = from.inflate(C4558R.layout.zm_kubi_loading_item, viewGroup, false);
                    view.setTag("LoadingItem");
                }
            } else if (item instanceof EmptyListItem) {
                if (view == null || !"EmptyListItem".equals(view.getTag())) {
                    view = from.inflate(C4558R.layout.zm_kubi_empty_list_item, viewGroup, false);
                    view.setTag("EmptyListItem");
                }
            } else if (item instanceof KubiItem) {
                KubiItem kubiItem = (KubiItem) item;
                if (kubiItem.connected) {
                    if (view == null || !"ConnectedItem".equals(view.getTag())) {
                        view = from.inflate(C4558R.layout.zm_kubi_item_connected, viewGroup, false);
                        view.setTag("ConnectedItem");
                    }
                    TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
                    if (textView != null) {
                        textView.setText(this.mActivity.getString(C4558R.string.zm_kubi_disconnect_kubi_xxx, new Object[]{kubiItem.device.getName()}));
                    }
                } else {
                    if (view == null || !"NormalItem".equals(view.getTag())) {
                        view = from.inflate(C4558R.layout.zm_kubi_item_normal, viewGroup, false);
                        view.setTag("NormalItem");
                    }
                    TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
                    if (textView2 != null) {
                        textView2.setText(kubiItem.device.getName());
                    }
                }
            } else {
                view = null;
            }
            return view;
        }
    }

    static class LoadingItem {
        LoadingItem() {
        }
    }

    public static void showDialog(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            KubiChoiceFragment kubiChoiceFragment = new KubiChoiceFragment();
            kubiChoiceFragment.setArguments(bundle);
            kubiChoiceFragment.show(fragmentManager, KubiChoiceFragment.class.getName());
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        this.mAdapter = new KubiListAdapter((ZMActivity) getActivity());
        int i = C4558R.string.zm_kubi_connect_kubi_list_title;
        KUBIDeviceController instance = KUBIDeviceController.getInstance();
        if (!(instance == null || instance.getCurrentKubi() == null)) {
            i = C4558R.string.zm_kubi_switch_kubi_list_title;
        }
        ZMAlertDialog create = new Builder(getActivity()).setTitle(i).setAdapter(this.mAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                KubiChoiceFragment kubiChoiceFragment = KubiChoiceFragment.this;
                kubiChoiceFragment.onClickItem(kubiChoiceFragment.mAdapter, i);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    public void onResume() {
        super.onResume();
        if (this.mKubiListener == null) {
            this.mKubiListener = new SimpleKubiListener() {
                public void onKubiScanComplete(@NonNull ArrayList<KubiDevice> arrayList) {
                    KubiChoiceFragment.this.onKubiScanComplete(arrayList);
                }
            };
        }
        KUBIDeviceController instance = KUBIDeviceController.getInstance();
        if (instance != null) {
            instance.addKubiListener(this.mKubiListener);
        }
        if (checkBluetoothStatus()) {
            startToFindAllKubis();
        } else {
            registerBluetoothStatusReceiver();
        }
    }

    public void onPause() {
        super.onPause();
        KUBIDeviceController instance = KUBIDeviceController.getInstance();
        if (instance != null) {
            instance.removeKubiListener(this.mKubiListener);
        }
        unregisterBluetoothStatusReceiver();
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        notifyDialogConfirmed();
    }

    private void notifyDialogConfirmed() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if ((zMActivity instanceof ConfActivity) && zMActivity.isActive()) {
            KubiComponent kubiComponent = ((ConfActivity) zMActivity).getmKubiComponent();
            if (kubiComponent != null) {
                kubiComponent.onKubiChoiceFragmentClosed();
            }
        }
    }

    /* access modifiers changed from: private */
    public void startToFindAllKubis() {
        KUBIDeviceController instance = KUBIDeviceController.getInstance();
        if (instance != null) {
            instance.findAllKubiDevices();
        }
    }

    private boolean checkBluetoothStatus() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return false;
        }
        return defaultAdapter.isEnabled();
    }

    private void registerBluetoothStatusReceiver() {
        if (this.mBluetoothStatusReceiver == null) {
            this.mBluetoothStatusReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, @NonNull Intent intent) {
                    if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1) == 12) {
                        KubiChoiceFragment.this.startToFindAllKubis();
                    }
                }
            };
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.registerReceiver(this.mBluetoothStatusReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
            }
        }
    }

    private void unregisterBluetoothStatusReceiver() {
        if (this.mBluetoothStatusReceiver != null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.unregisterReceiver(this.mBluetoothStatusReceiver);
            }
            this.mBluetoothStatusReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void onKubiScanComplete(@NonNull ArrayList<KubiDevice> arrayList) {
        this.mAdapter.loadSearchResult(arrayList);
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    public void onClickItem(KubiListAdapter kubiListAdapter, int i) {
        notifyDialogConfirmed();
        Object item = kubiListAdapter.getItem(i);
        if (item != null) {
            KUBIDeviceController instance = KUBIDeviceController.getInstance();
            if (instance != null && (item instanceof KubiItem)) {
                KubiItem kubiItem = (KubiItem) item;
                if (kubiItem.connected) {
                    instance.disconnectKubi();
                } else {
                    instance.connectToKubi(kubiItem.device);
                }
            }
        }
    }
}
