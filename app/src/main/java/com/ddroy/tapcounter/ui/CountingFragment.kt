package com.ddroy.tapcounter.ui

import com.ddroy.tapcounter.utils.VibrationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.ddroy.tapcounter.BaseFragment
import com.ddroy.tapcounter.CountAppWidget
import com.ddroy.tapcounter.R
import com.ddroy.tapcounter.databinding.FragmentCountingBinding
import com.ddroy.tapcounter.viewmodel.CounterViewModel
import com.ddroy.tapcounter.navigation.Navigation
import com.ddroy.tapcounter.utils.getDynamicTextSize
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


class CountingFragment : BaseFragment(R.layout.fragment_counting){

    private lateinit var binding: FragmentCountingBinding
    private val viewModel: CounterViewModel by activityViewModels()
    private lateinit var vibrationManager: VibrationManager
    private val fragmentInstance = this

    override fun getHomeFragmentId(): Int {
        return R.id.countingFragment
    }

    override fun getFragmentInstance(): Fragment {
        return this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentCountingBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        vibrationManager = VibrationManager(requireContext())

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.apply {
            imageView3.setOnClickListener {
                viewModel.incrementCount()
                reCallWidget()
            }
            decreaseCountBtn.setOnClickListener {
                viewModel.decrementCount()
                reCallWidget()
            }
            resetBtn.setOnClickListener {
                viewModel.resetCount()
                reCallWidget()
                setConfettiValue()
                viewModel.resetConfettiCount()
            }
            lockBtn.setOnClickListener { viewModel.toggleLock() }
            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.settings -> {
                        viewModel.resetConfettiState()
                        Navigation.navigate(fragmentInstance,R.id.countingFragment,R.id.settingsFragment)
                        true
                    }
                    else -> false
                }
            }
            confettiTxt.isVisible = viewModel.isEnableConfetti()
        }
        setConfettiValue(viewModel.confettiCount.toString())
    }

    private fun observeViewModel() {
        viewModel.count.observe(viewLifecycleOwner) { count ->
            setTextAndSize(count.toString())
            animateCountText()
        }

        viewModel.isLocked.observe(viewLifecycleOwner) { isLocked ->
            binding.lockBtn.setIconResource(
                if (isLocked) R.drawable.ic_lock_closed
                else R.drawable.ic_lock_open
            )
        }

        viewModel.showConfetti.observe(viewLifecycleOwner) { shouldShow ->
            if (shouldShow) {
                showConfetti()
                val count = viewModel.confettiCount.toString()
                setConfettiValue(count)
            }
        }
    }

    private fun reCallWidget(){
        val manager = AppWidgetManager.getInstance(context)
        val ids = manager.getAppWidgetIds(context?.let { it1 -> ComponentName(it1, CountAppWidget::class.java) })
        ids.forEach { context?.let { it1 -> CountAppWidget.updateAppWidget(it1, manager, it) } }
    }

    private fun setTextAndSize(text: String){
        binding.countTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, text.getDynamicTextSize())
        binding.countTxt.text = text
    }

    private fun setConfettiValue(count : String?="0"){
        binding.confettiTxt.text = "× ${count}"
    }

    private fun animateCountText() {
        binding.countTxt.apply {
            scaleX = 0.7f
            scaleY = 0.7f
            animate().scaleX(1f).scaleY(1f).setDuration(500).start()
        }
    }

    private fun showConfetti() {
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def).map { it },
            emitter = Emitter(duration = 300, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.5)  // Center of screen
        )
        binding.konfettiView.post {
            binding.konfettiView.start(party)
        }
    }
}