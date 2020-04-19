package com.google.firebase.components;

import com.google.android.gms.common.annotation.KeepForSdk;
import java.util.Arrays;
import java.util.List;

@KeepForSdk
/* compiled from: com.google.firebase:firebase-common@@16.1.0 */
public class DependencyCycleException extends DependencyException {
    private final List<Component<?>> componentsInCycle;

    @KeepForSdk
    public DependencyCycleException(List<Component<?>> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dependency cycle detected: ");
        sb.append(Arrays.toString(list.toArray()));
        super(sb.toString());
        this.componentsInCycle = list;
    }

    @KeepForSdk
    public List<Component<?>> getComponentsInCycle() {
        return this.componentsInCycle;
    }
}
