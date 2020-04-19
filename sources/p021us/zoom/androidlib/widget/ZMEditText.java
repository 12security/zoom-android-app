package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

/* renamed from: us.zoom.androidlib.widget.ZMEditText */
public class ZMEditText extends EditText {
    private List<onSelectionChangedListener> listeners = new ArrayList();

    /* renamed from: us.zoom.androidlib.widget.ZMEditText$onSelectionChangedListener */
    public interface onSelectionChangedListener {
        void onEditTextSelectionChanged(int i, int i2);
    }

    private void initView() {
    }

    public ZMEditText(Context context) {
        super(context);
        initView();
    }

    public ZMEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ZMEditText(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public void addOnSelectionChangedListener(onSelectionChangedListener onselectionchangedlistener) {
        this.listeners.add(onselectionchangedlistener);
    }

    /* access modifiers changed from: protected */
    public void onSelectionChanged(int i, int i2) {
        super.onSelectionChanged(i, i2);
        List<onSelectionChangedListener> list = this.listeners;
        if (list != null && list.size() > 0) {
            for (onSelectionChangedListener onEditTextSelectionChanged : this.listeners) {
                onEditTextSelectionChanged.onEditTextSelectionChanged(i, i2);
            }
        }
    }
}
