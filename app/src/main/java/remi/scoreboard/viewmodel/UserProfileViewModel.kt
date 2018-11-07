package remi.scoreboard.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import remi.scoreboard.data.User
import remi.scoreboard.repository.UserRepository
import kotlin.coroutines.CoroutineContext


class UserProfileViewModel : ViewModel() {

    val userRepository: UserRepository = UserRepository()

    var currentUser: LiveData<User>? = null

    val isCurrentUserAvailable: MutableLiveData<Boolean> by lazy {
        val ret = MutableLiveData<Boolean>()
        ret.value = false
        ret
    }


    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }


    fun createUser(username: String, email: String, password: String) {
        Log.d("THREAD", "createUser = #${Thread.currentThread().id} // ${Thread.currentThread()}")
        scope.launch {
            Log.d("THREAD", "createUser/Scope = #${Thread.currentThread().id} // ${Thread.currentThread()}")

            val user = User(username = username, email = email, password = password)
            val id = userRepository.createUser(user)!! // now we have a current user TODO exception

            Log.d("THREAD", "createUser/Scope2 = #${Thread.currentThread().id} // ${Thread.currentThread()}")

            currentUser = userRepository.user(id)

//            currentUser = userRepository.storedCurrentUser!!
            isCurrentUserAvailable.postValue(true)
        }
    }
}