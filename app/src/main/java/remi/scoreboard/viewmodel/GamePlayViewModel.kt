package remi.scoreboard.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import remi.scoreboard.data.Match
import remi.scoreboard.repository.MatchRepository
import kotlin.coroutines.CoroutineContext

class GamePlayViewModel(matchId: String) : ViewModel() {
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

    class GamePlayViewModelFactory(val matchId: String): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return GamePlayViewModel(matchId) as T
        }
    }
}



