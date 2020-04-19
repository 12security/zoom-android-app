package p021us.zoom.androidlib.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/* renamed from: us.zoom.androidlib.util.ViewPressEffectHelper */
public class ViewPressEffectHelper {

    /* renamed from: us.zoom.androidlib.util.ViewPressEffectHelper$ASetOnTouchListener */
    private static class ASetOnTouchListener implements OnTouchListener {
        final float HALF_ALPHA = 0.8f;
        float alphaOriginally = 1.0f;

        public ASetOnTouchListener(View view) {
            this.alphaOriginally = view.getAlpha();
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action != 3) {
                switch (action) {
                    case 0:
                        view.setAlpha(0.8f);
                        break;
                    case 1:
                        break;
                }
            }
            view.setAlpha(this.alphaOriginally);
            return false;
        }
    }

    public static void attach(View view) {
        view.setOnTouchListener(new ASetOnTouchListener(view));
    }
}
