package cl.truchoradios.chile.service;

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
public final class RadioPlayerService_MembersInjector implements MembersInjector<RadioPlayerService> {
  private final Provider<RadioPlayerManager> playerManagerProvider;

  public RadioPlayerService_MembersInjector(Provider<RadioPlayerManager> playerManagerProvider) {
    this.playerManagerProvider = playerManagerProvider;
  }

  public static MembersInjector<RadioPlayerService> create(
      Provider<RadioPlayerManager> playerManagerProvider) {
    return new RadioPlayerService_MembersInjector(playerManagerProvider);
  }

  @Override
  public void injectMembers(RadioPlayerService instance) {
    injectPlayerManager(instance, playerManagerProvider.get());
  }

  @InjectedFieldSignature("cl.truchoradios.chile.service.RadioPlayerService.playerManager")
  public static void injectPlayerManager(RadioPlayerService instance,
      RadioPlayerManager playerManager) {
    instance.playerManager = playerManager;
  }
}
