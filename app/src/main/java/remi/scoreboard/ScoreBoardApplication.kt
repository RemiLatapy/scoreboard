package remi.scoreboard

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


open class ScoreBoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("scoreboard.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
