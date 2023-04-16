package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Car;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Car createCar(String plate, String infoCar, User user);

    Car createCarIfNotExists(String plate, String infoCar, User user);

    Optional<Car> findById(long carId);

    Optional<Car> findByPlateAndUser(String plate, User user);

    Optional<List<Car>> findByUser(User user);
}
