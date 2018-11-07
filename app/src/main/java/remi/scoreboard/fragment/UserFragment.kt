package remi.scoreboard.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.parse.ParseUser
import remi.scoreboard.R
import remi.scoreboard.activity.LoginSignupActivity


class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.sign_out_btn).setOnClickListener {
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
    }
}
