package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.models.Car;
import ar.edu.itba.paw.models.User;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Car createCar(String plate, String infoCar, User user, long image_id);

    Optional<Car> findById(long carId);

    List<Car> findByUser(User user);

    Optional<Car> findByUserAndPlate(User user, String plate);
}
