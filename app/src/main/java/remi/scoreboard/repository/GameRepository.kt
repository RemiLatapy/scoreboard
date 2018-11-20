package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.parse.ParseException
import io.realm.exceptions.RealmMigrationNeededException
import remi.scoreboard.dao.realm.GameDao
import remi.scoreboard.data.Game
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Status
import remi.scoreboard.remote.parse.ParseManager

class GameRepository {

    val allGames: LiveData<List<Game>> = GameDao.loadAll()

    val updateGameListState = MutableLiveData<MessageStatus>()

    @WorkerThread
    suspend fun updateGameList() {
        updateGameListState.postValue(MessageStatus(Status.LOADING))
        try {
            val gameList = ParseManager.getGameList()
            GameDao.insertOrUpdate(gameList)
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
