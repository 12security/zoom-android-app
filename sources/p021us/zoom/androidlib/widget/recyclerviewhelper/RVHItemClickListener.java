package p021us.zoom.androidlib.widget.recyclerviewhelper;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener;

/* renamed from: us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener */
public class RVHItemClickListener implements OnItemTouchListener {
    private final GestureDetector mGestureDetector;
    private final OnItemClickListener mListener;

    /* renamed from: us.zoom.androidlib.widget.recyclerviewhelper.RVHItemClickListener$OnItemClickListener */
    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    public RVHItemClickListener(Context context, OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
        this.mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }
        });
    }

    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null || this.mListener == null || !this.mGestureDetector.onTouchEvent(motionEvent)) {
            return false;
        }
        this.mListener.onItemClick(findChildViewUnder, recyclerView.getChildAdapterPosition(findChildViewUnder));
        return true;
    }
}
