package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.TripDao;
import ar.edu.itba.paw.interfaces.services.EmailService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.trips.Trip;
import ar.edu.itba.paw.services.EmailServiceImpl;
import ar.edu.itba.paw.services.TripServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.junit.MockitoJUnitRunner;

import javax.mail.MessagingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class TripServiceImplTest {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private static final User user = new User(1, "USER", "SURNAME", "user@gmail.com", "PHONE", "PASSWORD", new City(1, "Agronomía", 1), new Locale("es"), "USER", 1L);
    private static final City originCity = new City(1, "Agronomía", 1);
    private static final City destinationCity = new City(1, "Agronomía", 1);
    private static final String originAddress = "ORIGIN ADDRESS";
    private static final String destinationAddress = "DESTINATION ADDRESS";
    private static final Car car = new Car(1, "ABC123", "INFO", user, 1L);
    private static final LocalDate startDate = LocalDate.parse("02/05/2023", DATE_FORMAT);
    private static final LocalTime startTime = LocalTime.parse("10:00", TIME_FORMAT);
    private static final BigDecimal price = BigDecimal.valueOf(10.0);
    private static final int maxSeats = 4;
    private static final User driver = new User(2, "DRIVER", "SURNAME", "driver@gmail.com", "PHONE", "PASSWORD", new City(1, "Agronomía", 1), new Locale("es"), "DRIVER", 1L);
    ;
    private static final LocalDate endDate = LocalDate.parse("02/05/2023", DATE_FORMAT);
    private static final LocalTime endTime = LocalTime.parse("10:00", TIME_FORMAT);
    private static final long tripId = 1L;
    private static final LocalDateTime dateTime = LocalDateTime.now();
    private static final Trip trip = new Trip(tripId, originCity, originAddress, destinationCity, destinationAddress, dateTime, dateTime.plusDays(7), maxSeats, driver, car, 0, price.doubleValue());
    private static final Passenger passenger = new Passenger(user, dateTime, dateTime.plusDays(7));


    @Mock
    private TripDao tripDao;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private TripServiceImpl tripService;



    @Test
    public void TestCreateTrip() throws Exception {
        // precondiciones
        when(tripDao.create(eq(originCity), eq(originAddress), eq(destinationCity), eq(destinationAddress), eq(car), any(), any(), eq(false), eq(price.doubleValue()), eq(maxSeats), eq(driver)))
                .thenReturn(new Trip(1, originCity, originAddress, destinationCity, destinationAddress, LocalDateTime.now(), LocalDateTime.now(), maxSeats, driver, car, 0, price.doubleValue()));
        doNothing().when(emailService).sendMailNewTrip(any());

        // ejercitar la clase
        Trip newTrip = tripService.createTrip(originCity, originAddress, destinationCity, destinationAddress, car, startDate, startTime, price, maxSeats, driver, endDate, endTime);

        // assertions
        Assert.assertNotNull(newTrip);
        Assert.assertEquals(tripId, newTrip.getTripId());
    }

    @Test(expected = Exception.class)
    public void TestCantCreateTrip() throws Exception {
        // precondiciones
        when(tripDao.create(eq(originCity), eq(originAddress), eq(destinationCity), eq(destinationAddress), eq(car), any(), any(), eq(false), eq(price.doubleValue()), eq(maxSeats), eq(driver)))
                .thenThrow(Exception.class);
        doNothing().when(emailService).sendMailNewTrip(any());

        // ejercitar la clase
        tripService.createTrip(originCity, originAddress, destinationCity, destinationAddress, car, startDate, startTime, price, maxSeats, driver, endDate, endTime);

        Assert.fail();
    }

    @Test
    public void TestRemovePassenger() throws Exception {
        // precondiciones
        when(tripDao.removePassenger(eq(trip), eq(passenger)))
                .thenReturn(true);
        when(tripDao.getPassenger(eq(trip), eq(user)))
                .thenReturn(Optional.of(passenger));

        // ejercitar la clase
        boolean ans = tripService.removePassenger(trip, user);

        // assertions
        Assert.assertTrue(ans);
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestRemovePassengerWithNull() throws Exception {
        // ejercitar la clase
        tripService.removePassenger(null, user);

        // assertions
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void TestBadOriginDateGetPassengers() throws Exception {
        // precondiciones

        // ejercitar la clase
        List<Passenger> passengers2 = tripService.getPassengers(trip, dateTime.plusDays(-3));

        // assertions
        Assert.fail();
    }

    @Test
    public void TestGetPassengers() throws Exception {
        // precondiciones
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(passenger);

        when(tripDao.getPassengers(eq(trip), eq(dateTime)))
                .thenReturn(passengers);

        // ejercitar la clase
        List<Passenger> passengers2 = tripService.getPassengers(trip, dateTime);

        // assertions
        Assert.assertNotNull(passengers2);
    }

}