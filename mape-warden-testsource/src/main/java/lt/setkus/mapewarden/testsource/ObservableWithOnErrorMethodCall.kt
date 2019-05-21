package lt.setkus.mapewarden.testsource

import io.reactivex.Observable

import lt.setkus.mapewarden.testsource.Utils.getIntegerInput

class ObservableWithOnErrorMethodCall {

    val integerObservable: Observable<Int>
        get() = Observable.create { e ->
            try {
                for (i in getIntegerInput()) {
                    e.onNext(i)
                }
                e.onComplete()
            } catch (ex: Exception) {
                e.onError(ex)
            }
        }
}