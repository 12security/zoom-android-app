package p021us.zoom.androidlib.widget.recyclerviewhelper;

/* renamed from: us.zoom.androidlib.widget.recyclerviewhelper.RVHAdapter */
public interface RVHAdapter {
    boolean isCanSwipe();

    void onItemDismiss(int i, int i2);

    boolean onItemMove(int i, int i2);
}
