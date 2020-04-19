package com.zipow.videobox.confapp.meeting.confhelper;

import androidx.annotation.Nullable;

public interface IAssembleConfComponent {
    @Nullable
    BOComponent getmBOComponent();

    @Nullable
    KubiComponent getmKubiComponent();

    @Nullable
    PollComponent getmPollComponent();
}
