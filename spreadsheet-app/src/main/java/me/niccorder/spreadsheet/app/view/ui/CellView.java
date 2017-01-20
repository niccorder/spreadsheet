package me.niccorder.spreadsheet.app.view.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import me.niccorder.spreadsheet.app.R;

/* package private */ class CellView extends CardView {

  private int x;
  private int y;
  private String data;

  private Drawable defaultBackground;
  private View itemLayout;
  private TextView textView;

  /* package private */ CellView(Context context, int x, int y) {
    super(context);
    this.x = x;
    this.y = y;
    this.data = null;

    initialize(null);
  }

  /* package private */ CellView(Context context, int x, int y, String data) {
    super(context);
    this.x = x;
    this.y = y;
    this.data = null;

    initialize(data);
  }

  private void initialize(String data) {
    setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
    setMaxCardElevation(getResources().getDimensionPixelSize(R.dimen.fab_elevation));
    setRadius(getResources().getDimensionPixelSize(R.dimen.card_corner_radius));

    itemLayout = inflate(getContext(), R.layout.view_cell, this);
    defaultBackground = itemLayout.getBackground();
    textView = (TextView) itemLayout.findViewById(R.id.text);
    requestLayout();
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);

    MarginLayoutParams margins = (MarginLayoutParams) getLayoutParams();
    int margin = getResources().getDimensionPixelSize(R.dimen.explore_grid_padding);
    margins.leftMargin = margin;
    margins.rightMargin = margin;

    setLayoutParams(margins);
  }

  @Override public void setSelected(boolean selected) {
    if (selected) {
      itemLayout.setBackgroundColor(
          ResourcesCompat.getColor(getResources(), R.color.app_selected_cell_tint,
              getContext().getTheme()));
    } else {
      itemLayout.setBackground(defaultBackground);
    }
  }

  /* package private */ void clearCell() {
    setSelected(false);
    this.data = null;
  }

  /* package private */ int getCellPositionX() {
    return x;
  }

  /* package private */ int getCellPositionY() {
    return y;
  }

  /* package private */ void setData(String data) {
    this.data = data;

    if (textView != null) {
      textView.setText(this.data);
    }
  }
}