package me.niccorder.spreadsheet.app.di.compontents;

import android.support.v7.app.AppCompatActivity;
import dagger.Component;
import me.niccorder.spreadsheet.app.di.module.ActivityModule;
import me.niccorder.spreadsheet.app.view.activity.MainActivity;
import me.niccorder.spreadsheet.util.di.PerActivity;

@PerActivity @Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
  void inject(MainActivity activity);

  AppCompatActivity activity();
}
