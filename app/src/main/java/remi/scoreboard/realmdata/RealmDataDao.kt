package remi.scoreboard.realmdata

import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults

//fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)
fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResults(this)

class GameDao {
    companion object {
        fun loadAllGames(): LiveRealmResults<GameRealm> {
            return Realm.getDefaultInstance().run { where(GameRealm::class.java).findAll().asLiveData() }
        }

        fun insert(game: GameRealm) {
            return Realm.getDefaultInstance().run {
                beginTransaction()
                insert(game)
                commitTransaction()
            }
        }

        fun insert(games: List<GameRealm>) {
            return Realm.getDefaultInstance().run {
                beginTransaction()
                insert(games)
                commitTransaction()
            }
        }
    }
}