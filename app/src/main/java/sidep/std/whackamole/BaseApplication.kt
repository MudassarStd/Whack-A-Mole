package sidep.std.whackamole

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import sidep.std.whackamole.di.appModule
import sidep.std.whackamole.di.databaseModule

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            modules(databaseModule, appModule)
        }
    }
}