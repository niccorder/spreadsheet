package me.niccorder.spreadsheet.app.data.persistent.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Func1;

@AutoValue public abstract class CellTable implements Parcelable {
  public static final String TABLE = "cell_table";

  public static final String ID = "_id";
  public static final String POS_X = "pos_x";
  public static final String POS_Y = "pos_y";
  public static final String SPREADSHEET_ID = "spreadsheet_id";

  public abstract long id();

  public abstract int x();

  public abstract int y();

  public abstract long spreadsheetId();

  public static Func1<Cursor, List<SpreadsheetTable>> MAP = cursor -> {
    try {
      List<SpreadsheetTable> values = new ArrayList<>(cursor.getCount());

      while (cursor.moveToNext()) {
        long id = Db.getLong(cursor, ID);
        int x = Db.getInt(cursor, POS_X);
        int y = Db.getInt(cursor, POS_Y);
        long spreadsheetId = Db.getLong(cursor, SPREADSHEET_ID);
        //values.add(new SpreadsheetTable_());
      }
      return values;
    } finally {
      cursor.close();
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
  }

  public static final class Builder {
    private final ContentValues values = new ContentValues();

    public Builder id(long id) {
      values.put(ID, id);
      return this;
    }

    public Builder x(int x) {
      values.put(POS_X, x);
      return this;
    }

    public Builder y(int y) {
      values.put(POS_Y, y);
      return this;
    }

    public Builder spreadsheetId(int spreadsheetId) {
      values.put(SPREADSHEET_ID, spreadsheetId);
      return this;
    }

    public ContentValues build() {
      return values;
    }
  }
}
