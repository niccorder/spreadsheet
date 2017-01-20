package me.niccorder.spreadsheet.app.pres.impl;

import javax.inject.Inject;
import me.niccorder.spreadsheet.app.data.persistent.SpreadsheetDatastore;
import me.niccorder.spreadsheet.app.model.CellModel;
import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import me.niccorder.spreadsheet.app.pres.CellGridPresenter;
import me.niccorder.spreadsheet.app.view.GridView;
import timber.log.Timber;

/**
 * Implementation of {@link CellGridPresenter} that will actually preform the contract that the
 * {@link CellGridPresenter} provides.
 */
public class CellGridPresenterImpl implements CellGridPresenter<GridView> {

  /** Our view that this presenter is managing. */
  private GridView view;

  /** Model that represents the current spreadsheet */
  private SpreadsheetModel spreadsheetModel;

  /** Coordinates pointing to the currently focused cell */
  private int[] currentFocus;

  /** Flag that represents if we are editing a cell. */
  private boolean isEditing;

  /** Our datastore that holds any historic information */
  private SpreadsheetDatastore datastore;

  @Inject public CellGridPresenterImpl(SpreadsheetDatastore datastore) {
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
    isEditing = true;
    currentFocus[0] = x;
    currentFocus[1] = y;

    view.focusPosition(x, y);
    view.focusInputField();
  }

  @Override public void addRow() {
    view.addRows(1);
  }

  @Override public void addColumn() {
    view.addColumns(1);
  }

  @Override public void undo() {
    Timber.d("undo()");
    final CellModel poppedModel = spreadsheetModel.undo();

    if (poppedModel == null) {
      view.showNoMoreUndoMessage();
      return;
    }

    view.updatePositionText(poppedModel.getX(), poppedModel.getY(), poppedModel.undo());
  }

  @Override public void saveGrid() {
    Timber.d("saveGrid()");
  }

  @Override public void clearGrid() {
  }

  @Override public void onFinishedEditing() {
    view.clearInputField();
    view.closeEdit();
  }

  @Override public void loadGrid() {

  }
}
