package me.niccorder.spreadsheet.app.model;

import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Basic model that represents a spreadsheet. This is also our representation when we store this
 * info to persist it against application opens.
 */
public class SpreadsheetModel {

  /** Used for persisting data */
  private long id;

  /** The number of rows in this spreadsheet */
  private int rows;

  /** The number of columns in this spreadsheet */
  private int columns;

  /** Used for displaying 'most current' */
  private long lastEditTimestamp;

  /**
   * Since the data can be sparse we use a {@link HashMap<Long, CellModel>} and hash against {@link
   * CellModel#id} since it is unique.
   */
  private HashMap<Long, CellModel> data;

  /** Holds our global edit history. */
  private Deque<Long> history;

  public SpreadsheetModel() {
    this.id = -1;
    this.lastEditTimestamp = -1;
    this.data = new HashMap<>();
    this.history = new LinkedList<>();
  }

  public SpreadsheetModel(long id, long lastEditTimestamp) {
    this.id = id;
    this.lastEditTimestamp = lastEditTimestamp;
  }

  public SpreadsheetModel(long id, int rows, int columns, long lastEditTimestamp) {
    this.id = id;
    this.rows = rows;
    this.columns = columns;
    this.lastEditTimestamp = lastEditTimestamp;
    this.data = new HashMap<>();
    this.history = new LinkedList<>();
  }

  public SpreadsheetModel(HashMap<Long, CellModel> data, Deque<Long> history) {
    this.data = data;
    this.history = history;
  }

  public void updateCell(int x, int y, String cellData) {
    long key = (x + 1) * (y + 1);
    final CellModel model = !data.containsKey(key) ? new CellModel() : data.get(key);

    setLastEditTimestamp();
    data.put(key, new CellModel(x, y, cellData));
  }

  public String getCellData(int x, int y) {
    long key = (x + 1) * (y + 1);
    final CellModel model = !data.containsKey(key) ? new CellModel() : data.get(key);
    return model.getCurrentData();
  }

  public CellModel undo() {
    if (history.size() <= 0) {
      return null;
    }

    setLastEditTimestamp();
    final CellModel model = data.get(history.pop());
    model.undo();
    return model;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getLastEditTimestamp() {
    return lastEditTimestamp;
  }

  public void setLastEditTimestamp() {
    lastEditTimestamp = Calendar.getInstance().getTimeInMillis();
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    if (this.rows != rows && this.rows != 0) {
      setLastEditTimestamp();
    }
    this.rows = rows;
  }

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    if (this.columns != columns && this.columns != 0) {
      setLastEditTimestamp();
    }
    this.columns = columns;
  }
}
