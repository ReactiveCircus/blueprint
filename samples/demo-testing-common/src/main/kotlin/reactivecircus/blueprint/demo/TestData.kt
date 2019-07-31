@file:Suppress("MagicNumber")

package reactivecircus.blueprint.demo

import reactivecircus.blueprint.demo.domain.model.Note

val testNotes: List<Note> by lazy {
    val timeCreated = System.currentTimeMillis()
    val timeUpdated = System.currentTimeMillis() + 10

    listOf(
        Note(
            content = "Architectural frameworks and toolkits for bootstrapping modern Android codebases.",
            timeCreated = timeCreated,
            timeLastUpdated = timeUpdated
        ),
        Note(
            content = "Wrapper API for threading with Kotlin CoroutineDispatcher",
            timeCreated = timeCreated - 10,
            timeLastUpdated = timeUpdated - 10
        ),
        Note(
            content = "Wrapper API for threading with RxJava's Schedulers",
            timeCreated = timeCreated - 20,
            timeLastUpdated = timeUpdated - 20
        ),
        Note(
            content = "Interactors (use case in Clean Architecture) based on Kotlin Coroutines",
            timeCreated = timeCreated - 30,
            timeLastUpdated = timeUpdated - 30
        ),
        Note(
            content = "Interactors (use case in Clean Architecture) based on RxJava",
            timeCreated = timeCreated - 40,
            timeLastUpdated = timeUpdated - 40
        ),
        Note(
            content = "Common APIs for all Blueprint Interactor implementations",
            timeCreated = timeCreated - 50,
            timeLastUpdated = timeUpdated - 50
        ),
        Note(
            content = "Android UI extensions, utilities and widgets",
            timeCreated = timeCreated - 60,
            timeLastUpdated = timeUpdated - 60
        ),
        Note(
            content = "Android UI testing framework with Testing Robot DSL",
            timeCreated = timeCreated - 70,
            timeLastUpdated = timeUpdated - 70
        )
    )
}
