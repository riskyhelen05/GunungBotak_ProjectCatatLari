package com.upn.catatlari.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.upn.catatlari.databinding.FragmentAddRunBinding
import com.upn.catatlari.viewmodel.RunViewModel
import android.widget.Toast
import android.util.Log
import com.upn.catatlari.data.local.entity.RunEntity
import androidx.lifecycle.ViewModelProvider

class AddRunFragment : Fragment() {

    private lateinit var binding: FragmentAddRunBinding
    private val runViewModel: RunViewModel by activityViewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveRun.setOnClickListener {

            val user = (activity as MainActivity).user

            if (user == null) {
                Toast.makeText(
                    requireContext(),
                    "User tidak ditemukan, login ulang!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // ✅ ambil input
            val runDate = binding.etDate.text.toString()
            val runDistance = binding.etRunDistance.text.toString()
            val runDuration = binding.etRunDuration.text.toString()

            // ✅ validasi kosong
            if (runDate.isEmpty() || runDistance.isEmpty() || runDuration.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // ✅ validasi angka
            val distance = runDistance.toIntOrNull()
            val duration = runDuration.toIntOrNull()

            if (distance == null || duration == null) {
                Toast.makeText(requireContext(), "Jarak & durasi harus angka!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // ✅ validasi nilai
            if (distance <= 0 || duration <= 0) {
                Toast.makeText(
                    requireContext(),
                    "Jarak & durasi harus lebih dari 0!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // ✅ buat entity
            val runEntity = RunEntity(
                runDate = runDate,
                runDistance = distance,
                runDuration = duration,
                userId = user.id
            )

            Log.d("ADD_RUN", "Data ditambahkan: $runEntity")

            runViewModel.addRun(runEntity)

            Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()

            view.post {
                findNavController().popBackStack()
            }
        }
    }
}