package com.upn.catatlari.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.upn.catatlari.databinding.FragmentAddRunBinding
import com.upn.catatlari.model.Run
import com.upn.catatlari.viewmodel.RunViewModel
import android.widget.Toast
import android.util.Log

class AddRunFragment : Fragment() {

    private lateinit var binding: FragmentAddRunBinding
    private val runViewModel: RunViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddRunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveRun.setOnClickListener {

            val runDate = binding.etDate.text.toString()
            val runDuration = binding.etRunDuration.text.toString()
            val runDistance = binding.etRunDistance.text.toString()

            // ✅ validasi kosong
            if (runDate.isEmpty() || runDuration.isEmpty() || runDistance.isEmpty()) {
                Toast.makeText(requireContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ validasi angka
            val duration = runDuration.toIntOrNull()
            val distance = runDistance.toIntOrNull()

            if (duration == null || distance == null) {
                Toast.makeText(requireContext(), "Durasi & jarak harus angka!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val runInput = Run(
                runDate = runDate,
                runDuration = duration,
                runDistance = distance
            )

            Log.d("ADD_RUN", "Data ditambahkan: $runInput")

            // ✅ simpan ke ViewModel
            runViewModel.addRun(runInput)

            Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()

            // kembali ke home
            // delay kecil biar LiveData update dulu
            view?.post {
                findNavController().popBackStack()
            }
        }
    }
}