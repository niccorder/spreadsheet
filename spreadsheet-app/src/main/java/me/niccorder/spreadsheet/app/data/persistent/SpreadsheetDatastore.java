package me.niccorder.spreadsheet.app.data.persistent;

import com.squareup.sqlbrite.BriteDatabase;
import java.util.List;
import me.niccorder.spreadsheet.app.data.SpreadsheetRepository;
import me.niccorder.spreadsheet.app.data.persistent.db.SpreadsheetTable;
import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import me.niccorder.spreadsheet.util.exception.NotYetImplementedException;
import rx.Observable;

public class SpreadsheetDatastore implements SpreadsheetRepository {

  /** Our database instance */
  private BriteDatabase database;

  public SpreadsheetDatastore(BriteDatabase database) {
    this.database = database;
  }

  @Override public Observable<SpreadsheetModel> loadSpreadsheet(long id) {
    return database.createQuery(SpreadsheetTable.TABLE, "SELECT * WHERE _id = ?", Long.toString(id))
        .mapToOne(cursor -> new SpreadsheetModel());
  }

  @Override public Observable<List<SpreadsheetModel>> getSavedSpreadsheets() {
    return Observable.error(new NotYetImplementedException("getSavedSpreadsheet()"));
  }

  @Override public Observable<Boolean> saveSpreadsheet(SpreadsheetModel spreadsheet) {
    return Observable.error(new NotYetImplementedException("saveSpreadsheet()"));
  }

  @Override public Observable<Boolean> deleteSpreadsheet(long id) {
    return Observable.error(new NotYetImplementedException("deleteSpreadsheet()"));
  }
}
