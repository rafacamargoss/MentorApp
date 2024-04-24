package br.com.fiap.mentorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.mentorapp.CommonImage
import br.com.fiap.mentorapp.CommonProgressSpinner
import br.com.fiap.mentorapp.DestinationScreen
import br.com.fiap.mentorapp.MAViewModel
import br.com.fiap.mentorapp.navigateTo
import org.checkerframework.common.subtyping.qual.Bottom

@Composable
fun ChatListScreen(navController: NavController, vm: MAViewModel) {
    val inProgress = vm.inProgressChats.value
    if (inProgress)
        CommonProgressSpinner()
    else {
        val chats = vm.chats.value
        val userData = vm.userData.value

        Column(modifier = Modifier.fillMaxSize()) {

            if (chats.isEmpty())
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Sem chats disponíveis...")
                }
            else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(chats) { chat ->
                        val chatUser = if (chat.user1.userId == userData?.userId) chat.user2
                        else chat.user1
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(75.dp)
                                .clickable {
                                    chat.chatId?.let {
                                        navigateTo(
                                            navController,
                                            DestinationScreen.SingleChat.createRoute(it)
                                        )
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CommonImage(
                                data = chatUser.imageUrl,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                            )
                            Text(
                                text = chatUser.name ?: "---",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.CHATLIST,
                navController = navController
            )
        }
    }
}