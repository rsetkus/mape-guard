package lt.setkus.laima.testsource;

import io.reactivex.Observable;

public class ObservableMethodReferenceProducer {

    private Listener listener = new Listener();

    public Observable<Integer> produceObservable() {
        return Observable.create(e -> {
            listener.doOnError(e::onError);
        });
    }

    class Listener {
        void doOnError(ErrorListener errorListener) {

        }
    }

    interface ErrorListener {
        void onError(Exception e);
    }
}
