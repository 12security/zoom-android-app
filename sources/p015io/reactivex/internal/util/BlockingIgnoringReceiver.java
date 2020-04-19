package p015io.reactivex.internal.util;

import java.util.concurrent.CountDownLatch;
import p015io.reactivex.functions.Action;
import p015io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.util.BlockingIgnoringReceiver */
public final class BlockingIgnoringReceiver extends CountDownLatch implements Consumer<Throwable>, Action {
    public Throwable error;

    public BlockingIgnoringReceiver() {
        super(1);
    }

    public void accept(Throwable th) {
        this.error = th;
        countDown();
    }

    public void run() {
        countDown();
    }
}
