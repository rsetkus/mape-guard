package lt.setkus.laima.testsource;

import io.reactivex.Observable;

import static lt.setkus.laima.testsource.Utils.getIntegerInput;

public class ObservableProducerWithTryOnErrorHandler {

    public Observable<Integer> getIntegerObservable() {
        return Observable.create(e -> {
            try {
                for (int i : getIntegerInput()) {
                    e.onNext(i);
                }
                e.onComplete();
            } catch (Exception ex) {
                e.tryOnError(ex);
            }
        });
    }
}
