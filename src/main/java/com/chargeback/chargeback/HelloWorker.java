package com.chargeback.chargeback;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.api.worker.JobClient;
import io.camunda.spring.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HelloWorker {

    @JobWorker(type = "External")
    public void handleJob(final JobClient client, final ActivatedJob job) {
        System.out.println("Handling job: " + job.getKey());
        System.out.println("Variables: " + job.getVariables());

        // Access and modify variables
        String name = job.getVariablesAsMap().get("name").toString();
        name = "ayush"; // modify or process as needed

        // Prepare variables to send back to the process
        Map<String, Object> updatedVariables = new HashMap<>();
        updatedVariables.put("name", name);

        // Complete the job and send variables back
        client.newCompleteCommand(job.getKey())
                .variables(updatedVariables) //
                .send()
                .join();
    }
}
