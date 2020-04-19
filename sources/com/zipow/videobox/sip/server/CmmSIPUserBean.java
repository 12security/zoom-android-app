package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLine;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPUser;
import java.util.LinkedHashMap;
import java.util.List;

public class CmmSIPUserBean {

    /* renamed from: ID */
    private String f330ID;
    private String extension;
    private String jid;
    private int lineCount;
    private LinkedHashMap<String, CmmSIPLineBean> lineMap = new LinkedHashMap<>();
    private String userName;

    public CmmSIPUserBean(CmmSIPUser cmmSIPUser) {
        this.f330ID = cmmSIPUser.getID();
        this.extension = cmmSIPUser.getExtension();
        this.jid = cmmSIPUser.getJid();
        this.userName = cmmSIPUser.getUserName();
        this.lineCount = cmmSIPUser.getLineCount();
        this.lineMap.clear();
        List<CmmSIPLine> linesList = cmmSIPUser.getLinesList();
        if (linesList != null) {
            for (CmmSIPLine cmmSIPLine : linesList) {
                this.lineMap.put(cmmSIPLine.getID(), new CmmSIPLineBean(cmmSIPLine));
            }
        }
    }

    public String getID() {
        return this.f330ID;
    }

    public String getExtension() {
        return this.extension;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getJid() {
        return this.jid;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    @NonNull
    public LinkedHashMap<String, CmmSIPLineBean> getLineMap() {
        return this.lineMap;
    }
}
