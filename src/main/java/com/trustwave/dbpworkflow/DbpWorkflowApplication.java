package com.trustwave.dbpworkflow;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(proxyBeanMethods = false)
public class DbpWorkflowApplication {
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	public static void main(String[] args) {
		SpringApplication.run(DbpWorkflowApplication.class, args);
	}
	@Bean
	public CommandLineRunner init( final RuntimeService runtimeService,
			final TaskService taskService) {

		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
				Map<String, Object> processVariables = new HashMap<>();
				processVariables.put("syncExceptions", Boolean.FALSE);
				processVariables.put("taskFailed", Boolean.FALSE);
				runtimeService.startProcessInstanceByKey("demo", processVariables);
				System.out.println("Number of tasks after process start: "
						+ taskService.createTaskQuery().count());
			}
		};
	}
}
