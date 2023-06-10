package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.UserDao;
import ar.edu.itba.paw.interfaces.services.CityService;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.City;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ImageService imageService;

    private final CityService cityService;

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private enum Roles{
        USER("USER"),
        DRIVER("DRIVER");
        private final String role;
        private Roles(String role){
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder,final AuthenticationManager authenticationManager, final ImageService imageService1, final CityService cityService1){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.imageService=imageService1;
        this.cityService=cityService1;
    }

    @Transactional
    @Override
    public User createUser(final String username, final String surname, final String email,
                           final String phone, final String password, final City bornCity, final Locale mailLocale, final String role, long user_image_id) throws EmailAlreadyExistsException{

        String finalRole = (role == null) ? Roles.USER.role : role;
        Optional<User> possibleUser = userDao.findByEmail(email);
        if(possibleUser.isPresent()){
            LOGGER.debug("Email '{}' already exists in the database", email);
            if(possibleUser.get().getPassword()!=null){
                final EmailAlreadyExistsException exception = new EmailAlreadyExistsException();
                LOGGER.error("Email '{}' already exists in the database and has already registered", email, exception);
                throw exception;
            }else{
                LOGGER.debug("Email '{}' already exists in the database but has not registered yet, updating user", email);
                User ans = userDao.updateProfile(username, surname, email, passwordEncoder.encode(password), bornCity, mailLocale.toString(), finalRole, user_image_id);
                LOGGER.info("User with email '{}' updated in the database", email);
                return ans;
            }
        }
        return userDao.create(username,surname,email, phone, passwordEncoder.encode(password), bornCity, mailLocale, finalRole, user_image_id);
    }

    @Override
    public void loginUser(final String email, final String password){
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(authRequest);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public Optional<User> getCurrentUser(){
        final Object authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (! (authUser instanceof org.springframework.security.core.userdetails.User)){
            return Optional.empty();
        }
        final org.springframework.security.core.userdetails.User aux = (org.springframework.security.core.userdetails.User) authUser;
        return findByEmail(aux.getUsername());
    }

    @Override
    public Optional<User> findById(long userId){
        return userDao.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email){
        return userDao.findByEmail(email);
    }

    @Transactional
    @Override
    public void changeToDriver(User user) {
        userDao.changeRole(user.getUserId(), Roles.DRIVER.role);
    }

    @Transactional
    @Override
    public void modifyUser(long userId, String username, String surname, String phone, long bornCityId, Locale mailLocale, byte[] imgData) {
        Optional<User> user = findById(userId);
        if(user.isPresent()){
            imageService.replaceImage(user.get().getUserImageId(),imgData);
        }
        userDao.modifyUser(userId,username,surname,phone,cityService.findCityById(bornCityId).get(),mailLocale);
    }

}
