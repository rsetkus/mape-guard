package lt.setkus.laima

import lt.setkus.laima.rx.ISSUE_ON_ERROR_CALL
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class MapeWardenIssueRegistryTest {

    private lateinit var mapeWardenRegistry: MapeWardenIssueRegistry

    @Before
    fun setUp() {
        mapeWardenRegistry = MapeWardenIssueRegistry()
    }

    @Test
    fun `expected that RxOnError issue exists in registry`() {
        assertThat(mapeWardenRegistry.issues).contains(ISSUE_ON_ERROR_CALL)
    }
}