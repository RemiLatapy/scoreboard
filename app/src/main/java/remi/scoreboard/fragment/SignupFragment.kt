package remi.scoreboard.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseACL
import com.parse.ParseUser
import remi.scoreboard.R

// TODO check connection state
class SignupFragment : Fragment() {

    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSignup(view)

        view.findViewById<EditText>(R.id.username).setOnFocusChangeListener { v, hasFocus ->
            (v as? EditText)?.let { if (!hasFocus) it.setText(it.text.toString().trim()) }
        }

        progressBar = view.findViewById(R.id.progress)
        progressBar.visibility = View.INVISIBLE
    }

    private fun setupSignup(view: View) {
        view.run {
            findViewById<Button>(R.id.signup_btn)?.setOnClickListener {
                val user = ParseUser()
                user.username = findViewById<EditText>(R.id.username).text.toString()
                user.email = findViewById<EditText>(R.id.email).text.toString()
                user.setPassword(findViewById<EditText>(R.id.password).text.toString())
                progressBar.visibility = View.VISIBLE
                user.signUpInBackground { e ->
                    if (e == null) {
                        val newUser = ParseUser.getCurrentUser()
                        newUser.acl = ParseACL(user)
                        newUser.saveInBackground()
                        val action = SignupFragmentDirections.actionSignupToMain()
                        findNavController().navigate(action)
                        activity?.finish()
                    } else {
                        // TODO handle error (UI)
                        progressBar.visibility = View.INVISIBLE
                        Log.d("LOGIN", "Signup failed: ${e.message}")
                        Snackbar.make(view, "Sign up failed", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
