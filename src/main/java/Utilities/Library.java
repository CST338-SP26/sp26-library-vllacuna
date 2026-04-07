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
        if (readers.contains(reader)) {
            System.out.println(reader.getName() + " already has an account");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }
        for (Reader r: readers) {
            if (r.getCardNumber() == reader.getCardNumber()) {
                System.out.println(r.getName() + " and " + reader.getName() + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }
        }
        if (reader.getCardNumber() > libraryCard) {
            libraryCard = reader.getCardNumber();
        }
        System.out.println(reader.getName() + " added to the library!");
        readers.add(reader);
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
        FileReader fr;
        try {
            fr = new FileReader(filename);
        } catch (FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }
        Scanner s = new Scanner(fr);
        int bookNum = convertInt(s.nextLine(), Code.BOOK_COUNT_ERROR);
        if (bookNum < 0) {
            return Code.BOOK_COUNT_ERROR;
        }
        Code code = initBooks(bookNum, s);
        if (!code.equals(Code.SUCCESS)) {
            return code;
        }
        listBooks();
        int shelfNum = convertInt(s.nextLine(), Code.SHELF_COUNT_ERROR);
        if (shelfNum < 0) {
            return Code.SHELF_COUNT_ERROR;
        }
        code = initShelves(shelfNum, s);
        if (!code.equals(Code.SUCCESS)) {
            return code;
        }
        listShelves();
        int readerNum = convertInt(s.nextLine(), Code.READER_COUNT_ERROR);
        if (readerNum < 0) {
            return Code.READER_COUNT_ERROR;
        }
        code = initReader(readerNum, s);
        if (!code.equals(Code.SUCCESS)) {
            return code;
        }
        listReaders();
        return Code.SUCCESS;
    }

    public Code initBooks(int bookCount, Scanner s) {
        if (bookCount<1) {
            return Code.LIBRARY_ERROR;
        }
        for (int i = 0; i < bookCount; i++) {
            if (!s.hasNextLine()) {
                return Code.BOOK_COUNT_ERROR;
            }
            String[] bookInfo = s.nextLine().split(",");
            if (bookInfo.length < 6) {
                return Code.BOOK_RECORD_COUNT_ERROR;
            }
            int pageCount = convertInt(bookInfo[3], Code.PAGE_COUNT_ERROR);
            if (pageCount <= 0) {
                return Code.PAGE_COUNT_ERROR;
            }
            addBook(new Book(bookInfo[Book.ISBN_], bookInfo[Book.TITLE_], bookInfo[Book.SUBJECT_], convertInt(bookInfo[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR), bookInfo[Book.AUTHOR_], convertDate(bookInfo[Book.DUE_DATE_],Code.DATE_CONVERSION_ERROR)));
        }
        return Code.SUCCESS;
    }

    public Code initReader(int readerCount, Scanner s) {
        if (readerCount <= 0) {
            return Code.READER_COUNT_ERROR;
        }
        for (int i = 0; i < readerCount; i++) {
            String[] readerInfo = s.nextLine().split(",");
            Reader reader = new Reader(convertInt(readerInfo[Reader.CARD_NUMBER_], Code.READER_CARD_NUMBER_ERROR), readerInfo[Reader.NAME_], readerInfo[Reader.PHONE_]);
            addReader(reader);
            int numBooks = convertInt(readerInfo[Reader.BOOK_COUNT_], Code.READER_COUNT_ERROR);
            for (int j = Reader.BOOK_START_; j < Reader.BOOK_START_ + (numBooks*2); j+=2) {
                Book tempBook = getBookByISBN(readerInfo[j]);
                if (tempBook == null) {
                    System.out.println("ERROR");
                }
                LocalDate due = convertDate(readerInfo[j+1], Code.DATE_CONVERSION_ERROR);
                Book newBook = new Book(tempBook.getISBN(), tempBook.getTitle(), tempBook.getSubject(), tempBook.getPageCount(), tempBook.getAuthor(), due);
                checkOutBook(reader, newBook);
            }
        }
        return Code.SUCCESS;
    }

    public Code initShelves(int shelfCount, Scanner s) {
        if (shelfCount < 1) {
            return Code.SHELF_COUNT_ERROR;
        }
        for (int i = 0; i < shelfCount; i++) {
            String[] shelfInfo = s.nextLine().split(",");
            Shelf shelf = new Shelf(convertInt(shelfInfo[Shelf.SHELF_NUMBER], Code.SHELF_NUMBER_PARSE_ERROR), shelfInfo[Shelf.SUBJECT_]);
            if (shelf.getShelfNumber() < 0) {
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }
            addShelf(shelf);
        }
        if (shelves.size() == shelfCount) {
            return Code.SUCCESS;
        }
        System.out.println("Number of shelves doesn't match expected");
        return Code.SHELF_NUMBER_PARSE_ERROR;
    }

    public int listBooks() {
        int totalBooks = 0;
        for (Book b: books.keySet()) {
            int count = books.get(b);
            System.out.println(count + " copies of " + b);
            totalBooks += count;
        }
        return totalBooks;
    }

    public int listReaders() {
        int readerCount = 0;
        for (Reader r: readers) {
            System.out.println(r);
            readerCount++;
        }
        return readerCount;
    }

    public int listReaders(Boolean showBooks) {
        int readerCount = 1;
        if (showBooks) {
            for (Reader r: readers) {
                System.out.println(r.getName() + "(#" + readerCount + ") has the following books:\n" + r.getBooks());
                readerCount++;
            }
        } else {
            for (Reader r: readers) {
                System.out.println(r);
                readerCount++;
            }
        }
        return readerCount-1;
    }

    public int listShelves(Boolean showBooks) {
        int count = 0;
        if (showBooks) {
            for (Shelf s: shelves.values()) {
                s.listBooks();
                count++;
            }
        } else {
            for (Shelf s: shelves.values()) {
                System.out.println(s);
                count++;
            }
        }
        return count;
    }

    public int listShelves() {
        return listShelves(false);
    }

    public Code removeReader(Reader reader) {
        if (readers.contains(reader) && reader.getBookCount() > 0) {
            System.out.println(reader.getName() + " must return all books!");
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        } else if (!readers.contains(reader)) {
            System.out.println(reader.getName() + " is not part of this Library");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        readers.remove(reader);
        return Code.SUCCESS;
    }

    public Code returnBook(Reader reader, Book book) {
        if (!books.containsKey(book)) {
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if (!reader.hasBook(book)) {
            System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        } else {
            System.out.println(reader.getName() + " is returning " + book.getTitle());
            Code c = reader.removeBook(book);
            if (c.equals(Code.SUCCESS)) {
                return returnBook(book);
            } else {
                System.out.println("Could not return " + book.getTitle());
                return c;
            }
        }
    }

    public Code returnBook(Book book) {
        if (shelves.containsKey(book.getSubject())) {
            System.out.println("No shelf for " + book.getTitle());
            return Code.SHELF_EXISTS_ERROR;
        } else {
            return shelves.get(book.getSubject()).addBook(book);
        }
    }
}
