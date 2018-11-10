package lt.setkus.mapewarden.rx

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFiles

class RxOnErrorDetectorTest : LintDetectorTest() {

    override fun getDetector() = RxOnErrorDetector()

    override fun getIssues() = listOf(RxOnErrorDetector.issue)

    fun `test Observable create with onError call`() {
        val result = lintProject(TestFiles.java(
                "package lt.setkus.mapewarden.testsource;\n" +
                "\n" +
                "import io.reactivex.Observable;\n" +
                "\n" +
                "import java.io.IOException;\n" +
                "import java.util.Random;\n" +
                "\n" +
                "public class ObservableProducer {\n" +
                "\n" +
                "    public Observable<Integer> getIntegerObservable() {\n" +
                "        return Observable.create(e -> {\n" +
                "            try {\n" +
                "                for (int i : getIntegerInput()) {\n" +
                "                    e.onNext(i);\n" +
                "                }\n" +
                "                e.onComplete();\n" +
                "            } catch (Exception ex) {\n" +
                "                e.onError(ex);\n" +
                "            }\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "    private int[] getIntegerInput() throws IOException {\n" +
                "        int[] stream = {1, 2, 3};\n" +
                "        Random random = new Random(System.currentTimeMillis());\n" +
                "        if (random.nextInt() % 2 == 0) {\n" +
                "            throw new IOException(\"Random error\");\n" +
                "        }\n" +
                "        return stream;\n" +
                "    }\n" +
                "}"))
        assertEquals("", result)
    }
}