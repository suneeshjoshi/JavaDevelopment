package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;

public class LambdaFunctionHandler implements RequestHandler<KinesisEvent, Integer> {

	@Override
	public Integer handleRequest(KinesisEvent event, Context context) {
		context.getLogger().log("Input: " + event);
		System.out.println("Input = " + event);

		for (KinesisEventRecord record : event.getRecords()) {

			String payload = new String(record.getKinesis().getData().array());
			context.getLogger().log("Payload: " + payload);
			System.out.println("Payload = " + record.getKinesis().toString());
		}

		System.out.println("event.getRecords().size() = " + event.getRecords().size());
		return event.getRecords().size();
	}
}
