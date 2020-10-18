package com.pratclot.oxygentracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.pratclot.oxygentracker.R
import com.pratclot.oxygentracker.viewmodels.OxygenActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class AddFragment : Fragment() {
    private val viewModel: OxygenActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                SetContent(viewModel, findNavController())
            }
        }
    }
}


@Composable
private fun SetContent(viewModel: OxygenActivityViewModel, navController: NavController) {
    val context = ContextAmbient.current
    val navController = navController
    var inputText = stringResource(id = R.string.oxygenActivityPutSpo2InputText)
    var state = remember { mutableStateOf(String()) }

    MaterialTheme() {
        Scaffold(
            bodyContent = {
                Row(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.value,
                        onValueChange = { state.value = it },
                        label = { Text(text = stringResource(id = R.string.oxygenActivityPutSpo2Label)) },
                        placeholder = { Text(text = inputText) },
                        modifier = Modifier,
                        keyboardType = KeyboardType.Number,
                        isErrorValue = state.value.toIntOrNull() == null,
                    )
                    Button(
                        enabled = state.value.toIntOrNull() != null,
                        onClick = {
                            GlobalScope.launch {
                                onAddMeasurement(
                                    viewModel,
                                    state,
                                    context
                                )

                                state.value = ""
                            }
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .fillMaxWidth()
                            .wrapContentWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.oxygenActivityPutSpo2ButtonText),
                            maxLines = 1
                        )
                    }
                }
            },
            bottomBar = {
                BottomNavigation() {
                    BottomNavigationItem(
                        icon = {
                            Icon(vectorResource(id = R.drawable.ic_baseline_add_24))
                        }, selected = true, onClick = {
                            showFragment(
                                navController,
                                R.id.action_global_addFragment
                            )
                        }
                    )
                    BottomNavigationItem(
                        icon = {
                            Icon(vectorResource(id = R.drawable.ic_baseline_list_24))
                        }, selected = false, onClick = {
                            showFragment(
                                navController,
                                R.id.action_global_listFragment
                            )
                        }
                    )
                }
            }
        )
    }
}

private suspend fun onAddMeasurement(
    viewModel: OxygenActivityViewModel,
    state: MutableState<String>,
    context: Context
) = coroutineScope {
    val data = async(Dispatchers.IO) {
        viewModel.addMeasurement(state.value)
    }

    withContext(Dispatchers.Main) {
        val id = data.await()
        showToast(context, id)
    }
}

private fun showToast(context: Context, id: Long) {
    val text = when (id) {
        -1L -> context.getString(R.string.oxygenActivityPutSpo2ToastFailure)
        else -> context.getString(R.string.oxygenActivityPutSpo2ToastSuccess)
    }
    Toast.makeText(
        context,
        text,
        Toast.LENGTH_SHORT
    ).show()
}
