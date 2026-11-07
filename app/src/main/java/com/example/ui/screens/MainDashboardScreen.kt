package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.McKaysViewModel
import com.example.ui.theme.RoyalBlue500
import com.example.ui.theme.RoyalBlue900

@Composable
fun MainDashboardScreen(
    viewModel: McKaysViewModel,
    onLogout: () -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentUser by viewModel.currentUser.collectAsState()
    val isDark = isSystemInDarkTheme()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen contents
            when (selectedIndex) {
                0 -> HomeTab(viewModel = viewModel, onNavigateTab = { selectedIndex = it })
                1 -> SalesTab(viewModel = viewModel)
                2 -> OrderTab(viewModel = viewModel)
                3 -> MemberTab(viewModel = viewModel)
                4 -> ProfileTab(viewModel = viewModel, onLogout = onLogout)
            }

            // Floating One UI Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    color = if (isDark) Color(0xFF1E293B).copy(alpha = 0.92f) else Color.White.copy(alpha = 0.94f),
                    shadowElevation = 14.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isDark) Color.White.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.08f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("floating_bottom_bar")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OneUINavItem(
                            index = 0,
                            currentIndex = selectedIndex,
                            icon = Icons.Default.Dashboard,
                            label = "Home",
                            onSelect = { selectedIndex = 0 }
                        )
                        OneUINavItem(
                            index = 1,
                            currentIndex = selectedIndex,
                            icon = Icons.Default.PointOfSale,
                            label = "Kasir",
                            onSelect = { selectedIndex = 1 }
                        )
                        OneUINavItem(
                            index = 2,
                            currentIndex = selectedIndex,
                            icon = Icons.Default.LocalShipping,
                            label = "Order",
                            onSelect = { selectedIndex = 2 }
                        )
                        OneUINavItem(
                            index = 3,
                            currentIndex = selectedIndex,
                            icon = Icons.Default.CardMembership,
                            label = "Member",
                            onSelect = { selectedIndex = 3 }
                        )
                        OneUINavItem(
                            index = 4,
                            currentIndex = selectedIndex,
                            icon = Icons.Default.Person,
                            label = "Akun",
                            onSelect = { selectedIndex = 4 }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OneUINavItem(
    index: Int,
    currentIndex: Int,
    icon: ImageVector,
    label: String,
    onSelect: () -> Unit
) {
    val isSelected = currentIndex == index
    val pillBgColor = if (isSelected) Color(0xFF1E3A8A).copy(alpha = 0.16f) else Color.Transparent
    val contentColor = if (isSelected) RoyalBlue500 else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.65f)

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(pillBgColor)
            .clickable { onSelect() }
            .padding(horizontal = if (isSelected) 14.dp else 10.dp, vertical = 8.dp)
            .testTag("nav_item_$index"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(22.dp)
        )
        if (isSelected) {
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}
