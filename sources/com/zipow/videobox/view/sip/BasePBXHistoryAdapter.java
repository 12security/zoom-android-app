package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPCallHistoryItemBean;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailItemBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.videomeetings.C4558R;

public abstract class BasePBXHistoryAdapter<T> extends BaseAdapter implements OnCheckedChangeListener {
    protected Context mContext;
    @NonNull
    protected List<T> mHistoryList = new ArrayList();
    @NonNull
    protected final LayoutInflater mInflater;
    protected boolean mIsSelectMode;
    protected IPBXListView mListView;
    @NonNull
    public Set<String> mSelectItem = new HashSet();

    public interface IPBXListView {
        void delete(String str, boolean z);
    }

    public interface SelectStatus {
        public static final int SELECT = 0;
        public static final int SELECT_ALL = 2;
        public static final int SELECT_NO = 3;
        public static final int UNSELECT = 1;
    }

    public class ViewHolder {
        CheckBox checkDeleteItem;
        ImageView imgOutCall;
        View recordingPanel;
        ImageView showDialog;
        TextView txtBuddyName;
        TextView txtCallNo;
        TextView txtDate;
        TextView txtRecording;
        TextView txtSlaInfo;
        TextView txtTime;

        public ViewHolder() {
        }
    }

    /* access modifiers changed from: protected */
    public abstract void bind(int i, View view, ViewHolder viewHolder, ViewGroup viewGroup);

    @Nullable
    public abstract T getItemById(String str);

    public long getItemId(int i) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public abstract boolean removeCall(String str);

    public BasePBXHistoryAdapter(Context context, IPBXListView iPBXListView) {
        this.mContext = context;
        this.mListView = iPBXListView;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    public void setSelectMode(boolean z) {
        this.mIsSelectMode = z;
        this.mSelectItem.clear();
        MainObservable.getInstance().notifyObservers(Boolean.valueOf(false));
    }

    public boolean isSelectMode() {
        return this.mIsSelectMode;
    }

    public void updateData(List<T> list) {
        this.mHistoryList.clear();
        addData(list);
    }

    public void addData(@Nullable List<T> list) {
        if (list != null) {
            this.mHistoryList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public boolean markAllElements() {
        boolean z = false;
        if (this.mSelectItem.size() == this.mHistoryList.size()) {
            cancelMarkAllElements();
            return false;
        }
        this.mSelectItem.clear();
        if (this.mHistoryList.size() > 0) {
            if (this.mHistoryList.get(0) instanceof CmmSIPCallHistoryItemBean) {
                for (T id : this.mHistoryList) {
                    this.mSelectItem.add(id.getId());
                }
            } else if (this.mHistoryList.get(0) instanceof CmmSIPVoiceMailItemBean) {
                for (T id2 : this.mHistoryList) {
                    this.mSelectItem.add(id2.getId());
                }
            }
        }
        MainObservable instance = MainObservable.getInstance();
        if (this.mSelectItem.size() > 0) {
            z = true;
        }
        instance.notifyObservers(Boolean.valueOf(z));
        return true;
    }

    public void cancelMarkAllElements() {
        this.mSelectItem.clear();
        MainObservable.getInstance().notifyObservers(Boolean.valueOf(false));
    }

    @NonNull
    public Set<String> getAllIds() {
        return this.mSelectItem;
    }

    @NonNull
    public List<T> getData() {
        return this.mHistoryList;
    }

    public int getCount() {
        return this.mHistoryList.size();
    }

    @Nullable
    public T getItem(int i) {
        if (i < 0 || this.mHistoryList.size() <= i) {
            return null;
        }
        return this.mHistoryList.get(i);
    }

    @Nullable
    public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflater.inflate(C4558R.layout.zm_sip_pbx_history_item, viewGroup, false);
            viewHolder = createViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        bind(i, view, viewHolder, viewGroup);
        return view;
    }

    /* access modifiers changed from: protected */
    @NonNull
    public ViewHolder createViewHolder(@NonNull View view) {
        ViewHolder viewHolder = new ViewHolder<>();
        viewHolder.imgOutCall = (ImageView) view.findViewById(C4558R.C4560id.imgOutCall);
        viewHolder.showDialog = (ImageView) view.findViewById(C4558R.C4560id.showDialog);
        viewHolder.txtBuddyName = (TextView) view.findViewById(C4558R.C4560id.txtBuddyName);
        viewHolder.txtCallNo = (TextView) view.findViewById(C4558R.C4560id.txtCallNo);
        viewHolder.txtDate = (TextView) view.findViewById(C4558R.C4560id.txtDate);
        viewHolder.txtTime = (TextView) view.findViewById(C4558R.C4560id.txtTime);
        viewHolder.txtRecording = (TextView) view.findViewById(C4558R.C4560id.txtRecording);
        viewHolder.checkDeleteItem = (CheckBox) view.findViewById(C4558R.C4560id.checkDeleteItem);
        viewHolder.txtSlaInfo = (TextView) view.findViewById(C4558R.C4560id.txtSlaInfo);
        viewHolder.recordingPanel = view.findViewById(C4558R.C4560id.recordingPanel);
        viewHolder.checkDeleteItem.setOnCheckedChangeListener(this);
        return viewHolder;
    }

    /* access modifiers changed from: protected */
    public void clearAll() {
        this.mHistoryList.clear();
        notifyDataSetChanged();
    }

    public static String formatTime(@NonNull Context context, long j) {
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = currentTimeMillis - 86400000;
        long j3 = j * 1000;
        if (TimeUtil.isSameDate(j3, currentTimeMillis)) {
            return TimeUtil.formatTime(context, j3);
        }
        if (TimeUtil.isSameDate(j3, j2)) {
            return context.getString(C4558R.string.zm_lbl_yesterday);
        }
        return TimeUtil.formatDateWithSystem(context, j3);
    }

    public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean z) {
        if (compoundButton.getId() == C4558R.C4560id.checkDeleteItem) {
            String str = (String) compoundButton.getTag();
            if (!StringUtil.isEmptyOrNull(str)) {
                IPBXListView iPBXListView = this.mListView;
                if (iPBXListView != null) {
                    iPBXListView.delete(str, z);
                    boolean z2 = true;
                    if (z) {
                        this.mSelectItem.add(str);
                        MainObservable.getInstance().notifyObservers(Boolean.valueOf(true));
                        if (this.mSelectItem.size() == this.mHistoryList.size()) {
                            MainObservable.getInstance().notifyObservers(Integer.valueOf(2));
                            return;
                        }
                        return;
                    }
                    this.mSelectItem.remove(str);
                    MainObservable instance = MainObservable.getInstance();
                    if (this.mSelectItem.size() <= 0) {
                        z2 = false;
                    }
                    instance.notifyObservers(Boolean.valueOf(z2));
                    MainObservable.getInstance().notifyObservers(Integer.valueOf(0));
                }
            }
        }
    }
}
