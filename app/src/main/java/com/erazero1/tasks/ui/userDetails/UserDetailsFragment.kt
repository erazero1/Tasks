package com.erazero1.tasks.ui.userDetails

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.erazero1.tasks.R
import com.erazero1.tasks.databinding.FragmentUserDetailsBinding
import com.erazero1.tasks.domain.model.User

class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: UserDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = args.user

        setupUI(user)
        setupListeners(user)
        setupEdgeToEdgeInsets()
    }

    private fun setupUI(user: User) {
        with(binding) {
            tvDetailName.text = user.name
            tvDetailUsername.text =
                getString(R.string.mention_user_format, user.username)

            tvDetailEmail.text = getString(R.string.email_format, user.email)
            tvDetailPhone.text = getString(R.string.phone_number_format, user.phone)
            tvDetailWebsite.text = getString(R.string.website_format, user.website)

            tvDetailCompanyName.text = user.company.name
            tvDetailCompanyPhrase.text =
                getString(R.string.guillemets_format, user.company.catchPhrase)
            tvDetailCompanyBs.text =
                getString(R.string.business_sphere_format, user.company.bs)

            tvDetailCity.text = getString(R.string.city_format, user.address.city)
            tvDetailStreet.text =
                getString(R.string.street_format, user.address.street)
            tvDetailSuite.text =
                getString(R.string.suite_format, user.address.suite)
            tvDetailZipcode.text =
                getString(R.string.zipcode_format, user.address.zipcode)
        }
    }

    private fun setupListeners(user: User) {
        binding.tvDetailEmail.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:${user.email}".toUri()
            }
            safelyStartActivity(emailIntent, getString(R.string.no_mail_app_error))
        }

        binding.tvDetailPhone.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:${user.phone}".toUri()
            }
            safelyStartActivity(phoneIntent, getString(R.string.no_apps_to_make_phone_call_error))
        }

        binding.tvDetailWebsite.setOnClickListener {
            val webUrl =
                if (!user.website.startsWith("http://") && !user.website.startsWith("https://")) {
                    "https://${user.website}"
                } else {
                    user.website
                }

            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = webUrl.toUri()
            }
            safelyStartActivity(webIntent, getString(R.string.no_browser_error))
        }

        binding.btnShowTasks.setOnClickListener {}
    }

    private fun safelyStartActivity(intent: Intent, errorMessage: String) {
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.btnShowTasks) { v, windowInsets ->
            val navBars = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBars.bottom
            }
            windowInsets
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}