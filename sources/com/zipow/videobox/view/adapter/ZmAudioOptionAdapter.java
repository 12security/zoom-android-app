package com.zipow.videobox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.confapp.meeting.AudioOptionItemModel;
import com.zipow.videobox.confapp.meeting.AudioOptionParcelItem;
import com.zipow.videobox.fragment.AudioOptionFragment;
import com.zipow.videobox.fragment.SelectDialInCountryFragment;
import com.zipow.videobox.fragment.SelectDialInCountryFragment.DialInCountry;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AvailableDialinCountry;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ZMUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.adapter.ZmSingleChoiceAdapter;
import p021us.zoom.androidlib.widget.adapter.ZmSingleChoiceItem;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHAdapter;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener.OnItemClickListener;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHViewHolder;
import p021us.zoom.videomeetings.C4558R;

public class ZmAudioOptionAdapter extends Adapter implements RVHAdapter {
    /* access modifiers changed from: private */
    public final ZMActivity mActivity;
    private List<AudioOptionItemModel> mAudioOptionItemModels;
    /* access modifiers changed from: private */
    public final AudioOptionParcelItem mAudioOptionParcelItem;
    /* access modifiers changed from: private */
    public boolean mEnableShowIncludeTollfree = true;

    public class FooterViewHolder extends ViewHolder implements OnClickListener {
        final CheckedTextView chkIncludeTollFree;

        public FooterViewHolder(@NonNull View view) {
            super(view);
            this.chkIncludeTollFree = (CheckedTextView) view.findViewById(C4558R.C4560id.chkIncludeTollFree);
            View findViewById = view.findViewById(C4558R.C4560id.optionIncludeTollFree);
            View findViewById2 = view.findViewById(C4558R.C4560id.viewFooterDivider);
            int i = 0;
            findViewById.setVisibility(ZmAudioOptionAdapter.this.mEnableShowIncludeTollfree ? 0 : 8);
            if (!ZmAudioOptionAdapter.this.mEnableShowIncludeTollfree) {
                i = 8;
            }
            findViewById2.setVisibility(i);
            view.findViewById(C4558R.C4560id.optionIncludeTollFree).setOnClickListener(this);
            view.findViewById(C4558R.C4560id.txtEditCountry).setOnClickListener(this);
        }

        public void bind(boolean z) {
            this.chkIncludeTollFree.setChecked(z);
        }

        public void onClick(@NonNull View view) {
            int id = view.getId();
            if (id == C4558R.C4560id.optionIncludeTollFree) {
                boolean z = !this.chkIncludeTollFree.isChecked();
                this.chkIncludeTollFree.setChecked(z);
                ZmAudioOptionAdapter.this.mAudioOptionParcelItem.setIncludeTollFree(z);
            } else if (id == C4558R.C4560id.txtEditCountry) {
                AudioOptionFragment audioOptionFragment = AudioOptionFragment.getAudioOptionFragment(ZmAudioOptionAdapter.this.mActivity);
                if (audioOptionFragment != null) {
                    SelectDialInCountryFragment.showAsActivity(audioOptionFragment, 1, getDialInCountries(), ZmAudioOptionAdapter.this.mAudioOptionParcelItem.getmShowSelectedDialInCountries());
                }
            }
        }

        @NonNull
        private ArrayList<DialInCountry> getDialInCountries() {
            ArrayList<DialInCountry> arrayList = new ArrayList<>();
            List list = ZmAudioOptionAdapter.this.mAudioOptionParcelItem.getmAllDialInCountries();
            if (list != null && !list.isEmpty()) {
                List list2 = ZmAudioOptionAdapter.this.mAudioOptionParcelItem.getmShowSelectedDialInCountries();
                if (list2 == null) {
                    list2 = new ArrayList();
                }
                for (String str : ZmAudioOptionAdapter.this.mAudioOptionParcelItem.getmAllDialInCountries()) {
                    arrayList.add(new DialInCountry(str, list2.contains(str)));
                }
            }
            return arrayList;
        }
    }

    public class HeaderViewHolder extends ViewHolder {
        @NonNull
        final ZmSingleChoiceAdapter mAudioOptionAdapter = new ZmSingleChoiceAdapter();
        final RecyclerView recyclerViewAudioOption;
        final TextView txtDialInSelectDesc;

        public HeaderViewHolder(@NonNull View view) {
            super(view);
            this.recyclerViewAudioOption = (RecyclerView) view.findViewById(C4558R.C4560id.recyclerViewAudioOption);
            this.recyclerViewAudioOption.setLayoutManager(new LinearLayoutManager(view.getContext()));
            this.txtDialInSelectDesc = (TextView) view.findViewById(C4558R.C4560id.txtDialInSelectDesc);
            this.recyclerViewAudioOption.setAdapter(this.mAudioOptionAdapter);
            this.recyclerViewAudioOption.addOnItemTouchListener(new RVHItemClickListener(ZmAudioOptionAdapter.this.mActivity, new OnItemClickListener(ZmAudioOptionAdapter.this) {
                public void onItemClick(View view, int i) {
                    ZmSingleChoiceItem item = HeaderViewHolder.this.mAudioOptionAdapter.getItem(i);
                    if (item != null) {
                        boolean isCanEditCountry = ZmAudioOptionAdapter.this.mAudioOptionParcelItem.isCanEditCountry();
                        ZmAudioOptionAdapter.this.mAudioOptionParcelItem.setmSelectedAudioType(((Integer) item.getData()).intValue());
                        ZmAudioOptionAdapter.this.onAudioOptionChange(isCanEditCountry, ZmAudioOptionAdapter.this.mAudioOptionParcelItem.isCanEditCountry());
                    }
                }
            }));
        }

