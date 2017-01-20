package me.niccorder.spreadsheet.app.view.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.TableRow;
import java.util.ArrayList;
import me.niccorder.spreadsheet.app.R;
import timber.log.Timber;

/**
 * A table row for our custom table layout.
 */
/* package private */ class SpreadsheetRow extends TableRow {

  private static int ROW_COUNT = 0;
  private static final int MIN_COLUMNS = 8;

  private final ArrayList<CellView> cells;
  private OnRowClickListener internalListener;
  private int rowNumber;
  private int verticalMargin;

  private static int getRowNumber() {
    return ++ROW_COUNT - 1;
  }

  public SpreadsheetRow(Context context) {
    super(context);
    this.rowNumber = getRowNumber();
    cells = new ArrayList<>();

    setLayoutParams(new LayoutParams(rowNumber));
    initialize(new SparseArray<>());
  }

  public SpreadsheetRow(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.rowNumber = getRowNumber();
    cells = new ArrayList<>();

    initialize(new SparseArray<>());
  }

  public SpreadsheetRow(Context context, final SparseArray<String> data) {
    super(context);
    this.rowNumber = getRowNumber();
    cells = new ArrayList<>();

    initialize(data == null ? new SparseArray<>() : data);
  }

  private void initialize(final SparseArray<String> data) {
    verticalMargin = getResources().getDimensionPixelSize(R.dimen.explore_grid_padding);
    int numColumns = Math.max(MIN_COLUMNS, data.size());
    Timber.d("initialize(%d)", numColumns);

    for (int i = 0; i < numColumns; ++i) {
      addCell(data.get(i, null));
    }
  }

  /* package private */ void addCell(String data) {
    final CellView cell = new CellView(getContext(), rowNumber, cells.size(), data);
    cell.setOnClickListener(v -> handleCellClick((CellView) v));
    LayoutParams params = new LayoutParams(cells.size());
    params.topMargin = verticalMargin;
    params.bottomMargin = verticalMargin;
    addView(cell, params);

    cells.add(cell);
  }

  /* package private */ void addCell() {
    addCell(null);
  }

  /* package private */ void setOnRowClickListener(final OnRowClickListener listener) {
    this.internalListener = listener;
  }

  /* package private */ void setCellSelected(int y, boolean isSelected) {
    cells.get(y).setSelected(isSelected);
  }

  /* package private */ void setCellText(int y, String text) {
    cells.get(y).setData(text);
  }

  private void handleCellClick(CellView cell) {
    internalListener.onRowClickListener(cell.getCellPositionX(), cell.getCellPositionY());
  }

  interface OnRowClickListener {
    void onRowClickListener(int x, int y);
  }
}
