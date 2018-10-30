package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import remi.scoreboard.realmdata.GameDao
import remi.scoreboard.realmdata.GameRealm

class GameRealmRepository() {

    val allGames: LiveData<List<GameRealm>> = GameDao.loadAllGames()

    @WorkerThread
    suspend fun insert(game: GameRealm) = GameDao.insert(game)

    @WorkerThread
    suspend fun insert(games: List<GameRealm>) = GameDao.insert(games)
}