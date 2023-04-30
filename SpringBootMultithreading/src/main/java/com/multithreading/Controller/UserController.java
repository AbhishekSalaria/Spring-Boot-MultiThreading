package com.multithreading.Controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.multithreading.Entity.User;
import com.multithreading.Service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;
	
	@PostMapping(value="/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces="application/json")
	public ResponseEntity saveUsers(@RequestParam(value="files") MultipartFile[] files) throws Exception {
		for(MultipartFile file: files) {
			service.saveUser(file);
		}
	return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@GetMapping(value = "/users", produces="application/json")
	public CompletableFuture<ResponseEntity> getAllUsers(){
		
		return service.getAllUsers().thenApply(ResponseEntity::ok);
	}
	
	@GetMapping(value= "/getUsersByThread", produces="application/json")
	public ResponseEntity getUsers() {
		
		CompletableFuture<List<User>> u1 = service.getAllUsers();
		CompletableFuture<List<User>> u2 = service.getAllUsers();
		CompletableFuture<List<User>> u3 = service.getAllUsers();
		
		CompletableFuture.allOf(u1,u2,u3).join();
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
