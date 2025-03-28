package sidep.std.whackamole.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import sidep.std.whackamole.R
import sidep.std.whackamole.game.Difficulty
import sidep.std.whackamole.game.GameConfig
import sidep.std.whackamole.game.GameViewModel
import sidep.std.whackamole.game.Plain
import sidep.std.whackamole.ui.navigation.Routes

@Composable
fun StartScreen(
    modifier: Modifier = Modifier, navController: NavController, viewModel: GameViewModel
) {
    var selectedDifficulty by remember { mutableStateOf(Difficulty.NOOB) }
    var selectedPlain by remember { mutableStateOf(Plain.SMALL) }

    var showNameDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Image(painter = painterResource(R.drawable.mole), contentDescription = null)

        Text("Whack A Mole", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        Text("Difficulty Level")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Difficulty.entries.forEach { difficulty ->
                FilterChip(selected = selectedDifficulty == difficulty, onClick = {
                    selectedDifficulty = difficulty
                }, label = { Text(difficulty.title) })
            }
        }

        Text("Plain size")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Plain.entries.forEach { plain ->
                FilterChip(selected = selectedPlain == plain, onClick = {
                    selectedPlain = plain
                }, label = { Text(plain.title) })
            }
        }

        Button(onClick = {
            showNameDialog = true
        }) { Text("Start Game") }
    }

    // show name dialog
    if (showNameDialog) {
        NameDialog(viewModel = viewModel, onDismiss = { showNameDialog = false }) { playerName ->
            showNameDialog = false
            viewModel.gameConfig =
                GameConfig(difficultyLevel = selectedDifficulty, plain = selectedPlain)
            viewModel.initGame(playerName)
            navController.navigate(Routes.GameScreen.route)
        }
    }
}

@Composable
fun NameDialog(
    viewModel: GameViewModel,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var isNameError by remember { mutableStateOf(false) }
    val players by viewModel.players.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .scrollable(state = scrollState, orientation = Orientation.Vertical),
            shape = RoundedCornerShape(5)
        ) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                Text("Who's playing?", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(value = name,
                    onValueChange = { changedText -> name = changedText },
                    placeholder = { Text("Player name") },
                    isError = isNameError,
                    supportingText = {
                        if (isNameError) {
                            Text("Enter player name")
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                if (players.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select player", fontWeight = FontWeight.Bold)
                        IconButton(onClick = {
                            viewModel.deletePlayers()
                        }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                        }
                    }
                } else {
                    Text("No players in list")

                }
                LazyColumn {
                    items(players) { player ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(player.playerName)
                            TextButton(onClick = { name = player.playerName }) {
                                Text("Select")
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        if (name.isNotEmpty()) {
                            onConfirm(name)
                            isNameError = false
                        } else {
                            isNameError = true
                        }
                    },
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                ) { Text("Start game") }
            }
        }
    }
}
