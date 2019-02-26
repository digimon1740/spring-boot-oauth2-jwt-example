package com.example.jwt.demo.web;

import com.example.jwt.demo.domain.user.User;
import com.example.jwt.demo.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping(path = "/me")
//    public ResponseEntity<UserDto> me(Principal principal) {
//        UserDto userDto = modelMapperUtils.convertToDto(userService.findByPrincipal(principal), UserDto.class);
//        return new ResponseEntity<>(userDto, HttpStatus.OK);
//    }

    @GetMapping(value = "")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(value = "")
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    //@PutMapping(value = "/{id}")
    //ResponseEntity<User> modify(Principal principal, @RequestBody BoardDto boardDto) {
//        User user = userService.findByPrincipal(principal);
//        boardService.modify(modelMapperUtils.convertToEntity(boardDto, Board.class), user.getId());
//        return new ResponseEntity<>(boardDto, HttpStatus.ACCEPTED);
    //  return null;
    //}

    @DeleteMapping(value = "/user/{id}")
    public String delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);
        return "success";
    }

}