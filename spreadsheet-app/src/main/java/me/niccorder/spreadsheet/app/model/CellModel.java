package me.niccorder.spreadsheet.app.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * Basic model for a cell in the spreadsheet.
 */
public class CellModel {

  /** The history of the cell */
  private Deque<String> history;

  /** The horizontal position of the cell. */
  private int x;

  /** The vertical position of the cell. */
  private int y;

  public String getCurrentData() {
    if (history != null) {
      return history.peek();
    }
    return "";
  }

  public void updateData(String data) {
    if (history == null) {
      history = new ArrayDeque<>();
    }
    history.push(data);
  }

  public String undo() {
    history.pop();
    return history.peek();
  }

  public Deque<String> getHistory() {
    return history;
  }

  public void setHistory(Deque<String> history) {
    this.history = history;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
