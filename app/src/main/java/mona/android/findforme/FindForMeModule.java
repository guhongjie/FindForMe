package mona.android.findforme;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mona.android.findforme.data.DataModule;
import mona.android.findforme.qualifiers.ApplicationContext;
import mona.android.findforme.services.PhotoUploadTaskService;
import mona.android.findforme.state.StateProvider;
import mona.android.findforme.state.persistence.PersistenceProvider;
import mona.android.findforme.tasks.PhotoUploadTask;
import mona.android.findforme.tasks.PhotoUploadTaskQueue;
import mona.android.findforme.ui.UiModule;
import mona.android.findforme.ui.grid.GridContainer;

/**
 * Created by cheikhna on 21/08/2014.
 */
@Module(
    includes = {
        MainProvider.class,
        DataModule.class,
        UiModule.class,
        StateProvider.class,
        PersistenceProvider.class
    },
    injects = {
            FindForMeApplication.class,
            FindForMeActivity.class,
            LoginActivity.class,
            PhotoUploadTaskQueue.class,
            PhotoUploadTaskService.class,
            PhotoUploadTask.class,
            GridContainer.class
    }
)
public class FindForMeModule {
    private final FindForMeApplication app;

    public FindForMeModule(FindForMeApplication app){
        this.app = app;
    }

    @Provides @Singleton
    Application provideApplication() { return app; }

    @Provides @ApplicationContext
    public Context provideApplicationContext(){
        return app.getApplicationContext();
    }

}
