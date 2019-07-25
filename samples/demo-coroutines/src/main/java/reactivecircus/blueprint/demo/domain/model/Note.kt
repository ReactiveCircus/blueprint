package reactivecircus.blueprint.demo.domain.model

import java.util.UUID

data class Note(
    val uuid: String = UUID.randomUUID().toString(),
    val content: String,
    val timeCreated: Long,
    val timeLastUpdated: Long
)
