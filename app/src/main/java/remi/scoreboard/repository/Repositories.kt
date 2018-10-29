package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import remi.scoreboard.data.*

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.loadAllUsers()

    @WorkerThread
    suspend fun insert(user: User) = userDao.insert(user)
}

class MatchRepository(private val matchDao: MatchDao) {

    val allMatches: LiveData<List<Match>> = matchDao.loadAllMatches()

    fun idMatch(mid: Int): LiveData<Match> = matchDao.loadMatchById(mid)

    @WorkerThread
    suspend fun insert(match: Match) = matchDao.insert(match)
}

class GameRepository(private val gameDao: GameDao) {

    val allGames: LiveData<List<Game>> = gameDao.loadAllGames()

    @WorkerThread
    suspend fun insert(game: Game) = gameDao.insert(game)

    @WorkerThread
    suspend fun insert(gameList: List<Game>) = gameDao.insert(gameList)

    fun gameById(gid: Int) = gameDao.loadGameById(gid)
}