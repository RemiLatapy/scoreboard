package remi.scoreboard

import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider

class DebugScoreBoardApplication : ScoreBoardApplication() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build()
        )
    }
}