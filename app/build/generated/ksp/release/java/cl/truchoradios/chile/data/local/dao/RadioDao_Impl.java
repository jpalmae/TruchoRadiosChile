package cl.truchoradios.chile.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.truchoradios.chile.data.local.entity.RadioEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RadioDao_Impl implements RadioDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RadioEntity> __insertionAdapterOfRadioEntity;

  public RadioDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRadioEntity = new EntityInsertionAdapter<RadioEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `radios` (`id`,`name`,`streamUrl`,`streamType`,`imageUrl`,`frequency`,`city`,`region`,`genres`,`description`,`website`,`listeners`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RadioEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getStreamUrl());
        statement.bindString(4, entity.getStreamType());
        statement.bindString(5, entity.getImageUrl());
        statement.bindString(6, entity.getFrequency());
        statement.bindString(7, entity.getCity());
        statement.bindString(8, entity.getRegion());
        statement.bindString(9, entity.getGenres());
        if (entity.getDescription() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getDescription());
        }
        if (entity.getWebsite() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getWebsite());
        }
        statement.bindLong(12, entity.getListeners());
      }
    };
  }

  @Override
  public Object insertAll(final List<RadioEntity> radios,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRadioEntity.insert(radios);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RadioEntity>> getAllRadios() {
    final String _sql = "SELECT * FROM radios ORDER BY listeners DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"radios"}, new Callable<List<RadioEntity>>() {
      @Override
      @NonNull
      public List<RadioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfStreamType = CursorUtil.getColumnIndexOrThrow(_cursor, "streamType");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
          final List<RadioEntity> _result = new ArrayList<RadioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RadioEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpStreamUrl;
            _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            final String _tmpStreamType;
            _tmpStreamType = _cursor.getString(_cursorIndexOfStreamType);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpRegion;
            _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpListeners;
            _tmpListeners = _cursor.getInt(_cursorIndexOfListeners);
            _item = new RadioEntity(_tmpId,_tmpName,_tmpStreamUrl,_tmpStreamType,_tmpImageUrl,_tmpFrequency,_tmpCity,_tmpRegion,_tmpGenres,_tmpDescription,_tmpWebsite,_tmpListeners);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getRadioById(final String id, final Continuation<? super RadioEntity> $completion) {
    final String _sql = "SELECT * FROM radios WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RadioEntity>() {
      @Override
      @Nullable
      public RadioEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfStreamType = CursorUtil.getColumnIndexOrThrow(_cursor, "streamType");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
          final RadioEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpStreamUrl;
            _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            final String _tmpStreamType;
            _tmpStreamType = _cursor.getString(_cursorIndexOfStreamType);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpRegion;
            _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpListeners;
            _tmpListeners = _cursor.getInt(_cursorIndexOfListeners);
            _result = new RadioEntity(_tmpId,_tmpName,_tmpStreamUrl,_tmpStreamType,_tmpImageUrl,_tmpFrequency,_tmpCity,_tmpRegion,_tmpGenres,_tmpDescription,_tmpWebsite,_tmpListeners);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RadioEntity>> searchRadios(final String query) {
    final String _sql = "SELECT * FROM radios WHERE name LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"radios"}, new Callable<List<RadioEntity>>() {
      @Override
      @NonNull
      public List<RadioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfStreamType = CursorUtil.getColumnIndexOrThrow(_cursor, "streamType");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
          final List<RadioEntity> _result = new ArrayList<RadioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RadioEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpStreamUrl;
            _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            final String _tmpStreamType;
            _tmpStreamType = _cursor.getString(_cursorIndexOfStreamType);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpRegion;
            _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpListeners;
            _tmpListeners = _cursor.getInt(_cursorIndexOfListeners);
            _item = new RadioEntity(_tmpId,_tmpName,_tmpStreamUrl,_tmpStreamType,_tmpImageUrl,_tmpFrequency,_tmpCity,_tmpRegion,_tmpGenres,_tmpDescription,_tmpWebsite,_tmpListeners);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RadioEntity>> getRadiosByRegion(final String region) {
    final String _sql = "SELECT * FROM radios WHERE region = ? ORDER BY listeners DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, region);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"radios"}, new Callable<List<RadioEntity>>() {
      @Override
      @NonNull
      public List<RadioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfStreamType = CursorUtil.getColumnIndexOrThrow(_cursor, "streamType");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
          final List<RadioEntity> _result = new ArrayList<RadioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RadioEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpStreamUrl;
            _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            final String _tmpStreamType;
            _tmpStreamType = _cursor.getString(_cursorIndexOfStreamType);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpRegion;
            _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpListeners;
            _tmpListeners = _cursor.getInt(_cursorIndexOfListeners);
            _item = new RadioEntity(_tmpId,_tmpName,_tmpStreamUrl,_tmpStreamType,_tmpImageUrl,_tmpFrequency,_tmpCity,_tmpRegion,_tmpGenres,_tmpDescription,_tmpWebsite,_tmpListeners);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<RadioEntity>> getRadiosByGenre(final String genre) {
    final String _sql = "SELECT * FROM radios WHERE genres LIKE '%' || ? || '%' ORDER BY listeners DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, genre);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"radios"}, new Callable<List<RadioEntity>>() {
      @Override
      @NonNull
      public List<RadioEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStreamUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streamUrl");
          final int _cursorIndexOfStreamType = CursorUtil.getColumnIndexOrThrow(_cursor, "streamType");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "frequency");
          final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
          final int _cursorIndexOfRegion = CursorUtil.getColumnIndexOrThrow(_cursor, "region");
          final int _cursorIndexOfGenres = CursorUtil.getColumnIndexOrThrow(_cursor, "genres");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfWebsite = CursorUtil.getColumnIndexOrThrow(_cursor, "website");
          final int _cursorIndexOfListeners = CursorUtil.getColumnIndexOrThrow(_cursor, "listeners");
          final List<RadioEntity> _result = new ArrayList<RadioEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RadioEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpStreamUrl;
            _tmpStreamUrl = _cursor.getString(_cursorIndexOfStreamUrl);
            final String _tmpStreamType;
            _tmpStreamType = _cursor.getString(_cursorIndexOfStreamType);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final String _tmpFrequency;
            _tmpFrequency = _cursor.getString(_cursorIndexOfFrequency);
            final String _tmpCity;
            _tmpCity = _cursor.getString(_cursorIndexOfCity);
            final String _tmpRegion;
            _tmpRegion = _cursor.getString(_cursorIndexOfRegion);
            final String _tmpGenres;
            _tmpGenres = _cursor.getString(_cursorIndexOfGenres);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpWebsite;
            if (_cursor.isNull(_cursorIndexOfWebsite)) {
              _tmpWebsite = null;
            } else {
              _tmpWebsite = _cursor.getString(_cursorIndexOfWebsite);
            }
            final int _tmpListeners;
            _tmpListeners = _cursor.getInt(_cursorIndexOfListeners);
            _item = new RadioEntity(_tmpId,_tmpName,_tmpStreamUrl,_tmpStreamType,_tmpImageUrl,_tmpFrequency,_tmpCity,_tmpRegion,_tmpGenres,_tmpDescription,_tmpWebsite,_tmpListeners);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM radios";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
