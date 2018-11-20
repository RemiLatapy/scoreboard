package remi.scoreboard

import com.facebook.stetho.Stetho
import remi.scoreboard.remote.parse.ParseManager

class DebugScoreBoardApplication : ScoreBoardApplication() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this);
    }

    override fun setupParse() {
        ParseManager.initWithInterceptor(this)
    }
}