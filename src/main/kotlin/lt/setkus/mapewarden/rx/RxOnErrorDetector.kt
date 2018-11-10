package lt.setkus.mapewarden.rx

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

class RxOnErrorDetector: Detector() , Detector.UastScanner {
    companion object {
        private val detectorClass: Class<out Detector> = RxOnErrorDetector::class.java
        private val detectorScope: EnumSet<Scope> = Scope.JAVA_FILE_SCOPE

        private val implementation = Implementation(detectorClass, detectorScope)

        private const val issueId = "RxJava"
        private const val issueDescription = "Avoid using onError while creating any Observable"
        private const val issueExplanation = "Calling onError might cause application crash when Observable is disposed. Use tryOnError instead."
        private val issueCategory = Category.CORRECTNESS
        private const val issuePriority = 7
        private val issueSeverity = Severity.ERROR

        val issue = Issue.create(
                issueId,
                issueDescription,
                issueExplanation,
                issueCategory,
                issuePriority,
                issueSeverity,
                implementation
        )
    }

    private val checker = RxJavaOnErrorChecker()

    override fun getApplicableMethodNames() = listOf("getIntegerObservable")

    override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        println("Visiting methods")
        if (checker.hasOnError(node, method)) {
            report(context)
        }
    }

    private fun report(context: JavaContext) {
        context.report(issue, Location.create(context.file), issue.getBriefDescription(TextFormat.TEXT))
    }

    inner class RxJavaOnErrorChecker {
        fun hasOnError(node: UCallExpression, method: PsiMethod): Boolean {
            return false
        }
    }
}