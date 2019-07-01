package lt.setkus.laima

import com.android.tools.lint.checks.infrastructure.TestFiles.bytes

fun Any.rxJava2() = bytes("libs/rxjava-2.2.3.jar", javaClass.getResourceAsStream("/rxjava-2.2.3.jar").readBytes())