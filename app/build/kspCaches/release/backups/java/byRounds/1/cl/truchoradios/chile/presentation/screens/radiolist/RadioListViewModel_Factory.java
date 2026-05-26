package cl.truchoradios.chile.presentation.screens.radiolist;

import androidx.lifecycle.SavedStateHandle;
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
public final class RadioListViewModel_Factory implements Factory<RadioListViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RadioRepositoryImpl> repositoryProvider;

  public RadioListViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RadioRepositoryImpl> repositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public RadioListViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get());
  }

  public static RadioListViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RadioRepositoryImpl> repositoryProvider) {
    return new RadioListViewModel_Factory(savedStateHandleProvider, repositoryProvider);
  }

  public static RadioListViewModel newInstance(SavedStateHandle savedStateHandle,
      RadioRepositoryImpl repository) {
    return new RadioListViewModel(savedStateHandle, repository);
  }
}
