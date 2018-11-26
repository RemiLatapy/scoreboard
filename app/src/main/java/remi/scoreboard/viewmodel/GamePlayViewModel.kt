package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.realm.Realm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Match
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.repository.MatchRepository
import kotlin.coroutines.CoroutineContext

class GamePlayViewModel(val matchId: String) : ViewModel() {
    private val matchRepository = MatchRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    val currentMatch: LiveData<Match> = matchRepository.getMatchById(matchId)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun updatePlayerScoreList(playerScoreList: List<PlayerScore>) {
        Realm.getDefaultInstance().use {
            val unmanagedList = playerScoreList.map { playerScore -> it.copyFromRealm(playerScore) }
            unmanagedList.forEachIndexed { idx, it -> it.order = idx }
            scope.launch(Dispatchers.IO) { matchRepository.updatePlayerScoreList(matchId, unmanagedList) }
        }
    }

    fun addPoints(playerScore: PlayerScore, points: Int) {
        Realm.getDefaultInstance().use { realm ->
            val unmanagedPlayerScore = realm.copyFromRealm(playerScore)
            scope.launch(Dispatchers.IO) {
                matchRepository.setScore(
                    matchId,
                    unmanagedPlayerScore,
                    points + unmanagedPlayerScore.score
                )
            }
        }
    }

    class GamePlayViewModelFactory(val matchId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GamePlayViewModel(matchId) as T
        }
    }
}



