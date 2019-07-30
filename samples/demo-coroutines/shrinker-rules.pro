-verbose

# Keep annotations with RUNTIME retention and their defaults.
-keepattributes RuntimeVisible*Annotations, AnnotationDefault

# For crash reporting
-keepattributes LineNumberTable, SourceFile

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# Enum.valueOf(Class, String) and others invoke this method reflectively.
-keepclassmembers,allowoptimization enum * {
    public static **[] values();
}

# Parcelable CREATOR fields are looked up reflectively when de-parceling.
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

# Ensure the custom, fast service loader implementation is removed.
-assumevalues class kotlinx.coroutines.internal.MainDispatcherLoader {
  boolean FAST_SERVICE_LOADER_ENABLED return false;
}
-checkdiscard class kotlinx.coroutines.internal.FastServiceLoader
