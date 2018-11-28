package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import remi.scoreboard.dao.realm.PlayerScoreDao
import remi.scoreboard.data.PlayerScore

class PlayerScoreRepository {

    val tempPlayerScoreList: LiveData<List<PlayerScore>> = PlayerScoreDao.loadPlayerScoreWithIdStartingBy("temp_")

    @WorkerThread
    suspend fun updateLocalPlayerScoreList(playerScoreList: List<PlayerScore>) {
        try {
            PlayerScoreDao.updatePlayerScore(playerScoreList)
        } catch (e: Exception) {
            throw e // TODO handling error
        }
    }

    fun setLocalScore(playerScore: PlayerScore, score: Int) {
        try {
            PlayerScoreDao.setScore(playerScore, score)
        } catch (e: Exception) {
            throw e // TODO handling error
        }
    }
}
