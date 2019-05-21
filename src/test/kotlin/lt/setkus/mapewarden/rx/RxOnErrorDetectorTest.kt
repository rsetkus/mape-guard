package lt.setkus.mapewarden.rx

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import lt.setkus.mapewarden.rxJava2
import org.junit.Test

class RxOnErrorDetectorTest {

    @Test
    fun `test Observable create with onError call`() {
        val expectedError = """
            src/foo/ObservableProducer.java:17: Error: Using onError might cause a crash. [RxJava]
                e.onError(ex);
                  ~~~~~~~
1 errors, 0 warnings
        """
        lint().files(rxJava2(), java("""
            package foo;

            import io.reactivex.Observable;

            import static foo.Utils.getIntegerInput;

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
            }""".trimIndent()).indented())
                .issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expect(expectedError.trimIndent())
    }

    @Test
    fun `onError method reference call should be reported`() {
        val expectedError = """
            src/foo/ObservableMethodReferenceProducer.java:11: Error: Using onError might cause a crash. [RxJava]
            listener.doOnError(e::onError);
                               ~~~~~~~~~~
1 errors, 0 warnings
        """

        lint().files(rxJava2(), java("""
            package foo;

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
            }""".trimIndent()).indented())
                .issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expect(expectedError.trimIndent())
    }

    @Test
    fun `tryOnError method call shouldn't report error`() {
        lint().files(rxJava2(), java("""
            package foo;

            import io.reactivex.Observable;

            import static foo.Utils.getIntegerInput;

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
        """.trimIndent()).indented())
                .issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expectClean()
    }

    @Test
    fun `tryOnError method reference call shouldn't report error`() {
        lint().files(rxJava2(), java("""
            package foo;

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
        """.trimIndent()).indented())
                .issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expectClean()
    }

    @Test
    fun `onError method call in Kotlin should report error`() {
        lint().files(rxJava2(), kotlin("""
            package foo

            import io.reactivex.Observable

            import foo.Utils.getIntegerInput

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
        """.trimIndent()).indented()).
                issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expect("""
                    src/foo/ObservableWithOnErrorMethodCall.kt:17: Error: Using onError might cause a crash. [RxJava]
                e.onError(ex)
                  ~~~~~~~
1 errors, 0 warnings
                """.trimIndent())
    }

    @Test
    fun `tryOnError method call should not report error`() {
        lint().files(rxJava2(), kotlin("""
            package foo

            import io.reactivex.Observable

            import foo.Utils.getIntegerInput

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
        """.trimIndent()).indented())
                .issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expectClean()
    }
}