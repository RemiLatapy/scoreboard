package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import remi.scoreboard.dao.realm.MatchDao
import remi.scoreboard.data.Match
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.data.Status
import remi.scoreboard.remote.parse.ParseManager

class MatchRepository {

    val createMatchState = MutableLiveData<MessageStatus>()
    val createLocalMatchState = MutableLiveData<MessageStatus>()

    val allMatches: LiveData<List<Match>> = MatchDao.loadAll()

    @WorkerThread
    suspend fun insert(match: Match) = MatchDao.insert(match)

    @WorkerThread
    suspend fun create(match: Match) {
        createMatchState.postValue(MessageStatus(Status.LOADING))
        try {
            val newMatch = ParseManager.createMatch(match)
            MatchDao.insertOrUpdate(newMatch)
            // TODO improve MessageStatus to pass data (or find another way)
            createMatchState.postValue(MessageStatus(Status.SUCCESS, newMatch.id))
        } catch (e: Exception) {
            createMatchState.postValue(MessageStatus(Status.ERROR, e.message ?: "Match creation failed"))
        }
    }

    @WorkerThread
    suspend fun createLocal(match: Match) {
        createLocalMatchState.postValue(MessageStatus(Status.LOADING))
        try {
            // TODO find better way.. pb = how & where to set temp/local id to playerScore
            match.scorePlayerList.forEach { playerScore ->
                playerScore.player?.let { player ->
                    playerScore.id = "temp_" + player.id
                }
            }
            MatchDao.insertOrUpdate(match)
            createLocalMatchState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            createLocalMatchState.postValue(MessageStatus(Status.ERROR, e.message ?: "Match creation failed"))
        }
    }

    fun addPlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        MatchDao.addPlayer(currentMatch, playerScore)

    fun removePlayer(currentMatch: LiveData<Match>, playerScore: PlayerScore) =
        MatchDao.removePlayer(currentMatch, playerScore)

    @WorkerThread
    suspend fun setScore(matchId: String, playerScore: PlayerScore, score: Int) {
        try {
            val newMatch = ParseManager.updateMatch(matchId, playerScore, score)
            MatchDao.insertOrUpdate(newMatch)
        } catch (e: Exception) {
            throw e // TODO handling error
        }
    }

    fun getMatchById(matchId: String): LiveData<Match> = MatchDao.loadGameWithId(matchId)

    @WorkerThread
    suspend fun updatePlayerScoreList(matchId: String, playerScoreList: List<PlayerScore>) {
        try {
            val newMatch = ParseManager.updateMatch(matchId, playerScoreList)
            MatchDao.insertOrUpdate(newMatch)
        } catch (e: Exception) {
            throw e // TODO handling error
        }
    }
}
