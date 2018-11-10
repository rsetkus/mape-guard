package lt.setkus.mapewarden

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import lt.setkus.mapewarden.rx.RxOnErrorDetector

class MapeWardenIssueRegistry : IssueRegistry() {
    private val mapeWardenIssues = listOf(RxOnErrorDetector.issue)

    override val issues: List<Issue>
        get() = mapeWardenIssues
}