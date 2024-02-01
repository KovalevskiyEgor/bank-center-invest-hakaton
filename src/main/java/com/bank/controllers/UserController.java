package com.bank.controllers;


import com.bank.dto.ReviewDTO;
import com.bank.dto.UserDTO;
import com.bank.models.User;
import com.bank.service.EmailService;
import com.bank.service.ReviewService;
import com.bank.service.UserService;
import com.bank.utils.enums.EmailType;
import com.bank.utils.mappers.impl.ReviewMapper;
import com.bank.utils.mappers.impl.UserMapper;
import com.bank.validators.UserDTOValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User controller", description = "User API")
public class UserController extends MainController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserDTOValidator userDTOValidator;
    private final ReviewMapper reviewMapper;
    private final EmailService emailService;
    private final ReviewService reviewService;

    private final static String GET_ALL_USERS = "";
    private final static String CREATE_USER = "";
    private final static String GET_USER_BY_ID = "/{user_id}";
    private final static String DELETE_USER_BY_ID = "/{user_id}";
    private final static String UPDATE_USER_BY_ID = "/{user_id}";
    private final static String GET_REVIEW_LIST = "/{user_id}/review";

    @Operation(summary = "Get all users")
    @GetMapping(GET_ALL_USERS)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>
                (userMapper.toDTOs(userService.getAll()),
                        HttpStatus.OK);
    }

    @Operation(summary = "Get user by ID")
    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<Object> getUserById(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>
                (userMapper.toDTO(userService.getById(userId)),
                        HttpStatus.OK);
    }

    @Operation(summary = "Get users review")
    @GetMapping(GET_REVIEW_LIST)
    public ResponseEntity<List<ReviewDTO>> getReviewList(
            @PathVariable("user_id") Long userId) {
        User user = userService.getById(userId);
        return ResponseEntity
                .ok()
                .body(reviewMapper.toDTOs(user.getReviews()));
    }

    @Operation(summary = "Create new user")
    @PostMapping(CREATE_USER)
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult){

        userDTOValidator.validate(userDTO, bindingResult);

        User user = userMapper.fromDTO(userDTO);

        User save = userService.save(user);

        return ResponseEntity
                .ok()
                .body(userMapper.toDTO(save));
    }

    @Operation(summary = "Update user")
    @PatchMapping(UPDATE_USER_BY_ID)
    public ResponseEntity<UserDTO> updateUserById(
            @PathVariable("user_id") Long userId,
            @RequestBody @Valid UserDTO userDTO,
            BindingResult bindingResult){

        userDTOValidator.validate(userDTO, bindingResult);

        User user = userMapper.fromDTO(userDTO);

        User update = userService.update(userId, user);

        return ResponseEntity
                .ok()
                .body(userMapper.toDTO(update));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping(DELETE_USER_BY_ID)
    public ResponseEntity<HttpStatus> deleteEventById(
            @PathVariable("user_id") Long id){
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/payment")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void payAndGetCheck(){
        emailService.sendEmailMessage(userService.getAuthorizedUser(), EmailType.PAYMENT, new Properties());
    }

}
