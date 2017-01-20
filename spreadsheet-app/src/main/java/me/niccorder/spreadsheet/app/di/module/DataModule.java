package me.niccorder.spreadsheet.app.di.module;

import android.app.Application;
import android.database.sqlite.SQLiteOpenHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.niccorder.spreadsheet.app.BuildConfig;
import me.niccorder.spreadsheet.app.data.SpreadsheetRepository;
import me.niccorder.spreadsheet.app.data.persistent.SpreadsheetDatastore;
import me.niccorder.spreadsheet.app.data.persistent.db.DbOpenHelper;
import rx.schedulers.Schedulers;
import timber.log.Timber;

@Module public class DataModule {

  public DataModule() {
  }

  @Provides @Singleton SpreadsheetRepository provideSpreadsheetRepository(
      BriteDatabase briteDatabase) {
    return new SpreadsheetDatastore(briteDatabase);
  }

  @Provides @Singleton SQLiteOpenHelper provideOpenHelper(Application application) {
    return new DbOpenHelper(application);
  }

  @Provides @Singleton SqlBrite provideSqlBrite() {
    return new SqlBrite.Builder().logger(message -> Timber.tag("Database").v(message)).build();
  }

  @Provides @Singleton BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
    BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper, Schedulers.io());
    db.setLoggingEnabled(BuildConfig.DEBUG);

    return db;
  }
}
