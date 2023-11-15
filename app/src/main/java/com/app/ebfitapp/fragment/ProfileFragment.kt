package com.app.ebfitapp.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.ebfitapp.R
import com.app.ebfitapp.databinding.FragmentProfileBinding
import com.app.ebfitapp.service.FirebaseAuthService
import com.app.ebfitapp.utils.AppPreferences
import com.app.ebfitapp.utils.CustomProgress
import com.app.ebfitapp.utils.downloadImageFromURL
import com.app.ebfitapp.view.AuthenticationActivity
import com.app.ebfitapp.viewmodel.MainViewModel
import org.w3c.dom.Text
import kotlin.math.abs
class ProfileFragment : Fragment() {
    private lateinit var firebaseAuthService: FirebaseAuthService
    private lateinit var fragmentProfileBinding : FragmentProfileBinding
    private lateinit var appPreferences : AppPreferences
    private lateinit var mainViewModel: MainViewModel
    private lateinit var customProgress: CustomProgress
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater)
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this@ProfileFragment)[MainViewModel::class.java]
        mainViewModel.getProfileDetail()
        customProgress = CustomProgress(requireActivity())
        customProgress.show()

        observeProfileDetail()
        with(fragmentProfileBinding)
        {
            editProfileLayout.setOnClickListener()
            {
                val backgroundDrawable = applyAlphaToDrawableWithDelay(
                    requireContext(),
                    R.drawable.kilogram_background,
                    0.7,
                    1.0,
                    200
                )
                it.background = backgroundDrawable
                Toast.makeText(requireContext(),"Edit profile tıklandı",Toast.LENGTH_SHORT).show()
            }
            notificationLayout.setOnClickListener()
            {
                val backgroundDrawable = applyAlphaToDrawableWithDelay(
                    requireContext(),
                    R.drawable.kilogram_background,
                    0.7,
                    1.0,
                    200
                )
                it.background = backgroundDrawable
                Toast.makeText(requireContext(),"notification tıklandı",Toast.LENGTH_SHORT).show()
            }
            languageLayout.setOnClickListener()
            {
                val backgroundDrawable = applyAlphaToDrawableWithDelay(
                    requireContext(),
                    R.drawable.kilogram_background,
                    0.7,
                    1.0,
                    200
                )
                it.background = backgroundDrawable
                Toast.makeText(requireContext(),"language  tıklandı",Toast.LENGTH_SHORT).show()
            }
            logoutLayout.setOnClickListener()
            {
                val backgroundDrawable = applyAlphaToDrawableWithDelay(
                    requireContext(),
                    R.drawable.kilogram_background,
                    0.7,
                    1.0,
                    200
                )
                it.background = backgroundDrawable

                val intent = Intent(requireActivity(),AuthenticationActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
               // firebaseAuthService.signOut()

            }
        }
    }



    fun applyAlphaToDrawableWithDelay(context: Context, drawableResId: Int, initialAlpha: Double, finalAlpha: Double,
                                      delayMillis: Long): Drawable? {
        val backgroundDrawable = ContextCompat.getDrawable(context, drawableResId)?.mutate()
        backgroundDrawable?.colorFilter = PorterDuffColorFilter(
            Color.parseColor("#232529"),
            PorterDuff.Mode.SRC_ATOP
        )
        backgroundDrawable?.mutate()?.alpha = (initialAlpha * 255).toInt()
        Handler(Looper.getMainLooper()).postDelayed({
            backgroundDrawable?.mutate()?.alpha = (finalAlpha * 255).toInt()
        }, delayMillis)
        return backgroundDrawable
    }


    private fun observeProfileDetail() {
        mainViewModel.profileDetails.observe(requireActivity(), Observer { userProfileDetails ->
            customProgress.dismiss()
            if (userProfileDetails != null) {
                fragmentProfileBinding.rootLinearForProfile.visibility = View.VISIBLE
                fragmentProfileBinding.profileImage.downloadImageFromURL(userProfileDetails.profileImageURL.toString())
                fragmentProfileBinding.usernameAndAgeText.text = "${userProfileDetails.username} , ${userProfileDetails.age}"
                fragmentProfileBinding.userEmailText.text = "${userProfileDetails.email}"
                fragmentProfileBinding.startWeightText.text = "${userProfileDetails.weight}"
                fragmentProfileBinding.targetWeightText.text = "${userProfileDetails.targetWeight}"
                fragmentProfileBinding.weightDifferencesText.text = "${CalculateWeightDifference(userProfileDetails.targetWeight!!,userProfileDetails.weight!!)}"
                fragmentProfileBinding.goalText.text = "${userProfileDetails.goal}"
            }
        })
    }

    private fun CalculateWeightDifference(targetWeight : Double,startWeight : Double) : String
    {
        val differences = abs(targetWeight-startWeight)
        return formatDoubleUsingSplit(differences,2)
    }

    fun formatDoubleUsingSplit(doubleValue: Double, decimalPlaces: Int): String {
        val stringValue = doubleValue.toString()
        val parts = stringValue.split(".")

        return if (parts.size > 1 && parts[1].length > decimalPlaces) {
            "${parts[0]}.${parts[1].substring(0, decimalPlaces)}"
        } else {
            stringValue
        }
    }

}