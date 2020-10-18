package com.pratclot.oxygentracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pratclot.oxygentracker.R
import com.pratclot.oxygentracker.data.BodyMeasurement
import com.pratclot.oxygentracker.viewmodels.OxygenActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {
    private val viewModel: OxygenActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SetContent(viewModel, findNavController(), viewLifecycleOwner)
            }
        }
    }
}

@Composable
private fun SetContent(
    viewModel: OxygenActivityViewModel,
    navController: NavController,
    lifecycleOwner: LifecycleOwner
) {
    var state = remember { mutableStateListOf(BodyMeasurement.OxygenMeasurement.empty()) }
    viewModel.oxygenMeasurements.observe(lifecycleOwner) {
        state.apply {
            clear()
            addAll(it)
        }
    }

    MaterialTheme() {
        Scaffold(
            bottomBar = {
                BottomNavigation() {
                    BottomNavigationItem(
                        icon = {
                            Icon(vectorResource(id = R.drawable.ic_baseline_add_24))
                        }, selected = false, onClick = {
                            showFragment(
                                navController,
                                R.id.action_global_addFragment
                            )
                        }
                    )
                    BottomNavigationItem(
                        icon = {
                            Icon(vectorResource(id = R.drawable.ic_baseline_list_24))
                        }, selected = true, onClick = {
                            showFragment(
                                navController,
                                R.id.action_global_listFragment
                            )
                        })
                }
            },
            bodyContent = { setList(state) }
        )
    }
}


@Composable
private fun setList(
    state: SnapshotStateList<BodyMeasurement.OxygenMeasurement>
) = LazyColumnFor(state) {
    Row(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(
            text = it.timestamp,
            modifier = Modifier
                .width(300.dp)
        )
        TextField(
            value = it.oxygenLevel.toString(), onValueChange = {},
        )
    }
}