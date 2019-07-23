package reactivecircus.blueprint.testing

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage.RESUMED
import java.util.concurrent.atomic.AtomicReference

/**
 * Finds the activity in the foreground (if any).
 */
fun currentActivity(): Activity? {
    val currentActivityReference = AtomicReference<Activity>()
    getInstrumentation().runOnMainSync {
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
            .getActivitiesInStage(RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            currentActivityReference.set(resumedActivities.iterator().next() as Activity)
        }
    }

    return currentActivityReference.get()
}
