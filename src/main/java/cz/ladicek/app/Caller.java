package cz.ladicek.app;

import cz.ladicek.fwk.MyConsumer;

import java.util.concurrent.ThreadLocalRandom;

public class Caller {
    public void doSomething() {
        MyConsumer consumer = new MyConsumer();
        consumer.accept(new Foo());
        consumer.accept(new Bar());
        consumer.accept(ThreadLocalRandom.current().nextBoolean() ? new Foo() : new Bar());
    }
}