        public void bind(@NonNull Context context, int i) {
            initAudioOption(context, i);
            this.txtDialInSelectDesc.setVisibility(ZmAudioOptionAdapter.this.mAudioOptionParcelItem.isCanEditCountry() ? 0 : 8);
        }

        private void initAudioOption(@NonNull Context context, int i) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                ArrayList arrayList = new ArrayList();
                ZmSingleChoiceItem zmSingleChoiceItem = new ZmSingleChoiceItem(Integer.valueOf(0), context.getString(C4558R.string.zm_lbl_audio_option_voip), C4558R.C4559drawable.zm_group_type_select, context.getString(C4558R.string.zm_accessibility_icon_item_selected_19247), i == 0);
                arrayList.add(zmSingleChoiceItem);
                if (!currentUserProfile.isDisablePSTN()) {
                    ZmSingleChoiceItem zmSingleChoiceItem2 = new ZmSingleChoiceItem(Integer.valueOf(1), context.getString(C4558R.string.zm_lbl_audio_option_telephony), C4558R.C4559drawable.zm_group_type_select, context.getString(C4558R.string.zm_accessibility_icon_item_selected_19247), i == 1);
                    arrayList.add(zmSingleChoiceItem2);
                    if (!currentUserProfile.isScheduleAudioBothDisabled()) {
                        ZmSingleChoiceItem zmSingleChoiceItem3 = new ZmSingleChoiceItem(Integer.valueOf(2), context.getString(C4558R.string.zm_lbl_audio_option_voip_and_telephony_detail_127873), C4558R.C4559drawable.zm_group_type_select, context.getString(C4558R.string.zm_accessibility_icon_item_selected_19247), i == 2);
                        arrayList.add(zmSingleChoiceItem3);
                    }
                }
                if (currentUserProfile.hasSelfTelephony()) {
                    ZmSingleChoiceItem zmSingleChoiceItem4 = new ZmSingleChoiceItem(Integer.valueOf(3), context.getString(C4558R.string.zm_lbl_audio_option_3rd_party_127873), C4558R.C4559drawable.zm_group_type_select, context.getString(C4558R.string.zm_accessibility_icon_item_selected_19247), i == 3);
                    arrayList.add(zmSingleChoiceItem4);
                }
                this.mAudioOptionAdapter.setmZmSingleChoiceItemList(arrayList);
            }
        }
    }

    public class SelectedCountryViewHolder extends ViewHolder implements RVHViewHolder {
        final TextView txtCountry;

        public SelectedCountryViewHolder(@NonNull View view) {
            super(view);
            this.txtCountry = (TextView) view.findViewById(C4558R.C4560id.txtCountry);
        }

        public void bind(String str) {
            this.txtCountry.setText(ZMUtils.getCountryName(str));
        }

        public void onItemSelected(int i) {
            if (i != 0) {
                this.itemView.setPressed(true);
            }
        }

        public void onItemClear() {
            this.itemView.setPressed(false);
        }
    }

    public ZmAudioOptionAdapter(ZMActivity zMActivity, AudioOptionParcelItem audioOptionParcelItem) {
        this.mActivity = zMActivity;
        this.mAudioOptionParcelItem = audioOptionParcelItem;
        updateData();
    }

    public void refresh(@Nullable List<String> list, @Nullable List<String> list2) {
        List list3 = this.mAudioOptionParcelItem.getmShowSelectedDialInCountries();
        if (list3 == null) {
            list3 = new ArrayList();
        }
        if (list2 != null && !list2.isEmpty()) {
            for (String indexOf : list2) {
                int indexOf2 = list3.indexOf(indexOf);
                int i = indexOf2 + 1;
                if (indexOf2 != -1 && i < this.mAudioOptionItemModels.size()) {
                    list3.remove(indexOf2);
                    this.mAudioOptionItemModels.remove(i);
                    notifyItemRemoved(i);
                    if (i != this.mAudioOptionItemModels.size() - 1) {
                        notifyItemRangeChanged(i, this.mAudioOptionItemModels.size() - i);
                    }
                }
            }
        }
        if (list != null && !list.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (String audioOptionItemModel : list) {
                arrayList.add(new AudioOptionItemModel(1, audioOptionItemModel));
            }
            if (this.mAudioOptionItemModels == null) {
                this.mAudioOptionItemModels = new ArrayList();
            }
            int findInsertedPos = findInsertedPos();
            this.mAudioOptionItemModels.addAll(findInsertedPos, arrayList);
            list3.addAll(list);
            notifyItemRangeInserted(findInsertedPos, arrayList.size());
            if (findInsertedPos != this.mAudioOptionItemModels.size() - 1) {
                notifyItemRangeChanged(findInsertedPos, this.mAudioOptionItemModels.size() - findInsertedPos);
            }
        }
        this.mAudioOptionParcelItem.setmSelectedDialInCountries(list3);
    }

    /* access modifiers changed from: private */
    public void onAudioOptionChange(boolean z, boolean z2) {
        if (z && !z2) {
            List<AudioOptionItemModel> list = this.mAudioOptionItemModels;
            if (list != null && !list.isEmpty()) {
                int size = ((this.mAudioOptionItemModels.size() - 1) - 1) + 1;
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        this.mAudioOptionItemModels.remove(1);
                    }
                    notifyItemRangeRemoved(1, size);
                }
            }
        } else if (!z && z2) {
            List<String> list2 = this.mAudioOptionParcelItem.getmShowSelectedDialInCountries();
            if (list2 == null) {
                list2 = new ArrayList<>();
            }
            ArrayList arrayList = new ArrayList();
            for (String audioOptionItemModel : list2) {
                arrayList.add(new AudioOptionItemModel(1, audioOptionItemModel));
            }
            arrayList.add(new AudioOptionItemModel(2, Integer.valueOf(2)));
            this.mAudioOptionItemModels.addAll(1, arrayList);
            notifyItemRangeInserted(1, arrayList.size());
            if (1 != this.mAudioOptionItemModels.size() - 1) {
                notifyItemRangeChanged(1, this.mAudioOptionItemModels.size() - 1);
            }
        }
        notifyDataSetChanged();
    }

    private int findInsertedPos() {
        List<AudioOptionItemModel> list = this.mAudioOptionItemModels;
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return this.mAudioOptionItemModels.size() - 1;
    }

    private void updateData() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AudioOptionItemModel(0, this.mAudioOptionParcelItem.getmShowSelectedDialInCountries()));
        List<String> list = this.mAudioOptionParcelItem.getmShowSelectedDialInCountries();
        if (list != null && !list.isEmpty()) {
            for (String audioOptionItemModel : list) {
                arrayList.add(new AudioOptionItemModel(1, audioOptionItemModel));
            }
        }
        if (this.mAudioOptionParcelItem.isCanEditCountry()) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                AvailableDialinCountry availableDiallinCountry = currentUserProfile.getAvailableDiallinCountry();
                if (availableDiallinCountry != null) {
                    this.mEnableShowIncludeTollfree = availableDiallinCountry.getEnableShowIncludeTollfree();
                }
            }
            arrayList.add(new AudioOptionItemModel(2, Integer.valueOf(2)));
        }
        this.mAudioOptionItemModels = arrayList;
    }

    @Nullable
    public ArrayList<String> getSelectedCountries() {
        List<AudioOptionItemModel> list = this.mAudioOptionItemModels;
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (AudioOptionItemModel audioOptionItemModel : this.mAudioOptionItemModels) {
            if (audioOptionItemModel != null && audioOptionItemModel.type == 1) {
                String str = (String) audioOptionItemModel.data;
                if (!StringUtil.isEmptyOrNull(str)) {
                    arrayList.add(str);
                }
            }
        }
        return arrayList;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_item_audio_option, viewGroup, false));
        }
        if (i == 2) {
            return new FooterViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_item_audio_option_footer, viewGroup, false));
        }
        return new SelectedCountryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C4558R.layout.zm_item_selected_dial_in_country, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) viewHolder).bind(this.mActivity, this.mAudioOptionParcelItem.getmSelectedAudioType());
        } else if (viewHolder instanceof FooterViewHolder) {
            ((FooterViewHolder) viewHolder).bind(this.mAudioOptionParcelItem.isIncludeTollFree());
        } else if (viewHolder instanceof SelectedCountryViewHolder) {
            ((SelectedCountryViewHolder) viewHolder).bind(((AudioOptionItemModel) this.mAudioOptionItemModels.get(i)).data.toString());
        }
    }

    public int getItemViewType(int i) {
        return ((AudioOptionItemModel) this.mAudioOptionItemModels.get(i)).type;
    }

    public int getItemCount() {
        List<AudioOptionItemModel> list = this.mAudioOptionItemModels;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public boolean onItemMove(int i, int i2) {
        swap(i, i2);
        return false;
    }

    public void onItemDismiss(int i, int i2) {
        remove(i);
    }

    public boolean isCanSwipe() {
        List list = this.mAudioOptionParcelItem.getmShowSelectedDialInCountries();
        if (list == null || list.size() <= 1) {
            return false;
        }
        return true;
    }

    private void remove(int i) {
        this.mAudioOptionItemModels.remove(i);
        notifyItemRemoved(i);
        this.mAudioOptionParcelItem.setmSelectedDialInCountries(getSelectedCountries());
    }

    private void swap(int i, int i2) {
        Collections.swap(this.mAudioOptionItemModels, i, i2);
        notifyItemMoved(i, i2);
    }
}
