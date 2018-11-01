package remi.scoreboard.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import remi.scoreboard.R
import remi.scoreboard.adapter.UserAdapter
import remi.scoreboard.data.User
import remi.scoreboard.databinding.FragmentUserListBinding
import remi.scoreboard.viewmodel.SharedViewModel
import remi.scoreboard.viewmodel.UserViewModel

class UserListFragment : Fragment(), UserAdapter.UserSelectedCallback {

    private lateinit var userViewModel: UserViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private var fab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUserListBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }

        val userListAdapter = UserAdapter(this)
        binding.recycler.adapter = userListAdapter
        // https://stackoverflow.com/questions/49726385/listadapter-not-updating-item-in-reyclerview
        userListAdapter.submitList(userViewModel.allUsers.value)

        userViewModel.allUsers.observe(this,
            Observer { userListAdapter.notifyDataSetChanged() })

        sharedViewModel.currentMatch.observe(this, Observer {
            if (it.scorePlayerList.size >= 2) fab?.show()
            else fab?.hide()
            Toast.makeText(context, "num player " + it.scorePlayerList.size, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = activity?.findViewById(R.id.fab)
        fab?.run {
            hide()
            setImageResource(R.drawable.ic_play_arrow_black_24dp)
            setOnClickListener {
                view.findNavController().navigate(R.id.gameplay_dest)
            }
        }
    }

    override fun onUserSelected(user: User) {
        sharedViewModel.toogleUser(user)
    }

    override fun isUserSelected(user: User): Boolean {
        return sharedViewModel.currentMatch.value?.scorePlayerList?.count { it.user == user } == 1
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_user, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val builder: AlertDialog.Builder? = activity?.let {
                    AlertDialog.Builder(it)
                }
                builder?.let {
                    val view = layoutInflater.inflate(R.layout.dialog_new_user, null)
                    it.setTitle(R.string.create_user)
                        .setView(view)
                        .setPositiveButton(
                            "Add"
                        ) { _, _ ->
                            val firstName = view.findViewById<TextInputEditText>(R.id.first_name)
                            val lastName = view.findViewById<TextInputEditText>(R.id.last_name)
                            userViewModel.insert(User(firstName.text.toString(), lastName.text.toString()))
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }


//                userViewModel.insert(
//                    User(
//                        firstName = "user ",
//                        lastName = (0..100).random().toString(),
//                        email = (0..100).random().toString() + "@" + (0..100).random().toString() + ".com"
//                    )
//                )
                true
            }
            R.id.action_delete -> {
                userViewModel.deleteAll()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}

