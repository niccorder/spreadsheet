package me.niccorder.spreadsheet.app.di.compontents;

import android.app.Application;
import android.content.Context;
import dagger.Component;
import javax.inject.Singleton;
import me.niccorder.spreadsheet.app.data.SpreadsheetRepository;
import me.niccorder.spreadsheet.app.di.module.ApplicationModule;
import me.niccorder.spreadsheet.app.di.module.DataModule;

@Singleton @Component(modules = { ApplicationModule.class, DataModule.class })
public interface ApplicationComponent {

  Context context();

  Application applicationContext();

  SpreadsheetRepository spreadsheetRepo();
}
