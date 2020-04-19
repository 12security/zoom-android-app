package com.zipow.videobox;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = 26)
public class PBXJobService extends JobService {
    public static final long JOB_OVERDIDE_DEADLINE = 100;
    public static final int PBX_CANCEL_CALL_JOB_ID = 2;
    public static final int PBX_INCOMING_CALL_JOB_ID = 1;
    private static final String TAG = "PBXJobService";
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            if (message.arg1 <= 30) {
                Message obtainMessage = PBXJobService.this.mHandler.obtainMessage(message.what);
                obtainMessage.arg1 = message.arg1 + 1;
                PBXJobService.this.mHandler.sendMessageDelayed(obtainMessage, 5000);
                return;
            }
            JobScheduler jobScheduler = (JobScheduler) PBXJobService.this.getSystemService(JobScheduler.class);
            if (jobScheduler != null) {
                jobScheduler.cancelAll();
            }
        }
    };

    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public boolean onStartJob(JobParameters jobParameters) {
        this.mHandler.removeMessages(0);
        this.mHandler.sendEmptyMessage(0);
        return true;
    }

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
