package br.com.fidelles.book.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fidelles.book.controller.BookController;
import br.com.fidelles.book.data.vo.v1.BookVO;
import br.com.fidelles.book.model.Book;
import br.com.fidelles.book.repository.BookRepository;
import br.com.fidelles.exception.RequiredObjectIsNullException;
import br.com.fidelles.exception.ResourceNotFoundException;
import br.com.fidelles.mapper.DozerMapper;

@Service
public class BookService {

	private Logger logger = Logger.getLogger(BookService.class.getName());

	@Autowired
	BookRepository repository;

	public BookVO create(BookVO book) throws Exception {

		if (book == null)
			throw new RequiredObjectIsNullException();

		logger.info("Creating a book");
		Book entity = DozerMapper.parseObject(book, Book.class);
		BookVO bookVo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		bookVo.add(linkTo(methodOn(BookController.class).findById(bookVo.getKey())).withSelfRel());

		return bookVo;
	}

	public List<BookVO> findAll() {

		logger.info("Finding all books");

		List<BookVO> books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);

		books.stream().forEach(p -> {
			try {
				p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return books;
	}

	public BookVO findById(Long id) {

		logger.info("Finding book by ID");

		Book entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		BookVO bookVo = DozerMapper.parseObject(entity, BookVO.class);
		bookVo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

		return bookVo;
	}

	public BookVO update(BookVO book) {

		if (book == null)
			throw new RequiredObjectIsNullException();

		logger.info("Updating book");

		Book entity = repository.findById(book.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setTitle(book.getTitle());
		entity.setAuthor(book.getAuthor());
		entity.setPrice(book.getPrice());
		entity.setLaunchDate(book.getLaunchDate());

		BookVO bookVo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
		bookVo.add(linkTo(methodOn(BookController.class).findById(book.getKey())).withSelfRel());

		return bookVo;
	}

	public void delete(Long id) {

		logger.info("Deleting book by ID");

		Book entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		repository.delete(entity);
	}

}
