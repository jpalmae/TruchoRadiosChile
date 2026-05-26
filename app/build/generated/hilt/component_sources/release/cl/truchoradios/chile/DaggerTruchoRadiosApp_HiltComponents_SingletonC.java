package cl.truchoradios.chile;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import cl.truchoradios.chile.data.local.RadioDatabase;
import cl.truchoradios.chile.data.local.dao.FavoriteDao;
import cl.truchoradios.chile.data.local.dao.RadioDao;
import cl.truchoradios.chile.data.local.dao.RecentDao;
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl;
import cl.truchoradios.chile.di.AppModule_ProvideDatabaseFactory;
import cl.truchoradios.chile.di.AppModule_ProvideFavoriteDaoFactory;
import cl.truchoradios.chile.di.AppModule_ProvidePlayerManagerFactory;
import cl.truchoradios.chile.di.AppModule_ProvideRadioDaoFactory;
import cl.truchoradios.chile.di.AppModule_ProvideRecentDaoFactory;
import cl.truchoradios.chile.di.AppModule_ProvideRepositoryFactory;
import cl.truchoradios.chile.navigation.PlayerSharedViewModel;
import cl.truchoradios.chile.navigation.PlayerSharedViewModel_HiltModules;
import cl.truchoradios.chile.player.RadioPlayerManager;
import cl.truchoradios.chile.presentation.screens.favorites.FavoritesViewModel;
import cl.truchoradios.chile.presentation.screens.favorites.FavoritesViewModel_HiltModules;
import cl.truchoradios.chile.presentation.screens.home.HomeViewModel;
import cl.truchoradios.chile.presentation.screens.home.HomeViewModel_HiltModules;
import cl.truchoradios.chile.presentation.screens.player.FullPlayerViewModel;
import cl.truchoradios.chile.presentation.screens.player.FullPlayerViewModel_HiltModules;
import cl.truchoradios.chile.presentation.screens.radiolist.RadioListViewModel;
import cl.truchoradios.chile.presentation.screens.radiolist.RadioListViewModel_HiltModules;
import cl.truchoradios.chile.presentation.screens.search.SearchViewModel;
import cl.truchoradios.chile.presentation.screens.search.SearchViewModel_HiltModules;
import cl.truchoradios.chile.service.RadioPlayerService;
import cl.truchoradios.chile.service.RadioPlayerService_MembersInjector;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerTruchoRadiosApp_HiltComponents_SingletonC {
  private DaggerTruchoRadiosApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public TruchoRadiosApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements TruchoRadiosApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements TruchoRadiosApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements TruchoRadiosApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements TruchoRadiosApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements TruchoRadiosApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements TruchoRadiosApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements TruchoRadiosApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public TruchoRadiosApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends TruchoRadiosApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends TruchoRadiosApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends TruchoRadiosApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends TruchoRadiosApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(ImmutableMap.<String, Boolean>builderWithExpectedSize(6).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_favorites_FavoritesViewModel, FavoritesViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_player_FullPlayerViewModel, FullPlayerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.cl_truchoradios_chile_navigation_PlayerSharedViewModel, PlayerSharedViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_radiolist_RadioListViewModel, RadioListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_search_SearchViewModel, SearchViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPlayerManager(instance, singletonCImpl.providePlayerManagerProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String cl_truchoradios_chile_presentation_screens_favorites_FavoritesViewModel = "cl.truchoradios.chile.presentation.screens.favorites.FavoritesViewModel";

      static String cl_truchoradios_chile_presentation_screens_player_FullPlayerViewModel = "cl.truchoradios.chile.presentation.screens.player.FullPlayerViewModel";

      static String cl_truchoradios_chile_navigation_PlayerSharedViewModel = "cl.truchoradios.chile.navigation.PlayerSharedViewModel";

      static String cl_truchoradios_chile_presentation_screens_search_SearchViewModel = "cl.truchoradios.chile.presentation.screens.search.SearchViewModel";

      static String cl_truchoradios_chile_presentation_screens_home_HomeViewModel = "cl.truchoradios.chile.presentation.screens.home.HomeViewModel";

      static String cl_truchoradios_chile_presentation_screens_radiolist_RadioListViewModel = "cl.truchoradios.chile.presentation.screens.radiolist.RadioListViewModel";

      @KeepFieldType
      FavoritesViewModel cl_truchoradios_chile_presentation_screens_favorites_FavoritesViewModel2;

      @KeepFieldType
      FullPlayerViewModel cl_truchoradios_chile_presentation_screens_player_FullPlayerViewModel2;

      @KeepFieldType
      PlayerSharedViewModel cl_truchoradios_chile_navigation_PlayerSharedViewModel2;

      @KeepFieldType
      SearchViewModel cl_truchoradios_chile_presentation_screens_search_SearchViewModel2;

      @KeepFieldType
      HomeViewModel cl_truchoradios_chile_presentation_screens_home_HomeViewModel2;

      @KeepFieldType
      RadioListViewModel cl_truchoradios_chile_presentation_screens_radiolist_RadioListViewModel2;
    }
  }

  private static final class ViewModelCImpl extends TruchoRadiosApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<FavoritesViewModel> favoritesViewModelProvider;

    private Provider<FullPlayerViewModel> fullPlayerViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<PlayerSharedViewModel> playerSharedViewModelProvider;

    private Provider<RadioListViewModel> radioListViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.favoritesViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.fullPlayerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.playerSharedViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.radioListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(ImmutableMap.<String, javax.inject.Provider<ViewModel>>builderWithExpectedSize(6).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_favorites_FavoritesViewModel, ((Provider) favoritesViewModelProvider)).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_player_FullPlayerViewModel, ((Provider) fullPlayerViewModelProvider)).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.cl_truchoradios_chile_navigation_PlayerSharedViewModel, ((Provider) playerSharedViewModelProvider)).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_radiolist_RadioListViewModel, ((Provider) radioListViewModelProvider)).put(LazyClassKeyProvider.cl_truchoradios_chile_presentation_screens_search_SearchViewModel, ((Provider) searchViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return ImmutableMap.<Class<?>, Object>of();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String cl_truchoradios_chile_presentation_screens_home_HomeViewModel = "cl.truchoradios.chile.presentation.screens.home.HomeViewModel";

      static String cl_truchoradios_chile_presentation_screens_favorites_FavoritesViewModel = "cl.truchoradios.chile.presentation.screens.favorites.FavoritesViewModel";

      static String cl_truchoradios_chile_presentation_screens_radiolist_RadioListViewModel = "cl.truchoradios.chile.presentation.screens.radiolist.RadioListViewModel";

      static String cl_truchoradios_chile_navigation_PlayerSharedViewModel = "cl.truchoradios.chile.navigation.PlayerSharedViewModel";

      static String cl_truchoradios_chile_presentation_screens_player_FullPlayerViewModel = "cl.truchoradios.chile.presentation.screens.player.FullPlayerViewModel";

      static String cl_truchoradios_chile_presentation_screens_search_SearchViewModel = "cl.truchoradios.chile.presentation.screens.search.SearchViewModel";

      @KeepFieldType
      HomeViewModel cl_truchoradios_chile_presentation_screens_home_HomeViewModel2;

      @KeepFieldType
      FavoritesViewModel cl_truchoradios_chile_presentation_screens_favorites_FavoritesViewModel2;

      @KeepFieldType
      RadioListViewModel cl_truchoradios_chile_presentation_screens_radiolist_RadioListViewModel2;

      @KeepFieldType
      PlayerSharedViewModel cl_truchoradios_chile_navigation_PlayerSharedViewModel2;

      @KeepFieldType
      FullPlayerViewModel cl_truchoradios_chile_presentation_screens_player_FullPlayerViewModel2;

      @KeepFieldType
      SearchViewModel cl_truchoradios_chile_presentation_screens_search_SearchViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // cl.truchoradios.chile.presentation.screens.favorites.FavoritesViewModel 
          return (T) new FavoritesViewModel(singletonCImpl.provideRepositoryProvider.get());

          case 1: // cl.truchoradios.chile.presentation.screens.player.FullPlayerViewModel 
          return (T) new FullPlayerViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideRepositoryProvider.get(), singletonCImpl.providePlayerManagerProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // cl.truchoradios.chile.presentation.screens.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.provideRepositoryProvider.get());

          case 3: // cl.truchoradios.chile.navigation.PlayerSharedViewModel 
          return (T) new PlayerSharedViewModel(singletonCImpl.providePlayerManagerProvider.get());

          case 4: // cl.truchoradios.chile.presentation.screens.radiolist.RadioListViewModel 
          return (T) new RadioListViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.provideRepositoryProvider.get());

          case 5: // cl.truchoradios.chile.presentation.screens.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.provideRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends TruchoRadiosApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends TruchoRadiosApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectRadioPlayerService(RadioPlayerService radioPlayerService) {
      injectRadioPlayerService2(radioPlayerService);
    }

    @CanIgnoreReturnValue
    private RadioPlayerService injectRadioPlayerService2(RadioPlayerService instance) {
      RadioPlayerService_MembersInjector.injectPlayerManager(instance, singletonCImpl.providePlayerManagerProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends TruchoRadiosApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<RadioPlayerManager> providePlayerManagerProvider;

    private Provider<RadioDatabase> provideDatabaseProvider;

    private Provider<RadioRepositoryImpl> provideRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private RadioDao radioDao() {
      return AppModule_ProvideRadioDaoFactory.provideRadioDao(provideDatabaseProvider.get());
    }

    private FavoriteDao favoriteDao() {
      return AppModule_ProvideFavoriteDaoFactory.provideFavoriteDao(provideDatabaseProvider.get());
    }

    private RecentDao recentDao() {
      return AppModule_ProvideRecentDaoFactory.provideRecentDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.providePlayerManagerProvider = DoubleCheck.provider(new SwitchingProvider<RadioPlayerManager>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<RadioDatabase>(singletonCImpl, 2));
      this.provideRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<RadioRepositoryImpl>(singletonCImpl, 1));
    }

    @Override
    public void injectTruchoRadiosApp(TruchoRadiosApp truchoRadiosApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // cl.truchoradios.chile.player.RadioPlayerManager 
          return (T) AppModule_ProvidePlayerManagerFactory.providePlayerManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // cl.truchoradios.chile.data.repository.RadioRepositoryImpl 
          return (T) AppModule_ProvideRepositoryFactory.provideRepository(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.radioDao(), singletonCImpl.favoriteDao(), singletonCImpl.recentDao());

          case 2: // cl.truchoradios.chile.data.local.RadioDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
