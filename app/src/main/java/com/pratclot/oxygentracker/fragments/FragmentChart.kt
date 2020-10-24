package com.pratclot.oxygentracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.pratclot.oxygentracker.data.BodyMeasurement
import com.pratclot.oxygentracker.databinding.FragmentChartBinding
import com.pratclot.oxygentracker.viewmodels.OxygenActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.DateTimeException
import org.threeten.bp.ZonedDateTime
import timber.log.Timber

@AndroidEntryPoint
class FragmentChart : Fragment() {
    private val viewModel: OxygenActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lineChart = FragmentChartBinding.inflate(inflater)
        return ComposeView(requireContext()).apply {
            setContent {
                SetContent(viewModel, findNavController(), lineChart)
            }
        }
    }
}

@Composable
private fun SetContent(
    viewModel: OxygenActivityViewModel,
    navController: NavController,
    lineChart: FragmentChartBinding
) {
    var state: State<List<BodyMeasurement.OxygenMeasurement>> =
        viewModel.getAll()
            .collectAsState(
                initial = mutableStateListOf(BodyMeasurement.OxygenMeasurement.empty())
            )

    MaterialTheme() {
        Scaffold(
            bottomBar = { setupBottomBar(navController) },
            bodyContent = { setChart(state, lineChart) }
        )
    }
}

@Composable
fun setChart(
    state: State<List<BodyMeasurement.OxygenMeasurement>>,
    lineChart: FragmentChartBinding
) = AndroidViewBinding(
    bindingBlock = { _, _, _ ->
        lineChart
    },
    modifier = Modifier
        .fillMaxSize(),
    update = { refreshChartData(lineChart, state.value) }
)


private fun refreshChartData(
    lineChart: FragmentChartBinding,
    measurements: List<BodyMeasurement.OxygenMeasurement>
) {
    lineChart.lineChartFragmentChart.apply {
        val entries = ArrayList<Entry>()
        measurements.map {
            try {
                val timestamp = ZonedDateTime.parse(it.timestamp).toInstant().epochSecond
                entries.add(Entry(timestamp.toFloat(), it.oxygenLevel.toFloat()))
            } catch (ex: DateTimeException) {
                Timber.e(ex)
            }
        }
        val dataSet = LineDataSet(entries, "SpO2")

        data = LineData(dataSet)
        isLogEnabled = true
        notifyDataSetChanged()
        invalidate()
    }
}
