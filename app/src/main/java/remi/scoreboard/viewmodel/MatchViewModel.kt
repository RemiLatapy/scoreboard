package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Match
import remi.scoreboard.data.ScoreboardDatabase
import remi.scoreboard.repository.MatchRepository
import kotlin.coroutines.CoroutineContext

class MatchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MatchRepository
    val allMatchs: LiveData<List<Match>>

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        val matchDao = ScoreboardDatabase.getDatabase(application, scope).matchDao()
        repository = MatchRepository(matchDao)
        allMatchs = repository.allMatches
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(match: Match) = scope.launch(Dispatchers.IO) { repository.insert(match) }
}