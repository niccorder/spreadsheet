package me.niccorder.spreadsheet.app.data.persistent.db;

import android.app.Application;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class DbOpenHelper extends SQLiteOpenHelper {
  private static final int VERSION = 1;

  private static final String CREATE_SPREADSHEET_TABLE = ""
      + "CREATE TABLE "
      + SpreadsheetTable.TABLE
      + "("
      + SpreadsheetTable.ID
      + " INTEGER NOT NULL PRIMARY KEY,"
      + SpreadsheetTable.ROWS
      + " INTEGER NOT NULL DEFAULT 8,"
      + SpreadsheetTable.COLUMNS
      + " INTEGER NOT NULL DEFAULT 8,"
      + SpreadsheetTable.LAST_EDIT_TIMESTAMP
      + " INTEGER NOT NULL DEFAULT CURRENT_TIMESTAMP"
      + ")";

  private static final String CREATE_CELL_TABLE = ""
      + "CREATE TABLE "
      + CellTable.TABLE
      + "("
      + CellTable.ID
      + " INTEGER NOT NULL PRIMARY KEY,"
      + CellTable.POS_X
      + " INTEGER NOT NULL,"
      + CellTable.POS_Y
      + " INTEGER NOT NULL,"
      + CellTable.SPREADSHEET_ID
      + " INTEGER NOT NULL REFERENCES "
      + SpreadsheetTable.TABLE
      + "("
      + SpreadsheetTable.ID
      + "))";

  public DbOpenHelper(Application application) {
    super(application, "spreadsheets.db", null, 1);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_SPREADSHEET_TABLE);
    db.execSQL(CREATE_CELL_TABLE);
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  }
}