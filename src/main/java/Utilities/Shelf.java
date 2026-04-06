package Utilities;

import java.util.HashMap;
import java.util.Objects;

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

    public int getBookCount(Book book) {
        if (!books.containsKey(book)) {
            return -1;
        } else {
            return books.get(book);
        }
    }

    public Code addBook(Book book) {
        if (!subject.equals(book.getSubject())) {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        } else if (books.containsKey(book)) {
            Integer temporary = books.get(book);
            temporary++;
            books.put(book, temporary);
            return Code.SUCCESS;
        } else {
            books.put(book, 1);
            System.out.println(book + "added to shelf");
            return Code.SUCCESS;
        }
    }

    public Code removeBook(Book book) {
        if (!books.containsKey(book)) {
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

    public String listBooks() {
        StringBuilder sb = new StringBuilder();
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
