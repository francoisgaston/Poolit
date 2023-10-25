package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.exceptions.CityNotFoundException;
import ar.edu.itba.paw.interfaces.exceptions.EmailAlreadyExistsException;
import ar.edu.itba.paw.interfaces.exceptions.UserNotFoundException;
import ar.edu.itba.paw.interfaces.services.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.controller.mediaType.VndType;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserRoleDto;
import ar.edu.itba.paw.webapp.dto.user.PrivateUserDto;
import ar.edu.itba.paw.webapp.dto.user.PublicUserDto;
import ar.edu.itba.paw.webapp.form.CreateUserForm;
import ar.edu.itba.paw.webapp.form.UpdateUserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;

@Path("/api/users")
@Component
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    @Context
    private UriInfo uriInfo;

    @Inject
    @Autowired
    public UserController(final UserService userService){
        this.userService = userService;
    }

    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_USER_PUBLIC)
    public Response getByIdPublic(@PathParam("id") final long id) throws UserNotFoundException{
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(PublicUserDto.fromUser(uriInfo,user)).build();
    }


//    TODO: ver por qué agrega "type" a la respuesta
    @GET
    @Path("/{id}")
    @Produces(VndType.APPLICATION_USER_PRIVATE)
    @PreAuthorize("@authValidator.checkIfWantedIsSelf(#id)") //TODO: ver por que lleva a 404
    public Response getByIdPrivate(@PathParam("id") final long id) throws UserNotFoundException{
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(PrivateUserDto.fromUser(uriInfo,user)).build();
    }

//    @GET
//    @Path("/{id}")
//    @Produces(VndType.APPLICATION_USER_PASSENGER)
////    @PreAuthorize("@authValidator.checkIfWantedIsSelf(#id)")
//    public Response getByIdPassenger(@PathParam("id") final long id) throws UserNotFoundException{
//        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
//        return Response.ok(UserDto.fromUser(uriInfo,user)).build();
//    }



    @POST
    @Produces( value = {MediaType.APPLICATION_JSON})
    public Response createUser(@Valid final CreateUserForm userForm) throws EmailAlreadyExistsException, CityNotFoundException, IOException {
        final User user = userService.createUser(userForm.getUsername(), userForm.getSurname(), userForm.getEmail(), userForm.getPhone(),
                userForm.getPassword(), userForm.getBornCityId(), userForm.getMailLocale(), null, userForm.getImageFile().getBytes());
        final URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(user.getUserId())).build();
        return Response.created(uri).build();
    }

    @PUT
    @Path("/{id}")
    @Produces( value = { MediaType.APPLICATION_JSON } )
    public Response modifyUser(@PathParam("id") final long id, @Valid final UpdateUserForm userForm) throws UserNotFoundException, IOException, CityNotFoundException {
        userService.modifyUser(id, userForm.getUsername(),userForm.getSurname(),userForm.getPhone(),userForm.getBornCityId(),userForm.getMailLocale(), userForm.getImageFile().getBytes());
        return Response.status(Response.Status.OK).build();
    }

    //TODO: revisar, lo necesitamos siempre al rol, pero como no lo cambiamos en el put lo pusimos aca
    @GET
    @Path("/{id}/role")
    @Produces( value = { MediaType.APPLICATION_JSON } )
    public Response getRole(@PathParam("id") final long id) throws UserNotFoundException{
        final User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
        return Response.ok(UserRoleDto.fromString(user.getRole())).build();
    }

    @PUT
    @Path("/{id}/role")
    @Produces( value = { MediaType.APPLICATION_JSON } )
    public Response modifyRole(@PathParam("id") final long id, @Valid final UserRoleDto userRoleDto) throws UserNotFoundException{
        userService.changeRole(id,userRoleDto.getRole());
        return Response.status(Response.Status.OK).build();
    }

}
