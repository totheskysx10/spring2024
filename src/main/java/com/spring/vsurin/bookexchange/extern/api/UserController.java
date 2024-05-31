package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "���������� ��������������", description = "API ��� ���������� ��������������")
@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    @Autowired
    public UserController(UserService userService, UserAssembler userAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @Operation(summary = "���������� ������������ �� ID", description = "���������� ���������� � ������������ �� ��� ID")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userAssembler.toModel(user));
    }

    @Operation(summary = "������� ������������", description = "������� ������������ �� ��� ID")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "���������� ������ �� �������� ������������", description = "���������� ������ �� �������� ������������")
    @PostMapping("/request-delete/{userId}")
    public ResponseEntity<Void> sendRequestToDeleteUser(@PathVariable long userId, @RequestBody String reason) {
        userService.sendRequestToDeleteUser(userId, reason);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "��������� ����� � ���������� ������������", description = "��������� ����� � ���������� ������������")
    @PutMapping("/{userId}/library/{bookId}")
    public ResponseEntity<Void> addBookToUserLibrary(@PathVariable long userId, @PathVariable long bookId) {
        userService.addBookToUserLibrary(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ����� � ������ ������������ �������������", description = "��������� ����� � ������ ������������ �������������")
    @PutMapping("/{userId}/offered-books/{bookId}")
    public ResponseEntity<Void> addBookToOfferedByUser(@PathVariable long userId, @PathVariable long bookId) {
        userService.addBookToOfferedByUser(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������� ����� �� ������ ������������ �������������", description = "������� ����� �� ������ ������������ �������������")
    @DeleteMapping("/{userId}/offered-books/{bookId}")
    public ResponseEntity<Void> removeBookFromOfferedByUser(@PathVariable long userId, @PathVariable long bookId) {
        userService.removeBookFromOfferedByUser(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������� ����� �� ���������� ������������", description = "������� ����� �� ���������� ������������")
    @DeleteMapping("/{userId}/library/{bookId}")
    public ResponseEntity<Void> removeBookFromUserLibrary(@PathVariable long userId, @PathVariable long bookId) {
        userService.removeBookFromUserLibrary(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ����� � ������������", description = "��������� ����� � ������������")
    @PutMapping("/{userId}/addresses")
    public ResponseEntity<Void> addAddressToUser(@PathVariable long userId, @RequestBody String address) {
        userService.addAddressToUser(userId, address);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������� ����� � ������������", description = "������� ����� � ������������")
    @DeleteMapping("/{userId}/addresses")
    public ResponseEntity<Void> removeAddressFromUser(@PathVariable long userId, @RequestBody String address) {
        userService.removeAddressFromUser(userId, address);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� �������� ����� ������������", description = "��������� �������� ����� ������������")
    @PutMapping("/{userId}/main-address/{index}")
    public ResponseEntity<Void> updateMainAddress(@PathVariable long userId, @PathVariable int index) {
        userService.updateMainAddress(userId, index);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "�������� ��� ������ ������������", description = "�������� ��� ������ ������������")
    @GetMapping("/{userId}/exchanges")
    public ResponseEntity<List<Exchange>> getAllUserExchanges(@PathVariable long userId) {
        List<Exchange> userExchanges = userService.getAllUserExchanges(userId);
        return ResponseEntity.ok(userExchanges);
    }

    @Operation(summary = "��������� ������������ ������ ��������������", description = "��������� ������������ ������ ��������������")
    @PutMapping("/admin/{userId}")
    public ResponseEntity<Void> setAdminStatus(@PathVariable long userId) {
        userService.setAdminStatus(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������� � ������������ ������ ��������������", description = "������� � ������������ ������ ��������������")
    @PutMapping("/no-admin/{userId}")
    public ResponseEntity<Void> removeAdminStatus(@PathVariable long userId) {
        userService.removeAdminStatus(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "�������� ����������� ��������� �������������", description = "�������� ����������� ��������� �������������")
    @PutMapping("/enable-show-contacts/{userId}")
    public ResponseEntity<Void> enableShowContacts(@PathVariable long userId) {
        userService.enableShowContacts(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ����������� ��������� �������������", description = "��������� ����������� ��������� �������������")
    @PutMapping("/disable-show-contacts/{userId}")
    public ResponseEntity<Void> disableShowContacts(@PathVariable long userId) {
        userService.disableShowContacts(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ��������� �������������", description = "��������� ��������� �������������")
    @PutMapping("/{userId}/preferences")
    public ResponseEntity<Void> updatePreferencesToUser(@PathVariable long userId, @RequestBody @Valid UserUpdateDTO updateDTO) {
        userService.updatePreferencesToUser(userId, updateDTO.getPreferences());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ������������", description = "��������� ������������")
    @PutMapping("/block/{userId}")
    public ResponseEntity<Void> blockUser(@PathVariable long userId) {
        userService.blockUser(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������������ ������������", description = "������������ ������������")
    @PutMapping("/unblock/{userId}")
    public ResponseEntity<Void> unblockUser(@PathVariable long userId) {
        userService.unblockUser(userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ����� � ������ �������� �������������", description = "��������� ����� � ������ �������� �������������")
    @PutMapping("/{userId}/wishlist/{bookId}")
    public ResponseEntity<Void> addBookToWishlist(@PathVariable long userId, @PathVariable long bookId) {
        userService.addBookToWishlist(userId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "������� ����� �� ������ �������� �������������", description = "������� ����� �� ������ �������� �������������")
    @DeleteMapping("/{userId}/wishlist/{bookId}")
    public ResponseEntity<Void> removeBookFromWishlist(@PathVariable long userId, @PathVariable long bookId) {
        userService.removeBookFromWishlist(userId, bookId);
        return ResponseEntity.ok().build();
    }
}