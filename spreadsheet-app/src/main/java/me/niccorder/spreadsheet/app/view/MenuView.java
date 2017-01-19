package me.niccorder.spreadsheet.app.view;

public interface MenuView extends BaseView {

  void onItemClicked(int position);

  void selectItem(int position);
}
