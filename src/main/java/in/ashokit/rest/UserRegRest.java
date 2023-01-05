package in.ashokit.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.ashokit.binding.ActivateAccount;
import in.ashokit.binding.Login;
import in.ashokit.binding.User;
import in.ashokit.service.UserRegServiceImpl;

@RestController
public class UserRegRest {

	@Autowired
	private UserRegServiceImpl service;

	@PostMapping("/user")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		boolean saveUser = service.saveUser(user);
		if (saveUser) {
			return new ResponseEntity<>("Registration Successfull", HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>("Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate")
	public ResponseEntity<String> activeAccount(@RequestBody ActivateAccount account) {
		boolean activeUserAcc = service.activeUserAcc(account);
		if (activeUserAcc) {
			return new ResponseEntity<>("Account Activated Successfully", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalied Temp password", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> allUser = service.getAllUser();
		return new ResponseEntity<>(allUser, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
		User user = service.getUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteById(@PathVariable Integer userId) {
		boolean deleteUser = service.deleteUserById(userId);
		if (deleteUser) {
			return new ResponseEntity<>("User Remove", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("UserId not found", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/user/{userId}/{status}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer userId, @PathVariable String status) {
		boolean changeAccoStatus = service.changeAccoStatus(userId, status);
		if (changeAccoStatus) {
			return new ResponseEntity<>("Status Changed", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Failed to Change", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Login login) {
		String loginStatus = service.login(login);
		return new ResponseEntity<>(loginStatus, HttpStatus.OK);
	}
	@GetMapping("/forgot/{email}")
	public ResponseEntity<String> forgotPwd(@PathVariable String email){
		String forgetPwd = service.forgetPwd(email);
		return new ResponseEntity<>(forgetPwd, HttpStatus.OK);
	}
}
