package reactivecircus.blueprint.testing

/**
 * Base class for implementing a robot DSL.
 */
abstract class ScreenRobot<out A : RobotActions, out S : RobotAssertions>(
    private val robotActions: A,
    private val robotAssertions: S
) {
    fun given(block: () -> Unit) = block()
    fun perform(block: A.() -> Unit) = robotActions.apply { block() }
    fun check(block: S.() -> Unit) = robotAssertions.apply { block() }
}

/**
 * Robot actions for performing common view actions.
 */
interface RobotActions

/**
 * Robot assertions for performing common view assertions.
 */
interface RobotAssertions
