package me.niccorder.spreadsheet.app.model;

/**
 * Basic model that represents a spreadsheet.
 */
public class SpreadsheetModel {

  /** Used for persisting data */
  private long id;

  /** Used for displaying 'most current' */
  private long lastEdit;

  /** The data itself. */
  private CellModel[][] spreadsheet;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getLastEdit() {
    return lastEdit;
  }

  public void setLastEdit(long lastEdit) {
    this.lastEdit = lastEdit;
  }

  public CellModel[][] getSpreadsheet() {
    return spreadsheet;
  }

  public void setSpreadsheet(CellModel[][] spreadsheet) {
    this.spreadsheet = spreadsheet;
  }
}
