package com.dontsu.composereaderapp.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.dontsu.composereaderapp.components.InputField
import com.dontsu.composereaderapp.components.ReaderAppBar
import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.navigation.ReaderScreens

@ExperimentalComposeUiApi
@Composable
fun ReaderSearchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false
            ) {
                navController.navigate(route = ReaderScreens.ReaderHomeScreen.name)
            }
        }
    ) {
        Surface {
            Column {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { query ->
                    Log.d("TEST", query)
                }

                Spacer(modifier = Modifier.height(13.dp))

                BookList(navController)

            }
        }
    }

}

@Composable
fun BookList(navController: NavController) {


    val listOfBooks = listOf(
        MBook(
            id = "dads",
            title = "Hello Again",
            authors = "All of us",
            notes = null
        ),MBook(
            id = "dads",
            title = "Hello Again",
            authors = "All of us",
            notes = null
        ),MBook(
            id = "dads",
            title = "Hello Again",
            authors = "All of us",
            notes = null
        ),MBook(
            id = "dads",
            title = "Hello Again",
            authors = "All of us",
            notes = null
        ),MBook(
            id = "dads",
            title = "Hello Again",
            authors = "All of us",
            notes = null
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(listOfBooks) { mBook ->
            BookRow(mBook = mBook, navController = navController)
        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
   Column {
       val searchQueryState = rememberSaveable { mutableStateOf("") }
       val keyboardController = LocalSoftwareKeyboardController.current
       val valid = remember(searchQueryState.value) { searchQueryState.value.trim().isNotEmpty() }

       InputField(
           valueState = searchQueryState,
           labelId = "Search",
           enabled = true,
           keyboardActions = KeyboardActions {
               if (!valid) return@KeyboardActions
               onSearch(searchQueryState.value.trim())
               searchQueryState.value = ""
               keyboardController?.hide()
           }
       )
   }
}



@Composable
fun BookRow(
    mBook: MBook,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(3.dp)
            .clickable {
                // do something
            },
        shape = RectangleShape,
        elevation = 7.dp
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
                    .padding(4.dp),
                painter = rememberImagePainter(data = "http://books.google.com/books/content?id=ImnyDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"),
                contentDescription = null
            )

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = mBook.title.toString(),
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "Authors: ${mBook.authors.toString()}",
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                )

                Text(
                    text = "Date: 2022-11-09",
                    style = TextStyle(
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                )

                Text(
                    text = "[Computer]"
                )
            }
        }
    }

}
