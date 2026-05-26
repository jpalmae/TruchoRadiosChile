package cl.truchoradios.chile.presentation.screens.favorites;

import cl.truchoradios.chile.data.repository.RadioRepositoryImpl;
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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<RadioRepositoryImpl> repositoryProvider;

  private final Provider<RadioPlayerManager> playerManagerProvider;

  public FavoritesViewModel_Factory(Provider<RadioRepositoryImpl> repositoryProvider,
      Provider<RadioPlayerManager> playerManagerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.playerManagerProvider = playerManagerProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(repositoryProvider.get(), playerManagerProvider.get());
  }

  public static FavoritesViewModel_Factory create(Provider<RadioRepositoryImpl> repositoryProvider,
      Provider<RadioPlayerManager> playerManagerProvider) {
    return new FavoritesViewModel_Factory(repositoryProvider, playerManagerProvider);
  }

  public static FavoritesViewModel newInstance(RadioRepositoryImpl repository,
      RadioPlayerManager playerManager) {
    return new FavoritesViewModel(repository, playerManager);
  }
}
