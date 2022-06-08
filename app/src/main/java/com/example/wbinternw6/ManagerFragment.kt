package com.example.wbinternw6

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.wbinternw6.databinding.FragmentManagerBinding
import com.example.wbinternw6.databinding.FragmentPiBinding
import kotlin.random.Random

class ManagerFragment : Fragment() {

    private var seconds = 0
    private var minutes = 0
    private var hours = 0
    private var counter = 0

    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            renderData()
            handler.postDelayed(this, 1000)
        }
    }

    private fun renderData() {
        seconds = counter % 60
        minutes = counter / 60
        hours = minutes / 60

        var time = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        binding.timer.text = time

        if (counter % 20 == 0) {
            val randomColor = -Random.nextInt(255 * 255 * 255)
            binding.container.setBackgroundColor(randomColor)
        }
        counter++
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentManagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pause.setOnClickListener {
            handler.removeCallbacks(runnable)
            viewModel.applyNewState("pause")
        }

        binding.start.setOnClickListener {
            handler.removeCallbacks(runnable)
            handler.post(runnable)
            viewModel.applyNewState("start")
        }

        binding.restart.setOnClickListener {
            handler.removeCallbacks(runnable)
            counter = 0
            minutes = 0
            seconds = 0
            binding.timer.text = "00:00:00"
            handler.post(runnable)
            viewModel.applyNewState("restart")
        }
    }
}