package com.zipow.videobox.eventbus;

import com.zipow.videobox.tempbean.IMessageTemplateSelectItem;
import java.util.List;

public class ZMTemplateSelectProcessingEvent {
    private String eventID;
    private String messageID;
    private boolean result;
    private List<IMessageTemplateSelectItem> selectItems;
    private String sessionID;

    public ZMTemplateSelectProcessingEvent(boolean z, String str, String str2, String str3, List<IMessageTemplateSelectItem> list) {
        this.result = z;
        this.sessionID = str;
        this.messageID = str2;
        this.eventID = str3;
        this.selectItems = list;
    }

    public boolean isResult() {
        return this.result;
    }

    public void setResult(boolean z) {
        this.result = z;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public void setMessageID(String str) {
        this.messageID = str;
    }

    public String getEventID() {
        return this.eventID;
    }

    public void setEventID(String str) {
        this.eventID = str;
    }

    public List<IMessageTemplateSelectItem> getSelectItems() {
        return this.selectItems;
    }

    public void setSelectItems(List<IMessageTemplateSelectItem> list) {
        this.selectItems = list;
    }
}
