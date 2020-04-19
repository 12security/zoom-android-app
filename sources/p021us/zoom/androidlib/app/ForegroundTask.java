package p021us.zoom.androidlib.app;

/* renamed from: us.zoom.androidlib.app.ForegroundTask */
public abstract class ForegroundTask {
    private String mName = null;

    public boolean hasAnotherProcessAtFront() {
        return false;
    }

    public boolean isExpired() {
        return false;
    }

    public boolean isMultipleInstancesAllowed() {
        return true;
    }

    public boolean isOtherProcessSupported() {
        return true;
    }

    public boolean isValidActivity(String str) {
        return true;
    }

    public abstract void run(ZMActivity zMActivity);

    public ForegroundTask() {
    }

    public ForegroundTask(String str) {
        this.mName = str;
    }

    public String getName() {
        return this.mName;
    }
}
