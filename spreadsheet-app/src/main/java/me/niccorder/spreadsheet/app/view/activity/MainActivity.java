package me.niccorder.spreadsheet.app.view.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import javax.inject.Inject;
import me.niccorder.spreadsheet.app.R;
import me.niccorder.spreadsheet.app.di.compontents.ActivityComponent;
import me.niccorder.spreadsheet.app.di.compontents.DaggerActivityComponent;
import me.niccorder.spreadsheet.app.di.module.ActivityModule;
import me.niccorder.spreadsheet.app.pres.impl.CellGridPresenterImpl;
import me.niccorder.spreadsheet.app.view.GridView;
import me.niccorder.spreadsheet.app.view.MenuView;

/** Currently waiting to finish the first microservice endpoint to implement */
public class MainActivity extends AbstractActivity implements GridView, MenuView {

  @Inject CellGridPresenterImpl mPresenter;

  @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @BindView(R.id.container) NestedScrollView mRecyclerParent;
  @BindView(R.id.recycler) RecyclerView mRecyclerView;
  @BindView(R.id.new_row_btn) ImageView mAddRowButton;
  @BindView(R.id.new_column_btn) ImageView mAddColumnButton;
  @BindView(R.id.fab) FloatingActionButton mFab;
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

    mActionbarToggler = new DrawerToggler(this, mDrawerLayout, R.string.navdrawer_open_content_desc,
        R.string.navdrawer_closed_content_desc);
    mDrawerLayout.addDrawerListener(mActionbarToggler);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);

    mPresenter.setView(this);
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

  }

  private void injectDependencies() {
    mActivityComponent = DaggerActivityComponent.builder()
        .activityModule(new ActivityModule(this))
        .applicationComponent(getApplicationComponent())
        .build();
    mActivityComponent.inject(this);
  }

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
