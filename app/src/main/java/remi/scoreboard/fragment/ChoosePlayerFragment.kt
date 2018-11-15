package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import remi.scoreboard.R
import remi.scoreboard.databinding.FragmentChoosePlayerBinding
import remi.scoreboard.fastadapter.item.ChoosePlayerItem
import remi.scoreboard.viewmodel.UserViewModel

class ChoosePlayerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var fastAdapter: FastAdapter<ChoosePlayerItem>

    private var userId = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.userId.observe(this, Observer { userId = it })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChoosePlayerBinding.inflate(inflater, container, false)

        binding.managePlayerListener = View.OnClickListener { startManagePlayerActivity() }

        val playerItemAdapter = ItemAdapter<ChoosePlayerItem>()
        fastAdapter = FastAdapter.with(playerItemAdapter)
        fastAdapter.withSelectable(true)
        fastAdapter.setHasStableIds(true)

        binding.recycler.adapter = fastAdapter

        fastAdapter.notifyAdapterDataSetChanged()

        // https://stackoverflow.com/questions/49981734/observer-for-android-livedata-not-called-but-it-is-with-observeforever
        userViewModel.currentUser.observe(this, Observer { user ->
            binding.playerList = user.playerList
            playerItemAdapter.setNewList(user.playerList.map { ChoosePlayerItem(it) })
        })

//        userViewModel.currentUser.observeForever { user ->
//            binding.playerList = user.playerList
//            playerItemAdapter.setNewList(user.playerList.map { ChoosePlayerItem(it) })
//        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_choose_player, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_manage_player -> {
                startManagePlayerActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startManagePlayerActivity() {
        val action = ChoosePlayerFragmentDirections.actionManagePlayers(userId)
        findNavController().navigate(action)
    }
}
