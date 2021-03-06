package com.agustosoftware.storechecker.resource;

import com.agustosoftware.storechecker.domain.entity.User;
import com.agustosoftware.storechecker.service.AuthorizationService;
import com.agustosoftware.storechecker.service.RoleEnum;
import com.agustosoftware.storechecker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Controller
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserService service;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity createUser (@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader, @RequestBody User user) throws Exception {
        User caller = authorizationService.checkAndGetCallerFromAuthorizationHeader(authorizationHeader);
        authorizationService.verifyUserHasAtLeastOneRoleByName(caller, Collections.singletonList(RoleEnum.ADMIN.name()));

        User createUser = service.createUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createUser.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity getUser(@PathVariable String userId) throws Exception {
        User user = service.findUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping(path = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity updateUser(@PathVariable String userId, @RequestBody User user) throws Exception {
        User updatedUser = service.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(path = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity deleteUser(@PathVariable String userId) throws Exception {
        service.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }   

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity getUsers() throws Exception {
        return ResponseEntity.ok(service.listUsers());
    }



}
