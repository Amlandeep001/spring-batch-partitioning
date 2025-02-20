package com.spring.batch.listener;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class BatchExecutionListener implements JobExecutionListener
{
	private static final Logger audit = LoggerFactory.getLogger("audit-log");

	@Override
	public void beforeJob(JobExecution jobExecution)
	{
		audit.info("Starting job: {}, at: {}", jobExecution.getJobInstance().getJobName(), LocalDateTime.now());
	}

	@Override
	public void afterJob(JobExecution jobExecution)
	{
		audit.info("Ending job: {}, at: {}", jobExecution.getJobInstance().getJobName(), LocalDateTime.now());
	}
}