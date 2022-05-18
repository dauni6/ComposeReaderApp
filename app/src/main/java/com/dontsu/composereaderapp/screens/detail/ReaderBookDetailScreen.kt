package com.dontsu.composereaderapp.screens.detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dontsu.composereaderapp.components.ReaderAppBar
import com.dontsu.composereaderapp.components.RoundedButton
import com.dontsu.composereaderapp.data.wrapper.Resource
import com.dontsu.composereaderapp.data.model.Item
import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.navigation.ReaderScreens
import com.google.firebase.firestore.FirebaseFirestore

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
    val context = LocalContext.current

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
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        style = MaterialTheme.typography.subtitle1
    )
    Text(
        text = "Authors: ${bookData?.authors.toString()}",
        style = MaterialTheme.typography.subtitle1
    )

    Spacer(modifier = Modifier.height(5.dp))

    val cleanDescription = HtmlCompat.fromHtml(bookData?.description.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    val localDims = LocalContext.current.resources.displayMetrics
    Surface(
        modifier = Modifier
            .height(localDims.heightPixels.dp.times(0.09f))
            .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(width = 1.dp, color = Color.DarkGray)
    ) {
        LazyColumn(
            modifier = Modifier.padding(3.dp)
        ) {
            item {
                Text(text = cleanDescription)
            }
        }
    }

    // Buttons
    Row(
        modifier = Modifier.padding(top = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround // SpaceAround는 어떤 효과가 있는 걸까?
    ) {
        RoundedButton(
            label = "Save"
        ) {
            val book = bookData?.toMBook(googleBookId)
            if (book != null) {
                saveToFirebaseStore(book = book, navController = navController)
            } else {
                Toast.makeText(context, "저장할 수 없습니다." , Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.width(25.dp))

        RoundedButton(
            label = "Cancel"
        ) {
            navController.popBackStack()
        }

    }

}

fun saveToFirebaseStore(
    book: MBook,
    navController: NavController
) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if (book.toString().isNotEmpty()) {
        dbCollection.add(book)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection
                    .document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.popBackStack()
                        }
                    }
                    .addOnFailureListener {
                        Log.e("Firebase", "saveToFirebaseStore : adding a book Error occurred.  ", it)
                    }
            }
    } else {

    }

}
