package cl.truchoradios.chile.presentation.screens.player;

import android.content.Context;
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
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class FullPlayerViewModel_Factory implements Factory<FullPlayerViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RadioRepositoryImpl> repositoryProvider;

  private final Provider<RadioPlayerManager> playerManagerProvider;

  private final Provider<Context> appContextProvider;

  public FullPlayerViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RadioRepositoryImpl> repositoryProvider,
      Provider<RadioPlayerManager> playerManagerProvider, Provider<Context> appContextProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
    this.playerManagerProvider = playerManagerProvider;
    this.appContextProvider = appContextProvider;
  }

  @Override
  public FullPlayerViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get(), playerManagerProvider.get(), appContextProvider.get());
  }

  public static FullPlayerViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RadioRepositoryImpl> repositoryProvider,
      Provider<RadioPlayerManager> playerManagerProvider, Provider<Context> appContextProvider) {
    return new FullPlayerViewModel_Factory(savedStateHandleProvider, repositoryProvider, playerManagerProvider, appContextProvider);
  }

  public static FullPlayerViewModel newInstance(SavedStateHandle savedStateHandle,
      RadioRepositoryImpl repository, RadioPlayerManager playerManager, Context appContext) {
    return new FullPlayerViewModel(savedStateHandle, repository, playerManager, appContext);
  }
}
