package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.AudioOptionActivity;
import com.zipow.videobox.confapp.meeting.AudioOptionParcelItem;
import com.zipow.videobox.view.adapter.ZmAudioOptionAdapter;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHItemTouchHelperCallback;
import p021us.zoom.androidlib.widget.recyclerviewhelper.RVHViewHolder;
import p021us.zoom.videomeetings.C4558R;

public class AudioOptionFragment extends ZMDialogFragment implements OnClickListener {
    public static final int REQUEST_SELECT_DIAL_IN_COUNTRIES = 1;
    /* access modifiers changed from: private */
    @Nullable
    public ZmAudioOptionAdapter mAudioOptionAdapter;
    /* access modifiers changed from: private */
    @Nullable
    public AudioOptionParcelItem mAudioOptionParcelItem = new AudioOptionParcelItem();

    public static void showInActivity(ZMActivity zMActivity, AudioOptionParcelItem audioOptionParcelItem) {
        AudioOptionFragment audioOptionFragment = new AudioOptionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AudioOptionActivity.ARG_SELECT_AUDIO_OPTION_ITEM, audioOptionParcelItem);
        audioOptionFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, audioOptionFragment, AudioOptionFragment.class.getName()).commit();
    }

    @Nullable
    public static AudioOptionFragment getAudioOptionFragment(ZMActivity zMActivity) {
        return (AudioOptionFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(AudioOptionFragment.class.getName());
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C4558R.layout.zm_audio_option, null);
    }

    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mAudioOptionParcelItem = (AudioOptionParcelItem) arguments.getParcelable(AudioOptionActivity.ARG_SELECT_AUDIO_OPTION_ITEM);
            if (this.mAudioOptionParcelItem == null) {
                this.mAudioOptionParcelItem = new AudioOptionParcelItem();
            }
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(C4558R.C4560id.recyclerView);
        view.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        view.findViewById(C4558R.C4560id.btnOK).setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAudioOptionAdapter = new ZmAudioOptionAdapter((ZMActivity) getActivity(), this.mAudioOptionParcelItem);
        recyclerView.setAdapter(this.mAudioOptionAdapter);
        C24771 r0 = new RVHItemTouchHelperCallback(this.mAudioOptionAdapter, true, false, true) {
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (viewHolder instanceof RVHViewHolder) {
                    ((RVHViewHolder) viewHolder).onItemClear();
                    if (AudioOptionFragment.this.mAudioOptionParcelItem != null) {
                        AudioOptionFragment.this.mAudioOptionParcelItem.setmSelectedDialInCountries(AudioOptionFragment.this.mAudioOptionAdapter.getSelectedCountries());
                    }
                }
            }
        };
        new ItemTouchHelper(r0).attachToRecyclerView(recyclerView);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1 && intent != null) {
            this.mAudioOptionAdapter.refresh(intent.getStringArrayListExtra(SelectDialInCountryFragment.RESULT_ARG_ADD_DIALIN_COUNTRIES), intent.getStringArrayListExtra(SelectDialInCountryFragment.RESULT_ARG_MINUS_DIALIN_COUNTRIES));
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnOK) {
            onClickOk();
        }
    }

    private void onClickBtnBack() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    private void onClickOk() {
        AudioOptionActivity audioOptionActivity = (AudioOptionActivity) getActivity();
        if (audioOptionActivity != null) {
            audioOptionActivity.onOkDone(this.mAudioOptionParcelItem);
        }
    }
}
