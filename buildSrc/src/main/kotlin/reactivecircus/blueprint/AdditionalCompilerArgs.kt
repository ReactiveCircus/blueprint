package reactivecircus.blueprint

val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+NewInference",
    "-Xuse-experimental=kotlin.Experimental"
)
