package lt.setkus.mapewarden.testsource;

import io.reactivex.Observable;

import java.io.IOException;
import java.util.Random;

public class ObservableProducer {

    public Observable<Integer> getIntegerObservable() {
        return Observable.create(e -> {
            try {
                for (int i : getIntegerInput()) {
                    e.onNext(i);
                }
                e.onComplete();
            } catch (Exception ex) {
                e.onError(ex);
            }
        });
    }

    private int[] getIntegerInput() throws IOException {
        int[] stream = {1, 2, 3};
        Random random = new Random(System.currentTimeMillis());
        if (random.nextInt() % 2 == 0) {
            throw new IOException("Random error");
        }
        return stream;
    }
}
