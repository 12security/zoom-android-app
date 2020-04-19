package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.zipow.videobox.ptapp.mm.ContactCloudSIP */
public class ContactCloudSIP implements Serializable {
    private String companyNumber;
    @Nullable
    private ArrayList<String> directNumber;
    private String extension;
    @Nullable
    private ArrayList<String> formattedDirectNumber;

    @Nullable
    public ArrayList<String> getDirectNumber() {
        return this.directNumber;
    }

    @Nullable
    public ArrayList<String> getFormattedDirectNumber() {
        return this.formattedDirectNumber;
    }

    public void setDirectNumber(@Nullable List<String> list) {
        if (list != null) {
            ArrayList<String> arrayList = this.directNumber;
            if (arrayList == null) {
                this.directNumber = new ArrayList<>();
            } else {
                arrayList.clear();
            }
            this.directNumber.addAll(list);
            return;
        }
        this.directNumber = null;
    }

    public void setFormattedDirectNumber(@Nullable List<String> list) {
        if (list != null) {
            ArrayList<String> arrayList = this.formattedDirectNumber;
            if (arrayList == null) {
                this.formattedDirectNumber = new ArrayList<>();
            } else {
                arrayList.clear();
            }
            this.formattedDirectNumber.addAll(list);
            return;
        }
        this.formattedDirectNumber = null;
    }

    public String getCompanyNumber() {
        return this.companyNumber;
    }

    public void setCompanyNumber(String str) {
        this.companyNumber = str;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String str) {
        this.extension = str;
    }
}
