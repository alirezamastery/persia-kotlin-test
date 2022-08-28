package com.persia.test.ui.panel.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.persia.test.R
import com.persia.test.databinding.FragmentProfileBinding
import timber.log.Timber


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        binding.goToIncomeListBtn.setOnClickListener {
            profileViewModel.displayIncomes()
        }

        profileViewModel.navigateToIncomeList.observe(viewLifecycleOwner) {
            if (it == true) {
                this.findNavController().navigate(R.id.incomeListFragment)
                Timber.i("clicked")
                profileViewModel.displayIncomesCompleted()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}