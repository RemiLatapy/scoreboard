package remi.scoreboard

import android.app.Application
import com.parse.Parse
import com.parse.ParseACL
import remi.scoreboard.dao.realm.RealmManager

// TODO https://support.flaticon.com/hc/en-us/articles/207248209-How-I-must-insert-the-attribution-
open class ScoreBoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupParse()
        RealmManager.init(this)
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
}
