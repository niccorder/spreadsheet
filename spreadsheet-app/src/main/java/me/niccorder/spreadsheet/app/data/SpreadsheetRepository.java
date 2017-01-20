package me.niccorder.spreadsheet.app.data;

import java.util.List;
import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import rx.Observable;

public interface SpreadsheetRepository {

  /** @return Fully loads the spreadsheet. */
  Observable<SpreadsheetModel> loadSpreadsheet(long id);

  /** @return all locally saved spreadsheets (this is a light model, only id + timestamp!) */
  Observable<List<SpreadsheetModel>> getSavedSpreadsheets();

  /** @return true if success, false otherwise. */
  Observable<Boolean> saveSpreadsheet(SpreadsheetModel spreadsheet);

  /** @return true if success, false otherwise. */
  Observable<Boolean> deleteSpreadsheet(long id);
}
