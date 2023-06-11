package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exceptions.CarNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.CityNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.models.trips.Trip;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.exceptions.UserNotLoggedInException;
import ar.edu.itba.paw.webapp.form.CreateUserForm;
import ar.edu.itba.paw.webapp.form.UpdateUserForm;
import ar.edu.itba.paw.webapp.form.*;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.interfaces.exceptions.EmailAlreadyExistsException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Controller
public class UserController extends LoggedUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final CityService cityService;

    private final ReviewService reviewService;
    private final CarService carService;

    private final TripService tripService;

    private final ImageService imageService;

    private final TokenService tokenService;

    //TODO: sacar de aca, llevarlo al service
    private final EmailService emailService;

    private final PawUserDetailsService pawUserDetailsService;

    private final UserService userService;
    private final static long DEFAULT_PROVINCE_ID = 1;
    private final static String BASE_RELATED_PATH = "/users/";
    private final static String CREATE_USER_PATH = BASE_RELATED_PATH + "create";
    private final static String LOGIN_USER_PATH = BASE_RELATED_PATH + "login";


    private final static int PAGE_SIZE = 3;

    @Autowired
    public UserController(final CityService cityService, ReviewService reviewService, final  UserService userService,
                          final PawUserDetailsService pawUserDetailsService, final TripService tripService,
                          final CarService carService, final ImageService imageService,
                          final TokenService tokenService, final EmailService emailService) {
        super(userService);
        this.cityService = cityService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.pawUserDetailsService = pawUserDetailsService;
        this.tripService = tripService;
        this.carService = carService;
        this.imageService = imageService;
        this.tokenService = tokenService; //TODO: sacar
        this.emailService = emailService; //TODO: sacar
    }

    @RequestMapping(value = CREATE_USER_PATH, method = RequestMethod.GET)
    public ModelAndView createUserGet(
             @ModelAttribute("createUserForm") final CreateUserForm form
    ) {
        LOGGER.debug("GET Request to {}", CREATE_USER_PATH);
        final List<City> cities = cityService.getCitiesByProvinceId(DEFAULT_PROVINCE_ID);
        final ModelAndView mav = new ModelAndView("users/register");
        mav.addObject("cities", cities);
        mav.addObject("postUrl", CREATE_USER_PATH);
        return mav;
    }


    @RequestMapping(value = CREATE_USER_PATH, method = RequestMethod.POST)
    public ModelAndView createUserPost(
            @Valid @ModelAttribute("createUserForm") final CreateUserForm form, final BindingResult errors
    ) throws IOException {
        LOGGER.debug("POST Request to {}", CREATE_USER_PATH);
        if(errors.hasErrors()){
            LOGGER.warn("Errors found in CreateUserForm: {}", errors.getAllErrors());
            return createUserGet(form);
        }
        final byte[] data = form.getImageFile().getBytes();
        final Image image=imageService.createImage(data);
        final City originCity = cityService.findCityById(form.getBornCityId()).orElseThrow(() -> new CityNotFoundException(form.getBornCityId()));
        try {
            User user = userService.createUser(form.getUsername(), form.getSurname(), form.getEmail(), form.getPhone(),
                    form.getPassword(), originCity, new Locale(form.getMailLocale()), null, image.getImageId());
            VerificationToken token = tokenService.createToken(user);
            emailService.sendVerificationEmail(user, token.getToken());

            //userService.loginUser(form.getEmail(), form.getPassword());
        }catch (EmailAlreadyExistsException e){
            errors.rejectValue("email", "validation.email.alreadyExists");
            LOGGER.warn("Email already exists: {}", form.getEmail());
            return createUserGet(form);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        final ModelAndView mav = new ModelAndView("/users/login");
        mav.addObject("sentToken", true);
        return mav;
    }

    @RequestMapping(value = LOGIN_USER_PATH, method = RequestMethod.GET)
    public ModelAndView loginUserGet() {
        LOGGER.debug("GET Request to {}", LOGIN_USER_PATH);
        final ModelAndView mav = new ModelAndView("users/login");
        return mav;
    }

    @RequestMapping(value = LOGIN_USER_PATH, method = RequestMethod.POST)
    public ModelAndView loginUserPost() {
        LOGGER.debug("POST Request to {}", LOGIN_USER_PATH);
        return new ModelAndView("users/login");
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.GET)
    public ModelAndView profileView(@RequestParam(value = "carAdded", required = false, defaultValue = "false") final Boolean carAdded,
                                    @ModelAttribute("updateUserForm") final UpdateUserForm form){
        LOGGER.debug("GET Request to /users/profile");
        final User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        final List<Trip> futureTripsPassenger = tripService.getTripsWhereUserIsPassengerFuture(user, 0, PAGE_SIZE).getElements();
        final List<Trip> pastTripsPassenger = tripService.getTripsWhereUserIsPassengerPast(user, 0, PAGE_SIZE).getElements();
        final List<Review> reviewsAsUser = reviewService.getUsersIdReviews(user);
        final List<City> cities = cityService.getCitiesByProvinceId(DEFAULT_PROVINCE_ID);

        form.setBornCityId(user.getBornCity().getId());
        form.setMailLocale(user.getMailLocale().getLanguage());
        form.setPhone(user.getPhone());
        form.setSurname(user.getSurname());
        form.setUsername(user.getName());

        if(Objects.equals(user.getRole(), "USER")){

            final ModelAndView mav = new ModelAndView("/users/user-profile");
            mav.addObject("cities", cities);
            mav.addObject("user", user);
            mav.addObject("futureTripsPassanger", futureTripsPassenger);
            mav.addObject("pastTripsPassanger", pastTripsPassenger);
            mav.addObject("reviewsAsUser", reviewsAsUser);
            return mav;
        }
        final List<Review> reviews = reviewService.getDriverReviews(user);
        final List<Trip> futureTrips = tripService.getTripsCreatedByUserFuture(user, 0, PAGE_SIZE).getElements();
        final List<Trip> pastTrips = tripService.getTripsCreatedByUserPast(user, 0, PAGE_SIZE).getElements();
        final List<Car> cars = carService.findByUser(user);
        final Double rating = reviewService.getDriverRating(user);

        final ModelAndView mav = new ModelAndView("/users/driver-profile");
        mav.addObject("user", user);
        mav.addObject("rating", rating);
        mav.addObject("futureTrips", futureTrips);
        mav.addObject("pastTrips",pastTrips);
        mav.addObject("futureTripsPassanger", futureTripsPassenger);
        mav.addObject("pastTripsPassanger",pastTripsPassenger);
        mav.addObject("cars", cars);
        mav.addObject("carAdded", carAdded);
        mav.addObject("reviews", reviews);
        mav.addObject("reviewsAsUser", reviewsAsUser);
        mav.addObject("cities", cities);
        return mav;
    }

    @RequestMapping(value = "/users/profile", method = RequestMethod.POST)
    public ModelAndView profileViewUpdate(@Valid @ModelAttribute("updateUserForm") final UpdateUserForm form,
                                          final BindingResult errors) throws IOException{
        LOGGER.debug("POST Request to /users/profile");
        if(errors.hasErrors()){
            LOGGER.warn("Errors found in updateUserForm: {}", errors.getAllErrors());
            return profileView(false,form);
        }
        final User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        userService.modifyUser(user.getUserId(), form.getUsername(),form.getSurname(),form.getPhone(),form.getBornCityId(),new Locale(form.getMailLocale()), form.getImageFile().getBytes());

        return profileView(false,form);
    }

    @RequestMapping(value = "/profile/{id:\\d+$}", method = RequestMethod.GET)
    public ModelAndView profilePost(@PathVariable("id") final long userId)
    {
        LOGGER.debug("GET Request to /profile/{}", userId);
        final User user = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        //TODO: hacer un redirect al perfil privado?
        final User currentUser = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        boolean isBlocked = userService.isBlocked(currentUser,user);
        boolean isOwnProfile = user.equals(currentUser);
        if(Objects.equals(user.getRole(), "USER")){
            List<Review> reviews = reviewService.getUsersIdReviews(user);

            final ModelAndView mav = new ModelAndView("/users/public-profile");
            mav.addObject("isBlocked", isBlocked);
            mav.addObject("isOwnProfile",isOwnProfile);
            mav.addObject("user", user);
            mav.addObject("reviews", reviews);
            return mav;
        }
        final List<Review> reviews = reviewService.getDriverReviews(user);
        final Double rating = reviewService.getDriverRating(user);
        final PagedContent<Trip> createdTrips = tripService.getTripsCreatedByUser(user,0,0);

        final ModelAndView mav = new ModelAndView("/users/public-profile");
        mav.addObject("isBlocked", isBlocked);
        mav.addObject("isOwnProfile",isOwnProfile);
        mav.addObject("user", user);
        mav.addObject("rating", rating);
        mav.addObject("countTrips",createdTrips.getTotalCount());
        mav.addObject("reviews", reviews);
        return mav;
    }
    //TODO: que reciban solo el id los metodos y hagan la logica atras
    @RequestMapping(value="/profile/{id:\\d+$}/block", method = RequestMethod.POST)
    public ModelAndView profileBlock(@PathVariable("id") final long userId){
        LOGGER.debug("Blocking /profile/{}", userId);
        final User userBlocked = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        final User userBlocker = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        userService.blockUser(userBlocker, userBlocked);

        return new ModelAndView("redirect:/profile/" + userId);

    }
    @RequestMapping(value="/profile/{id:\\d+$}/unblock", method = RequestMethod.POST)
    public ModelAndView profileUnblock(@PathVariable("id") final long userId){
        LOGGER.debug("Unblocking /profile/{}", userId);
        final User userBlocked = userService.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        final User userBlocker = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);

        userService.unblockUser(userBlocker, userBlocked);

        return new ModelAndView("redirect:/profile/" + userId);

    }
    @RequestMapping(value = "/changeRole", method = RequestMethod.POST)
    public ModelAndView changeRoleToDriver(){
        LOGGER.debug("POST Request to /changeRole");
        final User user = userService.getCurrentUser().orElseThrow(UserNotLoggedInException::new);
        pawUserDetailsService.update(user);
        userService.changeToDriver(user);
        return new ModelAndView("redirect:/trips/create");
    }

    @RequestMapping(value = "/register/confirm")
    public ModelAndView confirmRegistration(@RequestParam("token") final String token) {
        VerificationToken verificationToken = tokenService.getToken(token).orElse(null);

        if (userService.confirmRegister(verificationToken)) {
            return new ModelAndView("redirect:/");
        }
        final ModelAndView mav = new ModelAndView("/users/sendToken");
        mav.addObject("failToken", true);
        mav.addObject("emailForm", new EmailForm());
        return mav;
    }

    @RequestMapping(value = "/users/sendToken", method = RequestMethod.GET)
    public ModelAndView sendToken(@ModelAttribute("emailForm") final EmailForm form) {
         return new ModelAndView("/users/sendToken");
    }

    @RequestMapping(value = "/users/sendToken", method = RequestMethod.POST)
    public ModelAndView postCar(@Valid @ModelAttribute("emailForm") final EmailForm form,
                                final BindingResult errors) throws Exception {
        if(errors.hasErrors()){
            return sendToken(form);
        }
        final ModelAndView mav = new ModelAndView("/users/login");
        //TODO: Revisar reducir la logica en controller
        Optional<User> user = userService.findByEmail(form.getEmail());
        if(user.isPresent()){
            if(user.get().isEnabled()){
                mav.addObject("alreadyValidation", true);
                return mav;
            }
            String token = tokenService.updateToken(user.get());
            emailService.sendVerificationEmail(user.get(), token);
        }
        mav.addObject("sentToken", true);
        return mav;
    }


}
