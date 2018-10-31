package remi.scoreboard.data

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
            Realm.getDefaultInstance().executeTransaction {
                val currentIdNum = it.where(User::class.java).max("id")
                user.id = if (currentIdNum != null) currentIdNum.toLong() + 1 else 1
                it.insert(user)
            }

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
            Realm.getDefaultInstance().executeTransaction {
                val currentIdNum = it.where(Match::class.java).max("id")
                match.id = if (currentIdNum != null) currentIdNum.toLong() + 1 else 1
                it.insert(match)
            }

        fun deleteAll() =
            Realm.getDefaultInstance().executeTransaction { it.delete(Match::class.java) }
    }
}

