package me.niccorder.spreadsheet.app.pres.impl;

import me.niccorder.spreadsheet.app.pres.CellGridPresenter;
import me.niccorder.spreadsheet.app.view.GridView;

/**
 * Implementation of {@link CellGridPresenter} that will actually preform the contract that the
 * {@link CellGridPresenter} provides.
 */
public class CellGridPresenterImpl implements CellGridPresenter<GridView> {

  /**
   * Our view that this presenter is managing.
   */
  private GridView view;

  /** The view is responsible for attaching/detaching itself. */
  @Override public void setView(GridView view) {
    this.view = view;
  }

  @Override public void create() {
  }

  @Override public void resume() {
  }

  @Override public void pause() {
  }

  @Override public void destroy() {
  }

  @Override public void onCellClick(int x, int y) {
  }

  @Override public void onAddRowClick() {
  }

  @Override public void onAddColumnClick() {
  }

  @Override public void onUndoClick() {
  }
}
