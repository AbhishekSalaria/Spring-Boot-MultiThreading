package com.multithreading.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.multithreading.Entity.User;
import com.multithreading.Repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;

	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Async
	public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception
	{
		long start = System.currentTimeMillis();
		
		List<User> users = parseCSVFile(file);
		
		logger.info("Saving list of users of size {}",users.size(), ""+Thread.currentThread().getName());
		
		users = repository.saveAll(users);
		long end = System.currentTimeMillis();
		logger.info("Total time {}", (end-start));
		
		return CompletableFuture.completedFuture(users);
		
	}
	
	@Async
	public CompletableFuture<List<User>> getAllUsers() {
		
		logger.info("Got list of user by "+ Thread.currentThread().getName());
		long start = System.currentTimeMillis();
		List<User> users = repository.findAll();
		long end = System.currentTimeMillis();
		logger.info("Total time {}", (end-start));
		return CompletableFuture.completedFuture(users);
	}
	
	private List<User> parseCSVFile(final MultipartFile file) throws Exception {
		
		final List<User> users =  new ArrayList<User>();
		
		try {
			final BufferedReader br = new BufferedReader(new  InputStreamReader(file.getInputStream()));
			
			String line;
			
			while((line = br.readLine()) != null) {
				final String [] data = line.split(",");
				final User user = new User();
				user.setName(data[0]);
				user.setEmail(data[1]);
				user.setGender(data[2]);
				users.add(user);
			}
			
			return users;
		}
		catch(final IOException e) {
			logger.error("Failed to Parse CSV File {}", e);
			throw new Exception("Failed to Parse CSV File {}",e);
		}
		
		
	}
}
