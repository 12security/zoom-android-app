package com.google.android.gms.internal.measurement;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

final class zzhc extends zzhb<FieldDescriptorType, Object> {
    zzhc(int i) {
        super(i, null);
    }

    public final void zzjz() {
        if (!isImmutable()) {
            for (int i = 0; i < zzoi(); i++) {
                Entry zzbf = zzbf(i);
                if (((zzes) zzbf.getKey()).zzmc()) {
                    zzbf.setValue(Collections.unmodifiableList((List) zzbf.getValue()));
                }
            }
            for (Entry entry : zzoj()) {
                if (((zzes) entry.getKey()).zzmc()) {
                    entry.setValue(Collections.unmodifiableList((List) entry.getValue()));
                }
            }
        }
        super.zzjz();
    }
}
