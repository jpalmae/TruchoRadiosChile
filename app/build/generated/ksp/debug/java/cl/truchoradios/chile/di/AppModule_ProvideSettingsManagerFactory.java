package cl.truchoradios.chile.di;

import cl.truchoradios.chile.data.local.SettingsManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideSettingsManagerFactory implements Factory<SettingsManager> {
  @Override
  public SettingsManager get() {
    return provideSettingsManager();
  }

  public static AppModule_ProvideSettingsManagerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SettingsManager provideSettingsManager() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSettingsManager());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideSettingsManagerFactory INSTANCE = new AppModule_ProvideSettingsManagerFactory();
  }
}
