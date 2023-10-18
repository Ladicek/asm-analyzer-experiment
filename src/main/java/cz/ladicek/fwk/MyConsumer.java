package cz.ladicek.fwk;

public class MyConsumer {
    public void accept(Object obj) {
        if (obj.getClass().isAnnotationPresent(MyAnn.class)) {
            System.out.println("OK");
        } else {
            System.out.println("KO");
        }
    }
}
