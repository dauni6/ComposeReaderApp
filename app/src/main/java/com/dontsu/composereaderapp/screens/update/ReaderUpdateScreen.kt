package com.dontsu.composereaderapp.screens.update

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.dontsu.composereaderapp.components.InputField
import com.dontsu.composereaderapp.components.RatingBar
import com.dontsu.composereaderapp.components.ReaderAppBar
import com.dontsu.composereaderapp.components.RoundedButton
import com.dontsu.composereaderapp.data.model.MBook
import com.dontsu.composereaderapp.data.wrapper.DataOrException
import com.dontsu.composereaderapp.screens.home.ReaderHomeScreenViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@ExperimentalComposeUiApi
@Composable
fun ReaderUpdateScreen(
    navController: NavHostController,
    bookItemId: String,
    viewModel: ReaderHomeScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                icon = Icons.Default.ArrowBack,
                showProfile = false,
                navController = navController
            ) {
                navController.popBackStack()
            }
        }
    ) {
        val bookInfo = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
            initialValue = DataOrException(data = emptyList(), true, Exception(""))
        ) {
            value = viewModel.data.value
        }.value

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookInfo.data != null) {
                    if (bookInfo.loading == true) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .align(Alignment.TopCenter)
                            )
                        }
                        bookInfo.loading = false
                    } else {
                        Surface(
                            modifier = Modifier
                                .padding(2.dp)
                                .fillMaxWidth(),
                            shape = CircleShape,
                            elevation = 4.dp
                        ) {
                            ShowingBookUpdate(
                                bookInfo = viewModel.data.value,
                                bookItemId = bookItemId
                            )
                        }

                        val mBook = viewModel.data.value.data?.first { mBook ->
                            mBook.googleBookId == bookItemId
                        }

                        mBook?.let {
                            ShowingSimpleForm(book = mBook, navController)
                        }

                    }
                }
            }
        }

    }

}

@ExperimentalComposeUiApi
@Composable
fun ShowingSimpleForm(
    book: MBook,
    navController: NavHostController
) {
    val notesText = remember { mutableStateOf("") }
    val ratingValue = remember { mutableStateOf(0) }

    SimpleForm(
        defaultValue = book.notes.toString().ifEmpty { "No thoughts available." }
    ) { note ->
        notesText.value = note
    }

    val isStartedReading = remember { mutableStateOf(false) }
    val isFinishedReading = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(
            onClick = {
                isStartedReading.value = true
            },
            enabled = book.startedReading == null
        ) {
            if (book.startedReading == null) {
                if (!isStartedReading.value) {
                    Text(text = "Start Reading")
                } else {
                    Text(
                        modifier = Modifier.alpha(0.8f),
                        text = "Started Reading",
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else {
                Text(text = "Started on: ${book.startedReading}")
            }

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = {
                    isFinishedReading.value = true
                },
                enabled = book.finishedReading == null
            ) {
                if (book.finishedReading == null) {
                    if (!isFinishedReading.value) {
                        Text(text = "Mark as Read")
                    } else {
                        Text(text = "Finished")
                    }
                } else {
                    Text(text = "Finished on: ${book.finishedReading}")
                }
            }
        }
    }

    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))

    book.rating?.toInt()?.let { rating ->
        RatingBar(rating = rating) {
            ratingValue.value = it
        }
    }

    Spacer(modifier = Modifier.padding(bottom = 15.dp))

    Row{
        val changeNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingValue.value
        val isFinishedTimeStamp = if (isFinishedReading.value) Timestamp.now() else book.finishedReading
        val isStartedTimeStamp = if (isStartedReading.value) Timestamp.now() else book.startedReading

        val bookUpdate = changeNotes || changedRating || isFinishedReading.value || isStartedReading.value

        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingValue.value,
            "notes" to notesText.value
        ).toMap()

        RoundedButton(label = "Update") {
            if (bookUpdate) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener { task ->
//                        Log.d("TEST", "SHowSimpleForm : Success => ${task.result.toString()}")
                    }
                    .addOnFailureListener {
                        Log.d("TEST", "ShowSimpleForm : Error => $it")
                    }
            }
        }

        Spacer(modifier = Modifier.width(100.dp))

        RoundedButton(label = "Delete") {

        }
    }

}

@ExperimentalComposeUiApi
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book!",
    onSearch: (String) -> Unit
) {

    Column {
        val textFieldValue = rememberSaveable { mutableStateOf(defaultValue) }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(key1 = textFieldValue.value) {
            mutableStateOf(
                textFieldValue.value.trim().isNotEmpty()
            )
        }

        InputField(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(3.dp)
                .background(color = Color.White, shape = CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            valueState = textFieldValue,
            labelId = "Enter Your thoughts",
            enabled = true,
            keyboardActions = KeyboardActions {
                if (!valid.value) return@KeyboardActions
                onSearch(textFieldValue.value.trim())
                keyboardController?.hide()
            }
        )

    }

}

@Composable
fun ShowingBookUpdate(
    bookInfo: DataOrException<List<MBook>, Boolean, Exception>,
    bookItemId: String
) {
    Row {
        Spacer(modifier = Modifier.width(43.dp))
        if (bookInfo.data != null) {
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                val mBook = bookInfo.data!!.first { mBook ->
                    mBook.googleBookId == bookItemId
                }
                CardListItem(book = mBook) {

                }
            }
        }
    }
}

@Composable
fun CardListItem(
    book: MBook,
    onPressDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { },
        elevation = 8.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .height(100.dp)
                    .padding(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 120.dp,
                            topEnd = 20.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    ),
                painter = rememberImagePainter(data = book.photoUrl),
                contentDescription = null
            )

            Column {
                Text(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    text = book.title.toString(),
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 2.dp,
                        bottom = 0.dp
                    ),
                    text = book.authors.toString(),
                    style = MaterialTheme.typography.body2
                )

                Text(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 8.dp
                    ),
                    text = book.publishedDate.toString(),
                    style = MaterialTheme.typography.body2
                )

            }
        }

    }
}
