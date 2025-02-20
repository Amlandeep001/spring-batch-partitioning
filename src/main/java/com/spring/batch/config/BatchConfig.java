package com.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.spring.batch.entity.Customer;
import com.spring.batch.listener.BatchExecutionListener;
import com.spring.batch.partition.ColumnRangePartitioner;
import com.spring.batch.processor.CustomerProcessor;
import com.spring.batch.reader.CustomerReader;
import com.spring.batch.writer.CustomerWriter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BatchConfig
{
	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final CustomerReader customerReader;
	private final CustomerProcessor customerProcessor;
	private final CustomerWriter customerWriter;
	private final ColumnRangePartitioner partitioner;
	private final BatchExecutionListener batchExecutionListener;

	@Bean
	PartitionHandler partitionHandler(TaskExecutor taskExecutor, Step slaveStep)
	{
		TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
		taskExecutorPartitionHandler.setGridSize(4);
		taskExecutorPartitionHandler.setTaskExecutor(taskExecutor);
		taskExecutorPartitionHandler.setStep(slaveStep);
		return taskExecutorPartitionHandler;
	}

	@Bean
	Step slaveStep()
	{
		return new StepBuilder("slaveStep", jobRepository)
				.<Customer, Customer>chunk(250, platformTransactionManager)
				.reader(customerReader)
				.processor(customerProcessor)
				.writer(customerWriter)
				.build();
	}

	@Bean
	Step masterStep(Step slaveStep, PartitionHandler partitionHandler)
	{
		return new StepBuilder("masterSTep", jobRepository)
				.partitioner(slaveStep.getName(), partitioner)
				.partitionHandler(partitionHandler)
				.build();
	}

	@Bean
	Job runJob(Step masterStep)
	{
		return new JobBuilder("importCustomers", jobRepository)
				.listener(batchExecutionListener)
				.flow(masterStep)
				.end()
				.build();
	}

	@Bean
	TaskExecutor taskExecutor()
	{
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(4);
		taskExecutor.setMaxPoolSize(4);
		taskExecutor.setQueueCapacity(4);
		taskExecutor.setThreadNamePrefix("spring-batch-");
		taskExecutor.initialize(); // This line initializes the task executor
		return taskExecutor;
	}

}
