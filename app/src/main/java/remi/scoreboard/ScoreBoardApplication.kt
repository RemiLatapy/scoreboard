package remi.scoreboard

import android.app.Application
import com.parse.Parse
import com.parse.ParseACL
import io.realm.Realm
import io.realm.RealmConfiguration


open class ScoreBoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupParse()
        setupRealm()
    }

    protected open fun setupParse() {
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.APP_ID))
                .clientKey(getString(R.string.CLIENT_KEY))
                .server(getString(R.string.PARSE_SERVER_URL))
                .enableLocalDataStore()
                .build()
        )
        ParseACL.setDefaultACL(ParseACL(), true)

    }

    protected open fun setupRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("scoreboard.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}
