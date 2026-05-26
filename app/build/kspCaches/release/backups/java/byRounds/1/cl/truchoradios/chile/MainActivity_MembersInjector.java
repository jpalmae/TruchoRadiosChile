package cl.truchoradios.chile;

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

  public MainActivity_MembersInjector(Provider<RadioPlayerManager> playerManagerProvider) {
    this.playerManagerProvider = playerManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<RadioPlayerManager> playerManagerProvider) {
    return new MainActivity_MembersInjector(playerManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPlayerManager(instance, playerManagerProvider.get());
  }

  @InjectedFieldSignature("cl.truchoradios.chile.MainActivity.playerManager")
  public static void injectPlayerManager(MainActivity instance, RadioPlayerManager playerManager) {
    instance.playerManager = playerManager;
  }
}
