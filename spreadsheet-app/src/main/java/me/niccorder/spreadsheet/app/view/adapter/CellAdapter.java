package me.niccorder.spreadsheet.app.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import me.niccorder.spreadsheet.app.R;
import me.niccorder.spreadsheet.app.model.CellModel;

public class CellAdapter extends RecyclerView.Adapter<CellAdapter.CellViewHolder> {

  /** A flattened matrix of our cells. */
  private final SparseArray<CellModel> cells = new SparseArray<>();

  @Override public CellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new CellViewHolder(View.inflate(parent.getContext(), R.layout.view_cell, parent));
  }

  @Override public void onBindViewHolder(CellViewHolder holder, int position) {
    CellModel current = cells.get(position);

    holder.title.setText("position");
    holder.subtitle.setText(Integer.toString(position));
  }

  @Override public int getItemCount() {
    return cells.size();
  }

  /* View Holder for our cell. */
  static class CellViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.subtitle_text) TextView subtitle;

    public CellViewHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }
  }
}
