package reactivecircus.blueprint

val additionalCompilerArgs = listOf(
    "-progressive",
    "-Xuse-ir",
    "-Xskip-runtime-version-check",
    "-Xskip-metadata-version-check",
    "-Xjvm-default=all",
    "-Xopt-in=kotlin.Experimental"
)
