package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import kotlinx.android.synthetic.main.dialog_new_user.view.*
import kotlinx.android.synthetic.main.item_card_player.view.*
import remi.scoreboard.R
import remi.scoreboard.data.MessageStatus
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentManagePlayerBinding
import remi.scoreboard.fastadapter.item.PlayerItem
import remi.scoreboard.viewmodel.UserViewModel

class ManagePlayerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var fastAdapter: FastAdapter<PlayerItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        // TODO loading state (ui + code)
        userViewModel.deleteAllPlayerState.observe(this, Observer { showError(it) })
        userViewModel.deletePlayerState.observe(this, Observer { showError(it) })
        userViewModel.renamePlayerState.observe(this, Observer {
            if (it.status == Status.SUCCESS)
                fastAdapter.notifyAdapterDataSetChanged()
            else
                showError(it)
        })
        userViewModel.addPlayerState.observe(this, Observer { showError(it) })
    }

    private fun showError(ms: MessageStatus) {
        if (ms.status == Status.ERROR) {
            view?.let {
                if (ms.message.isNotEmpty())
                    Snackbar.make(it, ms.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentManagePlayerBinding.inflate(inflater, container, false)
        binding.addPlayerListener = View.OnClickListener { showAddPlayerDialog() }

        val playerItemAdapter = ItemAdapter<PlayerItem>()
        fastAdapter = FastAdapter.with(playerItemAdapter)

        fastAdapter.setHasStableIds(true)
        fastAdapter.withEventHook(object : ClickEventHook<PlayerItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return (viewHolder as? PlayerItem.ViewHolder)?.itemView?.more_btn
            }

            override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<PlayerItem>, playerItem: PlayerItem) {
                context?.let {
                    val popupMenu = PopupMenu(it, v)
                    popupMenu.inflate(R.menu.popup_menu_player)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem?.itemId) {
                            R.id.action_rename_player -> {
                                showRenamePlayerDialog(playerItem)
                                true
                            }
                            R.id.action_delete_player -> {
                                userViewModel.deletePlayer(playerItem.player.id)
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                }
                Toast.makeText(context, "Click more option on ${playerItem.player}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.recycler.adapter = fastAdapter

        fastAdapter.notifyAdapterDataSetChanged()

        userViewModel.currentUser.observe(this, Observer { user ->
            playerItemAdapter.setNewList(user.playerList.map { PlayerItem(it) })
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add_player -> {
                showAddPlayerDialog()
                true
            }
            R.id.action_delete_all_player -> {
                userViewModel.deleteAllPlayer()
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
                    userViewModel.addPlayer(view.username.text.toString().trim())
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showRenamePlayerDialog(playerItem: PlayerItem) {
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.let {
            val view = layoutInflater.inflate(R.layout.dialog_new_user, null)
            view.username.setText(playerItem.player.username)
            it.setTitle("Rename ${playerItem.player.username}")
                .setView(view)
                .setPositiveButton(
                    "Rename"
                ) { _, _ ->
                    userViewModel.renamePlayer(playerItem.player.id, view.username.text.toString().trim())
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
