package lt.setkus.mapewarden.testsource;

import io.reactivex.Observable;

public class ObservableTryOnErrorMethodReferenceProducer {
    private Listener listener = new Listener();

    public Observable<Integer> produceObservable() {
        return Observable.create(e -> {
            listener.doOnError(e::tryOnError);
        });
    }

    class Listener {
        void doOnError(ObservableMethodReferenceProducer.ErrorListener errorListener) {

        }
    }

    interface ErrorListener {
        void onError(Exception e);
    }
}
