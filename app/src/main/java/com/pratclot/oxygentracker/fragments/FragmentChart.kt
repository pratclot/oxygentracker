package com.pratclot.oxygentracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.pratclot.oxygentracker.data.BodyMeasurement
import com.pratclot.oxygentracker.data.entryFromMeasurement
import com.pratclot.oxygentracker.databinding.FragmentChartBinding
import com.pratclot.oxygentracker.viewmodels.OxygenActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.DateTimeException
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
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
                SetContent(viewModel, findNavController(), lineChart, viewLifecycleOwner)
            }
        }
    }
}

@Composable
private fun SetContent(
    viewModel: OxygenActivityViewModel,
    navController: NavController,
    lineChart: FragmentChartBinding,
    viewLifecycleOwner: LifecycleOwner
) {
//    var state: State<List<BodyMeasurement.OxygenMeasurement>> =
//        viewModel.getAll()
//            .collectAsState(
//                initial = mutableStateListOf(BodyMeasurement.OxygenMeasurement.empty())
//            )

    var state: SnapshotStateList<BodyMeasurement.OxygenMeasurement> = remember {
        mutableStateListOf(BodyMeasurement.OxygenMeasurement.empty())
    }
    viewModel.oxygenMeasurements.observe(viewLifecycleOwner) {
        state.clear()
        state.addAll(it)
    }

    var refreshState = remember { mutableStateOf("") }

    lineChart.lineChartFragmentChart.apply {
        setOnChartValueSelectedListener(
            object : OnChartValueSelectedListener {
                override fun onValueSelected(p0: Entry?, p1: Highlight?) {
                    refreshState.value = p0.toString()
                }

                override fun onNothingSelected() {
                }
            }
        )
    }

    MaterialTheme {
        Scaffold(
            bottomBar = { setupBottomBar(navController) },
            bodyContent = { setChart(state, lineChart, refreshState) },
        )
    }
}

@Composable
fun setChart(
    state: SnapshotStateList<BodyMeasurement.OxygenMeasurement>,
    lineChart: FragmentChartBinding,
    refreshState: MutableState<String>
) = AndroidViewBinding(
    bindingBlock = { _, _, _ ->
        lineChart
    },
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.9f),
    update = { refreshChartData(lineChart, state, refreshState) }
)

private fun refreshChartData(
    lineChart: FragmentChartBinding,
    measurements: List<BodyMeasurement.OxygenMeasurement>,
    refreshState: MutableState<String>
) {
    Timber.e(refreshState.toString())
    lineChart.lineChartFragmentChart.apply {
        xAxis.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return Instant.ofEpochSecond(value.toLong()).atZone(ZoneId.systemDefault())
                        .toLocalDate().toString()
                }
            }
        }

        val entries = ArrayList<Entry>()
        measurements.map {
            try {
                entries.add(entryFromMeasurement(it))
            } catch (ex: DateTimeException) {
                Timber.e(ex)
            }
        }

        val dataSet = LineDataSet(
            entries
                .filter { it.y in 50f..100f }
                .sortedBy { it.x },
            "SpO2"
        ).apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
        }
        dataSet.let {
            data = LineData(it).apply {
//                barWidth = (it.xMax - it.xMin) / it.entryCount
            }
        }

        notifyDataSetChanged()
        invalidate()
    }
}
