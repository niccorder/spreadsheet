package me.niccorder.spreadsheet.app.di.compontents;

import dagger.Component;
import javax.inject.Singleton;
import me.niccorder.spreadsheet.app.di.module.DataModule;

@Singleton @Component(modules = DataModule.class) public interface DataComponent {
}
