package cl.truchoradios.chile.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import cl.truchoradios.chile.data.local.dao.FavoriteDao;
import cl.truchoradios.chile.data.local.dao.FavoriteDao_Impl;
import cl.truchoradios.chile.data.local.dao.RadioDao;
import cl.truchoradios.chile.data.local.dao.RadioDao_Impl;
import cl.truchoradios.chile.data.local.dao.RecentDao;
import cl.truchoradios.chile.data.local.dao.RecentDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RadioDatabase_Impl extends RadioDatabase {
  private volatile RadioDao _radioDao;

  private volatile FavoriteDao _favoriteDao;

  private volatile RecentDao _recentDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(7) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `radios` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `streamUrl` TEXT NOT NULL, `streamType` TEXT NOT NULL, `imageUrl` TEXT NOT NULL, `frequency` TEXT NOT NULL, `city` TEXT NOT NULL, `region` TEXT NOT NULL, `genres` TEXT NOT NULL, `description` TEXT, `website` TEXT, `listeners` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `favorites` (`radioId` TEXT NOT NULL, `addedAt` INTEGER NOT NULL, PRIMARY KEY(`radioId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `recent` (`radioId` TEXT NOT NULL, `playedAt` INTEGER NOT NULL, PRIMARY KEY(`radioId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7565cb8a04ae355724968caf61f88382')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `radios`");
        db.execSQL("DROP TABLE IF EXISTS `favorites`");
        db.execSQL("DROP TABLE IF EXISTS `recent`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRadios = new HashMap<String, TableInfo.Column>(12);
        _columnsRadios.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("streamUrl", new TableInfo.Column("streamUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("streamType", new TableInfo.Column("streamType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("frequency", new TableInfo.Column("frequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("city", new TableInfo.Column("city", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("region", new TableInfo.Column("region", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("genres", new TableInfo.Column("genres", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("website", new TableInfo.Column("website", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRadios.put("listeners", new TableInfo.Column("listeners", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRadios = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRadios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRadios = new TableInfo("radios", _columnsRadios, _foreignKeysRadios, _indicesRadios);
        final TableInfo _existingRadios = TableInfo.read(db, "radios");
        if (!_infoRadios.equals(_existingRadios)) {
          return new RoomOpenHelper.ValidationResult(false, "radios(cl.truchoradios.chile.data.local.entity.RadioEntity).\n"
                  + " Expected:\n" + _infoRadios + "\n"
                  + " Found:\n" + _existingRadios);
        }
        final HashMap<String, TableInfo.Column> _columnsFavorites = new HashMap<String, TableInfo.Column>(2);
        _columnsFavorites.put("radioId", new TableInfo.Column("radioId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFavorites.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFavorites = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFavorites = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFavorites = new TableInfo("favorites", _columnsFavorites, _foreignKeysFavorites, _indicesFavorites);
        final TableInfo _existingFavorites = TableInfo.read(db, "favorites");
        if (!_infoFavorites.equals(_existingFavorites)) {
          return new RoomOpenHelper.ValidationResult(false, "favorites(cl.truchoradios.chile.data.local.entity.FavoriteEntity).\n"
                  + " Expected:\n" + _infoFavorites + "\n"
                  + " Found:\n" + _existingFavorites);
        }
        final HashMap<String, TableInfo.Column> _columnsRecent = new HashMap<String, TableInfo.Column>(2);
        _columnsRecent.put("radioId", new TableInfo.Column("radioId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRecent.put("playedAt", new TableInfo.Column("playedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRecent = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRecent = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRecent = new TableInfo("recent", _columnsRecent, _foreignKeysRecent, _indicesRecent);
        final TableInfo _existingRecent = TableInfo.read(db, "recent");
        if (!_infoRecent.equals(_existingRecent)) {
          return new RoomOpenHelper.ValidationResult(false, "recent(cl.truchoradios.chile.data.local.entity.RecentEntity).\n"
                  + " Expected:\n" + _infoRecent + "\n"
                  + " Found:\n" + _existingRecent);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "7565cb8a04ae355724968caf61f88382", "e3c9ae10edf7afd93d55c4b6b951a400");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "radios","favorites","recent");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `radios`");
      _db.execSQL("DELETE FROM `favorites`");
      _db.execSQL("DELETE FROM `recent`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(RadioDao.class, RadioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FavoriteDao.class, FavoriteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RecentDao.class, RecentDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public RadioDao radioDao() {
    if (_radioDao != null) {
      return _radioDao;
    } else {
      synchronized(this) {
        if(_radioDao == null) {
          _radioDao = new RadioDao_Impl(this);
        }
        return _radioDao;
      }
    }
  }

  @Override
  public FavoriteDao favoriteDao() {
    if (_favoriteDao != null) {
      return _favoriteDao;
    } else {
      synchronized(this) {
        if(_favoriteDao == null) {
          _favoriteDao = new FavoriteDao_Impl(this);
        }
        return _favoriteDao;
      }
    }
  }

  @Override
  public RecentDao recentDao() {
    if (_recentDao != null) {
      return _recentDao;
    } else {
      synchronized(this) {
        if(_recentDao == null) {
          _recentDao = new RecentDao_Impl(this);
        }
        return _recentDao;
      }
    }
  }
}
