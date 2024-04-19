package br.com.fiap.mentorapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.mentorapp.CommonDivider
import br.com.fiap.mentorapp.CommonImage
import br.com.fiap.mentorapp.CommonProgressSpinner
import br.com.fiap.mentorapp.DestinationScreen
import br.com.fiap.mentorapp.MAViewModel
import br.com.fiap.mentorapp.R
import br.com.fiap.mentorapp.navigateTo
import br.com.fiap.mentorapp.ui.components.OutlineSelectField
import br.com.fiap.mentorapp.ui.components.SelectOptionsType
import br.com.fiap.mentorapp.ui.theme.OpenSansBold
import br.com.fiap.mentorapp.ui.theme.OpenSansRegular

enum class Gender {
    MENTOR, MENTEE, ANY
}

@Composable
fun ProfileScreen(navController: NavController, vm: MAViewModel) {
    val inProgress = vm.inProgress.value
    if (inProgress)
        CommonProgressSpinner()
    else {
        val userData = vm.userData.value
        val g = if (userData?.gender.isNullOrEmpty()) "MENTOR"
            else userData!!.gender!!.uppercase()
        val gPref = if (userData?.genderPreference.isNullOrEmpty()) "MENTEE"
            else userData!!.genderPreference!!.uppercase()
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }
        var gender by rememberSaveable { mutableStateOf(Gender.valueOf(g)) }
        var genderPreference by rememberSaveable {mutableStateOf(Gender.valueOf(gPref)) }

        val scrollState = rememberScrollState()
        
        Column {
            ProfileHeader(
                vm = vm,
                onSave = {
                    vm.updateProfileData(name, username, bio, gender, genderPreference)
                },
                onBack = { navigateTo(navController, DestinationScreen.Swipe.route) },
            )

            ProfileForm(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(8.dp),
                vm = vm,
                name = name,
                username = username,
                bio = bio,
                gender = gender,
                genderPreference = genderPreference,
                onNameChange = { name = it },
                onUsernameChange = { username = it },
                onBioChange = { bio = it },
                onGenderChange = { gender = it },
                onGenderPreferenceChange = { genderPreference = it },
                onLogout = {
                    vm.onLogout()
                    navigateTo(navController, DestinationScreen.Login.route)
                },
            )

            BottomNavigationMenu(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        }
    }
}

@Composable
fun ProfileHeader(
    vm: MAViewModel,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    val imageUrl = vm.userData.value?.imageUrl

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Voltar", modifier = Modifier.clickable { onBack.invoke() })
        Text(text = "Salvar", modifier = Modifier.clickable { onSave.invoke() })
    }

    CommonDivider()

    ProfileImage(imageUrl = imageUrl, vm = vm)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileForm(
    modifier: Modifier,
    vm: MAViewModel,
    name: String,
    username: String,
    bio: String,
    gender: Gender,
    genderPreference: Gender,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onGenderChange: (Gender) -> Unit,
    onGenderPreferenceChange: (Gender) -> Unit,
    onLogout: () -> Unit
) {
    val genderOptions = listOf(
        SelectOptionsType(label = "Mentor", value = "MENTOR"),
        SelectOptionsType(label = "Mentorado", value = "MENTEE")
    )

    val genderPreferenceOptions = listOf(
        SelectOptionsType(label = "Mentores", value = "MENTOR"),
        SelectOptionsType(label = "Mentorados", value = "MENTEE"),
        SelectOptionsType(label = "Todas as opções", value = "ANY")
    )

    Column(modifier = modifier) {
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nome",
                    fontFamily = OpenSansRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(2.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Nome",
                            fontFamily = OpenSansRegular,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.primary_75)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.primary_50),
                        focusedBorderColor = colorResource(id = R.color.primary_100)
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Nome de usuário",
                    fontFamily = OpenSansRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(2.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Nome",
                            fontFamily = OpenSansRegular,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.primary_75)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.primary_50),
                        focusedBorderColor = colorResource(id = R.color.primary_100)
                    ),
                    shape = RoundedCornerShape(12.dp),
                )
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Eu sou:",
                    fontFamily = OpenSansRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(2.dp)
                )
                val selectedItem = genderOptions.find{ item -> item.value === gender.name }
                OutlineSelectField(
                    items = genderOptions,
                    selectedItem = selectedItem!!.label,
                    onSelectedItemChange = { onGenderChange(Gender.valueOf(it)) },
                    label = ""
                )
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Estou procurando por:",
                    fontFamily = OpenSansRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(2.dp)
                )
                val selectedItem = genderPreferenceOptions.find{ item -> item.value === genderPreference.name }
                OutlineSelectField(
                    items = genderPreferenceOptions,
                    selectedItem = selectedItem!!.label,
                    onSelectedItemChange = { onGenderPreferenceChange(Gender.valueOf(it)) },
                    label = ""
                )
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Bio",
                    fontFamily = OpenSansRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(2.dp)
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = onBioChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    placeholder = {
                        Text(
                            text = "Sobre mim...",
                            fontFamily = OpenSansRegular,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            color = colorResource(id = R.color.primary_25)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = colorResource(id = R.color.primary_50),
                        focusedBorderColor = colorResource(id = R.color.primary_100)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = false
                )
            }
        }

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = { onLogout.invoke() },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 0.5.dp, color = colorResource(id = R.color.primary_100))
            ) {
                Text(
                    text = "Logout",
                    fontWeight = FontWeight.Bold,
                    fontFamily = OpenSansBold,
                    color = colorResource(id = R.color.primary_100),
                    fontSize = 13.sp
                )
            }
            ClickableText(text = AnnotatedString(text = "Logout"), onClick = { onLogout.invoke() })

        }

    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: MAViewModel) {

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) {
            uri: Uri? ->
            uri?.let { vm.uploadProfileImage(uri) }
        }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Alterar a imagem de perfil")
        }
        val isLoading = vm.inProgress.value
        if (isLoading)
            CommonProgressSpinner()
    }
}
