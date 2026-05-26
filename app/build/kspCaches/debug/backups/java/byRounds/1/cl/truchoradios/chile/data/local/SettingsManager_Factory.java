package cl.truchoradios.chile.data.local;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class SettingsManager_Factory implements Factory<SettingsManager> {
  @Override
  public SettingsManager get() {
    return newInstance();
  }

  public static SettingsManager_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SettingsManager newInstance() {
    return new SettingsManager();
  }

  private static final class InstanceHolder {
    private static final SettingsManager_Factory INSTANCE = new SettingsManager_Factory();
  }
}
