package com.ProjectByElvara.controllers;


import com.ProjectByElvara.configs.PageConfig;
import com.ProjectByElvara.configs.enums.Pages;
import com.ProjectByElvara.dto.UserDTO;
import com.ProjectByElvara.entities.additionalClasses.UserRole;
import com.ProjectByElvara.services.MyUserDetailsService;
import com.ProjectByElvara.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;


@Controller
public class UserController {
    private final UserService userService;
    private final MyUserDetailsService myUserDetailsService;

    public UserController(UserService userService, UserDetailsService myUserDetailsService) {
        this.userService = userService;
        this.myUserDetailsService = (MyUserDetailsService) myUserDetailsService;
    }

    @GetMapping(value = "/signup")
    public ModelAndView openSignUpPage() {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.SIGN_UP));
        modelAndView.addObject("newUser", new UserDTO());
        return modelAndView;
    }

    @PostMapping(value = "/signup")
    public RedirectView signUp(@ModelAttribute(name = "newUser") UserDTO currentUser,
                               RedirectAttributes redirectAttributes) throws SQLException {
        if (this.userService.signUpUser(currentUser)) {
            redirectAttributes.addFlashAttribute("message", "Вітаємо!\nВаш аккаунт успішно створено.");
            UserDetails updatedUserDetails = this.myUserDetailsService.loadUserByUsername(currentUser.getEmail());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    updatedUserDetails,
                    updatedUserDetails.getPassword(),
                    updatedUserDetails.getAuthorities()));
            return new RedirectView("/products");
        } else {
            redirectAttributes.addFlashAttribute("message", "Аккаунт з такою поштою вже зареєстрований!");
            return new RedirectView("/signup");
        }
    }

    @GetMapping(value = "/login")
    public ModelAndView openLogInPage() {
        return new ModelAndView(PageConfig.getInstance().getProperty(Pages.LOGIN));
    }

    @GetMapping(value = "/loginFailure")
    private RedirectView failureLogin(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "Неправильна пошта чи пароль!");
        return new RedirectView("/login");
    }

    @GetMapping(value = "/user")
    public ModelAndView openUserProfile(@RequestParam(name = "id") String id,
                                        RedirectAttributes redirectAttributes) throws SQLException {
        UserDTO user = this.userService.getUserById(
                this.myUserDetailsService.hasAccess(UserRole.ROLE_ADMIN) ? Integer.parseInt(id) : this.myUserDetailsService.getSessionUserId()
        );
        if (user == null) {
            if (this.myUserDetailsService.hasAccess(UserRole.ROLE_ADMIN)) {
                redirectAttributes.addFlashAttribute("message", "Користувача не знайдено");
                return new ModelAndView(new RedirectView("/users"));
            }
            if (this.myUserDetailsService.hasAccess(UserRole.ROLE_CLIENT)) {
                return new ModelAndView(new RedirectView("/logout"));
            }
        }
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.USER));
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping(value = "/updateUserInfo")
    public RedirectView updateUserInfo(@ModelAttribute(name = "user") UserDTO updatedUser,
                                       RedirectAttributes redirectAttributes) throws SQLException {
        this.userService.updateUserInfo(updatedUser);
        UserDetails updatedUserDetails = this.myUserDetailsService.loadUserByUsername(updatedUser.getEmail());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                updatedUserDetails,
                updatedUserDetails.getPassword(),
                updatedUserDetails.getAuthorities()));
        redirectAttributes.addFlashAttribute("message", "Дані було оновлено");
        return new RedirectView("/user?id=" + updatedUser.getId());
    }

    @PostMapping(value = "/updateUserPassword")
    public RedirectView changeUserPassword(@RequestParam(name = "id") String id,
                                           @RequestParam(name = "oldPassword") String oldPassword,
                                           @RequestParam(name = "newPassword") String newPassword,
                                           RedirectAttributes redirectAttributes) throws SQLException {
        if (this.userService.updateUserPassword(id, oldPassword, newPassword)) {
            redirectAttributes.addFlashAttribute("message", "Пароль успішно оновлено!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Неправильний старий пароль!");
        }
        return new RedirectView("/user?id=" + id);
    }

    @PostMapping(value = "/deleteUser")
    public RedirectView deleteUser(@RequestParam(name = "email") String email,
                                   RedirectAttributes redirectAttributes) throws SQLException {
        this.userService.deleteUser(email);
        if (this.myUserDetailsService.hasAccess(UserRole.ROLE_ADMIN)) {
            redirectAttributes.addFlashAttribute("message", "Користувача успішно видалено");
            return new RedirectView("/users");
        }
        else {
            return new RedirectView("/logout");
        }
    }

    @GetMapping(value = "/users")
    public ModelAndView openUsersPage(@RequestParam(required = false, name = "emailPattern") String emailPattern) throws SQLException {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.USERS));
        if (emailPattern != null) {
            modelAndView.addObject("emailPattern", emailPattern);
            modelAndView.addObject("users", this.userService.getAllUsers(emailPattern));
        } else {
            modelAndView.addObject("users", this.userService.getAllUsers());
        }
        return modelAndView;
    }
}
