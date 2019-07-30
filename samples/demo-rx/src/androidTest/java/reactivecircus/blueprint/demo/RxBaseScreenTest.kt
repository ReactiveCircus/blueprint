package reactivecircus.blueprint.demo

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.intent.Intents
import org.junit.After
import org.junit.Before
import reactivecircus.blueprint.demo.data.cache.NoteCache

abstract class RxBaseScreenTest {

    val noteCache: NoteCache by lazy {
        (ApplicationProvider.getApplicationContext() as RxScreenTestApp).injector.provideNoteCache()
    }

    @Before
    open fun setUp() {
        Intents.init()
    }

    @After
    open fun tearDown() {
        Intents.release()
    }

    inline fun <reified A : Activity> launchActivityScenario(
        intent: android.content.Intent? = null
    ): ActivityScenario<A> {
        return launchActivity<A>(intent).also {
            Espresso.onIdle()
        }
    }
}
