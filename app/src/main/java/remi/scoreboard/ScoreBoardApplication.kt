package remi.scoreboard

import android.app.Application
import remi.scoreboard.dao.realm.RealmManager
import remi.scoreboard.remote.parse.ParseManager

// TODO https://support.flaticon.com/hc/en-us/articles/207248209-How-I-must-insert-the-attribution-
// TODO use AndroidViewModel only if context needed in viewmodel
open class ScoreBoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setupParse()
        RealmManager.init(this)
        ParseManager.init(this)
    }

    protected open fun setupParse() {
        ParseManager.init(this)
    }
}
