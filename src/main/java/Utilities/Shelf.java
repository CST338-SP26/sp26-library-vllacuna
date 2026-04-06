package Utilities;

import java.util.HashMap;
import java.util.Objects;

/**
 * Name: Von Andre Llacuna
 * Date: 04/05/26
 * FileName: Shelf.java
 * AssignmentName: Project 01 Part 03/04: Shelf.java
 * Description: A file that represents a shelf in the larger library project
 */
public class Shelf {
    public static final int SHELF_NUMBER = 0;
    public static final int SUBJECT_ = 1;

    private HashMap<Book, Integer> books ;
    private int shelfNumber;
    private String subject;

    public Shelf() {
    }

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        books = new HashMap<>();
    }

    /**
     * gets the book count
     * @param book represents book
     * @return the book count, -1 if not found
     */
    public int getBookCount(Book book) {
        if (!books.containsKey(book)) {
            return -1;
        } else {
            return books.get(book);
        }
    }

    /**
     * adds parameter 'book' to 'books' HashMap of the shelf
     * @param book represents the book trying to be added to the HashMap
     * @return a SUCCESS code if added and a shelf subject mismatch code
     * if the parameter 'book' subject doesn't match the shelf subject
     */
    public Code addBook(Book book) {
        if (!subject.equals(book.getSubject())) {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        } else if (books == null) {
            books = new HashMap<>();
            books.put(book, 1);
            System.out.println(book + " added to shelf");
            return Code.SUCCESS;
        } else if (books.containsKey(book)){
            Integer temp = books.get(book);
            temp++;
            books.put(book, temp);
            return Code.SUCCESS;
        } else {
            books.put(book, 1);
            System.out.println(book + " added to shelf");
            return Code.SUCCESS;
        }
    }

    /**
     * removes a book from the shelf
     * @param book represents the book trying to be removed
     * @return SUCCESS code if book was successfully removed and
     * not in inventory code if book isn't on the shelf
     */
    public Code removeBook(Book book) {
        if (books == null) {
            System.out.println(book.getTitle() + " is not on the shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if (!books.containsKey(book)) {
            System.out.println(book.getTitle() + " is not on the shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else if (books.containsKey(book) && (books.get(book) == 0)) {
            System.out.println("No copies of " + book.getTitle() + " remains on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else {
            books.put(book, books.get(book)-1);
            System.out.println(book.getTitle() + " successfully removed from the shelf " + subject);
            return Code.SUCCESS;
        }
    }

    /**
     * Shows how many books are on the shelf and which books
     * @return the string listing of all the books on the shelf
     */
    public String listBooks() {
        StringBuilder sb = new StringBuilder();
        if (books == null || books.isEmpty()) {
            return "0 books on shelf: " + shelfNumber + " : " + subject;
        }
        if(books.size() == 1){
            sb.append(books.size()).append(" book on shelf: ").append(shelfNumber).append(" : ").append(subject).append("\n");
        } else {
            sb.append(books.size()).append(" books on shelf: ").append(shelfNumber).append(" : ").append(subject).append("\n");
        }
        for(Book b : books.keySet()){
            sb.append(books.get(b)).append(" ").append(b).append("\n");
        }
        return sb.toString();
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return getShelfNumber() == shelf.getShelfNumber() && Objects.equals(getSubject(), shelf.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShelfNumber(), getSubject());
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }
}
