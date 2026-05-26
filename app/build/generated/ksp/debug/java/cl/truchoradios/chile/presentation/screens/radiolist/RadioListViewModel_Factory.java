package cl.truchoradios.chile.presentation.screens.radiolist;

import androidx.lifecycle.SavedStateHandle;
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
public final class RadioListViewModel_Factory implements Factory<RadioListViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RadioRepositoryImpl> repositoryProvider;

  private final Provider<RadioPlayerManager> playerManagerProvider;

  public RadioListViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RadioRepositoryImpl> repositoryProvider,
      Provider<RadioPlayerManager> playerManagerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
    this.playerManagerProvider = playerManagerProvider;
  }

  @Override
  public RadioListViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get(), playerManagerProvider.get());
  }

  public static RadioListViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RadioRepositoryImpl> repositoryProvider,
      Provider<RadioPlayerManager> playerManagerProvider) {
    return new RadioListViewModel_Factory(savedStateHandleProvider, repositoryProvider, playerManagerProvider);
  }

  public static RadioListViewModel newInstance(SavedStateHandle savedStateHandle,
      RadioRepositoryImpl repository, RadioPlayerManager playerManager) {
    return new RadioListViewModel(savedStateHandle, repository, playerManager);
  }
}
