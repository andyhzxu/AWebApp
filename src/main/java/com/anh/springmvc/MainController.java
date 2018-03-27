package com.anh.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HH
 */
@Controller
    public class MainController {

    @RequestMapping(value = "/uploadFile", method = {RequestMethod.GET, RequestMethod.POST})
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("............000");

        return "home";
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("............index");
        String json = "<html><h1>111</h1></html>";

        return json;
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("............home");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("home");

        return mav;
    }

}
