package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.services.CarService;
import ar.edu.itba.paw.interfaces.services.CityService;
import ar.edu.itba.paw.interfaces.services.TripService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.models.Car;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.trips.Trip;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.AuthUser;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.exceptions.CityNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.TripNotFoundException;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CreateTripForm;
import ar.edu.itba.paw.webapp.form.DiscoveryForm;
import ar.edu.itba.paw.webapp.form.SelectionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class TripController {
    private final TripService tripService;
    private final CityService cityService;
    private final UserService userService;
    private final CarService carService;
    private final ImageService imageService;

    private final static long DEFAULT_PROVINCE_ID = 1;

    @Autowired
    public TripController(TripService tripService, CityService cityService, UserService userService, CarService carService, ImageService imageService){
        this.tripService = tripService;
        this.cityService = cityService;
        this.userService = userService;
        this.carService = carService;
        this.imageService = imageService;
    }

    @RequestMapping(value = "/trips/{id:\\d+$}",method = RequestMethod.GET)
    public ModelAndView getTripDetails(@PathVariable("id") final long tripId,
                                       @ModelAttribute("selectForm") final SelectionForm form
                                       ){
//        User driver = userService.createUser("jmentasti@itba.edu.ar","1129150686");
//        Trip trip = tripService.createTrip(cityService.findById(1),"Av Callao 1348",cityService.findById(3),"Av Cabildo 1200","AE062TP","12/2/22","12:00",2,driver);
        Optional<Trip> trip = tripService.findById(tripId);
        if(!trip.isPresent()){//Usar Optional?
            throw new TripNotFoundException();
        }
        ModelAndView mv = new ModelAndView("/select-trip/trip-detail");
        mv.addObject("trip",trip.get());
        return mv;
    }

    @RequestMapping(value = "/trips/{id:\\d+$}",method = RequestMethod.POST)
    public ModelAndView addPassengerToTrip(@PathVariable("id") final long tripId,
                                           @Valid @ModelAttribute("selectForm") final SelectionForm form,
                                           final BindingResult errors){
        if(errors.hasErrors()){
            return getTripDetails(tripId,form);
        }
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //final String userId = userService.findByEmail(email).orElseThrow(UsernameNotFoundException::new).getUserId();
        final User passenger = userService.findByEmail(authUser.getUsername()).orElseThrow(UserNotFoundException::new);
        //User passenger = userService.createUserIfNotExists(form.getEmail(), form.getPhone(), form.getPhone());
        //sacar el form.getPhone(), esta solo para que no falle
//        boolean ans = tripService.addPassenger(tripId,passenger);
//        Optional<Trip> trip = tripService.findById(tripId);
//        if(ans && trip.isPresent()){
//        User passenger = userService.createUserIfNotExists(form.getEmail(),form.getPhone());
        //TODO: agregar excepciones nuestras y nuestro manejo
        Trip trip = tripService.findById(tripId).orElseThrow(TripNotFoundException::new);
        boolean ans = tripService.addPassenger(trip,passenger,trip.getStartDateTime());
        if(ans){
            ModelAndView successMV = new ModelAndView("/select-trip/success");
            successMV.addObject("trip",trip);
            successMV.addObject("passenger",passenger);
            return successMV;
        }
        ModelAndView mv = new ModelAndView("/select-trip/response");
        mv.addObject("response",ans);
        return mv;
    }

    @RequestMapping(value = {"/trips"}, method = RequestMethod.POST)
    public ModelAndView getSelectedTrips(@Valid @ModelAttribute("registerForm") final DiscoveryForm form, final BindingResult errors){
        if(errors.hasErrors()){
            return getTrips(form);
        }
        List<City> cities = cityService.getCitiesByProvinceId(DEFAULT_PROVINCE_ID);
        final List<Trip> trips = tripService.getTripsByDateTimeAndOriginAndDestination(form.getOriginCityId(),form.getDestinationCityId(), form.getDate(),form.getTime(), form.getDate(), form.getTime(),0,10).getElements();
        final ModelAndView mav = new ModelAndView("/discovery/main");
        mav.addObject("trips", trips);
        mav.addObject("cities", cities);

        return mav;
    }
    @RequestMapping(value = {"/","/trips"}, method = RequestMethod.GET)
    public ModelAndView getTrips(@ModelAttribute("registerForm") final DiscoveryForm form){
        List<City> cities = cityService.getCitiesByProvinceId(DEFAULT_PROVINCE_ID);
        List<Trip> trips = tripService.getIncomingTrips(0,10).getElements();
//        System.out.println(trips.get(0));
        final ModelAndView mav = new ModelAndView("/discovery/main");
        mav.addObject("trips", trips);
        mav.addObject("cities", cities);
        return mav;
    }

    @RequestMapping(value = "/trips/create", method = RequestMethod.GET)
    public ModelAndView createTripForm(@ModelAttribute("createTripForm") final CreateTripForm form){
        List<City> cities = cityService.getCitiesByProvinceId(DEFAULT_PROVINCE_ID);
        final ModelAndView mav = new ModelAndView("/create-trip/main");
        mav.addObject("cities", cities);

        return mav;
    }

    @RequestMapping(value = "/trips/create", method = RequestMethod.POST)
    public ModelAndView createTrip(
            @Valid @ModelAttribute("createTripForm") final CreateTripForm form,
            final BindingResult errors
    ){
        if(errors.hasErrors()){
            return createTripForm(form);
        }
        City originCity = cityService.findCityById(form.getOriginCityId()).orElseThrow(CityNotFoundException::new);
        City destinationCity = cityService.findCityById(form.getDestinationCityId()).orElseThrow(CityNotFoundException::new);
        final AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //final String userId = userService.findByEmail(email).orElseThrow(UsernameNotFoundException::new).getUserId();
        final User user = userService.findByEmail(authUser.getUsername()).orElseThrow(UserNotFoundException::new);
        //User user = userService.createUserIfNotExists(form.getEmail(),form.getPhone(), form.getPhone());
        //TODO: get car from user list (in CreateTripForm)
        Car car = carService.createCarIfNotExists(form.getCarPlate(), form.getCarInfo(), user);
        //TODO: get price for trip
        Trip trip = tripService.createTrip(originCity, form.getOriginAddress(), destinationCity, form.getDestinationAddress(), car, form.getOriginDate(), form.getOriginTime(),0.0, form.getMaxSeats(),user,form.getOriginDate(), form.getOriginTime());
        final ModelAndView mav = new ModelAndView("/create-trip/success");
        mav.addObject("trip", trip);

        return mav;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public ModelAndView showUploadForm() {
        ModelAndView mav = new ModelAndView("/image-tester/uploadForm");
        return mav;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ModelAndView handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] data = file.getBytes();
        Image image=imageService.createImage(data);
        ModelAndView modelAndView = new ModelAndView("/image-tester/imageDetails");
        modelAndView.addObject("image", image);
        return modelAndView;
    }

    @RequestMapping(value = "/image/{imageId}", method = RequestMethod.GET, produces = "image/*")
    public @ResponseBody
    byte[] getImage(@PathVariable("imageId") final int imageId) {
        return imageService.findById(imageId).orElseThrow(ImageNotFoundException::new).getData();
    }

//    @RequestMapping(value = "*", method = RequestMethod.GET)
//    public ModelAndView pageNotFound() {
//        return new ModelAndView("/static/not-found-404");
//    }
}
