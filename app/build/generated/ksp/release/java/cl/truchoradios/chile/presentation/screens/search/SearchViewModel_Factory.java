package cl.truchoradios.chile.presentation.screens.search;

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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<RadioRepositoryImpl> repositoryProvider;

  public SearchViewModel_Factory(Provider<RadioRepositoryImpl> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchViewModel_Factory create(Provider<RadioRepositoryImpl> repositoryProvider) {
    return new SearchViewModel_Factory(repositoryProvider);
  }

  public static SearchViewModel newInstance(RadioRepositoryImpl repository) {
    return new SearchViewModel(repository);
  }
}
