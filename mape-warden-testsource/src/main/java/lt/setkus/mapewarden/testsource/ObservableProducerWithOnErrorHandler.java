package lt.setkus.mapewarden.testsource;

import io.reactivex.Observable;

import static lt.setkus.mapewarden.testsource.Utils.getIntegerInput;

public class ObservableProducerWithOnErrorHandler {

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
}
