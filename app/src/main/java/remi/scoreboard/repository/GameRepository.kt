package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import io.realm.exceptions.RealmMigrationNeededException
import remi.scoreboard.dao.realm.GameDao
import remi.scoreboard.data.Game
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Status

class GameRepository {

    val allGames: LiveData<List<Game>> = GameDao.loadAll()

    val updateGameListState = MutableLiveData<MessageStatus>()

    @WorkerThread
    suspend fun updateGameList() {
        updateGameListState.postValue(MessageStatus(Status.LOADING))
        val query = ParseQuery.getQuery<ParseObject>("games")
        try {
            val parseGameList = query.find()
            GameDao.insertOrUpdate(parseGameList.map { Game(it) })
            updateGameListState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException, is RealmMigrationNeededException -> {
                    updateGameListState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun insert(game: Game) = GameDao.insert(game)

    @WorkerThread
    suspend fun insert(games: List<Game>) = GameDao.insert(games)

    @WorkerThread
    suspend fun deleteAll() = GameDao.deleteAll()

}
