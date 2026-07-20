# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclassmembers,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses, EnclosingMethod
-keep class com.google.gson.** { *; }

# Gson: TypeToken anonimos (sin esto, fromJson revienta con ClassCastException)
-keepclassmembers class * extends com.google.gson.reflect.TypeToken {
    <init>();
}

# Gson: modelos JSON (los nombres de campos mapean las keys del JSON)
-keep class cl.truchoradios.chile.data.repository.RadiosJson { *; }
-keep class cl.truchoradios.chile.data.repository.RadioEntityJson { *; }
-keep class cl.truchoradios.chile.data.repository.RegionJson { *; }
-keep class cl.truchoradios.chile.data.repository.GenreJson { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Google Cast (referenciado por nombre desde el Manifest)
-keep class cl.truchoradios.chile.cast.TruchoCastOptionsProvider { *; }
