package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Match
import remi.scoreboard.repository.GameRepository
import remi.scoreboard.repository.MatchRepository
import kotlin.coroutines.CoroutineContext

class MatchViewModel(application: Application) : AndroidViewModel(application) {
    private val matchRepository = MatchRepository()
    private val gameRepository = GameRepository()

    val allMatchs = matchRepository.allMatches
    lateinit var currentMatch: LiveData<Match>

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(match: Match) = scope.launch(Dispatchers.IO) { matchRepository.insert(match) }

    fun create(match: Match): LiveData<Match> {
        currentMatch = matchRepository.create(match)
        return currentMatch
    }
}