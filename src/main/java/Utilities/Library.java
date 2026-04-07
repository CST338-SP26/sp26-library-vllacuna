package Utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Library {
    public static final int LENDING_LIMIT = 5;

    private String name;
    private static int libraryCard;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;
    private HashMap<Book, Integer> books;

    public Library(String name) {
        this.name = name;
        readers = new ArrayList<Reader>();
        shelves = new HashMap<>();
        books = new HashMap<>();
    }

    public Code addBook(Book newBook) {
        if (books.containsKey(newBook)) {
            books.put(newBook, books.get(newBook) + 1);
            System.out.println(books.get(newBook) + " copies of " + newBook.getTitle() + " in the stacks");
        } else {
            books.put(newBook, 1);
            System.out.println(newBook.getTitle() + " added to the stacks.");
        }
        if (shelves.containsKey(newBook.getSubject())) {
            Shelf shelf = shelves.get(newBook.getSubject());
            shelf.addBook(newBook);
        } else {
            System.out.println("No shelf for " + newBook.getSubject() + " books");
            return Code.SHELF_EXISTS_ERROR;
        }
        return Code.SUCCESS;
    }

    public Code addReader(Reader reader) {
        return Code.SUCCESS;
    }

    public Code addShelf(Shelf shelf) {
        if (shelves.containsKey(shelf.getSubject())) {
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;
        } else {
            shelves.put(shelf.getSubject(), shelf);
            return Code.SUCCESS;
        }
    }

    public Code addShelf(String str) {
        Shelf shelf = new Shelf(shelves.size()+1, str);
        return addShelf(shelf);
    }

    public Code checkOutBook(Reader reader, Book book) {
        if (reader == null) {
            System.out.println(reader.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        } else if (reader.getBookCount() >= LENDING_LIMIT) {
            System.out.println(reader.getName() + " has reached the lending limit, " + LENDING_LIMIT);
            return Code.BOOK_LIMIT_REACHED_ERROR;
        } else if (!books.containsKey(book)) {
            System.out.println("ERROR: could not find " + book.getTitle());
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if (shelves.containsKey(book.getSubject())) {
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        } else if (shelves.get(book.getSubject()).getBookCount(book) < 1) {
            System.out.println("ERROR: no copies of " + book.getTitle() + " remain");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if (reader.addBook(book) != Code.SUCCESS) {
            System.out.println("Couldn't checkout " + book.getTitle());
            return reader.addBook(book);
        }
        if (shelves.get(book.getSubject()).removeBook(book) != Code.SUCCESS) {
            System.out.println(book.getTitle() + " checked out successfully");
        }
        return shelves.get(book.getSubject()).removeBook(book);
    }

    public static LocalDate convertDate(String date, Code errorCode) {
        if (date.equals("0000")) {
            return LocalDate.of(1970, 1, 1);
        }
        String[] arrayDate = date.split("-");
        if (arrayDate.length != 3) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
        try {
            if (Integer.parseInt(arrayDate[0])<0 || Integer.parseInt(arrayDate[1])<0 || Integer.parseInt(arrayDate[2])<0) {
                System.out.println("Error converting date: Year [year] " + "\n" +
                        "Error converting date: Month [month]" + "\n" +
                        "Error converting date: Dat [day]" + "\n" +
                        "Using default date (01-jan-1970)"
                );
                return LocalDate.of(1970, 1, 1);
            }
            return LocalDate.of(Integer.parseInt(arrayDate[0]), Integer.parseInt(arrayDate[1]), Integer.parseInt(arrayDate[2]));
        } catch (Exception e) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
    }

    public static int convertInt(String recordCountString, Code code) {
        try {
            return Integer.parseInt(recordCountString);
        } catch (NumberFormatException e) {
            System.out.println("Value which caused the error: " + recordCountString);
            if (code.equals(Code.BOOK_COUNT_ERROR)) {
                System.out.println("Error: Could not read number of books");
            } else if (code.equals(Code.PAGE_COUNT_ERROR)) {
                System.out.println("Error: could not parse page count");
            } else if (code.equals(Code.DATE_CONVERSION_ERROR)) {
                System.out.println("Error: Could not parse date component");
            } else {
                System.out.println("Error: Unknown conversion error");
            }
            return code.getCode();
        }
    }

    private Code errorCode(int i) {
        return Code.SUCCESS;
    }

    public Book getBookByISBN(String str) {
        for (Book b: books.keySet()) {
            if (b.getISBN().equals(str)) {
                return b;
            }
        }
        System.out.println("ERROR: Could not find a book with isbn: " + str);
        return null;
    }

    public static int getLibraryCardNumber() {
        return libraryCard + 1;
    }

    public String getName() {
        return name;
    }

    public Reader getReaderByCard(int cardNumber) {
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i).getCardNumber() == cardNumber) {
                return readers.get(i);
            }
        }
        System.out.println("Could not find a reader with card #" + cardNumber);
        return null;
    }

    public Shelf getShelf(String subject) {
        if (shelves.containsKey(subject)) {
            return shelves.get(subject);
        }
        System.out.println("No shelf for " + subject + " books");
        return null;
    }

    public Shelf getShelf(Integer shelfNum) {
        for (Shelf s: shelves.values()) {
            if (s.getShelfNumber() == shelfNum) {
                return s;
            }
        }
        System.out.println("No shelf number " + shelfNum + " found");
        return null;
    }

    public Code init(String filename) {
        return Code.SUCCESS;
    }

    public Code initBooks(int i, Scanner s) {
        return Code.SUCCESS;
    }

    public Code initReader(int i, Scanner s) {
        return Code.SUCCESS;
    }

    public Code initShelves(int i, Scanner s) {
        return Code.SUCCESS;
    }

    public int listBooks() {
        return 0;
    }

    public int listReaders() {
        return 0;
    }

    public int listReaders(Boolean b) {
        return 0;
    }

    public int listShelves(Boolean b) {
        return 0;
    }

    public int listShelves() {
        return 0;
    }

    public Code removeReader(Reader reader) {
        return Code.SUCCESS;
    }

    public Code returnBook(Reader reader, Book book) {
        return Code.SUCCESS;
    }

    public Code returnBook(Book book) {
        return Code.SUCCESS;
    }
}
