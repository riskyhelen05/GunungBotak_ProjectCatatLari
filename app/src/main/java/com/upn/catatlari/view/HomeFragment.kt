package com.upn.catatlari.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.upn.catatlari.databinding.FragmentHomeBinding
import com.upn.catatlari.viewmodel.RunViewModel
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val runViewModel: RunViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ✅ Ambil user & username
        val mainActivity = activity as MainActivity
        val user = mainActivity.user

        if (user == null) {
            Log.e("HOME", "User null!")
            return
        }

        binding.welcomingTxt.text = "Halo, ${user.email.substringBefore("@")}"

        val runAdapter = RunAdapter()
        binding.rvRunList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = runAdapter
        }

        runViewModel.getRunsByUser(user.id)
            .observe(viewLifecycleOwner) { runList ->

                Log.d("HOME_RUN", "Data diterima: $runList")

                runAdapter.setData(runList)

                if (runList.isNotEmpty()) {
                    binding.rvRunList.scrollToPosition(runList.size - 1)
                }
            }

        binding.floatingBtnAddRun.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddRunFragment()
            )
        }
    }
}