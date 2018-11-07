package remi.scoreboard.data

import android.util.Log
import androidx.lifecycle.LiveData
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import remi.scoreboard.AbsentLiveData

// TODO CLOSE ALL REALM INSTANCE!!

//fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)
fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResults(this)

class UserDao {
    companion object {
        fun loadAll(): LiveRealmResults<User> =
            Realm.getDefaultInstance().run { where(User::class.java).findAll().asLiveData() }

        fun insert(user: User) {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.insert(user)
            realm.commitTransaction()
            realm.close()
        }

        fun update(user: User) {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val dbUser = realm.where(User::class.java).equalTo("id", user.id).findFirst()
            // TODO setter in model
            dbUser?.username = user.username
            dbUser?.email = user.email
            dbUser?.avatar = user.avatar
            realm.commitTransaction()
            realm.close()
        }

        fun copyToRealm(user: User): LiveData<User> =
            Realm.getDefaultInstance().run {
                beginTransaction()
                val ret = LiveRealmObject(copyToRealm(user))
                commitTransaction()
                ret
            }

        fun deleteAll() =
            Realm.getDefaultInstance().executeTransaction { it.delete(User::class.java) }

        fun load(userId: String): LiveData<User> {
            val realm = Realm.getDefaultInstance()
            val user = realm.where(User::class.java).equalTo("id", userId).findFirst()
            realm.close()
            return if (user == null) {
                Log.d("LOGIN", "DAO Realm load $userId failed")
                AbsentLiveData.create()
            }
            else {
                Log.d("LOGIN", "DAO Realm load $userId succeed")
                LiveRealmObject(user)
            }
        }

        fun create(user: User): LiveRealmObject<User> {
            Realm.getDefaultInstance().let {
                it.beginTransaction()
                val realmUser: User = it.copyToRealm(user)
                it.commitTransaction()
                return LiveRealmObject(realmUser)
            }
        }
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

        fun addPoints(playerScore: PlayerScore, points: Int) {
            Realm.getDefaultInstance().executeTransaction { playerScore.score += points }
        }
    }
}

