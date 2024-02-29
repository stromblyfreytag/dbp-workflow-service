package com.trustwave.dbpworkflow;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class DbpWorkflowApplication {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	public static void main(String[] args) {
		SpringApplication.run(DbpWorkflowApplication.class, args);
		System.out.println("\nOpen for business!\n");
		System.out.println("Begin by calling scan: http://localhost:8080/scan");
		System.out.println("Or for a failing case, by calling scan: http://localhost:8080/scan?assetToFail=asset2");
	}
}
