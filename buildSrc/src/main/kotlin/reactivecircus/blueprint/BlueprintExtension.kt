package reactivecircus.blueprint

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.property

/**
 * Extension for [BlueprintPlugin].
 */
@Suppress("UnstableApiUsage", "unused")
open class BlueprintExtension internal constructor(objects: ObjectFactory) {

    /**
     * Whether to enable strict explicit API mode for the project.
     *
     * Default is `false`.
     */
    val enableExplicitApi: Property<Boolean> = objects.property<Boolean>().convention(false)
}
