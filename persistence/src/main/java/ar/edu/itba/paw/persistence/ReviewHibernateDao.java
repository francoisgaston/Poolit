package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.persistence.ReviewDao;
import ar.edu.itba.paw.models.Car;
import ar.edu.itba.paw.models.Passenger;
import ar.edu.itba.paw.models.Review;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.trips.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewHibernateDao implements ReviewDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(ReviewHibernateDao.class);

    @PersistenceContext
    private EntityManager em;


    @Override
    public Review create(Trip trip, User user, int rating, String review) {
        LOGGER.debug("Adding new review to the trip with id {} and written by user with id {} to the database", trip.getTripId(), user.getUserId());
        final Review reviewModel= new Review(trip, user, rating, review);
        em.persist(reviewModel);
        LOGGER.info("Review for {} added to the database", user.getUserId());
        LOGGER.debug("New {}", reviewModel);
        return reviewModel;
    }

    @Override
    public double getRating(User driver) {
        Query avgQuery = em.createNativeQuery("SELECT AVG(rating) as avg FROM reviews NATURAL JOIN trips WHERE driver_id = :driverId");
        avgQuery.setParameter("driverId", driver.getUserId());
        return avgQuery.getFirstResult();
    }

    @Override
    public List<Review> findByDriver(User driver) {
        Query ListQuery = em.createQuery("from Review where trip.driver.userId = :driverId",Review.class);
        ListQuery.setParameter("driverId", driver.getUserId());
        return (List<Review>) ListQuery.getResultList();
    }


    @Override
    public List<Review> findReviewsByUser(User user) {
        LOGGER.debug("Looking for reviews written by user with id {} in the database", user.getUserId());
        final TypedQuery<Review> query = em.createQuery("from Review r where r.user = :user", Review.class);
        query.setParameter("user",user);
        List<Review>  result = query.getResultList();
        LOGGER.debug("Found {} in the database", result);
        return result;
    }

    @Override
    public List<Long> findTripIdByUser(User user) {
        LOGGER.debug("Looking for trips that were reviewed by user with id {} in the database", user.getUserId());
        final List<Long> result = em.createQuery("select trip_id from Review r where r.user = :user", Long.class).setParameter("user",user).getResultList();
        LOGGER.debug("Found {} in the database", result);
        return result;
    }

    @Override
    public Optional<Review> reviewByTripAndPassanger(Trip trip, Passenger passenger) {
        LOGGER.debug("Looking for review of trip {} written by passenger with id {} in the database", trip, passenger.getUserId());
        final TypedQuery<Review> query = em.createQuery("from Review r where r.trip = :trip AND r.user = :user", Review.class);
        query.setParameter("trip",trip);
        query.setParameter("user",passenger.getUser());
        Optional<Review> result = query.getResultList().stream().findFirst();
        return result;
    }
}
