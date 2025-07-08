package com.zoo.model.AnimalController;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
	
	List<Animal> findByAnimalName(String animalName);

}
