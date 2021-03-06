package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class MainController {
    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    public MainController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    //@RequestMapping(value = { "/usersList" }, method = RequestMethod.GET)
    @GetMapping()
    //Возвращаем список из людей
    public String allUsers (Model model, @AuthenticationPrincipal User user) {
        List<User> users = userService.getAllUsers();
        Set<Role> listRoles = userService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("userObj", new User());
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("userRep", userRepository.findByUsername(user.getUsername()));
        return "admin";
    }
    @GetMapping("/{id}")
    // Получим одного человека из ДАО и передадим в отображение
    public String showById (@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "admin";
    }
    /*@GetMapping("/create")
    public String newUserForm (User user, Model model) { //@ModelAttribute("user")
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("listRoles", listRoles);
        //model.addAttribute("user", new User());
        return "create";
    }
    */
    @PostMapping("/create")
    public String create(User user, @RequestParam("listRoles") Set<Role> roles) { //@ModelAttribute("user")
        userService.addUser(user, roles);
        return "redirect:/admin";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUserForm(@ModelAttribute("user") User user, @RequestParam("listRoles") Set<Role> roles) {
        userService.addUser(user, roles);
        return "redirect:/admin";
    }

   /* @GetMapping("/update/{id}")
    public String edit (@PathVariable("id") long id,Model model) {
        User user = userService.getUser(id);
        List<Role> listRoles = userService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "update";
    }
    @PostMapping ("/update")
    public String editUsers(User user) {
        userService.addUser(user);//addUser(user)
        return "redirect:/admin";
    }

    */
}
