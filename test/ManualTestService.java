package test;

import remote.RemoteObjectException;
import remote.Service;
import remote.StubFactory;
import test.remote.SimpleInterface;
import test.remote.SimpleObject;

import java.io.FileNotFoundException;

public class ManualTestService {

    public static void main(String[] args) {
        try {
            Service<SimpleInterface> service = new Service<>(SimpleInterface.class, new SimpleObject(), 8080 );
            service.start();
            SimpleInterface simpleObject = StubFactory.create(SimpleInterface.class, "127.0.0.1:8080");
            simpleObject.method(false);
            service.stop();
            service.start();
            simpleObject.method(true);

        } catch (RemoteObjectException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
