package remi.scoreboard.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.parse.ParseException
import io.realm.exceptions.RealmPrimaryKeyConstraintException
import remi.scoreboard.dao.realm.UserDao
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Player
import remi.scoreboard.data.Status
import remi.scoreboard.data.User
import remi.scoreboard.remote.parse.ParseManager

class UserRepository {

    // callbacks
    val signUpState = MutableLiveData<MessageStatus>()
    val logInState = MutableLiveData<MessageStatus>()
    val resetPasswordState = MutableLiveData<MessageStatus>()
    val addPlayerState = MutableLiveData<MessageStatus>()
    val deleteAllPlayerState = MutableLiveData<MessageStatus>()
    val deletePlayerState = MutableLiveData<MessageStatus>()
    val renamePlayerState = MutableLiveData<MessageStatus>()
    val logOutState = MutableLiveData<MessageStatus>()
    val updateUserState = MutableLiveData<MessageStatus>()

    private val currentUserId = ParseManager.currentUserId() ?: "0"
    val currentUser = UserDao.load(currentUserId)

    @WorkerThread
    suspend fun insert(user: User) = UserDao.insert(user)

    @WorkerThread
    suspend fun insertOrUpdate(user: User) = UserDao.insertOrUpdate(user)

    @WorkerThread
    suspend fun signUpUser(user: User) {
        signUpState.postValue(MessageStatus(Status.LOADING)) // Inform UI
        try {
            val newUser = ParseManager.signUpUser(user)
            UserDao.insert(newUser)
            signUpState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalStateException, is RealmPrimaryKeyConstraintException -> {
                    signUpState.postValue(MessageStatus(Status.ERROR, e.message.toString())) // Inform UI
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun loginUser(username: String, password: String) {
        logInState.postValue(MessageStatus(Status.LOADING))
        try {
            val user = ParseManager.logInUser(username, password)
            UserDao.insertOrUpdate(user)
            logInState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalStateException -> {
                    logInState.postValue(MessageStatus(Status.ERROR, e.message.toString()))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun logOut() {
        logOutState.postValue(MessageStatus(Status.LOADING))
        try {
            ParseManager.logOut()
            logOutState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalStateException -> {
                    logOutState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun refreshCurrentUser() {
        updateUserState.postValue(MessageStatus(Status.LOADING))
        try {
            val user = ParseManager.fetchCurrentUser()
            UserDao.insertOrUpdate(user)
            updateUserState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException -> {
                    logInState.postValue(MessageStatus(Status.ERROR, e.message.toString()))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun resetPassword(email: String) {
        resetPasswordState.postValue(MessageStatus(Status.LOADING))
        try {
            ParseManager.resetCurrentUserPassword(email)
            resetPasswordState.postValue(MessageStatus(Status.SUCCESS, "Reset password email sent"))
        } catch (e: ParseException) {
            resetPasswordState.postValue(MessageStatus(Status.ERROR, e.message.toString()))
        }
    }

    @WorkerThread
    suspend fun addPlayerToCurrentUser(player: Player) {
        addPlayerState.postValue(MessageStatus(Status.LOADING))

        try {
            val updatedUser = ParseManager.addPlayerToCurrentUser(player)
            UserDao.update(updatedUser)
            addPlayerState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException -> {
                    addPlayerState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun deleteAllPlayersOfCurrentUser() {
        deleteAllPlayerState.postValue(MessageStatus(Status.LOADING))
        try {
            val updatedUser = ParseManager.deleteAllPlayersOfCurrentUser()
            UserDao.update(updatedUser)
            deleteAllPlayerState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException -> {
                    deleteAllPlayerState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun deletePlayerOfCurrentUser(playerId: String) {
        deletePlayerState.postValue(MessageStatus(Status.LOADING))
        try {
            val updatedUser = ParseManager.deletePlayerOfCurrentUser(playerId)
            UserDao.update(updatedUser)
            deletePlayerState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException, is NullPointerException, is UnsupportedOperationException -> {
                    deletePlayerState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                }
                else -> throw e
            }
        }
    }

    @WorkerThread
    suspend fun renamePlayerOfCurrentUser(playerId: String, newPlayerName: String) {
        renamePlayerState.postValue(MessageStatus(Status.LOADING))
        try {
            val updatedUser = ParseManager.renamePlayerOfCurrentUser(playerId, newPlayerName)
            UserDao.update(updatedUser)
            renamePlayerState.postValue(MessageStatus(Status.SUCCESS))
        } catch (e: Exception) {
            when (e) {
                is ParseException, is IllegalArgumentException, is NullPointerException, is UnsupportedOperationException -> {
                    renamePlayerState.postValue(MessageStatus(Status.ERROR, e.message ?: ""))
                }
                else -> throw e
            }
        }
    }
}
