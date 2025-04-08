package com.experiment.camunda;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class c7test implements CommandLineRunner {

//   Spring Boot Anwendung zum starten der Camunda Engine sowie dem Deployen und Starten der 10 Prozessinstanzen (numberOfProcessInstances)

    private static final Logger log = LoggerFactory.getLogger(c7test.class);
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    public static void main(String... args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(c7test.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        int numberOfProcessInstances = 10;

        // BPMN-Prozessmodell deployen
        repositoryService.createDeployment()
                .addClasspathResource("c7ex.bpmn") // BPMN-Datei aus dem Ressourcen-Ordner
                .deploy();
        c7test.log.info("Prozess deployed!");

        // Prozessinstanz starten
        for (int i = 1; i <= numberOfProcessInstances; i++) {
            // Instanz starten
            ProcessInstance instance = runtimeService.startProcessInstanceByKey("typicalC7process");
            String instanceId = instance.getId();
            log.info("Prozessinstanz #" + i + " gestartet mit ID: " + instanceId);

            // Warten, bis die Instanz beendet ist
            while (true) {
                HistoricProcessInstance historicInstance = historyService
                        .createHistoricProcessInstanceQuery()
                        .processInstanceId(instanceId)
                        .finished()
                        .singleResult();

                if (historicInstance != null) {
                    log.info("Prozessinstanz #" + i + " wurde beendet.");
                    break;
                }
            }
        }

    }
}

