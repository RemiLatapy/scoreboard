package remi.scoreboard

import android.app.Application
import com.parse.Parse
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


open class ScoreBoardApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // OkHttp interceptor
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY

        // Initialize Parse
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("gameScoring")
                .clientKey(null)
                .server("https://server.lunabee.studio:9008/parse/")
                .enableLocalDataStore()
                .clientBuilder(OkHttpClient.Builder().addInterceptor(logger))
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
