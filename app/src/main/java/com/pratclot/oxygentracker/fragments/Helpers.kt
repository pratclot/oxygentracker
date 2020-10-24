package com.pratclot.oxygentracker.fragments

import androidx.compose.foundation.Icon
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.pratclot.oxygentracker.R

fun showFragment(navController: NavController, resId: Int) = navController.navigate(resId)

@Composable
fun setupBottomBar(navController: NavController) {
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
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(vectorResource(id = R.drawable.ic_baseline_bar_chart_24))
            }, selected = false, onClick = {
                showFragment(
                    navController,
                    R.id.action_global_fragmentChart
                )
            }
        )
    }
}