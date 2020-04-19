package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.gson.JsonParser;
import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSelectCustomListView */
public class MMSelectCustomListView extends ListView {
    private MMSelectCustomAdapter mAdapter;

    /* renamed from: com.zipow.videobox.view.mm.MMSelectCustomListView$MMSelectCustomAdapter */
    static class MMSelectCustomAdapter extends BaseAdapter {
        private Context mContext;
        @NonNull
        private List<IMessageTemplateSelectItem> mData = new ArrayList();
        @NonNull
        private List<IMessageTemplateSelectItem> mDisplayData = new ArrayList();
        private boolean mIsMultSelect = false;
        private String mKey;
        @Nullable
        private List<IMessageTemplateSelectItem> mPreSelects = null;
        @NonNull
        private ArrayList<IMessageTemplateSelectItem> mSelects = new ArrayList<>();

        public long getItemId(int i) {
            return (long) i;
        }

        public MMSelectCustomAdapter(Context context) {
            this.mContext = context;
        }

        public void setFilter(String str) {
            this.mKey = str;
        }

        public boolean isItemSelected(@Nullable IMessageTemplateSelectItem iMessageTemplateSelectItem) {
            if (iMessageTemplateSelectItem == null) {
                return false;
            }
            return this.mSelects.contains(iMessageTemplateSelectItem);
        }

        public void setData(@Nullable List<IMessageTemplateSelectItem> list) {
            if (!CollectionsUtil.isListEmpty(list)) {
                this.mData.clear();
                this.mData.addAll(list);
            }
        }

        public void onItemClicked(@Nullable IMessageTemplateSelectItem iMessageTemplateSelectItem) {
            if (iMessageTemplateSelectItem != null) {
                List<IMessageTemplateSelectItem> list = this.mPreSelects;
                if (list == null || !list.contains(iMessageTemplateSelectItem)) {
                    if (!this.mSelects.contains(iMessageTemplateSelectItem)) {
                        this.mSelects.add(iMessageTemplateSelectItem);
                    } else {
                        this.mSelects.remove(iMessageTemplateSelectItem);
                    }
                }
            }
        }

        public void unselectItem(@Nullable IMessageTemplateSelectItem iMessageTemplateSelectItem) {
            if (iMessageTemplateSelectItem != null) {
                this.mSelects.remove(iMessageTemplateSelectItem);
            }
        }

        public void setIsMultSelect(boolean z) {
            this.mIsMultSelect = z;
        }

        public void setPreSelects(@Nullable List<IMessageTemplateSelectItem> list) {
            this.mPreSelects = list;
        }

        @NonNull
        public ArrayList<IMessageTemplateSelectItem> getSelectedItems() {
            return this.mSelects;
        }

        public int getCount() {
            return this.mDisplayData.size();
        }

        @Nullable
        public Object getItem(int i) {
            if (i < 0 || i >= this.mDisplayData.size()) {
                return null;
            }
            return this.mDisplayData.get(i);
        }

        public void notifyDataSetChanged() {
            sortAll();
            super.notifyDataSetChanged();
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            IMessageTemplateSelectItem iMessageTemplateSelectItem = (IMessageTemplateSelectItem) getItem(i);
            if (iMessageTemplateSelectItem == null) {
                return null;
            }
            if (view == null) {
                view = View.inflate(this.mContext, C4558R.layout.zm_mm_select_custom_list_item, null);
            }
            AvatarView avatarView = (AvatarView) view.findViewById(C4558R.C4560id.avatarView);
            CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(C4558R.C4560id.check);
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.select_text);
            if (this.mIsMultSelect) {
                checkedTextView.setVisibility(0);
                List<IMessageTemplateSelectItem> list = this.mPreSelects;
                if (list == null || !list.contains(iMessageTemplateSelectItem)) {
                    checkedTextView.setEnabled(true);
                    checkedTextView.setChecked(this.mSelects.contains(iMessageTemplateSelectItem));
                } else {
                    checkedTextView.setEnabled(false);
                    checkedTextView.setChecked(true);
                }
            } else {
                checkedTextView.setVisibility(8);
            }
            avatarView.show(new ParamsBuilder().setName(iMessageTemplateSelectItem.getText(), iMessageTemplateSelectItem.getText()));
            textView.setText(iMessageTemplateSelectItem.getText());
            return view;
        }

