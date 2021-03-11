package guru.springframework.controllers;

import guru.springframework.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Javid on 3/1/2021.
 */

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception e) {
        log.error("Handling number format exception: " + e.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/400error");
        modelAndView.getModelMap().addAttribute("title", "400 Bad Request");
        modelAndView.getModelMap().addAttribute("message", e.getMessage());

        return modelAndView;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception e) {
        log.error("Handling not found exception: " + e.getMessage());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/404error");
        modelAndView.getModelMap().addAttribute("title", "404 Not Found");
        modelAndView.getModelMap().addAttribute("message", e.getMessage());

        return modelAndView;
    }
}
