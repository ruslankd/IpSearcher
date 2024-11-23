package ru.kabirov.iporganisationselector.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.kabirov.iporganisationselector.presentation.viewmodel.MainViewModel
import ru.kabirov.iporganisationselector.navigation.NavClass
import ru.kabirov.organisation_info.presentation.ui.OrganisationInfoScreen
import ru.kabirov.searcherbyip.presentation.ui.SubnetScreen
import ru.kabirov.sercherbyorg.presentation.ui.OrganisationsScreen

@SuppressLint("RestrictedApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    val navigator by viewModel.navigator.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    LaunchedEffect(navigator) {
        if (navigator is NavClass.Organisation) {
            navController.navigate(navigator) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        } else if (navigator is NavClass.Subnet) {
            navController.navigate(navigator) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        Searcher(
            modifier = Modifier.fillMaxWidth(),
            query = query,
            onQueryChange = viewModel::onQueryChange,
            onSearchClick = viewModel::onSearchClick,
            hasBackBtn = currentDestination?.hierarchy?.any { it.hasRoute(NavClass.OrganisationInfo::class) } == true,
            onBackBtnClick = {
                navController.popBackStack()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
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