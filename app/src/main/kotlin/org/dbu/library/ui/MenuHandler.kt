
package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {
    return when (choice) {
        "1" -> {
            addBook(service)
            true
        }
        "2" -> {
            registerPatron(repository)
            true
        }
        "3" -> {
            borrowBook(service)
            true
        }
        "4" -> {
            returnBook(service)
            true
        }
        "5" -> {
            search(service)
            true
        }
        "6" -> {
            listAllBooks(repository)
            true
        }
        "0" -> false
        else -> {
            println("Invalid option")
            true
        }
    }
}

private fun addBook(service: LibraryService) {
    print("ISBN: ")
    val isbn = readln().trim()
    print("Title: ")
    val title = readln().trim()
    print("Author: ")
    val author = readln().trim()
    print("Year: ")
    val year = readln().trim().toIntOrNull()

    if (isbn.isBlank() || title.isBlank() || author.isBlank() || year == null) {
        println("Invalid input.")
        return
    }

    val added = service.addBook(Book(isbn, title, author, year))
    println(if (added) "Book added." else "A book with that ISBN already exists.")
}

private fun registerPatron(repository: LibraryRepository) {
    print("Patron ID: ")
    val id = readln().trim()
    print("Patron name: ")
    val name = readln().trim()

    if (id.isBlank() || name.isBlank()) {
        println("ID and name cannot be empty.")
        return
    }

    val added = repository.addPatron(Patron(id, name))
    println(if (added) "Patron registered." else "That patron ID already exists.")
}

private fun borrowBook(service: LibraryService) {
    print("Patron ID: ")
    val patronId = readln().trim()
    print("Book ISBN: ")
    val isbn = readln().trim()

    when (service.borrowBook(patronId, isbn)) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully.")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found.")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found.")
        BorrowResult.NOT_AVAILABLE -> println("Book is already borrowed.")
        BorrowResult.LIMIT_REACHED -> println("Patron has reached the borrowing limit.")
    }
}

private fun returnBook(service: LibraryService) {
    print("Patron ID: ")
    val patronId = readln().trim()
    print("Book ISBN: ")
    val isbn = readln().trim()

    println(
        if (service.returnBook(patronId, isbn)) {
            "Book returned successfully."
        } else {
            "Return failed. Check the patron ID and ISBN."
        }
    )
}

private fun search(service: LibraryService) {
    print("Search by ISBN, title, or author: ")
    val query = readln().trim()
    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found.")
    } else {
        results.forEach { println(it) }
    }
}

private fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()

    if (books.isEmpty()) {
        println("No books in the library.")
    } else {
        books.forEach { println(it) }
    }
}
