package remi.scoreboard.jetpack.repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import remi.scoreboard.jetpack.model.*

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.loadAllUsers()

    @WorkerThread
    suspend fun insert(user: User) = userDao.insert(user)
}

class ScoreRepository(private val scoreDao: ScoreDao) {

//    val allScores: LiveData<List<Score>> = scoreDao.loadAllScore()

    @WorkerThread
    suspend fun insert(score: Score) = scoreDao.insert(score)
}

class MatchRepository(private val matchDao: MatchDao) {

    val allMatches: LiveData<List<Match>> = matchDao.loadAllMatches()

    @WorkerThread
    suspend fun insert(match: Match) = matchDao.insert(match)
}

class GameRepository(private val gameDao: GameDao) {

    val allGames: LiveData<List<Game>> = gameDao.loadAllGames()

    @WorkerThread
    suspend fun insert(game: Game) = gameDao.insert(game)
}