package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.dao.realm.RealmManager
import remi.scoreboard.data.PlayerScore
import remi.scoreboard.repository.PlayerScoreRepository
import kotlin.coroutines.CoroutineContext

class GamePlayViewModel : ViewModel() {
    private val playerScoreRepository = PlayerScoreRepository()

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    val currentPlayerScoreList: LiveData<List<PlayerScore>> = playerScoreRepository.tempPlayerScoreList

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
        scope.launch(Dispatchers.IO) { playerScoreRepository.updateLocalPlayerScoreList(unmanagedList) }
    }

    fun addPoints(playerScore: PlayerScore, points: Int) {
        // Main thread needed to edit playerScore
        scope.launch(Dispatchers.Main) {
            playerScoreRepository.setLocalScore(playerScore, playerScore.score + points)
        }
    }

//    class GamePlayViewModelFactory(val matchId: String) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            return GamePlayViewModel(matchId) as T
//        }
//    }
}



