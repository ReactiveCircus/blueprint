package reactivecircus.blueprint

val additionalCompilerArgs = listOf(
    "-progressive",
    "-XXLanguage:+NewInference",
    "-Xopt-in=kotlin.Experimental"
)
