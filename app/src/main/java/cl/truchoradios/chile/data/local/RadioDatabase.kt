package cl.truchoradios.chile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.truchoradios.chile.data.local.dao.FavoriteDao
import cl.truchoradios.chile.data.local.dao.RadioDao
import cl.truchoradios.chile.data.local.dao.RecentDao
import cl.truchoradios.chile.data.local.entity.FavoriteEntity
import cl.truchoradios.chile.data.local.entity.RadioEntity
import cl.truchoradios.chile.data.local.entity.RecentEntity

@Database(
    entities = [RadioEntity::class, FavoriteEntity::class, RecentEntity::class],
    version = 7,
    exportSchema = false
)
abstract class RadioDatabase : RoomDatabase() {
    abstract fun radioDao(): RadioDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun recentDao(): RecentDao

    companion object {
        @Volatile
        private var INSTANCE: RadioDatabase? = null

        // Example migration for future schema changes.
        // Add new Migration objects here and pass to database builder instead
        // of fallbackToDestructiveMigration().
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // db.execSQL("ALTER TABLE ...")
            }
        }

        fun getDatabase(context: Context): RadioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RadioDatabase::class.java,
                    "trucho_radios_db"
                )
                    .addMigrations(MIGRATION_7_8)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
