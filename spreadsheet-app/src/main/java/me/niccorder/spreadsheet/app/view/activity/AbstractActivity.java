package me.niccorder.spreadsheet.app.view.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import me.niccorder.spreadsheet.app.BaseApplication;
import me.niccorder.spreadsheet.app.di.compontents.ApplicationComponent;
import timber.log.Timber;

/**
 * A base activity of which all child activities should extend from. This provides functionalities
 * that can be managed in a one-to-many relationship (i.e. view injection) which need to be managed
 * correctly as to not induce a memory leak. This also provides functionalities for simplicity
 * dependency injection, and ease of use methods for extending activities.
 */
public abstract class AbstractActivity extends AppCompatActivity {

  /** @return the tag that will show in logcat. */
  protected abstract String provideLogTag();

  /** @return the layout id for this activity */
  @LayoutRes protected abstract int provideLayoutId();

  /** Allows children to be able to provide an id, and remove the inflating code bloat. */
  @CallSuper @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(provideLayoutId());

    // Logging for debugging.
    Timber.tag(provideLogTag()).i("onCreate()");

    // Injects view field members.
    ButterKnife.bind(this);
  }

  /** Get the Main Application component for dependency injection. */
  protected ApplicationComponent getApplicationComponent() {
    return ((BaseApplication) getApplication()).getApplicationComponent();
  }
}
