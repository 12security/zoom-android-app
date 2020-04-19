package com.zipow.videobox.fragment.p012mm;

import com.zipow.videobox.ptapp.ABContactsCache.Contact;
import com.zipow.videobox.view.IMAddrBookItem;

/* renamed from: com.zipow.videobox.fragment.mm.ContactItem */
/* compiled from: MMPhoneContactsInZoomFragment */
class ContactItem {
    private IMAddrBookItem buddy;
    private Contact contact;

    ContactItem() {
    }

    public Contact getContact() {
        return this.contact;
    }

    public void setContact(Contact contact2) {
        this.contact = contact2;
    }

    public IMAddrBookItem getBuddy() {
        return this.buddy;
    }

    public void setBuddy(IMAddrBookItem iMAddrBookItem) {
        this.buddy = iMAddrBookItem;
    }
}
