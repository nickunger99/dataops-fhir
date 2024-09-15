package com.dataops.api;

import com.dataops.api.domain.auth.role.RoleService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
@SpringBootApplication
public class DataOpsApiApplication implements CommandLineRunner {
	@Autowired
	RoleService roleService;
	private static Logger logger = LoggerFactory.getLogger(DataOpsApiApplication.class);

	public static void main(String[] args) {
		logger.info("Starting API DataOps..");
		SpringApplication.run(DataOpsApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Starting Role initialize.");
		roleService.initialize();
	}
}
