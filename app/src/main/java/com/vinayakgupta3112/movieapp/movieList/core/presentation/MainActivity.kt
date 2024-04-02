package com.vinayakgupta3112.movieapp.movieList.core.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vinayakgupta3112.movieapp.movieList.details.presentation.DetailsScreen
import com.vinayakgupta3112.movieapp.movieList.details.presentation.MediaDetailScreen
import com.vinayakgupta3112.movieapp.movieList.details.presentation.MediaDetailsScreenEvents
import com.vinayakgupta3112.movieapp.movieList.details.presentation.MediaDetailsViewModel
import com.vinayakgupta3112.movieapp.movieList.details.presentation.SearchScreen
import com.vinayakgupta3112.movieapp.movieList.details.presentation.SimilarMediaListScreen
import com.vinayakgupta3112.movieapp.movieList.details.presentation.SomethingWentWrong
import com.vinayakgupta3112.movieapp.movieList.details.presentation.WatchVideoScreen
import com.vinayakgupta3112.movieapp.movieList.presentation.MainUiEvents
import com.vinayakgupta3112.movieapp.movieList.presentation.MainUiState
import com.vinayakgupta3112.movieapp.movieList.presentation.MovieListViewModel
import com.vinayakgupta3112.movieapp.movieList.util.Route
import com.vinayakgupta3112.movieapp.movieList.util.Screen
import com.vinayakgupta3112.movieapp.ui.theme.MovieAppTheme
import com.vinayakgupta3112.movieapp.ui.theme.TheMoviesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            MovieAppTheme {
//                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//
//                    NavHost(navController = navController, startDestination = Screen.Home.route) {
//                        composable(Screen.Home.route) {
//                            HomeScreen(navController)
//                        }
//                        composable(
//                            Screen.Details.route + "/{movieId}",
//                            arguments = listOf(
//                                navArgument("movieId") {
//                                    type = NavType.IntType
//                                }
//                            )
//                        ) {
//                            DetailsScreen()
//                        }
//                    }
//                }
//            }
            TheMoviesTheme {

                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainUiState = mainViewModel.mainUiState.collectAsState().value

                installSplashScreen().apply {
                    setKeepOnScreenCondition {
                        mainViewModel.showSplashScreen.value
                    }
                }

                Navigation(
                    mainUiState = mainUiState,
                    onEvent = mainViewModel::onEvent
                )

            }
        }
    }

    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}

@Composable
fun Navigation(
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {
    val navController = rememberNavController()

    val mediaDetailsViewModel = hiltViewModel<MediaDetailsViewModel>()
    val mediaDetailsScreenState =
        mediaDetailsViewModel.mediaDetailsScreenState.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = Route.MEDIA_MAIN_SCREEN
    ) {

        composable(Route.MEDIA_MAIN_SCREEN) {
            MediaMainScreen(
                navController = navController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

        composable(Route.SEARCH_SCREEN) {
            SearchScreen(
                navController = navController,
                mainUiState = mainUiState,
            )
        }

        composable(
            "${Route.MEDIA_DETAILS_SCREEN}?id={id}&type={type}&category={category}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("type") { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType }
            )
        ) {

            val id = it.arguments?.getInt("id") ?: 0
            val type = it.arguments?.getString("type") ?: ""
            val category = it.arguments?.getString("category") ?: ""

            LaunchedEffect(key1 = true) {
                mediaDetailsViewModel.onEvent(
                    MediaDetailsScreenEvents.SetDataAndLoad(
                        moviesGenresList = mainUiState.moviesGenresList,
                        tvGenresList = mainUiState.tvGenresList,
                        id = id,
                        type = type,
                        category = category
                    )
                )
            }

            if (mediaDetailsScreenState.media != null) {
                MediaDetailScreen(
                    navController = navController,
                    media = mediaDetailsScreenState.media,
                    mediaDetailsScreenState = mediaDetailsScreenState,
                    onEvent = mediaDetailsViewModel::onEvent
                )
            } else {
                SomethingWentWrong()
            }
        }

        composable(
            "${Route.SIMILAR_MEDIA_LIST_SCREEN}?title={title}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
            )
        ) {

            val name = it.arguments?.getString("title") ?: ""

            SimilarMediaListScreen(
                navController = navController,
                mediaDetailsScreenState = mediaDetailsScreenState,
                name = name,
            )
        }

        composable(
            "${Route.WATCH_VIDEO_SCREEN}?videoId={videoId}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            )
        ) {

            val videoId = it.arguments?.getString("videoId") ?: ""

            WatchVideoScreen(
                lifecycleOwner = LocalLifecycleOwner.current,
                videoId = videoId
            )
        }
    }
}