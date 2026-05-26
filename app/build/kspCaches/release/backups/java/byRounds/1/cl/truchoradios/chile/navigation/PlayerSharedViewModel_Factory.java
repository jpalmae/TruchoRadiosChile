package cl.truchoradios.chile.navigation;

import cl.truchoradios.chile.player.RadioPlayerManager;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class PlayerSharedViewModel_Factory implements Factory<PlayerSharedViewModel> {
  private final Provider<RadioPlayerManager> playerManagerProvider;

  public PlayerSharedViewModel_Factory(Provider<RadioPlayerManager> playerManagerProvider) {
    this.playerManagerProvider = playerManagerProvider;
  }

  @Override
  public PlayerSharedViewModel get() {
    return newInstance(playerManagerProvider.get());
  }

  public static PlayerSharedViewModel_Factory create(
      Provider<RadioPlayerManager> playerManagerProvider) {
    return new PlayerSharedViewModel_Factory(playerManagerProvider);
  }

  public static PlayerSharedViewModel newInstance(RadioPlayerManager playerManager) {
    return new PlayerSharedViewModel(playerManager);
  }
}
