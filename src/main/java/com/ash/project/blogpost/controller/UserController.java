package com.ash.project.blogpost.controller;

import com.ash.project.blogpost.model.User;
import com.ash.project.blogpost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/login")
    public String userLogin() {
        return "userLogin";
    }

    @RequestMapping("/register")
    public String newUserRegister(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @RequestMapping("/saveuser")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("AUTHOR");
        userService.saveUser(user);
//        String accountCreationMessage = "Thanks for creating Account";
//        model.addAttribute("accountCreationMessage", accountCreationMessage);
        return "userLogin";
    }
}
