package me.niccorder.spreadsheet.app.pres;

import me.niccorder.spreadsheet.app.view.GridView;
import me.niccorder.spreadsheet.app.view.MenuView;

/**
 * Our contract for our presenter. We set it up as an interface since it allows us to easily inject
 * a mock object for testing, or different implementations of our contract. This could be useful in
 * the case of... hooking this up to a REST api?
 */
public interface CellGridPresenter<T extends GridView & MenuView> extends Presenter<T> {

  void onCellClick(int x, int y);

  void addRow();

  void addColumn();

  void undo();

  void saveGrid();

  void onLoadSelected();

  void loadGrid(long id);

  void clearGrid();

  void onFinishedEditing(String data);
}
