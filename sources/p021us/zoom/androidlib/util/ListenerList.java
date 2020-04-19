package p021us.zoom.androidlib.util;

import java.util.Iterator;
import java.util.Vector;

/* renamed from: us.zoom.androidlib.util.ListenerList */
public class ListenerList {
    private Vector<IListener> mList = new Vector<>();

    public int add(IListener iListener) {
        int size;
        synchronized (this.mList) {
            if (iListener != null) {
                if (!this.mList.contains(iListener)) {
                    this.mList.add(iListener);
                }
            }
            size = this.mList.size();
        }
        return size;
    }

    public int remove(IListener iListener) {
        int size;
        synchronized (this.mList) {
            if (iListener != null) {
                this.mList.remove(iListener);
            }
            size = this.mList.size();
        }
        return size;
    }

    public int removeAll(IListener iListener) {
        int size;
        synchronized (this.mList) {
            if (iListener != null) {
                Vector vector = new Vector();
                vector.add(iListener);
                this.mList.removeAll(vector);
                vector.clear();
            }
            size = this.mList.size();
        }
        return size;
    }

    public int removeAllSameClass(IListener iListener) {
        int size;
        synchronized (this.mList) {
            if (iListener != null) {
                Iterator it = this.mList.iterator();
                while (it.hasNext()) {
                    if (it.next().getClass() == iListener.getClass()) {
                        it.remove();
                    }
                }
            }
            size = this.mList.size();
        }
        return size;
    }

    public void clear() {
        synchronized (this.mList) {
            this.mList.clear();
        }
    }

    public IListener[] getAll() {
        IListener[] iListenerArr;
        synchronized (this.mList) {
            iListenerArr = new IListener[this.mList.size()];
            this.mList.toArray(iListenerArr);
        }
        return iListenerArr;
    }

    public int size() {
        int size;
        synchronized (this.mList) {
            size = this.mList.size();
        }
        return size;
    }
}
