package org.example.pro5.jpa.controller;

import org.example.pro5.jpa.Book;
import org.example.pro5.jpa.BookRepository;
import org.example.pro5.jpa.Publisher;
import org.example.pro5.jpa.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Import Model class
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    // Display all books
    @GetMapping("/")
    public String showAllBooks(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    // Delete a book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookRepository.deleteById(id);
        return "redirect:/";
    }

    // Update a book (show form)
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            // Lấy danh sách tất cả các nhà xuất bản
            List<Publisher> allPublishers = publisherRepository.findAll();
            model.addAttribute("book", book);
            model.addAttribute("allPublishers", allPublishers);
            return "update-book";
        } else {
            return "redirect:/";
        }
    }

    // Process update book form submission
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable("id") Long id, @ModelAttribute("book") Book updatedBook, @RequestParam("publisherIds") List<Long> publisherIds) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setName(updatedBook.getName());
            // Lấy danh sách nhà xuất bản từ IDs và thiết lập cho sách
            Set<Publisher> publishers = publisherIds.stream()
                    .map(publisherId -> publisherRepository.findById(publisherId).orElse(null))
                    .collect(Collectors.toSet());
            book.setPublishers(publishers);
            bookRepository.save(book);
        }
        return "redirect:/";
    }


    // Display add book form
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("publishers", publisherRepository.findAll());
        return "add-book";
    }

    // Process add book form submission
    @PostMapping("/add")
    public String addBook(@ModelAttribute Book book, @RequestParam("publisherIds") List<Long> publisherIds) {
        Set<Publisher> publishers = publisherIds.stream()
                .map(id -> publisherRepository.findById(id).orElse(null))
                .collect(Collectors.toSet());
        book.setPublishers(publishers);
        // Xử lý logic để thêm sách vào cơ sở dữ liệu
        bookRepository.save(book);
        return "redirect:/";
    }
}
