package me.niccorder.spreadsheet.app.pres;

import me.niccorder.spreadsheet.app.view.GridView;
import me.niccorder.spreadsheet.app.view.MenuView;

public interface CellGridPresenter<T extends GridView & MenuView> extends Presenter<T> {

  void onCellClick(int x, int y);

  void onAddRowClick();

  void onAddColumnClick();

  void onUndoClick();
}
