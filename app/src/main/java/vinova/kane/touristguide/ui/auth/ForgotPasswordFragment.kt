package vinova.kane.touristguide.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import vinova.kane.touristguide.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater)

        binding.fragment = this

        return binding.root
    }

    fun initEvent(view: View) {
        when (view) {
            binding.ivArrowBack -> {
                findNavController().popBackStack()
            }

            binding.btnResetPw -> {
                if (binding.edtEmail.text.toString().isNotEmpty()) {
                    auth.sendPasswordResetEmail(binding.edtEmail.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    activity,
                                    "Reset link sent to your email",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Unable to send reset mail",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                }
            }
        }
    }


}