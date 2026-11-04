package kr.playax.novel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LibraryBooks
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kr.playax.novel.ui.library.LibraryScreen
import kr.playax.novel.ui.reader.ReaderScreen
import kr.playax.novel.ui.settings.SettingsScreen
import kr.playax.novel.ui.theme.PlayAXNovelTheme
import kr.playax.novel.ui.write.WriteScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = viewModel(factory = AppViewModel.factory(applicationContext))
            PlayAXNovelTheme {
                val navController = rememberNavController()
                val backStack by navController.currentBackStackEntryAsState()
                val route = backStack?.destination?.route
                val hideBottomBar = route?.startsWith("reader/") == true

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (!hideBottomBar) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = route == "write",
                                    onClick = { navController.navigate("write") { launchSingleTop = true } },
                                    icon = { Icon(Icons.Outlined.EditNote, contentDescription = null) },
                                    label = { Text(stringResource(R.string.nav_write)) },
                                )
                                NavigationBarItem(
                                    selected = route == "library" || route?.startsWith("reader/") == true,
                                    onClick = { navController.navigate("library") { launchSingleTop = true } },
                                    icon = { Icon(Icons.Outlined.LibraryBooks, contentDescription = null) },
                                    label = { Text(stringResource(R.string.nav_library)) },
                                )
                                NavigationBarItem(
                                    selected = route == "settings",
                                    onClick = { navController.navigate("settings") { launchSingleTop = true } },
                                    icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                                    label = { Text(stringResource(R.string.nav_settings)) },
                                )
                            }
                        }
                    },
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "write",
                        modifier = Modifier.padding(padding),
                    ) {
                        composable("write") { WriteScreen(appViewModel) }
                        composable("library") {
                            LibraryScreen(
                                appViewModel = appViewModel,
                                onOpenChapter = { slug, vol, ch ->
                                    navController.navigate("reader/$slug/$vol/$ch")
                                },
                            )
                        }
                        composable("settings") { SettingsScreen(appViewModel) }
                        composable("reader/{slug}/{vol}/{ch}") { entry ->
                            val slug = entry.arguments?.getString("slug").orEmpty()
                            val vol = entry.arguments?.getString("vol")?.toIntOrNull() ?: 1
                            val ch = entry.arguments?.getString("ch")?.toIntOrNull() ?: 1
                            ReaderScreen(
                                appViewModel = appViewModel,
                                workSlug = slug,
                                volumeIndex = vol,
                                chapterIndex = ch,
                                onBack = { navController.popBackStack() },
                            )
                        }
                    }
                }
            }
        }
    }
}
