package me.niccorder.spreadsheet.app.view;

/**
 * An initial pass for the view contract.
 */
public interface GridView extends MenuView {

  void addRows(int num);

  void addColumns(int num);

  void clearPosition(int x, int y);

  void onPositionClick(int x, int y);

  void closeEdit();

  void focusPosition(int x, int y);
}
