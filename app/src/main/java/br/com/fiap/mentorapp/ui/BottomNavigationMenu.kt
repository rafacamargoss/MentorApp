package br.com.fiap.mentorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.mentorapp.DestinationScreen
import br.com.fiap.mentorapp.R
import br.com.fiap.mentorapp.navigateTo

enum class BottomNavigationItem(val icon: Int, val navDestination: DestinationScreen) {
    CHATLIST(R.drawable.chat_icon, DestinationScreen.ChatList),
    SWIPE(R.drawable.match_icon, DestinationScreen.Swipe),
    PROFILE(R.drawable.profile_icon, DestinationScreen.Profile)
}

@Composable
fun BottomNavigationMenu(selectedItem: BottomNavigationItem, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp, start = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (item in BottomNavigationItem.entries) {
            Card(
                modifier = Modifier
                    .size(60.dp)
                    .fillMaxSize(),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor =
                    if (item == selectedItem)
                        colorResource(
                            id = R.color.background_button_nav
                        )
                    else
                        Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 3.dp)
                            .clickable {
                                navigateTo(navController, item.navDestination.route)
                            },
                        colorFilter = if (item == selectedItem) ColorFilter.tint(Color.White)
                        else ColorFilter.tint(Color.Black)
                    )
                }
            }
        }
    }
}