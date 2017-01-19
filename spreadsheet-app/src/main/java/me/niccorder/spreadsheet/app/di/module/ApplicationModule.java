package me.niccorder.spreadsheet.app.di.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class ApplicationModule {

  private Application mApplication;

  public ApplicationModule(Application application) {
    mApplication = application;
  }

  @Provides @Singleton Application provideApplication() {
    return mApplication;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return mApplication;
  }
}
