package lt.setkus.mapewarden.rx

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category.Companion.CORRECTNESS
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity.ERROR
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.asJava.getRepresentativeLightMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UCallableReferenceExpression
import java.util.*

const val ON_ERROR_MESSAGE = "Using onError might cause a crash."

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
    override fun getApplicableUastTypes() = listOf(UCallableReferenceExpression::class.java)

    override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (context.isMemberInEmitterClass(method)) {
            context.report(ISSUE_ON_ERROR_CALL, node, context.getNameLocation(node), ON_ERROR_MESSAGE)
        }
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return MyElementHandler(context)
    }
}

class MyElementHandler(private val context: JavaContext) : UElementHandler() {

    override fun visitCallableReferenceExpression(node: UCallableReferenceExpression) {
        node.resolve()?.let {
            if (context.isMemberInEmitterClass(it.getRepresentativeLightMethod())) {
                context.report(ISSUE_ON_ERROR_CALL, node, context.getNameLocation(node), ON_ERROR_MESSAGE)
            }
        }
    }
}

private fun JavaContext.isMemberInEmitterClass(method: PsiMethod?) = evaluator.isMemberInClass(method, "io.reactivex.Emitter")