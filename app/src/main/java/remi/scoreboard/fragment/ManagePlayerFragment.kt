package remi.scoreboard.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.dialog_new_user.view.*
import kotlinx.android.synthetic.main.item_card_manage_player.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import remi.scoreboard.R
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentManagePlayerBinding
import remi.scoreboard.fastadapter.item.ManagePlayerItem
import remi.scoreboard.viewmodel.UserViewModel

class ManagePlayerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentManagePlayerBinding
    private lateinit var fastAdapter: FastItemAdapter<ManagePlayerItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        fastAdapter = getFastAdapter()

        // TODO loading state (ui + code)
        userViewModel.deleteAllPlayerState.observe(this, Observer { showErrorIfNeeded(it) })
        userViewModel.deletePlayerState.observe(this, Observer { showErrorIfNeeded(it) })
        userViewModel.renamePlayerState.observe(this, Observer {
            if (it.status == Status.SUCCESS)
                fastAdapter.notifyAdapterDataSetChanged()
            else
                showErrorIfNeeded(it)
        })
        userViewModel.addPlayerState.observe(this, Observer { showErrorIfNeeded(it) })
        userViewModel.updateUserState.observe(this, Observer {
            if (it.status != Status.LOADING)
                binding.swipeRefresh.isRefreshing = false
            showErrorIfNeeded(it)
        })

        userViewModel.currentUser.observe(this, Observer { user ->
            binding.playerListIsEmpty = user.playerList.isEmpty()
            activity?.invalidateOptionsMenu()
            fastAdapter.setNewList(user.playerList.map { ManagePlayerItem(it) })
        })

        userViewModel.updateUser()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagePlayerBinding.inflate(inflater, container, false)
        binding.addPlayerListener = View.OnClickListener { showAddPlayerDialog() }
        binding.recycler.adapter = fastAdapter
        binding.swipeRefresh.setOnRefreshListener { userViewModel.updateUser() }

        return binding.root
    }

    private fun getFastAdapter(): FastItemAdapter<ManagePlayerItem> {
        val adapter: FastItemAdapter<ManagePlayerItem> = FastItemAdapter()
        adapter.setHasStableIds(true)
        adapter.withEventHook(object : ClickEventHook<ManagePlayerItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return (viewHolder as? ManagePlayerItem.ViewHolder)?.itemView?.btn_delete
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<ManagePlayerItem>,
                playerItem: ManagePlayerItem
            ) {
                activity?.let {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.confirm_delete_player_format, playerItem.player.username))
                        .setPositiveButton(R.string.action_delete) { _: DialogInterface, _: Int ->
                            userViewModel.deletePlayer(playerItem.player.id)
                        }.setNegativeButton(R.string.action_cancel, null)
                        .show()
                }
            }
        })
        adapter.withEventHook(object : ClickEventHook<ManagePlayerItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return (viewHolder as? ManagePlayerItem.ViewHolder)?.itemView?.btn_edit
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<ManagePlayerItem>,
                playerItem: ManagePlayerItem
            ) {
                showRenamePlayerDialog(playerItem)
            }
        })
        adapter.itemFilter.withFilterPredicate { item, constraint ->
            runBlocking {
                async(Dispatchers.Main) {
                    // Need to run on UI thread to access player (live realm object)
                    item.player.username.contains(constraint ?: "", true)
                }.await()
            }
        }
        return adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_manage_player, menu)
        (menu?.findItem(R.id.action_search)?.actionView as? SearchView)?.setOnQueryTextListener(queryTextListener)
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            fastAdapter.filter(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            fastAdapter.filter(newText)
            return true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        if (binding.playerListIsEmpty == true) {
            menu?.removeItem(R.id.action_delete_all_player)
            menu?.removeItem(R.id.action_search)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add_player -> {
                showAddPlayerDialog()
                true
            }
            R.id.action_delete_all_player -> {
                activity?.let {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.confirm_delete_all_player)
                        .setMessage(R.string.delete_all_player_message)
                        .setPositiveButton(R.string.action_delete) { _: DialogInterface, _: Int ->
                            userViewModel.deleteAllPlayer()
                        }.setNegativeButton(R.string.action_cancel, null)
                        .show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAddPlayerDialog() {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.let {
            val view = layoutInflater.inflate(R.layout.dialog_new_user, null)
            it.setTitle(R.string.create_player)
                .setView(view)
                .setPositiveButton(
                    "Add"
                ) { _, _ ->
                    userViewModel.addPlayer(view.txt_username.text.toString().trim(), activity)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showRenamePlayerDialog(playerItem: ManagePlayerItem) {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.let {
            val view = layoutInflater.inflate(R.layout.dialog_new_user, null)
            view.txt_username.setText(playerItem.player.username)
            it.setTitle("Rename ${playerItem.player.username}")
                .setView(view)
                .setPositiveButton(
                    "Rename"
                ) { _, _ ->
                    userViewModel.renamePlayer(playerItem.player.id, view.txt_username.text.toString().trim())
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showErrorIfNeeded(ms: MessageStatus) {
        if (ms.status == Status.ERROR) {
            view?.let {
                if (ms.message.isNotEmpty())
                    Snackbar.make(it, ms.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
