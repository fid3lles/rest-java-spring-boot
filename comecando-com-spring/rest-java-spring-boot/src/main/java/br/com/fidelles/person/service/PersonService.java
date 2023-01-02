package br.com.fidelles.person.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fidelles.exception.RequiredObjectIsNullException;
import br.com.fidelles.exception.ResourceNotFoundException;
import br.com.fidelles.mapper.DozerMapper;
import br.com.fidelles.person.controller.PersonController;
import br.com.fidelles.person.data.vo.v1.PersonVO;
import br.com.fidelles.person.model.Person;
import br.com.fidelles.person.repository.PersonRepository;

@Service
public class PersonService {

	private Logger logger = Logger.getLogger(PersonService.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public PersonVO create(PersonVO person) throws Exception {
		
		if(person == null) throw new br.com.fidelles.exception.RequiredObjectIsNullException();
		
		logger.info("Creating person");
		Person entity = DozerMapper.parseObject(person, Person.class);
		PersonVO personVo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		personVo.add(linkTo(methodOn(PersonController.class).findById(personVo.getKey())).withSelfRel());
		
		return personVo;
	}
	
	public List<PersonVO> findAll() throws Exception{
		
		logger.info("Finding all persons");
		
		List<PersonVO> persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
		
		persons
			.stream()
			.forEach(p -> {
				try {
					p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		
		return persons;
	}
	
	public PersonVO findById(Long id) throws Exception {
		
		logger.info("Finding person");
		
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		PersonVO personVo = DozerMapper.parseObject(entity, PersonVO.class);
		personVo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return personVo;
						
	}
	
	public PersonVO update(PersonVO person) throws Exception {
		
		if(person == null) throw new RequiredObjectIsNullException();
		
		logger.info("Updating person");

		Person entity = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		PersonVO personVo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		personVo.add(linkTo(methodOn(PersonController.class).findById(person.getKey())).withSelfRel());
		
		return personVo;
	}
	
	public void delete(Long id) {
		
		logger.info("Deleting person");
		
		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(entity);
	}

}
