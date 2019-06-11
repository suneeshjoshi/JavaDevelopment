package com.test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.event.S3EventNotification;

public class LambdaFunctionHandler implements RequestHandler<S3EventNotification, String> {

	@Override
	public String handleRequest(S3EventNotification event, Context context) {
		System.out.println("I AM HERE" + event.toJson());
		return "lambda_triggered";
	}

}