package cl.truchoradios.chile.navigation;

import cl.truchoradios.chile.data.local.SettingsManager;
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

  private final Provider<SettingsManager> settingsManagerProvider;

  public PlayerSharedViewModel_Factory(Provider<RadioPlayerManager> playerManagerProvider,
      Provider<SettingsManager> settingsManagerProvider) {
    this.playerManagerProvider = playerManagerProvider;
    this.settingsManagerProvider = settingsManagerProvider;
  }

  @Override
  public PlayerSharedViewModel get() {
    return newInstance(playerManagerProvider.get(), settingsManagerProvider.get());
  }

  public static PlayerSharedViewModel_Factory create(
      Provider<RadioPlayerManager> playerManagerProvider,
      Provider<SettingsManager> settingsManagerProvider) {
    return new PlayerSharedViewModel_Factory(playerManagerProvider, settingsManagerProvider);
  }

  public static PlayerSharedViewModel newInstance(RadioPlayerManager playerManager,
      SettingsManager settingsManager) {
    return new PlayerSharedViewModel(playerManager, settingsManager);
  }
}
