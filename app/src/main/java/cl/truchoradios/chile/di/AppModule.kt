package cl.truchoradios.chile.di

import android.content.Context
import cl.truchoradios.chile.data.local.RadioDatabase
import cl.truchoradios.chile.data.local.SettingsManager
import cl.truchoradios.chile.data.local.dao.FavoriteDao
import cl.truchoradios.chile.data.local.dao.RadioDao
import cl.truchoradios.chile.data.local.dao.RecentDao
import cl.truchoradios.chile.data.repository.RadioRepositoryImpl
import cl.truchoradios.chile.player.RadioPlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RadioDatabase =
        RadioDatabase.getDatabase(context)

    @Provides
    fun provideRadioDao(db: RadioDatabase): RadioDao = db.radioDao()

    @Provides
    fun provideFavoriteDao(db: RadioDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    fun provideRecentDao(db: RadioDatabase): RecentDao = db.recentDao()

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
        radioDao: RadioDao,
        favoriteDao: FavoriteDao,
        recentDao: RecentDao,
    ) = RadioRepositoryImpl(context, radioDao, favoriteDao, recentDao)

    @Provides
    @Singleton
    fun providePlayerManager(@ApplicationContext context: Context) = RadioPlayerManager(context)

    @Provides
    @Singleton
    fun provideSettingsManager() = SettingsManager()
}
