package ru.kabirov.iporganisationselector.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.kabirov.iporganisationselector.R
import ru.kabirov.iporganisationselector.navigation.NavClass
import ru.kabirov.iporganisationselector.presentation.ui.searcher.Searcher
import ru.kabirov.iporganisationselector.presentation.viewmodel.MainViewModel
import ru.kabirov.organisation_info.presentation.ui.OrganisationInfoScreen
import ru.kabirov.searcherbyip.presentation.ui.SubnetScreen
import ru.kabirov.sercherbyorg.presentation.ui.OrganisationsScreen

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    val navigator by viewModel.navigator.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    LaunchedEffect(navigator) {
        navController.navigate(navigator) {
            popUpTo(0) {
                inclusive = true
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val searchButtonContentDescription = viewModel.getResourceString(R.string.search_btn)
        val backButtonContentDescription = viewModel.getResourceString(R.string.back_button)
        val placeholderText = viewModel.getResourceString(R.string.search_placeholder)

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Searcher(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearchClick = {
                viewModel.onSearchClick()
                focusManager.clearFocus()
            },
            hasBackBtn = currentDestination?.hierarchy?.any { it.hasRoute(NavClass.OrganisationInfo::class) } == true,
            onBackBtnClick = {
                navController.popBackStack()
                focusManager.clearFocus()
            },
            searchButtonContentDescription = searchButtonContentDescription,
            backButtonContentDescription = backButtonContentDescription,
            placeholderText = placeholderText,
        )
        NavHost(navController = navController, startDestination = NavClass.Empty) {
            val modifier: Modifier = Modifier.weight(1f)
            composable<NavClass.Empty> {

            }
            composable<NavClass.Organisation> { backStackEntry ->
                OrganisationsScreen(
                    query = backStackEntry.toRoute<NavClass.Organisation>().query,
                    onOrganisationClick = { orgId ->
                        navController.navigate(
                            NavClass.OrganisationInfo(
                                orgId
                            )
                        )
                        focusManager.clearFocus()
                    },
                    modifier = modifier,
                )
            }
            composable<NavClass.Subnet> { backStackEntry ->
                SubnetScreen(
                    ipAddress = backStackEntry.toRoute<NavClass.Subnet>().ipAddress,
                    onSubnetClick = { orgId ->
                        navController.navigate(
                            NavClass.OrganisationInfo(
                                orgId
                            )
                        )
                        focusManager.clearFocus()
                    },
                    modifier = modifier,
                )
            }
            composable<NavClass.OrganisationInfo> { backStackEntry ->
                OrganisationInfoScreen(
                    orgId = backStackEntry.toRoute<NavClass.OrganisationInfo>().orgId,
                    modifier = modifier,
                )
            }
        }
    }
}