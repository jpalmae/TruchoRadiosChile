package cl.truchoradios.chile.di;

import cl.truchoradios.chile.data.local.RadioDatabase;
import cl.truchoradios.chile.data.local.dao.FavoriteDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideFavoriteDaoFactory implements Factory<FavoriteDao> {
  private final Provider<RadioDatabase> dbProvider;

  public AppModule_ProvideFavoriteDaoFactory(Provider<RadioDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FavoriteDao get() {
    return provideFavoriteDao(dbProvider.get());
  }

  public static AppModule_ProvideFavoriteDaoFactory create(Provider<RadioDatabase> dbProvider) {
    return new AppModule_ProvideFavoriteDaoFactory(dbProvider);
  }

  public static FavoriteDao provideFavoriteDao(RadioDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFavoriteDao(db));
  }
}
