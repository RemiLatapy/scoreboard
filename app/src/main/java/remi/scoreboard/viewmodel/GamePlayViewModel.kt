package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.dao.realm.RealmManager
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.repository.MatchRepository
import kotlin.coroutines.CoroutineContext

class GamePlayViewModel : ViewModel() {
    private val matchRepository = MatchRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    val currentPlayerScoreList: LiveData<List<PlayerScore>> = matchRepository.tempPlayerScoreList

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun reorderPlayerScoreList(playerScoreList: List<PlayerScore>) {
        // TODO no realm here
        val unmanagedList = playerScoreList.mapIndexed { idx, playerScore ->
            RealmManager.instance.copyFromRealm(playerScore).apply {
                order = idx
            }
        }
        scope.launch(Dispatchers.IO) { matchRepository.updateLocalPlayerScoreList(unmanagedList) }
    }

    fun addPoints(playerScore: PlayerScore, points: Int) {
        // Main thread needed to edit playerScore
        scope.launch(Dispatchers.Main) {
            matchRepository.setLocalScore(playerScore, playerScore.score + points)
        }
    }

//    class GamePlayViewModelFactory(val matchId: String) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return GamePlayViewModel(matchId) as T
//        }
//    }
}



