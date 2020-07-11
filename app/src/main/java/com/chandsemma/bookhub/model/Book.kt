package com.chandsemma.bookhub.model

data class Book(
    val bookId: String,
    val bookName: String,
    val bookAuthor: String,
    val bookRating: String,
    val bookPrice: String,
    val bookImage: String) { //becoz server sends link to the image, as a string
                // Int) {   //bookImage is int since it will hold the id of the image we will place in the resources directory
}