package p021us.zoom.androidlib.util;

import androidx.annotation.NonNull;

/* renamed from: us.zoom.androidlib.util.EventAction */
public abstract class EventAction {
    private String mName;

    public abstract void run(IUIElement iUIElement);

    public EventAction() {
        this(null);
    }

    public EventAction(String str) {
        this.mName = str;
    }

    public String getName() {
        return this.mName;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[EventAction:");
        sb.append(this.mName);
        sb.append("]");
        return sb.toString();
    }
}
