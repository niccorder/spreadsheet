package me.niccorder.spreadsheet.app.data.persistent;

import android.database.Cursor;
import com.squareup.sqlbrite.BriteDatabase;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import me.niccorder.spreadsheet.app.data.SpreadsheetRepository;
import me.niccorder.spreadsheet.app.data.persistent.db.CellTable;
import me.niccorder.spreadsheet.app.data.persistent.db.SpreadsheetTable;
import me.niccorder.spreadsheet.app.model.CellModel;
import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import me.niccorder.spreadsheet.util.exception.NotYetImplementedException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SpreadsheetDatastore implements SpreadsheetRepository {

  /** Our database instance */
  private BriteDatabase database;

  public SpreadsheetDatastore(BriteDatabase database) {
    this.database = database;
  }

  @Override public Observable<SpreadsheetModel> loadSpreadsheet(long id) {
    return Observable.create(subscriber -> {
      database.createQuery(SpreadsheetTable.TABLE,
          "SELECT * FROM " + SpreadsheetTable.TABLE + " WHERE _id = ?", Long.toString(id))
          .mapToOne(
              cursor -> new SpreadsheetModel(cursor.getLong(0), cursor.getInt(1), cursor.getInt(2),
                  cursor.getLong(3)))
          .subscribe(spreadsheetModel -> {
            database.createQuery(CellTable.TABLE, "SELECT "
                + CellTable.POS_X
                + ", "
                + CellTable.POS_Y
                + ", "
                + CellTable.DATA
                + " FROM "
                + CellTable.TABLE
                + " WHERE "
                + CellTable.SPREADSHEET_ID
                + " = ?", Long.toString(id))
                .mapToList(cursor -> new CellModel(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(2)))
                .subscribe(cellModels -> {
                  Timber.i(Arrays.toString(cellModels.toArray()));
                  spreadsheetModel.setCells(cellModels);
                  subscriber.onNext(spreadsheetModel);
                });
          });
    }).observeOn(AndroidSchedulers.mainThread()).cast(SpreadsheetModel.class);
  }

  @Override public Observable<List<SpreadsheetModel>> getSavedSpreadsheets() {
    return database.createQuery(SpreadsheetTable.TABLE, "SELECT "
        + SpreadsheetTable.ID
        + ", "
        + SpreadsheetTable.LAST_EDIT_TIMESTAMP
        + " FROM "
        + SpreadsheetTable.TABLE
        + " ORDER BY "
        + SpreadsheetTable.LAST_EDIT_TIMESTAMP
        + " ASC")
        .mapToList(cursor -> new SpreadsheetModel(cursor.getLong(0), cursor.getLong(1)))
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Override public Observable<Boolean> saveSpreadsheet(SpreadsheetModel spreadsheet) {
    return Observable.create(subscriber -> {
      if (spreadsheet.getId() == -1) {
        spreadsheet.setId(database.insert(SpreadsheetTable.TABLE,
            new SpreadsheetTable.Builder().columns(spreadsheet.getColumns())
                .rows(spreadsheet.getRows())
                .build()));
      } else {
        database.update(SpreadsheetTable.TABLE,
            new SpreadsheetTable.Builder().columns(spreadsheet.getColumns())
                .rows(spreadsheet.getRows())
                .build(), SpreadsheetTable.ID + " = ?", Long.toString(spreadsheet.getId()));
      }

      final Collection<CellModel> cells = spreadsheet.getCells();
      for (CellModel cell : cells) {
        database.insert(CellTable.TABLE, new CellTable.Builder().x(cell.getX())
            .y(cell.getY())
            .data(cell.getCurrentData())
            .spreadsheetId((int) spreadsheet.getId())
            .build());
      }
      subscriber.onNext(true);
    }).observeOn(AndroidSchedulers.mainThread()).cast(Boolean.class);
  }

  @Override public Observable<Boolean> deleteSpreadsheet(long id) {
    return Observable.create(subscriber -> {
      subscriber.onNext(
          database.delete(SpreadsheetTable.TABLE, "WHERE " + SpreadsheetTable.ID + " = ?",
              Long.toString(id)) != 0);
    }).observeOn(AndroidSchedulers.mainThread()).cast(Boolean.class);
  }
}
