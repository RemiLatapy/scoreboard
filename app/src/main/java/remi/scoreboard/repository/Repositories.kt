package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import remi.scoreboard.data.*

class UserRepository {

    val allUsers: LiveData<List<User>> = UserDao.loadAll()

    @WorkerThread
    suspend fun insert(user: User) = UserDao.insert(user)

    @WorkerThread
    suspend fun deleteAll() = UserDao.deleteAll()
}

class MatchRepository {

    val allMatches: LiveData<List<Match>> = MatchDao.loadAll()

    @WorkerThread
    suspend fun insert(match: Match) = MatchDao.insert(match)
}

class GameRepository {

    val allGames: LiveData<List<Game>> = GameDao.loadAll()

    @WorkerThread
    suspend fun insert(game: Game) = GameDao.insert(game)

    @WorkerThread
    suspend fun insert(games: List<Game>) = GameDao.insert(games)

    @WorkerThread
    suspend fun deleteAll() = GameDao.deleteAll()
}