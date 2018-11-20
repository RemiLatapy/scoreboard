package remi.scoreboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.Game
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.repository.GameRepository
import kotlin.coroutines.CoroutineContext

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameRepository: GameRepository = GameRepository()
    val allGames: LiveData<List<Game>> = gameRepository.allGames

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    val updateGameListState: LiveData<MessageStatus> = gameRepository.updateGameListState

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(game: Game) = scope.launch(Dispatchers.IO) { gameRepository.insert(game) }

    fun insert(gameList: List<Game>) = scope.launch(Dispatchers.IO) { gameRepository.insert(gameList) }

    fun deleteAll() = scope.launch(Dispatchers.IO) { gameRepository.deleteAll() }

    fun updateGameList() {
        scope.launch(Dispatchers.IO) { gameRepository.updateGameList() }
    }
}