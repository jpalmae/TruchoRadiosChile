package cl.truchoradios.chile.di;

import cl.truchoradios.chile.data.local.RadioDatabase;
import cl.truchoradios.chile.data.local.dao.RecentDao;
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
public final class AppModule_ProvideRecentDaoFactory implements Factory<RecentDao> {
  private final Provider<RadioDatabase> dbProvider;

  public AppModule_ProvideRecentDaoFactory(Provider<RadioDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RecentDao get() {
    return provideRecentDao(dbProvider.get());
  }

  public static AppModule_ProvideRecentDaoFactory create(Provider<RadioDatabase> dbProvider) {
    return new AppModule_ProvideRecentDaoFactory(dbProvider);
  }

  public static RecentDao provideRecentDao(RadioDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRecentDao(db));
  }
}
