package me.niccorder.spreadsheet.app.view.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import javax.inject.Inject;
import me.niccorder.spreadsheet.app.R;
import me.niccorder.spreadsheet.app.di.compontents.ActivityComponent;
import me.niccorder.spreadsheet.app.di.compontents.DaggerActivityComponent;
import me.niccorder.spreadsheet.app.di.module.ActivityModule;
import me.niccorder.spreadsheet.app.pres.impl.CellGridPresenterImpl;
import me.niccorder.spreadsheet.app.view.GridView;
import me.niccorder.spreadsheet.app.view.MenuView;
import me.niccorder.spreadsheet.app.view.ui.SpreadsheetView;
import timber.log.Timber;

/** Currently waiting to finish the first microservice endpoint to implement */
public class MainActivity extends AbstractActivity implements GridView, MenuView {

  @Inject CellGridPresenterImpl mPresenter;

  @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @BindView(R.id.spreadsheet) SpreadsheetView mSpreadsheet;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.input) EditText mEditText;

  private ActionBarDrawerToggle mActionbarToggler;
  private ActivityComponent mActivityComponent;

  @Override protected String provideLogTag() {
    return "MainActivity";
  }

  @Override protected int provideLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectDependencies();
    initNavigationDrawer();
    initSpreadsheet();

    mPresenter.setView(this);
  }

  /** Initializes our navigation drawer + our up-navigation. */
  private void initNavigationDrawer() {
    mActionbarToggler = new DrawerToggler(this, mDrawerLayout, R.string.navdrawer_open_content_desc,
        R.string.navdrawer_closed_content_desc);
    mDrawerLayout.addDrawerListener(mActionbarToggler);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
  }

  private void initSpreadsheet() {
    mSpreadsheet.setOnCellClickListener(this::onPositionClick);
  }

  @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    mActionbarToggler.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    mActionbarToggler.onConfigurationChanged(newConfig);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (mActionbarToggler.onOptionsItemSelected(item)) {
      return true;
    }

    switch (item.getItemId()) {
      case R.id.item_save:
        return true;
      case R.id.item_load:
        return true;
      case R.id.item_clear:
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onResume() {
    super.onResume();

    mPresenter.resume();
  }

  @Override protected void onPause() {
    super.onPause();

    mPresenter.pause();
  }

  @Override protected void onDestroy() {
    super.onDestroy();

    mPresenter.destroy();
  }

  @OnClick(R.id.new_row_btn) void onNewRowClicked() {
    mPresenter.addRow();
  }

  @OnClick(R.id.new_column_btn) void onNewColumnClicked() {
    mPresenter.addColumn();
  }

  @OnClick(R.id.fab) void onUndoClicked() {
    mPresenter.undo();
  }

  @Override public void addRows(int num) {
    mSpreadsheet.addRow();
  }

  @Override public void addColumns(int num) {
    mSpreadsheet.addColumn();
  }

  @Override public void clearPosition(int x, int y) {
    mSpreadsheet.setCellText(x, y, null);
  }

  @Override public void onPositionClick(int x, int y) {
    mPresenter.onCellClick(x, y);
  }

  @Override public void closeEdit() {
    if (getCurrentFocus() != null) {
      InputMethodManager inputMethodManager =
          (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  @Override public void focusPosition(int x, int y) {
    mSpreadsheet.selectCell(x, y);
  }

  @Override public void unfocusPosition(int x, int y) {
    mSpreadsheet.deselectCell(x, y);
  }

  @Override public void focusInputField() {
    mEditText.requestFocus();
  }

  @Override public void clearInputField() {
    mEditText.clearFocus();
    mEditText.setFocusable(false);
  }

  @Override public void updatePositionText(int x, int y, String text) {
    mSpreadsheet.setCellText(x, y, text);
  }

  @Override public void showNoMoreUndoMessage() {
    Snackbar.make(mCoordinatorLayout, "Can't undo anymore changes!", Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(android.R.string.ok), null)
        .setActionTextColor(ResourcesCompat.getColor(getResources(), R.color.app_green, getTheme()))
        .show();
  }

  @Override public void onItemClicked(int position) {

  }

  @Override public void selectItem(int position) {

  }

  @Override public void showLoading(boolean show) {

  }

  @Override public void showDataRetrievalError(boolean show) {
    Snackbar.make(mCoordinatorLayout, "We had trouble spreadsheet.", Snackbar.LENGTH_LONG)
        .setAction("Retry", v -> Timber.i("Retry loading."))
        .setActionTextColor(ContextCompat.getColor(this, R.color.app_green))
        .show();
  }

  /** Dagger2 to inject dependencies into activity */
  private void injectDependencies() {
    mActivityComponent = DaggerActivityComponent.builder()
        .activityModule(new ActivityModule(this))
        .applicationComponent(getApplicationComponent())
        .build();
    mActivityComponent.inject(this);
  }

  /** A requirement for using the navigationo drawer layout */
  private class DrawerToggler extends ActionBarDrawerToggle {

    public DrawerToggler(Activity activity, DrawerLayout drawerLayout,
        @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
      super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);

      final DrawerArrowDrawable arrowDrawable = getDrawerArrowDrawable();
      arrowDrawable.setColor(
          ResourcesCompat.getColor(getResources(), R.color.app_white, getTheme()));
      setDrawerArrowDrawable(arrowDrawable);
    }

    @Override public void onDrawerOpened(View drawerView) {
      super.onDrawerOpened(drawerView);
      invalidateOptionsMenu();
      mPresenter.onFinishedEditing();
    }

    @Override public void onDrawerClosed(View drawerView) {
      super.onDrawerClosed(drawerView);
      invalidateOptionsMenu();
    }
  }
}
