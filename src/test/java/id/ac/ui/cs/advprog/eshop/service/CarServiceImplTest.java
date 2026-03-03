package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarDataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

  @Mock
  private CarDataStore carRepository;

  @InjectMocks
  private CarServiceImpl service;

  @Test
  void create_delegatesToRepository_andReturnsCar() {
    Car car = new Car();
    car.setCarName("X");

    when(carRepository.create(car)).thenReturn(car);

    Car result = service.create(car);

    assertSame(car, result);
    verify(carRepository).create(car);
  }

  @Test
  void findAll_convertsIteratorToList() {
    Car a = new Car();
    a.setCarId("1");
    Car b = new Car();
    b.setCarId("2");
    List<Car> list = Arrays.asList(a, b);
    Iterator<Car> iterator = list.iterator();

    when(carRepository.findAll()).thenReturn(iterator);

    List<Car> result = service.findAll();

    assertEquals(2, result.size());
    assertEquals("1", result.get(0).getCarId());
    assertEquals("2", result.get(1).getCarId());
  }

  @Test
  void findById_delegates() {
    Car car = new Car();
    car.setCarId("10");
    when(carRepository.findById("10")).thenReturn(car);

    Car result = service.findById("10");

    assertSame(car, result);
    verify(carRepository).findById("10");
  }

  @Test
  void update_delegates() {
    Car car = new Car();
    car.setCarId("A");

    service.update("A", car);

    verify(carRepository).update("A", car);
  }

  @Test
  void deleteCarById_delegates() {
    service.deleteCarById("C-1");

    verify(carRepository).delete("C-1");
  }
}
