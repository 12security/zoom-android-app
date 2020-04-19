package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import com.zipow.videobox.fragment.StarredConcactFragment;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class StarredActivity extends ZMActivity implements OnClickListener {
    private View backBtn;

    public static void launch(Context context) {
        ActivityStartHelper.startActivityForeground(context, new Intent(context, StarredActivity.class));
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C4558R.layout.activity_starred);
        this.backBtn = findViewById(C4558R.C4560id.zm_starred_title_back_btn);
        this.backBtn.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(C4558R.C4560id.zm_starred_fragment_container, new StarredConcactFragment()).commit();
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.zm_starred_title_back_btn) {
            finish();
        }
    }
}
