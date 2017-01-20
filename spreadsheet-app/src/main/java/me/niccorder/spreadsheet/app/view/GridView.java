package me.niccorder.spreadsheet.app.view;

/**
 * Our contract for our GridView. We set it up as an interface since it allows us to easily inject
 * a mock object for testing, or different implementations of our contract. This would be good for
 * displayinig the same thing, but in like a fragment or something. yeah.
 */
public interface GridView extends MenuView {

  void addRows(int num);

  void addColumns(int num);

  void clearPosition(int x, int y);

  void onPositionClick(int x, int y);

  void closeEdit();

  void focusPosition(int x, int y);

  void unfocusPosition(int x, int y);

  void focusInputField();

  void clearInputField();

  void updatePositionText(int x, int y, String text);

  void showNoMoreUndoMessage();
}
