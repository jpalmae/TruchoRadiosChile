package cl.truchoradios.chile.di;

import android.content.Context;
import cl.truchoradios.chile.player.RadioPlayerManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvidePlayerManagerFactory implements Factory<RadioPlayerManager> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvidePlayerManagerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public RadioPlayerManager get() {
    return providePlayerManager(contextProvider.get());
  }

  public static AppModule_ProvidePlayerManagerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvidePlayerManagerFactory(contextProvider);
  }

  public static RadioPlayerManager providePlayerManager(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePlayerManager(context));
  }
}
