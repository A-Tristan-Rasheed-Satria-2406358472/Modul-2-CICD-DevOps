package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CarController.class)
class CarControllerTest {

  private static final String CAR_LIST_PATH = "/car/listCar";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CarService carService;

  @Test
  void createCarPage_returnsCreateViewAndAddsCarModel() throws Exception {
    mockMvc.perform(get("/car/createCar"))
        .andExpect(status().isOk())
        .andExpect(view().name("CreateCar"))
        .andExpect(model().attributeExists("car"));
  }

  @Test
  void createCarPost_callsCreateAndRedirects() throws Exception {
    mockMvc.perform(post("/car/createCar")
        .param("carName", "Civic")
        .param("carColor", "Black")
        .param("carQuantity", "6"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CAR_LIST_PATH));

    ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
    verify(carService).create(captor.capture());
    Car sent = captor.getValue();
    assertEquals("Civic", sent.getCarName());
    assertEquals("Black", sent.getCarColor());
    assertEquals(6, sent.getCarQuantity());
  }

  @Test
  void carListPage_returnsListViewAndCarsModel() throws Exception {
    Car car = new Car();
    car.setCarId("C-1");
    car.setCarName("Ayla");
    car.setCarColor("White");
    car.setCarQuantity(2);
    when(carService.findAll()).thenReturn(List.of(car));

    mockMvc.perform(get(CAR_LIST_PATH))
        .andExpect(status().isOk())
        .andExpect(view().name("CarList"))
        .andExpect(model().attributeExists("cars"));

    verify(carService).findAll();
  }

  @Test
  void editCarPage_whenCarExists_returnsEditViewWithModel() throws Exception {
    Car car = new Car();
    car.setCarId("C-2");
    when(carService.findById("C-2")).thenReturn(car);

    mockMvc.perform(get("/car/editCar/C-2"))
        .andExpect(status().isOk())
        .andExpect(view().name("EditCar"))
        .andExpect(model().attributeExists("car"));

    verify(carService).findById("C-2");
  }

  @Test
  void editCarPage_whenCarNotFound_redirectsToList() throws Exception {
    when(carService.findById("X")).thenReturn(null);

    mockMvc.perform(get("/car/editCar/X"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CAR_LIST_PATH));

    verify(carService).findById("X");
  }

  @Test
  void editCarPost_callsUpdateAndRedirects() throws Exception {
    mockMvc.perform(post("/car/editCar")
        .param("carId", "C-3")
        .param("carName", "Brio")
        .param("carColor", "Yellow")
        .param("carQuantity", "8"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CAR_LIST_PATH));

    ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
    verify(carService).update(eq("C-3"), captor.capture());
    Car sent = captor.getValue();
    assertEquals("C-3", sent.getCarId());
    assertEquals("Brio", sent.getCarName());
    assertEquals("Yellow", sent.getCarColor());
    assertEquals(8, sent.getCarQuantity());
  }

  @Test
  void deleteCar_callsServiceAndRedirects() throws Exception {
    mockMvc.perform(post("/car/deleteCar").param("carId", "C-4"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(CAR_LIST_PATH));

    verify(carService).deleteCarById("C-4");
  }
}
