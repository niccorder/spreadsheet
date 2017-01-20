package me.niccorder.spreadsheet.app.di.module;

import android.support.v7.app.AppCompatActivity;
import dagger.Module;
import dagger.Provides;
import me.niccorder.spreadsheet.app.data.SpreadsheetRepository;
import me.niccorder.spreadsheet.app.data.persistent.SpreadsheetDatastore;
import me.niccorder.spreadsheet.app.pres.CellGridPresenter;
import me.niccorder.spreadsheet.app.pres.impl.CellGridPresenterImpl;
import me.niccorder.spreadsheet.app.view.GridView;
import me.niccorder.spreadsheet.util.di.PerActivity;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module public class ActivityModule {

  private final AppCompatActivity activity;

  public ActivityModule(AppCompatActivity activity) {
    this.activity = activity;
  }

  /** Expose to dependent children in dependency graph */
  @PerActivity @Provides AppCompatActivity activity() {
    return this.activity;
  }

  @PerActivity @Provides CellGridPresenter<GridView> provideMainPresenterImpl(
      SpreadsheetRepository spreadsheetRepository) {
    return new CellGridPresenterImpl(spreadsheetRepository);
  }
}