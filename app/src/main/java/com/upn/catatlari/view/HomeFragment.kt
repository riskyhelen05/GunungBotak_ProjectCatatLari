package com.upn.catatlari.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.upn.catatlari.databinding.FragmentHomeBinding
import com.upn.catatlari.model.Run
import com.upn.catatlari.viewmodel.RunViewModel
import android.util.Log

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val runViewModel: RunViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // ✅ 1. Ambil user
        val username = (activity as MainActivity).username
        val nama = username?.substringBefore("@")

        binding.welcomingTxt.text = "Halo, ${nama ?: "User"} 👋"

        // ✅ 2. Setup adapter
        val runAdapter = RunAdapter()
        binding.rvRunList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRunList.adapter = runAdapter

        // ✅ 3. Observe data
        runViewModel.runHistory.observe(viewLifecycleOwner) { runList ->
            Log.d("HOME_RUN", "Data diterima: $runList")
            runAdapter.setData(runList)

            binding.rvRunList.scrollToPosition(runList.size - 1)
        }

        // ✅ 4. Tombol tambah
        binding.floatingBtnAddRun.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddRunFragment()
            )
        }
    }
}