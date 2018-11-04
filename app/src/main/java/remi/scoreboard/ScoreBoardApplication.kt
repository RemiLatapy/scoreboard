package remi.scoreboard

import android.app.Application
import com.parse.Parse
import io.realm.Realm
import io.realm.RealmConfiguration


open class ScoreBoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Parse
        Parse.enableLocalDatastore(applicationContext)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("gameScoring")
                .clientKey(null)
                .server("https://server.lunabee.studio:9008/parse/")
                .build()
        )

        // Initialize Realm
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("scoreboard.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
