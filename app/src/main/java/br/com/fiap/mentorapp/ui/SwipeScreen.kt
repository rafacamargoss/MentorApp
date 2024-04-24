package br.com.fiap.mentorapp.ui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.mentorapp.CommonImage
import br.com.fiap.mentorapp.CommonProgressSpinner
import br.com.fiap.mentorapp.MAViewModel
import br.com.fiap.mentorapp.R
import br.com.fiap.mentorapp.data.UserData
import br.com.fiap.mentorapp.swipecards.Direction
import br.com.fiap.mentorapp.swipecards.SwipeableCardState
import br.com.fiap.mentorapp.swipecards.rememberSwipeableCardState
import br.com.fiap.mentorapp.swipecards.swipableCard
import kotlinx.coroutines.launch

@Composable
fun SwipeScreen(navController: NavController, vm: MAViewModel) {
    val inProgress = vm.inProgressProfiles.value
    if (inProgress)
        CommonProgressSpinner()
    else {
        val profiles = vm.matchProfiles.value
        val states = profiles.map { it to rememberSwipeableCardState() }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
            ) {
                if (states.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Não há mais perfis disponíveis")
                    }
                } else {
                    states.forEach { (matchProfile, state) ->
                        ProfileCard(
                            modifier = Modifier
                                .fillMaxSize()
                                .swipableCard(
                                    state = state,
                                    blockedDirections = listOf(Direction.Down),
                                    onSwiped = {},
                                    onSwipeCancel = { Log.d("Swipeable card", "Cancelled swipe") }),
                            matchProfile = matchProfile,
                            states = states
                        )
                        LaunchedEffect(matchProfile, state.swipedDirection) {
                            if (state.swipedDirection != null) {
                                if (state.swipedDirection == Direction.Left ||
                                    state.swipedDirection == Direction.Down
                                ) {
                                    vm.onDislike(matchProfile)
                                } else {
                                    vm.onLike(matchProfile)
                                }
                            }
                        }
                    }
                }
            }
            Box {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.SWIPE,
                    navController = navController
                )

            }
        }
    }
}

@Composable
private fun ProfileCard(
    modifier: Modifier,
    matchProfile: UserData,
    states: List<Pair<UserData, SwipeableCardState>>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier) {
        Box {
            CommonImage(
                data = matchProfile.imageUrl,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            )
            Scrim(Modifier.align(Alignment.BottomCenter))
            Column(Modifier.align(Alignment.BottomStart)) {
                Box {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = matchProfile.name ?: matchProfile.username ?: "",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                tint = Color.White,
                                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (expanded) {
                                    "Mostrar mais"
                                } else {
                                    "Mostrar menos"
                                }
                            )
                        }
                    }
                    Row(modifier = Modifier.animateContentSize()) {
                        if (expanded) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp, top = 35.dp)
                                    .background(
                                        Color(0x60000000)
                                    )
                            ) {
                                Text(
                                    text = matchProfile.bio!!,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }
                    }
                }
                ProfileActions(states)
            }
        }
    }
}


@Composable
fun ProfileActions(states: List<Pair<UserData, SwipeableCardState>>) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CircleButton(onClick = {
            scope.launch {
                val last = states.reversed().firstOrNull {
                    it.second.offset.value == Offset(0f, 0f)
                }?.second
                last?.swipe(Direction.Left)
            }
        }, icon = Icons.Rounded.Close)
        CircleButton(onClick = {
            scope.launch {
                val last = states.reversed().firstOrNull {
                    it.second.offset.value == Offset(0f, 0f)
                }?.second
                last?.swipe(Direction.Right)
            }
        }, icon = Icons.Rounded.Favorite)
    }
}

@Composable
fun Scrim(modifier: Modifier = Modifier) {
    Box(
        modifier
            .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
            .height(200.dp)
            .fillMaxWidth()
    )
}


@Composable
private fun CircleButton(
    onClick: () -> Unit,
    icon: ImageVector,
) {
    IconButton(
        modifier = Modifier
            .clip(CircleShape)
            .background(colorResource(id = R.color.background_button_nav))
            .size(56.dp)
            .border(2.dp, colorResource(id = R.color.background_button_nav), CircleShape),
        onClick = onClick
    ) {
        Icon(
            icon, null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}