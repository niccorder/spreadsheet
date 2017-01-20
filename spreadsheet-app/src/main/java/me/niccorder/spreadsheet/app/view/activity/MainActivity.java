package me.niccorder.spreadsheet.app.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import butterknife.BindView;
import butterknife.OnClick;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import me.niccorder.spreadsheet.app.R;
import me.niccorder.spreadsheet.app.di.compontents.ActivityComponent;
import me.niccorder.spreadsheet.app.di.compontents.DaggerActivityComponent;
import me.niccorder.spreadsheet.app.di.module.ActivityModule;
import me.niccorder.spreadsheet.app.model.SpreadsheetModel;
import me.niccorder.spreadsheet.app.pres.CellGridPresenter;
import me.niccorder.spreadsheet.app.pres.impl.CellGridPresenterImpl;
import me.niccorder.spreadsheet.app.view.GridView;
import me.niccorder.spreadsheet.app.view.MenuView;
import me.niccorder.spreadsheet.app.view.ui.SpreadsheetView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Usually I try to not put everything in a main method, but due to constraints in time, as well as
 * the scope of the application being so small... it seemed like it was proper.
 *
 * I'm following a basic MVP layout for this application since having any other architectures
 * (clean architecture by uncle bob, for example) would contain too much overhead for the payoff.
 *
 * This implements our {@link GridView} and {@link MenuView} interfaces which denote that this
 * activity has a menu, along with the grid we will be displaying. Our
 */
public class MainActivity extends AbstractActivity implements GridView {

  /** Our presenter for this view. Presenters host all business logic, should be tested. */
  @Inject CellGridPresenter<GridView> mPresenter;

  /* Using Jake Wharton's butterknife. Just saves alot of code bloat for inflating views. */
  @BindView(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
  @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @BindView(R.id.navigation_view) NavigationView mNavigationView;
  @BindView(R.id.spreadsheet) SpreadsheetView mSpreadsheet;
  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.loading_container) View mLoadingContainer;
  @BindView(R.id.input) EditText mEditText;

  /** Used in unison with the nav drawer to provide a nice up animation + handles our drawer */
  private ActionBarDrawerToggle mActionbarToggler;

  /** Our activity component that holds the dependency scopes we need to inject our view */
  private ActivityComponent mActivityComponent;

  /** @inheritDoc */
  @Override protected String provideLogTag() {
    return "MainActivity";
  }

  /** @inheritDoc */
  @Override protected int provideLayoutId() {
    return R.layout.activity_main;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    injectDependencies();
    initNavigationDrawer();
    initSpreadsheet();

    // Attach the view to the presenter.
    mPresenter.setView(this);
  }

  /** Dagger2 to inject dependencies into activity */
  private void injectDependencies() {
    mActivityComponent = DaggerActivityComponent.builder()
        .activityModule(new ActivityModule(this))
        .applicationComponent(getApplicationComponent())
        .build();
    mActivityComponent.inject(this);
  }

  /** Initializes our navigation drawer + our up-navigation. */
  private void initNavigationDrawer() {
    mActionbarToggler = new DrawerToggler(this, mDrawerLayout, R.string.navdrawer_open_content_desc,
        R.string.navdrawer_closed_content_desc);
    mDrawerLayout.addDrawerListener(mActionbarToggler);
    mNavigationView.setNavigationItemSelectedListener(this::onOptionsItemSelected);

    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeButtonEnabled(true);
  }

  private void initSpreadsheet() {
    mSpreadsheet.setOnCellClickListener(this::onPositionClick);
  }

