package me.niccorder.spreadsheet.app.pres.impl;

import android.text.TextUtils;
import java.util.List;
import javax.inject.Inject;
import me.niccorder.spreadsheet.app.data.SpreadsheetRepository;
import me.niccorder.spreadsheet.app.data.persistent.SpreadsheetDatastore;
import me.niccorder.spreadsheet.app.model.CellModel;
import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import me.niccorder.spreadsheet.app.pres.CellGridPresenter;
import me.niccorder.spreadsheet.app.view.GridView;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Implementation of {@link CellGridPresenter} that will actually preform the contract that the
 * {@link CellGridPresenter} provides.
 */
public class CellGridPresenterImpl implements CellGridPresenter<GridView> {

  /** Our view that this presenter is managing. */
  private GridView view;

  /** Model that represents the current spreadsheet */
  SpreadsheetModel spreadsheetModel;

  /** Coordinates pointing to the currently focused cell */
  int[] currentFocus;

  /** Flag that represents if we are editing a cell. */
  boolean isEditing;

  /** Flag that represents if we are loading a spreadsheet. */
  boolean isLoading;

  /** Our datastore that holds any historic information */
  SpreadsheetRepository datastore;

  @Inject public CellGridPresenterImpl(SpreadsheetRepository datastore) {
    this.datastore = datastore;
    this.spreadsheetModel = new SpreadsheetModel();
    this.currentFocus = new int[2];
    this.isEditing = false;
  }

  /** The view is responsible for attaching/detaching itself. */
  @Override public void setView(GridView view) {
    this.view = view;
  }

  /** Formality methods */
  @Override public void create() {
    Timber.d("create()");
  }

  /** Formality methods */
  @Override public void resume() {
    Timber.d("resume()");
  }

  /** Formality methods */
  @Override public void pause() {
    Timber.d("pause()");
  }

  /** Formality methods */
  @Override public void destroy() {
    Timber.d("destroy()");
  }

  @Override public void onCellClick(int x, int y) {
    if (isEditing) {
      view.unfocusPosition(currentFocus[0], currentFocus[1]);

      final String inputText = view.getCurrentInputText();
      if (!TextUtils.equals(inputText,
          spreadsheetModel.getCellData(currentFocus[0], currentFocus[1]))) {
        spreadsheetModel.updateCell(currentFocus[0], currentFocus[1], view.getCurrentInputText());
        view.updatePositionText(currentFocus[0], currentFocus[1], view.getCurrentInputText());
      }
    }

    isEditing = true;
    currentFocus[0] = x;
    currentFocus[1] = y;

    view.focusPosition(x, y);
    view.setCurrentInput(spreadsheetModel.getCellData(x, y));
    view.focusInputField();
  }

  @Override public void addRow() {
    Timber.d("addColumn()");
    spreadsheetModel.setRows(spreadsheetModel.getRows() + 1);
    view.addRows(1);
  }

  @Override public void addColumn() {
    Timber.d("addColumn()");
    spreadsheetModel.setColumns(spreadsheetModel.getColumns() + 1);
    view.addColumns(1);
  }

  @Override public void undo() {
    Timber.d("undo()");
    final CellModel poppedModel = spreadsheetModel.undo();

    if (poppedModel == null) {
      view.showNoMoreUndoMessage();
      return;
    }

    final String previousData = poppedModel.undo();
    Timber.d("Update cell (%d, %d) = %s", poppedModel.getX(), poppedModel.getY(), previousData);
    view.updatePositionText(poppedModel.getX(), poppedModel.getY(), previousData);
  }

  @Override public void saveGrid() {
    Timber.d("saveGrid()");
    datastore.saveSpreadsheet(spreadsheetModel).subscribe(success -> {
      if (!success) {
        Timber.e("Could not save the spreadsheet.");
        view.showDataRetrievalError(true);
      } else {
        view.displaySaveSuccess();
      }
    }, throwable -> Timber.e(throwable, "saveGrid()"));
  }

  @Override public void clearGrid() {
    view.clearGrid();
  }

  @Override public void onFinishedEditing(String data) {
    if (!isEditing) return;

    Timber.d("onFinishedEditing()");
    spreadsheetModel.updateCell(currentFocus[0], currentFocus[1], data);
    view.unfocusPosition(currentFocus[0], currentFocus[1]);
    view.clearInputField();
    view.closeEdit();
    isEditing = false;
  }

  @Override public void loadGrid(long id) {
    if (isLoading) return;

    isLoading = true;
    view.showLoading(true);
    datastore.loadSpreadsheet(id).subscribe(spreadsheet -> {
      spreadsheetModel = spreadsheet;
      isLoading = false;
      view.showLoading(false);
      parseLoadedData(spreadsheet);
    }, throwable -> {
      Timber.w(throwable, "loadGrid(%d) Failure.", id);
      isLoading = false;
      view.showLoading(false);
      view.showDataRetrievalError(true);
    });
  }

  private void parseLoadedData(final SpreadsheetModel model) {
    spreadsheetModel = model;
    int rows = spreadsheetModel.getRows();
    int cols = spreadsheetModel.getColumns();

    // Clear any current data & adjust rows/cols
    view.clearGrid();
    view.addRows(8 - rows);
    view.addColumns(8 - cols);

    // Set loaded data to grid.
    for (int i = 0; i < rows; ++i) {
      for (int j = 0; j < cols; ++j) {
        final String data = spreadsheetModel.getCellData(i, j);
        Timber.d("loading (%d, %d) = %s", i, j, data);

        view.updatePositionText(i, j, data);
      }
    }
  }

  @Override public void onLoadSelected() {
    datastore.getSavedSpreadsheets().subscribe(spreadsheetModels -> {
      view.showAvailableSpreadsheets(spreadsheetModels);
    }, throwable -> Timber.wtf(throwable, "onLoadSelected()"));
  }
}
