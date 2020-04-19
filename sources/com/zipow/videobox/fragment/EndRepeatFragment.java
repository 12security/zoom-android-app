package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import java.util.Calendar;
import java.util.Date;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class EndRepeatFragment extends ZMDialogFragment {
    private static final String ARG_END_REPEAT = "endRepeat";
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    private DatePicker mDatePicker;
    /* access modifiers changed from: private */
    @Nullable
    public Date mEndRepeat;

    private void updateUI() {
    }

    public static void showInActivity(ZMActivity zMActivity, Date date) {
        EndRepeatFragment endRepeatFragment = new EndRepeatFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_END_REPEAT, date);
        endRepeatFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, endRepeatFragment, EndRepeatFragment.class.getName()).commit();
    }

    public static void showDialog(@NonNull FragmentManager fragmentManager, Date date) {
        if (getMeetingInfoFragment(fragmentManager) == null) {
            EndRepeatFragment endRepeatFragment = new EndRepeatFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_END_REPEAT, date);
            endRepeatFragment.setArguments(bundle);
            endRepeatFragment.show(fragmentManager, EndRepeatFragment.class.getName());
        }
    }

    @Nullable
    public static EndRepeatFragment getMeetingInfoFragment(FragmentManager fragmentManager) {
        return (EndRepeatFragment) fragmentManager.findFragmentByTag(EndRepeatFragment.class.getName());
    }

    public EndRepeatFragment() {
        setCancelable(true);
    }

    public void onStart() {
        super.onStart();
    }

    private View createView(LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_end_repeat, null);
        this.mDatePicker = (DatePicker) inflate.findViewById(C4558R.C4560id.datePicker);
        Bundle arguments = getArguments();
        if (arguments == null) {
            return null;
        }
        this.mEndRepeat = (Date) arguments.getSerializable(ARG_END_REPEAT);
        if (bundle != null) {
            this.mEndRepeat = (Date) bundle.getSerializable("mEndRepeat");
        }
        if (this.mEndRepeat == null) {
            this.mEndRepeat = new Date();
        }
        this.mCalendar = Calendar.getInstance();
        this.mCalendar.setTime(this.mEndRepeat);
        this.mDatePicker.init(this.mCalendar.get(1), this.mCalendar.get(2), this.mCalendar.get(5), new OnDateChangedListener() {
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
                EndRepeatFragment.this.mCalendar.set(1, i);
                EndRepeatFragment.this.mCalendar.set(2, i2);
                EndRepeatFragment.this.mCalendar.set(5, i3);
                EndRepeatFragment endRepeatFragment = EndRepeatFragment.this;
                endRepeatFragment.mEndRepeat = endRepeatFragment.mCalendar.getTime();
            }
        });
        return inflate;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        View createView = createView(getActivity().getLayoutInflater(), null, bundle);
        if (createView == null) {
            return createEmptyDialog();
        }
        ZMAlertDialog create = new Builder(getActivity()).setView(createView).setTitle(C4558R.string.zm_lbl_end_repeat).setPositiveButton(C4558R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                EndRepeatFragment.this.onClickBtnDone();
            }
        }).setNegativeButton(C4558R.string.zm_btn_repeat_forever, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                EndRepeatFragment.this.onClickBtnRepeatForever();
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        return create;
    }

    /* access modifiers changed from: private */
    public void onClickBtnRepeatForever() {
        this.mEndRepeat = new Date(0);
        onClickBtnDone();
    }

    /* access modifiers changed from: private */
    public void onClickBtnDone() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        ScheduleFragment scheduleFragment = (ScheduleFragment) getParentFragment();
        if (scheduleFragment != null) {
            Date date = this.mEndRepeat;
            if (date != null) {
                scheduleFragment.onSelectEndRepeat(date);
            }
        }
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mEndRepeat", this.mEndRepeat);
    }
}
