package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
abstract class CollectionFuture<V, C> extends AggregateFuture<V, C> {

    abstract class CollectionFutureRunningState extends RunningState {
        private List<Optional<V>> values;

        /* access modifiers changed from: 0000 */
        public abstract C combine(List<Optional<V>> list);

        CollectionFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean z) {
            super(immutableCollection, z, true);
            this.values = immutableCollection.isEmpty() ? ImmutableList.m113of() : Lists.newArrayListWithCapacity(immutableCollection.size());
            for (int i = 0; i < immutableCollection.size(); i++) {
                this.values.add(null);
            }
        }

        /* access modifiers changed from: 0000 */
        public final void collectOneValue(boolean z, int i, @Nullable V v) {
            List<Optional<V>> list = this.values;
            if (list != null) {
                list.set(i, Optional.fromNullable(v));
            } else {
                Preconditions.checkState(z || CollectionFuture.this.isCancelled(), "Future was done before all dependencies completed");
            }
        }

        /* access modifiers changed from: 0000 */
        public final void handleAllCompleted() {
            List<Optional<V>> list = this.values;
            if (list != null) {
                CollectionFuture.this.set(combine(list));
            } else {
                Preconditions.checkState(CollectionFuture.this.isDone());
            }
        }

        /* access modifiers changed from: 0000 */
        public void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.values = null;
        }
    }

    static final class ListFuture<V> extends CollectionFuture<V, List<V>> {

        private final class ListFutureRunningState extends CollectionFutureRunningState {
            ListFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean z) {
                super(immutableCollection, z);
            }

            public List<V> combine(List<Optional<V>> list) {
                ArrayList newArrayListWithCapacity = Lists.newArrayListWithCapacity(list.size());
                for (Optional optional : list) {
                    newArrayListWithCapacity.add(optional != null ? optional.orNull() : null);
                }
                return Collections.unmodifiableList(newArrayListWithCapacity);
            }
        }

        ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> immutableCollection, boolean z) {
            init(new ListFutureRunningState(immutableCollection, z));
        }
    }

    CollectionFuture() {
    }
}
