package me.niccorder.spreadsheet.app.view.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;
import java.util.ArrayList;
import timber.log.Timber;

public class SpreadsheetView extends TableLayout {

  private static final int MIN_ROWS = 8;
  private final ArrayList<SpreadsheetRow> rows;

  private OnCellClickListener onCellClickListener;

  public SpreadsheetView(Context context) {
    super(context);
    rows = new ArrayList<>();

    init();
  }

  public SpreadsheetView(Context context, AttributeSet attrs) {
    super(context, attrs);
    rows = new ArrayList<>();

    init();
  }

  public SpreadsheetView(Context context, ArrayList<SpreadsheetRow> rows) {
    super(context);
    this.rows = rows;
  }

  private void init() {
    Timber.d("init()");
    for (int i = 0; i < MIN_ROWS; ++i) {
      addRow();
    }

    requestLayout();
  }

  /** Delegates this up to the registered cell click listener, if available. */
  private void handleCellClick(int x, int y) {
    if (onCellClickListener != null) {
      onCellClickListener.onCellClick(x, y);
    }
  }

  /** Register a listener to receive cell click events. */
  public void setOnCellClickListener(final OnCellClickListener listener) {
    this.onCellClickListener = listener;
  }

  /** Selects the specified cell. */
  public void selectCell(int x, int y) {
    rows.get(x).setCellSelected(y, true);
  }

  /** Deselects the specified cell. */
  public void deselectCell(int x, int y) {
    rows.get(x).setCellSelected(y, false);
  }

  /** Sets the cells text. set null to clear. */
  public void setCellText(int x, int y, String text) {
    rows.get(x).setCellText(y, text);
  }

  /** Adds a row to the spreadsheet. */
  public void addRow() {
    Timber.d("addRow()");
    final SpreadsheetRow row = new SpreadsheetRow(getContext());
    row.setOnRowClickListener(this::handleCellClick);
    rows.add(row);
    addView(row);
  }

  /** Adds a column to the spreadsheet. */
  public void addColumn() {
    Timber.d("addColumn()");
    for (SpreadsheetRow row : rows) {
      row.addCell();
    }
  }

  public interface OnCellClickListener {
    void onCellClick(int x, int y);
  }
}
