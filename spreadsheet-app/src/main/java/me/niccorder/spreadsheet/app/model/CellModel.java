package me.niccorder.spreadsheet.app.model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

/**
 * Basic model for a cell in the spreadsheet.
 */
public class CellModel {

  /**
   * Our ID should always be <b>+/- x * y</b> [a.k.a. (x + 1) * (y + 1) * ( x >= y ? 1 : -1 ) since
   * we are 0-th based].
   *
   * This means the top right half to the center (inclusive) will be positive, and the bottom-left
   * half will always be negative.
   */
  private long id;

  /** The history of the cell */
  /* package private */ Deque<String> history;

  /** The horizontal position of the cell. */
  /* package private */ int x;

  /** The vertical position of the cell. */
  /* package private */ int y;

  public CellModel() {
    this.id = 0;
    this.x = -1;
    this.y = -1;
    this.history = new LinkedList<>();
  }

  public CellModel(int x, int y) {
    this.id = 0;
    this.x = x;
    this.y = y;
    this.history = new LinkedList<>();
  }

  public CellModel(int x, int y, String data) {
    this(x, y);
    this.history = new LinkedList<>(Collections.singletonList(data));
  }

  public CellModel(long id, int x, int y, String data) {
    this.id = 0;
    this.x = x;
    this.y = y;
    this.history = new LinkedList<>(Collections.singletonList(data));
  }

  /** We are 0-th based, we need to add 1 */
  public long getId() {
    if (id == 0 && x != -1 && y != -1) {
      id = (x + 1) * (y + 1) * (x >= y ? 1 : -1);
    }
    return id;
  }

  /** @return the most current data for the cell, or null if there is none. */
  public String getCurrentData() {
    if (history != null && history.size() > 0) {
      return history.peek();
    }
    return null;
  }

  /** update the cell with the specified data */
  public void updateData(String data) {
    if (history == null) {
      history = new ArrayDeque<>();
    }
    history.push(data);
  }

  public String undo() {
    if (history == null || history.size() == 0) {
      return null;
    }
    history.pop();
    return history.peek();
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
