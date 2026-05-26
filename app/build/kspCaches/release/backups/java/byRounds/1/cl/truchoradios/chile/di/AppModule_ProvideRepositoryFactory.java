package cl.truchoradios.chile.di;

import android.content.Context;
import cl.truchoradios.chile.data.local.dao.FavoriteDao;
import cl.truchoradios.chile.data.local.dao.RadioDao;
import cl.truchoradios.chile.data.local.dao.RecentDao;
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideRepositoryFactory implements Factory<RadioRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<RadioDao> radioDaoProvider;

  private final Provider<FavoriteDao> favoriteDaoProvider;

  private final Provider<RecentDao> recentDaoProvider;

  public AppModule_ProvideRepositoryFactory(Provider<Context> contextProvider,
      Provider<RadioDao> radioDaoProvider, Provider<FavoriteDao> favoriteDaoProvider,
      Provider<RecentDao> recentDaoProvider) {
    this.contextProvider = contextProvider;
    this.radioDaoProvider = radioDaoProvider;
    this.favoriteDaoProvider = favoriteDaoProvider;
    this.recentDaoProvider = recentDaoProvider;
  }

  @Override
  public RadioRepositoryImpl get() {
    return provideRepository(contextProvider.get(), radioDaoProvider.get(), favoriteDaoProvider.get(), recentDaoProvider.get());
  }

  public static AppModule_ProvideRepositoryFactory create(Provider<Context> contextProvider,
      Provider<RadioDao> radioDaoProvider, Provider<FavoriteDao> favoriteDaoProvider,
      Provider<RecentDao> recentDaoProvider) {
    return new AppModule_ProvideRepositoryFactory(contextProvider, radioDaoProvider, favoriteDaoProvider, recentDaoProvider);
  }

  public static RadioRepositoryImpl provideRepository(Context context, RadioDao radioDao,
      FavoriteDao favoriteDao, RecentDao recentDao) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRepository(context, radioDao, favoriteDao, recentDao));
  }
}
