package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorsController {

    @RequestMapping("/static/403")
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView error403(){
        return new ModelAndView("static/403");
    }
}
