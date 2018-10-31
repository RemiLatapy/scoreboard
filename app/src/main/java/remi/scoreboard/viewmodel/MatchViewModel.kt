package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Match
import remi.scoreboard.repository.MatchRepository
import kotlin.coroutines.CoroutineContext

class MatchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MatchRepository()
    val allMatchs = repository.allMatches

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(match: Match) = scope.launch(Dispatchers.IO) { repository.insert(match) }
}