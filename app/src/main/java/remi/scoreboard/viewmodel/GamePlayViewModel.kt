package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
        scope.launch(Dispatchers.Main) {
            playerScoreRepository.setLocalOrder(
                playerScoreList,
                playerScoreList.mapIndexed { idx, _ -> idx })
        }
    }

    fun addPoints(playerScore: PlayerScore, points: Int) {
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



