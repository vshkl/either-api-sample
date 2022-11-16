package com.vshkl.either

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.vshkl.either.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ViewModel by lazy {
        ViewModel(
            coroutineScope = lifecycleScope,
            coroutineDispatcherIO = Dispatchers.IO,
            api = ApiService.api,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fun setupUi() {
            with(binding) {
                btnSuccess.setOnClickListener {
                    viewModel.fetchSuccess()
                }
                btnFailure.setOnClickListener {
                    viewModel.fetchFailure()
                }
            }
        }

        fun observeViewMode() {
            lifecycleScope.launch {
                viewModel.state
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .collect { state ->
                        when (state) {
                            is State.Idle -> Unit
                            is State.Loading -> {
                                binding.tvResult.text = ""
                            }
                            is State.Success -> {
                                binding.tvResult.text = state.profile.pretty()
                            }
                            is State.Failure -> {
                                binding.tvResult.text = state.error.pretty()
                            }
                        }
                    }
            }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        observeViewMode()
    }
}
