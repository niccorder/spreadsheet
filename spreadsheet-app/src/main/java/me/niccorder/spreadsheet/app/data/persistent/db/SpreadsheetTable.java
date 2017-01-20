package me.niccorder.spreadsheet.app.data.persistent.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func1;

@AutoValue public abstract class SpreadsheetTable implements Parcelable {
  public static final String TABLE = "spreadsheet_table";

  public static final String ID = "_id";
  public static final String ROWS = "rows";
  public static final String COLUMNS = "columns";
  public static final String LAST_EDIT_TIMESTAMP = "last_edit_timestamp";

  public abstract long id();

  public abstract int rows();

  public abstract int columns();

  public abstract long lastEdit();

  public static Func1<Cursor, List<SpreadsheetTable>> MAP = cursor -> {
    try {
      List<SpreadsheetTable> values = new ArrayList<>(cursor.getCount());

      while (cursor.moveToNext()) {
        long id = Db.getLong(cursor, ID);
        int rows = Db.getInt(cursor, ROWS);
        int columns = Db.getInt(cursor, COLUMNS);
        //values.add(new SpreadsheetTable_());
      }
      return values;
    } finally {
      cursor.close();
    }
  };

  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder id(long id) {
      values.put(ID, id);
      return this;
    }

    public Builder rows(int rows) {
      values.put(ROWS, rows);
      return this;
    }

    public Builder columns(boolean columns) {
      values.put(COLUMNS, columns);
      return this;
    }

    public Builder timestamp(long timestamp) {
      values.put(COLUMNS, timestamp);
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}