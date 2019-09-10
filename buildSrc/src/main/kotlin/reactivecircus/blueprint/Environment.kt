package reactivecircus.blueprint

val isCiBuild: Boolean get() = System.getenv("CI") == "true"
