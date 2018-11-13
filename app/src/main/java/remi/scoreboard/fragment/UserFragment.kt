package remi.scoreboard.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.parse.ParseUser
import remi.scoreboard.activity.LoginSignupActivity
import remi.scoreboard.databinding.FragmentUserBinding
import remi.scoreboard.viewmodel.UserViewModel

// TODO fragment need to be 'recreate' when coming back from login activity to refresh view model with new current user id
class UserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: FragmentUserBinding
    private var userId = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel.userId.observe(this, Observer { userId = it })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.signoutListener = createSignoutListener()
        binding.loginListener = createLoginListener()
        binding.managePlayersListener = createManagePlayersListener()
        return binding.root
    }

    // TODO should move listener to viewModel ?
    private fun createSignoutListener() = View.OnClickListener {
        ParseUser.logOutInBackground { e ->
            if (e == null) {
                Toast.makeText(context, "Sign out succeed", Toast.LENGTH_SHORT).show()
                activity?.run {
                    startActivity(Intent(this, LoginSignupActivity::class.java))
                    finish()
                }
            } else
                Toast.makeText(context, "Sign out failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createLoginListener() = View.OnClickListener {
        val action = UserFragmentDirections.actionLoginSignup()
        findNavController().navigate(action)
    }

    private fun createManagePlayersListener() = View.OnClickListener {
        val action = UserFragmentDirections.actionManagePlayers(userId)
        findNavController().navigate(action)
    }
}
