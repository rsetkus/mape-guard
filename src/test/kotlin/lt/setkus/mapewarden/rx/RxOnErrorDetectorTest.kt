package lt.setkus.mapewarden.rx

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import lt.setkus.mapewarden.rxJava2
import org.junit.Test

class RxOnErrorDetectorTest {

    @Test
    fun `test Observable create with onError call`() {
        lint().files(rxJava2(), java("""
            package foo;

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
            }""".trimIndent()).indented())
                .issues(ISSUE_ON_ERROR_CALL)
                .run()
                .expect("src/foo/ObservableProducer.java:18: Error: Using onError might cause a crash. [RxJava]\n" +
                        "                e.onError(ex);\n" +
                        "                  ~~~~~~~\n" +
                        "1 errors, 0 warnings".trimIndent())
    }

    @Test
    fun `onError method reference call should be reported`() {
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
                .expect("src/foo/ObservableMethodReferenceProducer.java:18: Error: Using onError might cause a crash. [RxJava]\n" +
                        "                e.onError(ex);\n" +
                        "                  ~~~~~~~\n" +
                        "1 errors, 0 warnings".trimIndent())
    }
}