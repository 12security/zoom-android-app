package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import androidx.annotation.NonNull;
import java.util.Calendar;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMDatePickerDialog */
public class ZMDatePickerDialog extends ZMAlertDialog implements OnClickListener, OnDateChangedListener {
    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";
    int currentapiVersion;
    private final Calendar mCalendar;
    private final OnDateSetListener mCallBack;
    private final DatePicker mDatePicker;
    private boolean mTitleNeedsUpdate;

    /* renamed from: us.zoom.androidlib.widget.ZMDatePickerDialog$OnDateSetListener */
    public interface OnDateSetListener {
        void onDateSet(DatePicker datePicker, int i, int i2, int i3);
    }

    public ZMDatePickerDialog(Context context, OnDateSetListener onDateSetListener, int i, int i2, int i3) {
        this(context, 0, onDateSetListener, i, i2, i3);
    }

    public ZMDatePickerDialog(Context context, int i, OnDateSetListener onDateSetListener, int i2, int i3, int i4) {
        super(context, i);
        this.currentapiVersion = VERSION.SDK_INT;
        this.mTitleNeedsUpdate = true;
        this.mCallBack = onDateSetListener;
        this.mCalendar = Calendar.getInstance();
        Context context2 = getContext();
        setButton(-1, context2.getText(C4409R.string.zm_date_time_set), this);
        setButton(-3, context2.getText(C4409R.string.zm_date_time_cancel), this);
        View inflate = ((LayoutInflater) context2.getSystemService("layout_inflater")).inflate(C4409R.layout.zm_date_picker_dialog, null);
        setView(inflate);
        this.mDatePicker = (DatePicker) inflate.findViewById(C4409R.C4411id.datePicker);
        this.mDatePicker.init(i2, i3, i4, this);
        if (this.currentapiVersion >= 11) {
            this.mDatePicker.setCalendarViewShown(false);
        }
        updateTitle(i2, i3, i4);
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -1) {
            tryNotifyDateSet();
        } else {
            dismiss();
        }
    }

    public void onDateChanged(DatePicker datePicker, int i, int i2, int i3) {
        this.mDatePicker.init(i, i2, i3, this);
        updateTitle(i, i2, i3);
    }

    public DatePicker getDatePicker() {
        return this.mDatePicker;
    }

    public void updateDate(int i, int i2, int i3) {
        this.mDatePicker.updateDate(i, i2, i3);
    }

    private void tryNotifyDateSet() {
        if (this.mCallBack != null) {
            this.mDatePicker.clearFocus();
            OnDateSetListener onDateSetListener = this.mCallBack;
            DatePicker datePicker = this.mDatePicker;
            onDateSetListener.onDateSet(datePicker, datePicker.getYear(), this.mDatePicker.getMonth(), this.mDatePicker.getDayOfMonth());
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

    private void updateTitle(int i, int i2, int i3) {
        if (this.currentapiVersion < 11) {
            updateTitl(i, i2, i3);
        } else if (!this.mDatePicker.getCalendarViewShown()) {
            updateTitl(i, i2, i3);
        } else if (this.mTitleNeedsUpdate) {
            this.mTitleNeedsUpdate = false;
            setTitle(OAuth.SCOPE_DELIMITER);
        }
    }

    private void updateTitl(int i, int i2, int i3) {
        this.mCalendar.set(1, i);
        this.mCalendar.set(2, i2);
        this.mCalendar.set(5, i3);
        setTitle(DateUtils.formatDateTime(this.mContext, this.mCalendar.getTimeInMillis(), 98326));
        this.mTitleNeedsUpdate = true;
    }

    @NonNull
    public Bundle onSaveInstanceState() {
        Bundle onSaveInstanceState = super.onSaveInstanceState();
        onSaveInstanceState.putInt(YEAR, this.mDatePicker.getYear());
        onSaveInstanceState.putInt(MONTH, this.mDatePicker.getMonth());
        onSaveInstanceState.putInt(DAY, this.mDatePicker.getDayOfMonth());
        return onSaveInstanceState;
    }

    public void onRestoreInstanceState(@NonNull Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mDatePicker.init(bundle.getInt(YEAR), bundle.getInt(MONTH), bundle.getInt(DAY), this);
    }

    public void show(long j, long j2) {
        DatePicker datePicker = this.mDatePicker;
        if (datePicker != null) {
            if (j2 > 0) {
                datePicker.setMinDate(j2);
            }
            if (j > 0) {
                this.mDatePicker.setMaxDate(j);
            }
        }
        super.show();
    }
}
