package me.niccorder.spreadsheet.app.view.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
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
    initTable();

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

  /** Sets up out recyclerview (spreadsheet) */
  private void initTable() {

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
    mPresenter.onAddRowClick();
  }

  @OnClick(R.id.new_column_btn) void onNewColumnClicked() {
    mPresenter.onAddColumnClick();
  }

  @OnClick(R.id.fab) void onUndoClicked() {
    mPresenter.onUndoClick();
  }

  @Override public void addRows(int num) {

  }

  @Override public void addColumns(int num) {

  }

  @Override public void clearPosition(int x, int y) {

  }

  @Override public void onPositionClick(int x, int y) {

  }

  @Override public void closeEdit() {

  }

  @Override public void focusPosition(int x, int y) {

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
        .setActionTextColor(ContextCompat.getColor(this, R.color.app_green));
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
  static class DrawerToggler extends ActionBarDrawerToggle {

    private Activity activity;

    public DrawerToggler(Activity activity, DrawerLayout drawerLayout,
        @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
      super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);
      this.activity = activity;
    }

    @Override public void onDrawerOpened(View drawerView) {
      super.onDrawerOpened(drawerView);
      activity.invalidateOptionsMenu();
    }

    @Override public void onDrawerClosed(View drawerView) {
      super.onDrawerClosed(drawerView);
      activity.invalidateOptionsMenu();
    }
  }
}
