package com.zoo.model.AnimalController;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    @Autowired
    private AnimalRepository repository;

    @PostMapping
    public ResponseEntity<?> addAnimal(@RequestBody Animal animal) {
        if (animal.getAnimalName() == null || animal.getAnimalName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Hayvan adı boş olamaz.");
        }
        Animal saved = repository.save(animal);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Animal> getAnimals() {
        return repository.findAll();
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getAnimalByName(@PathVariable String name) {
        List<Animal> animals = repository.findByAnimalName(name);

        if (animals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hayvan bulunamadı.");
        } else if (animals.size() == 1) {
            return ResponseEntity.ok(animals.get(0));
        } else {
            return ResponseEntity.ok(animals);
        }
    }

    @PutMapping("/{name}")
    public ResponseEntity<String> updateAnimal(@PathVariable String name, @RequestBody Animal updatedAnimal) {
        List<Animal> existingAnimals = repository.findByAnimalName(name);

        if (existingAnimals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hayvan bulunamadı.");
        }

        for (Animal animal : existingAnimals) {
            animal.setAge(updatedAnimal.getAge());
            animal.setHeight(updatedAnimal.getHeight());
            animal.setWeight(updatedAnimal.getWeight());
            animal.setSpeed(updatedAnimal.getSpeed());
            animal.setColor(updatedAnimal.getColor());
            repository.save(animal);
        }

        return ResponseEntity.ok(name + " adlı hayvan başarıyla güncellendi.");
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteAnimalByName(@PathVariable String name) {
        List<Animal> animals = repository.findByAnimalName(name);

        if (animals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Hayvan bulunamadı.");
        }

        repository.deleteAll(animals);
        return ResponseEntity.ok(name + " adlı hayvan(lar) başarıyla silindi.");
    }

    @GetMapping("/paged")
    public Page<Animal> getAnimalsPaged(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }
}
