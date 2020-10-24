package com.pratclot.oxygentracker.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.pratclot.oxygentracker.R
import com.pratclot.oxygentracker.fragments.showFragment
import com.pratclot.oxygentracker.viewmodels.OxygenActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OxygenActivity : AppCompatActivity() {
    private val viewModel: OxygenActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.oxygen_activity)
//        setContent { SetContent() }
    }

}
//
//@Preview
//@Composable
//private fun SetContentPreview() {
//    SetContent()
//}

@Composable
private fun SetContent(navController: NavController) {

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
                            )})
                    BottomNavigationItem(
                        icon = {
                            Icon(vectorResource(id = R.drawable.ic_baseline_list_24))
                        }, selected = false, onClick = {})
                    BottomNavigationItem(
                        icon = {
                            Icon(vectorResource(id = R.drawable.ic_baseline_bar_chart_24))
                        }, selected = false, onClick = {})
                }
            }
        ) {

        }
    }
}