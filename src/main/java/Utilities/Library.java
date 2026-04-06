package Utilities;

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

    public Code addBook(Book book) {
        return Code.SUCCESS;
    }

    private Code addBookToShelf(Book book, Shelf shelf) {
        return Code.SUCCESS;
    }

    public Code addReader(Reader reader) {
        return Code.SUCCESS;
    }

    public Code addShelf(Shelf shelf) {
        return Code.SUCCESS;
    }

    public Code addShelf(String str) {
        return Code.SUCCESS;
    }

    public Code checkOutBook(Reader reader, Book book) {
        return Code.SUCCESS;
    }

    public static LocalDate convertDate(String str, Code code) {
        return LocalDate.now();
    }

    public static int convertInt(String str, Code code) {
        return 0;
    }

    private Code errorCode(int i) {
        return Code.SUCCESS;
    }

    public Book getBookByISBN(String str) {

    }

    public static int getLibraryCardNumber() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public Reader getReaderByCard(int card) {

    }

    public Shelf getShelf(String str) {

    }

    public Shelf getShelf(Integer i) {

    }

    public Code init(String str) {
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
