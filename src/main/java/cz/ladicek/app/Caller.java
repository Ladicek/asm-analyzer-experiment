package cz.ladicek.app;

import cz.ladicek.fwk.MyConsumer;

public class Caller {
    public void doSomething() {
        MyConsumer consumer = new MyConsumer();
        consumer.accept(new Foo());
        consumer.accept(new Bar());
    }
}
