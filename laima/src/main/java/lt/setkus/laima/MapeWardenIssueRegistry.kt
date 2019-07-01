package lt.setkus.laima

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import lt.setkus.laima.rx.ISSUE_ON_ERROR_CALL

class MapeWardenIssueRegistry : IssueRegistry() {
    private val mapeWardenIssues = listOf(ISSUE_ON_ERROR_CALL)

    override val issues: List<Issue>
        get() = mapeWardenIssues
}