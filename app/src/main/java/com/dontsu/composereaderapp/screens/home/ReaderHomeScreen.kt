package com.dontsu.composereaderapp.screens.home

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dontsu.composereaderapp.components.*
import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderHomeScreen(
    navController: NavController,
    viewModel: ReaderHomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "A.Reader",
                showProfile = true,
                navController = navController
            )
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(route = ReaderScreens.ReaderBookSearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HoneContent(navController = navController, viewModel = viewModel)
        }
    }

}


@Composable
fun HoneContent(
    navController: NavController,
    viewModel: ReaderHomeScreenViewModel
) {
    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
    }

    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName = if (!email.isNullOrEmpty()) email.substringBefore("@") else "No name"

    Column(
        modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.Start)
                .border(width = 1.dp, color = Color(0xFF92CBDF), shape = RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleSection(label = "Your reading \n activity right now...")

            Column {
                Icon(
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            navController.navigate(route = ReaderScreens.ReaderStatsScreen.name)
                        },
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    tint = Color(0xFF92CBDF)
                )

                Text(
                    modifier = Modifier
                        .padding(2.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    text = currentUserName,
                    style = MaterialTheme.typography.overline,
                    color = Color.Black,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
        }
        ReadingRightNowArea(listOfBooks = listOfBooks, navController = navController)

        TitleSection(label = "Reading List")

        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }
}

@Composable
fun ReadingRightNowArea(
    listOfBooks: List<MBook>,
    navController: NavController
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {
        listOfBooks.forEach { book ->
            ListCard(mBook = book) { title ->

            }
        }
    }
}

@Composable
fun BookListArea(
    listOfBooks: List<MBook>,
    navController: NavController
) {
    HorizontalScrollableComponent(listOfBooks) { googleBookId ->
       navController.navigate(route = ReaderScreens.ReaderUpdateScreen.name + "/${googleBookId}")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    onCardPressed: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {
        listOfBooks.forEach { book ->
            ListCard(mBook = book) {
                onCardPressed(book.googleBookId.toString())
            }
        }
    }
}

