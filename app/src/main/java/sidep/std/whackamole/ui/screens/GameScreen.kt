package sidep.std.whackamole.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import sidep.std.whackamole.R
import sidep.std.whackamole.game.GameState
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.ui.navigation.Routes
import kotlin.random.Random


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: GameViewModel = viewModel()
) {

//    var gameState by remember { mutableStateOf(GameState()) }
    var timerCount by remember { mutableIntStateOf(0) }
    var localScore by remember { mutableIntStateOf(0) }

    var currentPos by remember { mutableIntStateOf(1) }

    // read state from viewmodel
    val state = viewModel.gameState.value
    StartTimer(state.isActive) { timerCount++ }

    Column(
        modifier = modifier.systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // row { timer + score }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Timer: $timerCount")

            Text("Score: ${localScore}", fontWeight = FontWeight.Bold)
        }
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = modifier.clickable {
                    localScore = 0
                    navController.navigate(Routes.GameOverScreen.route)
                },
                contentScale = ContentScale.FillBounds
            )

            // updates mole position periodically
            RandomizeMolePosition { pos -> currentPos = pos }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3)
            ) {
                items(9) { index ->
                    if (currentPos == index) {
                        Image(
                            painter = painterResource(R.drawable.mole),
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.updateScore(1)
                                localScore++
//                        viewModel.updateScore(1)
                            })
                    }
                }

            }
        }
    }
}


@Composable
fun RandomizeMolePosition(pos: (Int) -> Unit) {
    LaunchedEffect(true) {
        while (true) {
            delay(10000)
            pos(Random.nextInt(9))
        }
    }
}

@Composable
fun StartTimer(isActive: Boolean, onTick: () -> Unit) {
    // fires a coroutine tied to this comp's lifecycle
    LaunchedEffect(isActive) {
        while (isActive) {
            delay(1000)
            onTick()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewGameScreen() {
    GameScreen(Modifier.fillMaxSize(), navController = rememberNavController())
}