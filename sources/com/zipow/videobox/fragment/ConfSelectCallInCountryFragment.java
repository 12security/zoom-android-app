package com.zipow.videobox.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.fragment.SelectCallInCountryFragment.CallInNumberItem;
import com.zipow.videobox.ptapp.MeetingInfoProtos.CountryCode;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import java.util.List;
import java.util.Map;

public class ConfSelectCallInCountryFragment extends SelectCallInCountryFragment {
    public static void showAsActivity(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            Bundle bundle = new Bundle();
            SimpleActivity.show(fragment, ConfSelectCallInCountryFragment.class.getName(), bundle, i, true, 2);
        }
    }

    public void loadAllCountryCodes(@Nullable Map<String, CallInNumberItem> map) {
        if (map != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    List<CountryCode> callinCountryCodesList = meetingItem.getCallinCountryCodesList();
                    if (callinCountryCodesList != null) {
                        for (CountryCode countryCode : callinCountryCodesList) {
                            String id = countryCode.getId();
                            if (!map.containsKey(id)) {
                                map.put(id, new CallInNumberItem(countryCode.getName(), countryCode.getCode(), countryCode.getId()));
                            }
                        }
                    }
                }
            }
        }
    }
}
