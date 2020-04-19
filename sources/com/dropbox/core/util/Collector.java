package com.dropbox.core.util;

import java.util.ArrayList;

public abstract class Collector<E, L> {

    public static final class ArrayListCollector<E> extends Collector<E, ArrayList<E>> {
        private ArrayList<E> list = new ArrayList<>();

        public void add(E e) {
            ArrayList<E> arrayList = this.list;
            if (arrayList != null) {
                arrayList.add(e);
                return;
            }
            throw new IllegalStateException("already called finish()");
        }

        public ArrayList<E> finish() {
            ArrayList<E> arrayList = this.list;
            if (arrayList != null) {
                this.list = null;
                return arrayList;
            }
            throw new IllegalStateException("already called finish()");
        }
    }

    public static final class NullSkipper<E, L> extends Collector<E, L> {
        private final Collector<E, L> underlying;

        public NullSkipper(Collector<E, L> collector) {
            this.underlying = collector;
        }

        /* renamed from: mk */
        public static <E, L> Collector<E, L> m19mk(Collector<E, L> collector) {
            return new NullSkipper(collector);
        }

        public void add(E e) {
            if (e != null) {
                this.underlying.add(e);
            }
        }

        public L finish() {
            return this.underlying.finish();
        }
    }

    public abstract void add(E e);

    public abstract L finish();
}
