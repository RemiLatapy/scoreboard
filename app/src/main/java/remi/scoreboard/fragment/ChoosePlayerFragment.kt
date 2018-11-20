package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.SelectExtension
import com.wajahatkarim3.easyflipview.EasyFlipView
import remi.scoreboard.R
import remi.scoreboard.data.Status
import remi.scoreboard.databinding.FragmentChoosePlayerBinding
import remi.scoreboard.fastadapter.item.ChoosePlayerItem
import remi.scoreboard.viewmodel.UserViewModel

class ChoosePlayerFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentChoosePlayerBinding
    private lateinit var fastAdapter: FastItemAdapter<ChoosePlayerItem>

    private var listOfSelectedId: List<Long>? = null
    private var selectExtension: SelectExtension<ChoosePlayerItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.updateUserState.observe(this, Observer {
            if (it.status != Status.LOADING)
                binding.swipeRefresh.isRefreshing = false
            if (it.status == Status.ERROR)
                view?.let { view ->
                    if (it.message.isNotEmpty())
                        Snackbar.make(view, it.message, Snackbar.LENGTH_SHORT).show()
                }
        })

        userViewModel.currentUser.observe(this, Observer { user ->
            binding.playerListIsEmpty = user.playerList.isEmpty()
            activity?.invalidateOptionsMenu()
            fastAdapter.setNewList(user.playerList.map { ChoosePlayerItem(it) })

            savedInstanceState?.let {
                fastAdapter.withSavedInstanceState(it)
            }
        })

        userViewModel.updateUser()

        fastAdapter = getFastAdapter()
        selectExtension = fastAdapter.getExtension(SelectExtension::class.java)
    }

    private fun getFastAdapter(): FastItemAdapter<ChoosePlayerItem> {
        val adapter: FastItemAdapter<ChoosePlayerItem> = FastItemAdapter()
        adapter.withSelectable(true)
        adapter.withMultiSelect(true)
        adapter.setHasStableIds(true)
        adapter.withOnClickListener { v, _, _, _ ->
            v?.findViewById<EasyFlipView>(R.id.avatar_flipview)?.flipTheView()
            true
        }
        return adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoosePlayerBinding.inflate(inflater, container, false)
        binding.managePlayerListener = View.OnClickListener { startManagePlayerFragment() }
        binding.recycler.adapter = fastAdapter
        binding.swipeRefresh.setOnRefreshListener { userViewModel.updateUser() }
        binding.setLifecycleOwner(viewLifecycleOwner)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_choose_player, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        if (binding.playerListIsEmpty == true)
            menu?.removeItem(R.id.action_search)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_manage_player -> {
                startManagePlayerFragment()
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
        val newOutState = fastAdapter.saveInstanceState(outState)
        super.onSaveInstanceState(newOutState)
    }

    private fun startManagePlayerFragment() {
        listOfSelectedId = selectExtension?.selectedItems?.map { it.identifier }
        val action = ChoosePlayerFragmentDirections.actionManagePlayers()
        findNavController().navigate(action)
    }
}
