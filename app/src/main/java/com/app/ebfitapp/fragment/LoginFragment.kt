package com.app.ebfitapp.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.Navigation
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentLoginBinding
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.view.MainActivity
import com.google.firebase.auth.FirebaseAuth
import org.checkerframework.common.subtyping.qual.Bottom

class LoginFragment : Fragment() {
    private var email: String? = null
    private var password: String? = null
    private lateinit var customProgress: CustomProgress
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var fragmentLoginBinding : FragmentLoginBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(layoutInflater)
        customProgress = CustomProgress(requireContext())
        firebaseAuthService = FirebaseAuthService(requireContext())
        return fragmentLoginBinding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(fragmentLoginBinding) {
            onLoginButton.setOnClickListener() {
                    email = loginEmailText.text.toString()
                    password = loginPasswordText.text.toString()


                if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), getString(R.string.please_fill_in_the_empty_fields), Toast.LENGTH_LONG).show()
                } else {
                    customProgress.show()
                    firebaseAuthService.loginAccount(email = email!! ,password = password!!) { task ->
                        if(task)
                        {   //Authentication succesful
                            customProgress.dismiss()
                            requireActivity().finish()
                            val intent =Intent(requireContext(),MainActivity::class.java)
                            startActivity(intent)

                        }
                        else{
                            //Authentication unsuccesful
                            customProgress.dismiss()
                        }
                    }
                }
            }

           forgotPasswordText.setOnClickListener()
           {
               ShowDialog()

           }
        }
    }


    private fun ShowDialog()
    {

        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)

        val resetEmailAdress = dialog?.findViewById<EditText>(R.id.resetEmailAddress)
        val resetButton = dialog?.findViewById<Button>(R.id.resetButton)


        resetButton?.setOnClickListener()
        {
            val sPassword = resetEmailAdress?.text.toString()
            auth.sendPasswordResetEmail(sPassword)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(),"Please Check Your Email Adress",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {

                    Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                }

        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

    }


}