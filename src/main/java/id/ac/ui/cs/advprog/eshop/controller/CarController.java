package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/car")
public class CarController {

  private final CarService carService;

  private static final String REDIRECT_CAR_LIST = "redirect:/car/listCar";

  public CarController(CarService carService) {
    this.carService = carService;
  }

  @GetMapping("/createCar")
  public String createCarPage(Model model) {
    model.addAttribute("car", new Car());
    return "CreateCar";
  }

  @PostMapping("/createCar")
  public String createCarPost(@ModelAttribute Car car) {
    carService.create(car);
    return REDIRECT_CAR_LIST;
  }

  @GetMapping("/listCar")
  public String carListPage(Model model) {
    List<Car> allCars = carService.findAll();
    model.addAttribute("cars", allCars);
    return "CarList";
  }

  @GetMapping("/editCar/{carId}")
  public String editCarPage(@PathVariable String carId, Model model) {
    Car car = carService.findById(carId);
    if (car == null) {
      return REDIRECT_CAR_LIST;
    }
    model.addAttribute("car", car);
    return "EditCar";
  }

  @PostMapping("/editCar")
  public String editCarPost(@ModelAttribute Car car) {
    carService.update(car.getCarId(), car);
    return REDIRECT_CAR_LIST;
  }

  @PostMapping("/deleteCar")
  public String deleteCar(@RequestParam("carId") String carId) {
    carService.deleteCarById(carId);
    return REDIRECT_CAR_LIST;
  }
}