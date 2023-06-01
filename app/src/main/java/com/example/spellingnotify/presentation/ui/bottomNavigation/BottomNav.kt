package com.example.spellingnotify.presentation.ui.bottomNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.spellingnotify.navigation.Screens
import com.example.spellingnotify.presentation.ui.SharedViewModel
import com.example.spellingnotify.presentation.ui.theme.GrayLighter

@Composable
fun BottomBar(
    navController: NavHostController,
    viewModel: SharedViewModel
) {

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Box(contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Screens.all.forEachIndexed() {  index, screen ->
                if (index == 1) return@forEachIndexed
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
        AddItem(
            screen = Screens.all[1],
            currentDestination = currentDestination,
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun AddItem(
    screen: Screens,
    currentDestination: NavDestination?,
    navController: NavHostController,
    viewModel: SharedViewModel
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val background =
        if (selected) MaterialTheme.colorScheme.onPrimary else GrayLighter
    val contentColor =
        if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable {
                viewModel.onTopBarTitleChanged(screen.title)
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector =  screen.icon,
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = screen.title,
                    color = contentColor,
                    fontSize = 18.sp
                )
            }
        }
    }

}

