package lt.setkus.laima.testsource

import io.reactivex.Observable

import lt.setkus.laima.testsource.Utils.getIntegerInput

class ObservableTryOnErrorMethodCall {

    val integerObservable: Observable<Int>
        get() = Observable.create { e ->
            try {
                for (i in getIntegerInput()) {
                    e.onNext(i)
                }
                e.onComplete()
            } catch (ex: Exception) {
                e.tryOnError(ex)
            }
        }
}
