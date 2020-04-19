package p021us.zoom.androidlib.util;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

/* renamed from: us.zoom.androidlib.util.ZMRecyclerView */
public class ZMRecyclerView extends RecyclerView {
    public ZMRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ZMRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMRecyclerView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void scrollToBottom(boolean z) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        Adapter adapter = getAdapter();
        if (adapter != null && linearLayoutManager != null) {
            final int itemCount = adapter.getItemCount() - 1;
            int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            if (z) {
                post(new Runnable() {
                    public void run() {
                        ZMRecyclerView.this.scrollToPosition(itemCount);
                    }
                });
            } else if (itemCount - findLastVisibleItemPosition < 5) {
                if (VERSION.SDK_INT >= 16) {
                    linearLayoutManager.scrollToPosition(itemCount);
                } else {
                    scrollToPosition(itemCount);
                }
            }
        }
    }
}
