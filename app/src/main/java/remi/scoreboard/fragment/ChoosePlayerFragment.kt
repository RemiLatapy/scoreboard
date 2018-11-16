package remi.scoreboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.select.SelectExtension
import remi.scoreboard.R
import remi.scoreboard.databinding.FragmentChoosePlayerBinding
import remi.scoreboard.fastadapter.item.ChoosePlayerItem
import remi.scoreboard.viewmodel.UserViewModel

class ChoosePlayerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var fastAdapter: FastAdapter<ChoosePlayerItem>

    private var listOfSelectedId: List<Long>? = null
    private var selectExtension: SelectExtension<ChoosePlayerItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
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
        fastAdapter.withMultiSelect(true)
        fastAdapter.setHasStableIds(true)

        selectExtension = fastAdapter.getExtension(SelectExtension::class.java)

        binding.recycler.adapter = fastAdapter

        userViewModel.currentUser.observe(this, Observer { user ->
            Log.d("PLAYER", "Current user observe called $user")
            binding.playerList = user.playerList
            playerItemAdapter.setNewList(user.playerList.map { ChoosePlayerItem(it) })
        })

        savedInstanceState?.let {
            fastAdapter.withSavedInstanceState(it)
        }

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

    override fun onResume() {
        super.onResume()
        selectExtension?.let {
            listOfSelectedId?.forEach { id ->
                it.selectByIdentifier(id, false, false)
            }
            listOfSelectedId = null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        fastAdapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun startManagePlayerActivity() {
        listOfSelectedId = selectExtension?.selectedItems?.map { it.identifier }
        val action = ChoosePlayerFragmentDirections.actionManagePlayers()
        findNavController().navigate(action)
    }
}
