package com.chayxana.chayxana.controller;

import com.chayxana.chayxana.entity.User;
import com.chayxana.chayxana.exceptions.PageSizeException;
import com.chayxana.chayxana.payload.ApiResponse;
import com.chayxana.chayxana.payload.UserDto;
import com.chayxana.chayxana.secret.CurrentUser;
import com.chayxana.chayxana.service.UserService;
import com.chayxana.chayxana.utills.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mobile/user")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class UserController {

    private final UserService userService;

    @GetMapping("/getUserById")
    public HttpEntity<?> getOneUser(@RequestParam UUID id) {
        ApiResponse apiResponse = userService.getOneUser(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> addUser(@RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.addUser(userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @DeleteMapping
    public HttpEntity<?> deleteUser(@RequestParam UUID id) {
        ApiResponse apiResponse = userService.deleteUser(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping()
    public HttpEntity<?> editUser(@RequestParam(name = "id") UUID id, @RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.editUserByUser(id, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 203).body(apiResponse);
    }

    @GetMapping()
    public HttpEntity<?> getAllPageable(
            @RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
            @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) throws  PageSizeException {
        ApiResponse apiResponse = userService.getAllPageable(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/disable/{id}")
    public HttpEntity<?> disableUser(@PathVariable UUID id, @RequestParam(name = "disable") boolean disable) {
        ApiResponse apiResponse = userService.disableUser(id, disable);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getChayxanaAdmins")
    public HttpEntity<?> getAllChayxanaIds(@RequestParam(value = "page",defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                           @RequestParam(value = "size",defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size) throws  PageSizeException {
        ApiResponse apiResponse = userService.getAllChayxanachi(page,size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);

    }

    /**
     * @param id
     * @return ResponseEntity<ApiResponse>
     *     Userni disabled qilish uchun
     */
    @PostMapping("/{id}")
    public HttpEntity<?> bangUser(@PathVariable UUID id, @RequestParam(name = "bang") boolean bang) {
        ApiResponse apiResponse = userService.bangUser(id,bang);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping("/get-current-user-by-token")
    public HttpEntity<?> getUserbyToken(@CurrentUser User user){
        ApiResponse response = userService.getUserByToken(user);
        return ResponseEntity.ok(response);
    }
}