  @Override protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    // Required for navigation drawer toggler.
    mActionbarToggler.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    // Required for navigation drawer toggler.
    mActionbarToggler.onConfigurationChanged(newConfig);
  }

  /** Called on navigation drawer click */
  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (mActionbarToggler.onOptionsItemSelected(item)) {
      return true;
    }

    mDrawerLayout.closeDrawers();
    switch (item.getItemId()) {
      case R.id.item_save:
        onItemClicked(0);
        return true;
      case R.id.item_new:
        onItemClicked(1);
        return true;
      case R.id.item_load:
        onItemClicked(2);
        return true;
      case R.id.item_clear:
        onItemClicked(3);
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

    // Hides the keyboard.
    if (getCurrentFocus() != null) {
      InputMethodManager inputMethodManager =
          (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  /** Highlights the currently edited cell */
  @Override public void focusPosition(int x, int y) {
    mSpreadsheet.selectCell(x, y);
  }

  /** Returns the cell back to normal once it loses focus. */
  @Override public void unfocusPosition(int x, int y) {
    mSpreadsheet.deselectCell(x, y);
  }

  /** Focuses the input field. */
  @Override public void focusInputField() {
    mEditText.setFocusable(true);
    mEditText.requestFocus();

    // Shows the keyboard.
    if (getCurrentFocus() != null) {
      InputMethodManager inputMethodManager =
          (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.showSoftInput(getCurrentFocus(), 0);
    }
  }

  /** Clears any residual text in the input field */
  @Override public void clearInputField() {
    mEditText.clearFocus();
    mEditText.setFocusable(false);
  }

  /** Updates the cell's text at position x, y */
  @Override public void updatePositionText(int x, int y, String text) {
    mSpreadsheet.setCellText(x, y, text);
  }

  /** Shows a message notifying the user that there is no more undo's for the current cell */
  @Override public void showNoMoreUndoMessage() {
    Snackbar.make(mCoordinatorLayout, "Can't undo anymore changes!", Snackbar.LENGTH_INDEFINITE)
        .setAction(getString(android.R.string.ok), v -> {
        })
        .setActionTextColor(ResourcesCompat.getColor(getResources(), R.color.app_green, getTheme()))
        .show();
  }

  @Override public void onItemClicked(int position) {
    switch (position) {
      case 0:
        Timber.d("save()");
        mPresenter.saveGrid();
        break;
      case 1:
        Timber.d("new()");
        mPresenter.clearGrid();
        mPresenter.saveGrid();
        break;
      case 2:
        Timber.d("load()");
        mPresenter.onLoadSelected();
        break;
      case 3:
        Timber.d("clear()");
        mPresenter.clearGrid();
        break;
    }
  }

  @Override public void selectItem(int position) {
    // not needed.
  }

  @Override public void showLoading(boolean show) {
    mLoadingContainer.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  @Override public String getCurrentInputText() {
    return TextUtils.isEmpty(mEditText.getText()) ? null : mEditText.getText().toString();
  }

  @Override public void setCurrentInput(String data) {
    mEditText.setText(data);
  }

  @Override public void showAvailableSpreadsheets(List<SpreadsheetModel> available) {
    Timber.d("showAvailableSpreadsheets(%d)", available.size());

    Observable.from(available)
        .map(spreadsheetModel -> convertMilisecondsToDate(spreadsheetModel.getLastEditTimestamp()))
        .toList()
        .subscribe(list -> {
          showAvailableDialog(list, available);
        }, throwable -> Timber.wtf(throwable,
            "Had trouble showing the possible past spreadsheets."));
  }

  private String convertMilisecondsToDate(final long millis) {
    Timber.d("convertMilisecondsToDate(%d)", millis);
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(millis);
    return formatter.format(calendar.getTime());
  }

  private void showAvailableDialog(List<String> items, List<SpreadsheetModel> models) {
    Timber.d("showAvailableDialog()");
    new AlertDialog.Builder(this, R.style.Theme_Spreadsheet_Light).setAdapter(
        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items), (dialog, which) -> {
          mPresenter.loadGrid(models.get(which).getId());
          dialog.dismiss();
        }).setTitle(R.string.previous_saves_choose_one).show();
  }

  /** If we had trouble retrieving/parsing/loading the saved spreadsheet, show a message. */
  @Override public void showDataRetrievalError(boolean show) {
    Snackbar.make(mCoordinatorLayout, R.string.error_opening_spreadsheet, Snackbar.LENGTH_LONG)
        .setAction("Retry", v -> mPresenter.onLoadSelected())
        .setActionTextColor(ContextCompat.getColor(this, R.color.app_green))
        .show();
  }

  @Override public void clearGrid() {
    mSpreadsheet.clearGrid();
    mSpreadsheet.scrollTo(0, 0);
  }

  /** A requirement for using the navigationo drawer layout */
  private class DrawerToggler extends ActionBarDrawerToggle {

    public DrawerToggler(Activity activity, DrawerLayout drawerLayout,
        @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
      super(activity, drawerLayout, openDrawerContentDescRes, closeDrawerContentDescRes);

      // For some reason, the hamburger icon is black. This sets it to white.
      final DrawerArrowDrawable arrowDrawable = getDrawerArrowDrawable();
      arrowDrawable.setColor(
          ResourcesCompat.getColor(getResources(), R.color.app_white, getTheme()));
      setDrawerArrowDrawable(arrowDrawable);
    }

    @Override public void onDrawerOpened(View drawerView) {
      super.onDrawerOpened(drawerView);
      invalidateOptionsMenu();

      // If the user opens the drawer while editing, notify the presenter that we are no longer editing.
      mPresenter.onFinishedEditing(
          TextUtils.isEmpty(mEditText.getText()) ? null : mEditText.getText().toString());
    }

    @Override public void onDrawerClosed(View drawerView) {
      super.onDrawerClosed(drawerView);
      invalidateOptionsMenu();
    }
  }
}
