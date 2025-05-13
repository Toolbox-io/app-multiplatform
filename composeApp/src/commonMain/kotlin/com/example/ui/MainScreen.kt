package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import com.pr0gramm3r101.utils.NavigationItem
import com.pr0gramm3r101.utils.SimpleNavigationBar
import com.pr0gramm3r101.utils.SimpleNavigationRail
import com.pr0gramm3r101.utils.WindowWidthSizeClass
import com.pr0gramm3r101.utils.currentWindowAdaptiveInfo
import com.pr0gramm3r101.utils.invoke
import com.pr0gramm3r101.utils.left
import com.pr0gramm3r101.utils.link
import com.pr0gramm3r101.utils.resetFocus
import com.pr0gramm3r101.utils.right
import com.example.Res
import com.example.home
import com.example.settings
import com.example.ui.tabs.HomeTab
import com.example.ui.tabs.SettingsTab

@Composable
internal inline fun MainScreen() {
    with (LocalWindowInsetsScope()) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (pager, nav) = createRefs()

            var selectedItem by remember { mutableIntStateOf(0) }
            val pagerState = rememberPagerState { 2 }
            val coroutineScope = rememberCoroutineScope()

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            LaunchedEffect(pagerState.currentPage) {
                if (pagerState.currentPage == pagerState.targetPage) {
                    if (selectedItem != pagerState.currentPage)
                        selectedItem = pagerState.currentPage
                    resetFocus(keyboardController, focusManager)
                }
            }

            val pages: @Composable PagerScope.(Int) -> Unit = { page ->
                when (page) {
                    0 -> HomeTab()
                    1 -> SettingsTab()
                }
            }

            val onClick: (Int) -> (() -> Unit) = {
                {
                    selectedItem = it
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            }

            val navItems = arrayOf(
                NavigationItem(
                    name = stringResource(Res.string.home),
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    onClick = onClick(0)
                ),
                NavigationItem(
                    name = stringResource(Res.string.settings),
                    selectedIcon = Icons.Filled.Settings,
                    unselectedIcon = Icons.Outlined.Settings,
                    onClick = onClick(1)
                )
            )

            if (
                currentWindowAdaptiveInfo().windowSizeClass.let {
                    it.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED ||
                            it.windowWidthSizeClass == WindowWidthSizeClass.MEDIUM
                }
            ) {
                VerticalPager(
                    state = pagerState,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .constrainAs(pager) {
                            top link parent.top
                            bottom link parent.bottom
                            left link nav.right
                            right link parent.right
                            height = Dimension.fillToConstraints
                            width = Dimension.fillToConstraints
                        },
                    pageContent = pages,
                    userScrollEnabled = false
                )
                SimpleNavigationRail(
                    selectedItem = selectedItem,
                    modifier = Modifier.constrainAs(nav) {
                        bottom link parent.bottom
                        top link parent.top
                        left link parent.left
                    },
                    items = navItems
                )
            } else {
                HorizontalPager(
                    state = pagerState,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .constrainAs(pager) {
                            top link parent.top
                            bottom link nav.top
                            left link parent.left
                            right link parent.right
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        },
                    pageContent = pages
                )
                SimpleNavigationBar(
                    selectedItem = selectedItem,
                    modifier = Modifier.constrainAs(nav) {
                        bottom link parent.bottom
                    },
                    items = navItems
                )
            }
        }
    }
}