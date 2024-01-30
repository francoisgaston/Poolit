package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.trips.Trip;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TripDao {
    //
    Trip create(final City originCity, final String originAddress, final City destinationCity, final String destinationAddress, final Car car, final LocalDateTime startDateTime, final LocalDateTime endDateTime, final boolean isRecurrent, final double price, final int maxPassengers, final User driver);
    //
    Passenger addPassenger(Trip trip,User user,LocalDateTime startDateTime,LocalDateTime endDateTime);
    //
    boolean removePassenger(final Trip trip, final Passenger passenger);
//    List<Passenger> getPassengers(final TripInstance tripInstance);
    //
//    List<Passenger> getPassengers(final Trip trip, final LocalDateTime dateTime);
    boolean userIsAcceptedPassengerOfDriver(final User user, final User driver);
    //
    List<Passenger> getPassengers(Trip trip, LocalDateTime startDateTime, LocalDateTime endDateTime);
    //
    public PagedContent<Passenger> getPassengers(Trip trip, LocalDateTime startDateTime, LocalDateTime endDateTime, Optional<Passenger.PassengerState> passengerState,List<Integer> excludedPassengers,int page, int pageSize);
    //
    Optional<Passenger> getPassenger(final Trip trip, final User user);
    //
    Optional<Passenger> getPassenger(final long tripId, final User user);
//    PagedContent<TripInstance> getTripInstances(final Trip trip,int page, int pageSize);
//    PagedContent<TripInstance> getTripInstances(final Trip trip, int page, int pageSize, LocalDateTime start, LocalDateTime end);
    //
    int getTripSeatCount(long tripId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    //
    PagedContent<Trip> getTripsCreatedByUser(final User user,Optional<LocalDateTime> minDateTime, Optional<LocalDateTime> maxDateTime,int page, int pageSize);
    PagedContent<Trip> getTripsWhereUserIsPassenger(final User user, Optional<LocalDateTime> minDateTime, Optional<LocalDateTime> maxDateTime, Passenger.PassengerState passengerState, int page, int pageSize);
    Optional<Trip> findById(long id);
    //
    Optional<Trip> findById(long tripId, LocalDateTime start, LocalDateTime end);
    //
    boolean deleteTrip(final Trip trip);
    //
    boolean markTripAsDeleted(Trip trip, LocalDateTime lastOccurrence);
    //
    void truncatePassengerEndDateTime(Passenger passenger, LocalDateTime newLastDateTime);
//    PagedContent<Trip> getTripsWithFilters(
//            long originCityId, long destinationCityId,
//            LocalDateTime startDateTime, Optional<DayOfWeek> dayOfWeek, Optional<LocalDateTime> endDateTime, int minutes,
//            Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice, Trip.SortType sortType, boolean descending,
//            long searchUserId, List<FeatureCar> carFeatures, int page, int pageSize);
    PagedContent<Trip> getTripsWithFilters(
            long originCityId, long destinationCityId,
            LocalDateTime startDateTime, DayOfWeek dayOfWeek, LocalDateTime endDateTime, int minutes,
            Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice, Trip.SortType sortType, boolean descending,
            /*long searchUserId,*/ List<FeatureCar> carFeatures, int page, int pageSize);

    PagedContent<Trip> getTripsByOriginAndStart(long originCityId, LocalDateTime startDateTime,/* long searchUserId,*/ int page, int pageSize);
    //
    List<Passenger> getAcceptedPassengers(Trip trip, LocalDateTime startDateTime, LocalDateTime endDateTime);
    //
//    public List<Passenger> getAcceptedPassengers(Trip trip, LocalDateTime dateTime);
    //
//    public List<Passenger> getAcceptedPassengers(TripInstance tripInstance);
    //
    public boolean acceptPassenger(Passenger passenger);
    //
    public boolean rejectPassenger(Passenger passenger);
}
