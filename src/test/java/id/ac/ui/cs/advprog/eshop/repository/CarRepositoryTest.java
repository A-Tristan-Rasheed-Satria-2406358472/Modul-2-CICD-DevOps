package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

  private CarRepository carRepository;

  @BeforeEach
  void setUp() {
    carRepository = new CarRepository();
  }

  private Car newCar(String name, String color, int quantity) {
    Car car = new Car();
    car.setCarName(name);
    car.setCarColor(color);
    car.setCarQuantity(quantity);
    return car;
  }

  @Test
  void create_setsIdWhenMissing_andSavesCar() {
    Car car = newCar("Avanza", "Black", 3);

    Car saved = carRepository.create(car);

    assertNotNull(saved.getCarId());
    Iterator<Car> all = carRepository.findAll();
    assertTrue(all.hasNext());
    assertEquals(saved.getCarId(), all.next().getCarId());
  }

  @Test
  void create_keepsExistingId_whenAlreadyProvided() {
    Car car = newCar("Fortuner", "White", 2);
    car.setCarId("preset-id");

    Car saved = carRepository.create(car);

    assertEquals("preset-id", saved.getCarId());
  }

  @Test
  void findById_returnsCarWhenFound() {
    Car saved = carRepository.create(newCar("Jazz", "Red", 2));

    Car found = carRepository.findById(saved.getCarId());

    assertNotNull(found);
    assertEquals("Jazz", found.getCarName());
  }

  @Test
  void findById_returnsNullWhenNotFound() {
    carRepository.create(newCar("BRV", "White", 1));

    Car found = carRepository.findById("unknown-id");

    assertNull(found);
  }

  @Test
  void update_returnsUpdatedCarWhenFound() {
    Car saved = carRepository.create(newCar("Yaris", "Silver", 5));
    Car updateRequest = newCar("Yaris Facelift", "Blue", 7);

    Car updated = carRepository.update(saved.getCarId(), updateRequest);

    assertNotNull(updated);
    assertEquals("Yaris Facelift", updated.getCarName());
    assertEquals("Blue", updated.getCarColor());
    assertEquals(7, updated.getCarQuantity());
  }

  @Test
  void update_returnsUpdatedCarWhenFoundAfterSkippingAnotherCar() {
    carRepository.create(newCar("Brio", "Yellow", 1));
    Car saved = carRepository.create(newCar("CRV", "Black", 3));
    Car updateRequest = newCar("CRV Turbo", "Green", 4);

    Car updated = carRepository.update(saved.getCarId(), updateRequest);

    assertNotNull(updated);
    assertEquals("CRV Turbo", updated.getCarName());
    assertEquals("Green", updated.getCarColor());
    assertEquals(4, updated.getCarQuantity());
  }

  @Test
  void update_returnsNullWhenNotFound() {
    Car updateRequest = newCar("No Car", "Gray", 1);

    Car updated = carRepository.update("missing-id", updateRequest);

    assertNull(updated);
  }

  @Test
  void delete_removesExistingCar() {
    Car saved = carRepository.create(newCar("Civic", "Black", 4));

    carRepository.delete(saved.getCarId());

    assertNull(carRepository.findById(saved.getCarId()));
  }
}
