package lt.setkus.mapewarden.rx

import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.ERROR
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.EnumSet


val ISSUE_ON_ERROR_CALL = Issue.create(
        "RxJava",
        "Avoid using onError while creating any Observable",
        "Calling onError might cause application crash when Observable is disposed. Use tryOnError instead.",
        CORRECTNESS,
        7,
        ERROR,
        Implementation(RxOnErrorDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES))
)

class RxOnErrorDetector : Detector(), Detector.UastScanner {
    override fun getApplicableMethodNames() = listOf("onError")

    override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.evaluator.isMemberInClass(method, "io.reactivex.Emitter")) {
            println("Found")
            context.report(ISSUE_ON_ERROR_CALL, node, context.getNameLocation(node), "Using onError might cause a crash.")
        } else {
            println("Not found")
        }
    }
}