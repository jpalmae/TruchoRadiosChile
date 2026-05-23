package cl.truchoradios.chile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.truchoradios.chile.data.local.dao.FavoriteDao
import cl.truchoradios.chile.data.local.dao.RadioDao
import cl.truchoradios.chile.data.local.dao.RecentDao
import cl.truchoradios.chile.data.local.entity.FavoriteEntity
import cl.truchoradios.chile.data.local.entity.RadioEntity
import cl.truchoradios.chile.data.local.entity.RecentEntity

@Database(
    entities = [RadioEntity::class, FavoriteEntity::class, RecentEntity::class],
    version = 2,
    exportSchema = false
)
abstract class RadioDatabase : RoomDatabase() {
    abstract fun radioDao(): RadioDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recentDao(): RecentDao

    companion object {
        @Volatile
        private var INSTANCE: RadioDatabase? = null

        fun getDatabase(context: Context): RadioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RadioDatabase::class.java,
                    "trucho_radios_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
