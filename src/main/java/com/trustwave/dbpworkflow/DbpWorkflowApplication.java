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
	}

//	@Configuration
//	public class FlowableConfiguration {
//
//		@Bean
//		public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customBpmnParseHandlers() {
//			return processEngineConfiguration -> {
//				List<BpmnParseHandler> parseHandlers = processEngineConfiguration.getPostBpmnParseHandlers();
//				if (parseHandlers == null) {
//					parseHandlers = new ArrayList<>();
//				}
//				parseHandlers.add(new SequenceFlowParseHandler());
//				processEngineConfiguration.setPostBpmnParseHandlers(parseHandlers);
//			};
//		}
//	}
//	@Bean
//	public CommandLineRunner init( final RuntimeService runtimeService,
//			final TaskService taskService) {
//
//		return new CommandLineRunner() {
//			@Override
//			public void run(String... strings) throws Exception {
//				System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
//				Map<String, Object> processVariables = new HashMap<>();
//				processVariables.put("syncExceptions", Boolean.FALSE);
//				processVariables.put("taskFailed", Boolean.FALSE);
//				ProcessInstance pi = runtimeService.startProcessInstanceByKey("demo", processVariables);
//				System.out.println("Number of tasks before receive task: "
//						+ taskService.createTaskQuery().count());
//				// imagine
//				Execution execution = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).activityId("waitTask").singleResult();
//				runtimeService.trigger(execution.getId());
//				System.out.println("Number of tasks after receive task: "
//						+ taskService.createTaskQuery().count());
//			}
//		};
//	}
}
