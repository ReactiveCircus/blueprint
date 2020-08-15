package reactivecircus.blueprint.testing

/**
 * Base class for implementing a robot DSL.
 */
public abstract class ScreenRobot<out A : RobotActions, out S : RobotAssertions>(
    private val robotActions: A,
    private val robotAssertions: S
) {
    public fun given(block: () -> Unit): Unit = block()
    public fun perform(block: A.() -> Unit): A = robotActions.apply { block() }
    public fun check(block: S.() -> Unit): S = robotAssertions.apply { block() }
}

/**
 * Robot actions for performing common view actions.
 */
public interface RobotActions

/**
 * Robot assertions for performing common view assertions.
 */
public interface RobotAssertions
