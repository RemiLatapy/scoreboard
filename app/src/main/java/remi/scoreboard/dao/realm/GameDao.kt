package remi.scoreboard.dao.realm

import androidx.lifecycle.LiveData
import io.realm.Realm
import remi.scoreboard.util.AbsentLiveData
import remi.scoreboard.data.Game

object GameDao {
    // READ
    fun loadAll(): LiveData<List<Game>> =
        RealmManager.instance.run { where(Game::class.java).findAll().asLiveData() }

    fun loadGameById(id: String): LiveData<Game> = RealmManager.instance.run {
        val game = where(Game::class.java).equalTo("id", id).findFirst()
        return if (game == null)
            AbsentLiveData.create() // TODO is it better to throw e?
        else
            LiveRealmObject(game)
    }

    // WRITE
    fun insert(game: Game) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insert(game) } }

    fun insert(games: List<Game>) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insert(games) } }

    fun insertOrUpdate(games: List<Game>) =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.insertOrUpdate(games) } }

    fun deleteAll() =
        Realm.getDefaultInstance().use { it.executeTransaction { realm -> realm.delete(Game::class.java) } }
}
