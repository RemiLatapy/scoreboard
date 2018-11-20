package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import remi.scoreboard.dao.realm.MatchDao
import remi.scoreboard.data.Match
import remi.scoreboard.data.PlayerScore

class MatchRepository {

    val allMatches: LiveData<List<Match>> = MatchDao.loadAll()

    @WorkerThread
    suspend fun insert(match: Match) = MatchDao.insert(match)

    fun create(match: Match): LiveData<Match> = MatchDao.create(match)

    fun addPlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        MatchDao.addPlayer(currentMatch, playerScore)

    fun removePlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        MatchDao.removePlayer(currentMatch, playerScore)

    fun addPoints(playerScore: PlayerScore, points: Int) {
        MatchDao.addPoints(playerScore, points)
    }
}
