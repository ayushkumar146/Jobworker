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

        Map<String, Object> variables = job.getVariablesAsMap();
        Map<String, Object> resultVariables = new HashMap<>();

        // Check if "cbid" is present
        if (variables.containsKey("cbid") && variables.get("cbid") != null) {
            String cbid = variables.get("cbid").toString();
            System.out.println("Received cbid: " + cbid);

            // Dummy map
            Map<String, String> dummyDb = new HashMap<>();
            dummyDb.put("cbid", "jhon");
            dummyDb.put("cb002", "Alice");
            dummyDb.put("cb003", "Bob");

            if (dummyDb.containsKey(cbid)) {
                resultVariables.put("isFound", 1);
                resultVariables.put("user", dummyDb.get(cbid));
                System.out.println("cbid found: " + dummyDb.get(cbid));
            } else {
                resultVariables.put("isFound", 0);
                System.out.println("cbid not found");
            }
        } else {
            resultVariables.put("isFound", 0);
            System.out.println("cbid was not provided");
        }

        // Complete the job with results
        client.newCompleteCommand(job.getKey())
                .variables(resultVariables)
                .send()
                .join();
    }

}
