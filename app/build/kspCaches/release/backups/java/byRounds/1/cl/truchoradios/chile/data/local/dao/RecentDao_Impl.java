package cl.truchoradios.chile.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import cl.truchoradios.chile.data.local.entity.RecentEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class RecentDao_Impl implements RecentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecentEntity> __insertionAdapterOfRecentEntity;

  private final SharedSQLiteStatement __preparedStmtOfRemoveRecent;

  public RecentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecentEntity = new EntityInsertionAdapter<RecentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recent` (`radioId`,`playedAt`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecentEntity entity) {
        statement.bindString(1, entity.getRadioId());
        statement.bindLong(2, entity.getPlayedAt());
      }
    };
    this.__preparedStmtOfRemoveRecent = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recent WHERE radioId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object addRecent(final RecentEntity recent, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecentEntity.insert(recent);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object removeRecent(final String radioId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveRecent.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, radioId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemoveRecent.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RecentEntity>> getRecent() {
    final String _sql = "SELECT * FROM recent ORDER BY playedAt DESC LIMIT 20";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"recent"}, new Callable<List<RecentEntity>>() {
      @Override
      @NonNull
      public List<RecentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRadioId = CursorUtil.getColumnIndexOrThrow(_cursor, "radioId");
          final int _cursorIndexOfPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "playedAt");
          final List<RecentEntity> _result = new ArrayList<RecentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RecentEntity _item;
            final String _tmpRadioId;
            _tmpRadioId = _cursor.getString(_cursorIndexOfRadioId);
            final long _tmpPlayedAt;
            _tmpPlayedAt = _cursor.getLong(_cursorIndexOfPlayedAt);
            _item = new RecentEntity(_tmpRadioId,_tmpPlayedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
