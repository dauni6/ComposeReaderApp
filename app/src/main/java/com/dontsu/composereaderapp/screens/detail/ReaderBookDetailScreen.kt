package com.dontsu.composereaderapp.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dontsu.composereaderapp.components.ReaderAppBar
import com.dontsu.composereaderapp.data.Resource
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.navigation.ReaderScreens

@Composable
fun ReaderBookDetailScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: ReaderBookDetailViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.navigate(route = ReaderScreens.ReaderBookSearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()) {
                    value = viewModel.getBookInfo(bookId = bookId)
                }.value

                if (bookInfo.data == null && bookInfo.data?.volumeInfo == null) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.TopCenter)
                        )
                    }
                } else {
//                    Text(text = "Book id: ${bookInfo.data.volumeInfo?.description}")
                    ShowBookDetails(bookInfo = bookInfo, navController = navController)
                }
            }
        }
    }

}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavHostController
) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(
        modifier = Modifier
            .padding(34.dp),
        shape = CircleShape,
        elevation = 4.dp
    ) {
        Image(
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp),
            painter = rememberImagePainter(data = bookData?.imageLinks?.thumbnail),
            contentDescription = null,
            contentScale = ContentScale.Crop // 걍 넣어봄
        )
    }

    Text(
        text = bookData?.title.toString(),
        style = MaterialTheme.typography.h6,
        overflow = TextOverflow.Ellipsis,
        maxLines = 19
    )

    Text(text = "Authors: ${bookData?.authors.toString()}")
    Text(text = "Page Count: ${bookData?.pageCount.toString()}")
    Text(
        text = "Categories: ${bookData?.categories.toString()}",
        style = MaterialTheme.typography.subtitle1
    )
    Text(
        text = "Authors: ${bookData?.authors.toString()}",
        style = MaterialTheme.typography.subtitle1
    )

    Spacer(modifier = Modifier.height(5.dp))

}
