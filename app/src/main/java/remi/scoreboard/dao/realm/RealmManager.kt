package remi.scoreboard.dao.realm

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel
import io.realm.RealmResults

fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResults(this)

object RealmManager {
    val instance: Realm by lazy { Realm.getDefaultInstance() }

    fun init(context: Context) {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .name("scoreboard.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}