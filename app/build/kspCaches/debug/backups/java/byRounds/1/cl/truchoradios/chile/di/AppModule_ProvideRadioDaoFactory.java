package cl.truchoradios.chile.di;

import cl.truchoradios.chile.data.local.RadioDatabase;
import cl.truchoradios.chile.data.local.dao.RadioDao;
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
public final class AppModule_ProvideRadioDaoFactory implements Factory<RadioDao> {
  private final Provider<RadioDatabase> dbProvider;

  public AppModule_ProvideRadioDaoFactory(Provider<RadioDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RadioDao get() {
    return provideRadioDao(dbProvider.get());
  }

  public static AppModule_ProvideRadioDaoFactory create(Provider<RadioDatabase> dbProvider) {
    return new AppModule_ProvideRadioDaoFactory(dbProvider);
  }

  public static RadioDao provideRadioDao(RadioDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRadioDao(db));
  }
}
