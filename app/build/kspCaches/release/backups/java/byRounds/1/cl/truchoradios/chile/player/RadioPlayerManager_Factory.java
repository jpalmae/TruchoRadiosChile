package cl.truchoradios.chile.player;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class RadioPlayerManager_Factory implements Factory<RadioPlayerManager> {
  private final Provider<Context> contextProvider;

  public RadioPlayerManager_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public RadioPlayerManager get() {
    return newInstance(contextProvider.get());
  }

  public static RadioPlayerManager_Factory create(Provider<Context> contextProvider) {
    return new RadioPlayerManager_Factory(contextProvider);
  }

  public static RadioPlayerManager newInstance(Context context) {
    return new RadioPlayerManager(context);
  }
}
