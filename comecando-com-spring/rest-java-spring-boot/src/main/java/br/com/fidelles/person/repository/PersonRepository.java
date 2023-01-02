package br.com.fidelles.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fidelles.person.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{}
