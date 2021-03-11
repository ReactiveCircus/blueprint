package reactivecircus.blueprint.testing

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * TestRule to execute tests multiple times.
 * This can be used to debug flaky tests.
 */
public class RepeatRule(private val iterations: Int) : TestRule {

    init {
        require(iterations > 0) { "iterations < 1: $iterations" }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                repeat(iterations) {
                    base.evaluate()
                }
            }
        }
    }
}