        private void sortAll() {
            this.mDisplayData.clear();
            for (IMessageTemplateSelectItem iMessageTemplateSelectItem : this.mData) {
                if ((TextUtils.isEmpty(this.mKey) || iMessageTemplateSelectItem.getText() == null || iMessageTemplateSelectItem.getText().contains(this.mKey)) && iMessageTemplateSelectItem.getText() != null) {
                    this.mDisplayData.add(iMessageTemplateSelectItem);
                }
            }
            Collections.sort(this.mDisplayData, new Comparator<IMessageTemplateSelectItem>() {
                public int compare(@NonNull IMessageTemplateSelectItem iMessageTemplateSelectItem, IMessageTemplateSelectItem iMessageTemplateSelectItem2) {
                    return SortUtil.fastCompare(SortUtil.getSortKey(iMessageTemplateSelectItem.getText(), CompatUtils.getLocalDefault()), SortUtil.getSortKey(iMessageTemplateSelectItem.getText(), CompatUtils.getLocalDefault()));
                }
            });
        }
    }

    public MMSelectCustomListView(Context context) {
        super(context);
        initView();
    }

    public MMSelectCustomListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMSelectCustomListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    @RequiresApi(api = 21)
    public MMSelectCustomListView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView();
    }

    private void initView() {
        this.mAdapter = new MMSelectCustomAdapter(getContext());
        setAdapter(this.mAdapter);
    }

    public void setFilter(String str) {
        this.mAdapter.setFilter(str);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onItemClicked(IMessageTemplateSelectItem iMessageTemplateSelectItem) {
        this.mAdapter.onItemClicked(iMessageTemplateSelectItem);
        this.mAdapter.notifyDataSetChanged();
    }

    public void setIsMultSelect(boolean z) {
        this.mAdapter.setIsMultSelect(z);
    }

    public void setPreSelects(@Nullable List<String> list) {
        if (list != null) {
            ArrayList arrayList = new ArrayList();
            for (String parse : list) {
                try {
                    arrayList.add(IMessageTemplateSelectItem.parse(new JsonParser().parse(parse).getAsJsonObject()));
                } catch (Exception unused) {
                }
            }
            this.mAdapter.setPreSelects(arrayList);
        }
    }

    public void setData(@Nullable List<String> list) {
        if (list != null) {
            ArrayList arrayList = new ArrayList();
            for (String parse : list) {
                try {
                    arrayList.add(IMessageTemplateSelectItem.parse(new JsonParser().parse(parse).getAsJsonObject()));
                } catch (Exception unused) {
                }
            }
            this.mAdapter.setData(arrayList);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @NonNull
    public ArrayList<IMessageTemplateSelectItem> getSelectedItems() {
        return this.mAdapter.getSelectedItems();
    }

    @Nullable
    public IMessageTemplateSelectItem getItem(int i) {
        MMSelectCustomAdapter mMSelectCustomAdapter = this.mAdapter;
        if (mMSelectCustomAdapter != null) {
            return (IMessageTemplateSelectItem) mMSelectCustomAdapter.getItem(i);
        }
        return null;
    }

    public boolean isItemSelected(IMessageTemplateSelectItem iMessageTemplateSelectItem) {
        return this.mAdapter.isItemSelected(iMessageTemplateSelectItem);
    }

    public void unsesectItem(IMessageTemplateSelectItem iMessageTemplateSelectItem) {
        MMSelectCustomAdapter mMSelectCustomAdapter = this.mAdapter;
        if (mMSelectCustomAdapter != null) {
            mMSelectCustomAdapter.unselectItem(iMessageTemplateSelectItem);
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
