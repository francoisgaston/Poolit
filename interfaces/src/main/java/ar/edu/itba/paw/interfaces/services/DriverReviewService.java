package ar.edu.itba.paw.interfaces.services;

import ar.edu.itba.paw.interfaces.exceptions.PassengerNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.TripNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotLoggedInException;
import ar.edu.itba.paw.models.PagedContent;
import ar.edu.itba.paw.models.reviews.DriverReview;
import ar.edu.itba.paw.models.reviews.DriverReviewOptions;

import java.util.Optional;


public interface DriverReviewService {

    DriverReview createDriverReview(final long tripId, final long driverId,  final int rating, final String comment, final DriverReviewOptions option) throws TripNotFoundException, UserNotLoggedInException, PassengerNotFoundException, UserNotFoundException;

    Optional<DriverReview> findById(final long id);

//    double getDriverRating(final long userId) throws UserNotFoundException;

//    double getDriverRatingOwnUser() throws UserNotLoggedInException;

    PagedContent<DriverReview> getDriverReviews(final long userId, int page, int pageSize) throws UserNotFoundException;

    PagedContent<DriverReview> getDriverReviewsMadeByUserOnTrip(final long reviewerId, final long tripId, final int page, final int pageSize) throws UserNotFoundException, TripNotFoundException;

//    PagedContent<DriverReview> getDriverReviewsOwnUser( int page, int pageSize) throws UserNotLoggedInException;

//    boolean canReviewDriver(final Trip trip, final Passenger reviewer, final User driver);

//    ItemReview<User> getDriverReviewState(final long tripId) throws TripNotFoundException, UserNotLoggedInException, PassengerNotFoundException;
}
