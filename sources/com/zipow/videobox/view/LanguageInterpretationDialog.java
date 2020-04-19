package com.zipow.videobox.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.InterpretationMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.widget.ZMCheckedTextView;
import p021us.zoom.androidlib.widget.ZMChoiceAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class LanguageInterpretationDialog extends ZMDialogFragment implements OnClickListener {
    private static final String TAG = "LanguageInterpretationDialog";
    /* access modifiers changed from: private */
    public ZMChoiceAdapter<ZMSimpleMenuItem> mAdapter;
    private int[] mAvailableInterpreteLansList;
    private View mBack;
    /* access modifiers changed from: private */
    public ZMCheckedTextView mChkMuteOriginalAudio;
    private View mDone;
    /* access modifiers changed from: private */
    public View mOptionMuteOriginalAudio;
    /* access modifiers changed from: private */
    public int mPos;
    private ListView mShowList;

    public static void show(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager != null && ConfLocalHelper.isInterpretationStarted(ConfMgr.getInstance().getInterpretationObj())) {
            Bundle bundle = new Bundle();
            LanguageInterpretationDialog languageInterpretationDialog = new LanguageInterpretationDialog();
            languageInterpretationDialog.setArguments(bundle);
            languageInterpretationDialog.show(fragmentManager, LanguageInterpretationDialog.class.getName());
        }
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null && ConfLocalHelper.isInterpretationStarted(ConfMgr.getInstance().getInterpretationObj())) {
            ZMActivity zMActivity2 = zMActivity;
            SimpleActivity.show(zMActivity2, LanguageInterpretationDialog.class.getName(), new Bundle(), 0, false, true);
        }
    }

    public static void hide(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            LanguageInterpretationDialog languageInterpretationDialog = (LanguageInterpretationDialog) fragmentManager.findFragmentByTag(LanguageInterpretationDialog.class.getName());
            if (languageInterpretationDialog != null) {
                languageInterpretationDialog.dismiss();
            }
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
        setCancelable(true);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        int i;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_language_interpretation, null);
        this.mShowList = (ListView) inflate.findViewById(C4558R.C4560id.show_languages);
        this.mBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBack.setOnClickListener(this);
        this.mDone = inflate.findViewById(C4558R.C4560id.btnDone);
        this.mDone.setOnClickListener(this);
        this.mOptionMuteOriginalAudio = inflate.findViewById(C4558R.C4560id.optionMuteOriginalAudio);
        this.mChkMuteOriginalAudio = (ZMCheckedTextView) inflate.findViewById(C4558R.C4560id.chkMuteOriginalAudio);
        this.mOptionMuteOriginalAudio.setOnClickListener(this);
        this.mAdapter = new ZMChoiceAdapter<>(getActivity(), C4558R.C4559drawable.zm_group_type_select, getString(C4558R.string.zm_accessibility_icon_item_selected_19247));
        this.mShowList.setAdapter(this.mAdapter);
        this.mShowList.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                for (int i2 = 0; i2 < LanguageInterpretationDialog.this.mAdapter.getCount(); i2++) {
                    ZMSimpleMenuItem zMSimpleMenuItem = (ZMSimpleMenuItem) adapterView.getItemAtPosition(i2);
                    if (i2 == i) {
                        LanguageInterpretationDialog.this.mPos = i;
                        zMSimpleMenuItem.setSelected(true);
                    } else {
                        zMSimpleMenuItem.setSelected(false);
                    }
                }
                if (LanguageInterpretationDialog.this.mPos == 0) {
                    LanguageInterpretationDialog.this.mOptionMuteOriginalAudio.setVisibility(8);
                } else {
                    InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
                    if (interpretationObj != null) {
                        LanguageInterpretationDialog.this.mChkMuteOriginalAudio.setChecked(!interpretationObj.isOriginalAudioChannelEnabled());
                    }
                    LanguageInterpretationDialog.this.mOptionMuteOriginalAudio.setVisibility(0);
                }
                LanguageInterpretationDialog.this.mAdapter.notifyDataSetChanged();
            }
        });
        inflate.setOnClickListener(this);
        InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
        int i2 = 0;
        this.mPos = 0;
        if (interpretationObj != null) {
            i = interpretationObj.getParticipantActiveLan();
            this.mChkMuteOriginalAudio.setChecked(!interpretationObj.isOriginalAudioChannelEnabled());
        } else {
            i = -1;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ZMSimpleMenuItem(getResources().getString(C4558R.string.zm_language_interpretation_original_audio_103374), (Drawable) null));
        if (interpretationObj != null) {
            this.mAvailableInterpreteLansList = interpretationObj.getAvailableInterpreteLansList();
            if (this.mAvailableInterpreteLansList != null) {
                while (true) {
                    int[] iArr = this.mAvailableInterpreteLansList;
                    if (i2 >= iArr.length) {
                        break;
                    }
                    int i3 = iArr[i2];
                    if (i == i3) {
                        this.mPos = i2 + 1;
                    }
                    arrayList.add(new ZMSimpleMenuItem(getResources().getString(InterpretationMgr.LAN_NAME_IDS[i3]), (Drawable) null));
                    i2++;
                }
            }
        }
        this.mAdapter.addAll((List<MenuItemType>) arrayList);
        return inflate;
    }

    public void onStart() {
        super.onStart();
        this.mShowList.performItemClick(null, this.mPos, 0);
    }

    public void onClick(View view) {
        boolean z = false;
        if (view == this.mBack) {
            finishFragment(false);
            return;
        }
        if (view == this.mOptionMuteOriginalAudio) {
            InterpretationMgr interpretationObj = ConfMgr.getInstance().getInterpretationObj();
            if (interpretationObj != null) {
                if (!(!this.mChkMuteOriginalAudio.isChecked())) {
                    z = true;
                }
                interpretationObj.setOriginalAudioChannelEnable(z);
                this.mChkMuteOriginalAudio.setChecked(!interpretationObj.isOriginalAudioChannelEnabled());
            }
        } else if (view == this.mDone) {
            InterpretationMgr interpretationObj2 = ConfMgr.getInstance().getInterpretationObj();
            if (interpretationObj2 != null) {
                int i = this.mPos;
                interpretationObj2.setParticipantActiveLan(i == 0 ? -1 : this.mAvailableInterpreteLansList[i - 1]);
            }
            finishFragment(false);
        }
    }
}
