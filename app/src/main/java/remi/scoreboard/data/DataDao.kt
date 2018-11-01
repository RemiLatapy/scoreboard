package remi.scoreboard.data

import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults


//fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)
fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResults(this)

class UserDao {
    companion object {
        fun loadAll(): LiveRealmResults<User> =
            Realm.getDefaultInstance().run { where(User::class.java).findAll().asLiveData() }

        fun insert(user: User) =
            Realm.getDefaultInstance().executeTransaction { it.insert(user) }

        fun update(user: User) =
            Realm.getDefaultInstance().executeTransaction { it.insertOrUpdate(user) }

        fun deleteAll() =
            Realm.getDefaultInstance().executeTransaction { it.delete(User::class.java) }
    }
}

class GameDao {
    companion object {
        fun loadAll(): LiveRealmResults<Game> =
            Realm.getDefaultInstance().run { where(Game::class.java).findAll().asLiveData() }

        fun insert(game: Game) =
            Realm.getDefaultInstance().executeTransaction { it.insertOrUpdate(game) }

        fun insert(games: List<Game>) =
            Realm.getDefaultInstance().executeTransaction { it.insertOrUpdate(games) }

        fun deleteAll() =
            Realm.getDefaultInstance().executeTransaction { it.delete(Game::class.java) }
    }
}

class MatchDao {
    companion object {
        fun loadAll(): LiveRealmResults<Match> =
            Realm.getDefaultInstance().run { where(Match::class.java).findAll().asLiveData() }

        fun insert(match: Match) =
            Realm.getDefaultInstance().executeTransaction { it.insert(match) }

        fun create(match: Match): LiveRealmObject<Match> {
            Realm.getDefaultInstance().let {
                it.beginTransaction()
                val realMatch: Match = it.copyToRealm(match)
                it.commitTransaction()
                return LiveRealmObject(realMatch)
            }
        }

        fun deleteAll() =
            Realm.getDefaultInstance().executeTransaction { it.delete(Match::class.java) }

        fun addPlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) {
            Realm.getDefaultInstance().executeTransaction { currentMatch.value?.scorePlayerList?.add(playerScore) }
        }

        fun removePlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) {
            Realm.getDefaultInstance().executeTransaction { currentMatch.value?.scorePlayerList?.remove(playerScore) }
        }
    }
}

