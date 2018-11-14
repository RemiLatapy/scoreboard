package remi.scoreboard.data

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
        fun insert(user: User) {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.insert(user)
            realm.commitTransaction()
            realm.close()
        }

        fun insertOrUpdate(user: User) {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.insertOrUpdate(user)
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

        fun deleteAllPlayerOfUser(userId: String) {
            Realm.getDefaultInstance().run {
                beginTransaction()
                val user = where(User::class.java).equalTo("id", userId).findFirst()
                user?.playerList = PlayerList()
                commitTransaction()
                close()
            }
        }

        fun load(userId: String): LiveData<User> {
            val realm = Realm.getDefaultInstance()
            val user = realm.where(User::class.java).equalTo("id", userId).findFirst()
            val ret: LiveData<User> = if (user == null)
                AbsentLiveData.create()
            else
                LiveRealmObject(user)
            realm.close()
            return ret
        }

        fun addPlayerToUser(player: Player, userId: String) {
            Realm.getDefaultInstance().run {
                beginTransaction()
                val user = where(User::class.java).equalTo("id", userId).findFirst()
                user?.playerList?.add(player)
                commitTransaction()
                close()
            }
        }

        fun deletePlayerOfUser(playerId: String, userId: String) {
            Realm.getDefaultInstance().run {
                beginTransaction()
                val user = where(User::class.java).equalTo("id", userId).findFirst()
                if (user?.playerList?.removeIf { it.id == playerId } == true)
                    commitTransaction()
                else
                    cancelTransaction()
                close()
            }
        }
    }
}

class GameDao {
    companion object {
        fun loadAll(): LiveData<List<Game>> =
            Realm.getDefaultInstance().run { where(Game::class.java).findAll().asLiveData() }

        fun insert(game: Game) =
            Realm.getDefaultInstance().executeTransaction { it.insert(game) }

        fun insert(games: List<Game>) =
            Realm.getDefaultInstance().executeTransaction { it.insert(games) }

        fun insertOrUpdate(games: List<Game>) =
            Realm.getDefaultInstance().run {
                executeTransaction { it.insertOrUpdate(games) }
                close()
            }

        fun deleteAll() =
            Realm.getDefaultInstance().executeTransaction { it.delete(Game::class.java) }
    }
}

class MatchDao {
    companion object {
        fun loadAll(): LiveData<List<Match>> =
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

