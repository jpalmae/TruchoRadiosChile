package cl.truchoradios.chile;

import cl.truchoradios.chile.data.local.SettingsManager;
import cl.truchoradios.chile.player.RadioPlayerManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<RadioPlayerManager> playerManagerProvider;

  private final Provider<SettingsManager> settingsManagerProvider;

  public MainActivity_MembersInjector(Provider<RadioPlayerManager> playerManagerProvider,
      Provider<SettingsManager> settingsManagerProvider) {
    this.playerManagerProvider = playerManagerProvider;
    this.settingsManagerProvider = settingsManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<RadioPlayerManager> playerManagerProvider,
      Provider<SettingsManager> settingsManagerProvider) {
    return new MainActivity_MembersInjector(playerManagerProvider, settingsManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPlayerManager(instance, playerManagerProvider.get());
    injectSettingsManager(instance, settingsManagerProvider.get());
  }

  @InjectedFieldSignature("cl.truchoradios.chile.MainActivity.playerManager")
  public static void injectPlayerManager(MainActivity instance, RadioPlayerManager playerManager) {
    instance.playerManager = playerManager;
  }

  @InjectedFieldSignature("cl.truchoradios.chile.MainActivity.settingsManager")
  public static void injectSettingsManager(MainActivity instance, SettingsManager settingsManager) {
    instance.settingsManager = settingsManager;
  }
}
