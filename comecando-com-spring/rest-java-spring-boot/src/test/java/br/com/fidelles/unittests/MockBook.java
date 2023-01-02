package br.com.fidelles.unittests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.fidelles.book.data.vo.v1.BookVO;
import br.com.fidelles.book.model.Book;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookVO mockVO() {
        return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Title test " + number);
        book.setAuthor("Author name " + number);
        book.setPrice(99D);
        book.setLaunchDate(new Date());
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setKey(number.longValue());
        book.setTitle("Title test " + number);
        book.setAuthor("Author name " + number);
        book.setPrice(99D);
        book.setLaunchDate(new Date());
        return book;
    }

}
