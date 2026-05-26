package cl.truchoradios.chile.presentation.screens.favorites;

import cl.truchoradios.chile.data.repository.RadioRepositoryImpl;
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

  public FavoritesViewModel_Factory(Provider<RadioRepositoryImpl> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      Provider<RadioRepositoryImpl> repositoryProvider) {
    return new FavoritesViewModel_Factory(repositoryProvider);
  }

  public static FavoritesViewModel newInstance(RadioRepositoryImpl repository) {
    return new FavoritesViewModel(repository);
  }
}
