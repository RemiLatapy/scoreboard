package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Game
import remi.scoreboard.data.ScoreboardDatabase
import remi.scoreboard.repository.GameRepository
import kotlin.coroutines.CoroutineContext

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GameRepository
    val allGames: LiveData<List<Game>>

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        val gameDao = ScoreboardDatabase.getDatabase(application, scope).gameDao()
        repository = GameRepository(gameDao)
        allGames = repository.allGames
    }

    fun gameById(gid: Int) = repository.gameById(gid)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(game: Game) = scope.launch(Dispatchers.IO) { repository.insert(game) }
}