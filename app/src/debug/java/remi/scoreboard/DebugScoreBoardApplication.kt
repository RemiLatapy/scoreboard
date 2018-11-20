package remi.scoreboard

import com.facebook.stetho.Stetho
import com.parse.Parse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class DebugScoreBoardApplication : ScoreBoardApplication() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this);
    }

    override fun setupParse() {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.APP_ID))
                .clientKey(getString(R.string.CLIENT_KEY))
                .server(getString(R.string.PARSE_SERVER_URL))
                .enableLocalDataStore()
                .clientBuilder(OkHttpClient.Builder().addInterceptor(logger))
                .build()
        )
    }
}