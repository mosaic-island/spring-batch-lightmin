package org.tuxdevelop.spring.batch.lightmin.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;

/**
 *
 * @author Marcel Becker
 *
 */
@Slf4j
public class JobServiceBean implements JobService{

	private JobOperator jobOperator;
	private JobRegistry jobRegistry;
	private JobInstanceDao jobInstanceDao;
	private JobExecutionDao jobExecutionDao;

	public JobServiceBean(final JobOperator jobOperator,
			final JobRegistry jobRegistry, final JobInstanceDao jobInstanceDao,
			final JobExecutionDao jobExecutionDao) {
		this.jobOperator = jobOperator;
		this.jobRegistry = jobRegistry;
		this.jobInstanceDao = jobInstanceDao;
		this.jobExecutionDao = jobExecutionDao;
	}

	@Override
	public int getJobInstanceCount(final String jobName) {
		// fix me
		return 0;
	}

	@Override
	public Set<String> getJobNames() {
		return new HashSet<String>(jobRegistry.getJobNames());
	}

	@Override
	public Job getJobByName(final String jobName) {
		Job job;
		try {
			job = jobRegistry.getJob(jobName);
		} catch (NoSuchJobException e) {
			log.info("Could not find job with jobName: " + jobName);
			job = null;
		}
		return job;
	}

	@Override
	public Collection<JobInstance> getJobInstances(final String jobName,
			final int startIndex, final int pageSize) {
		return jobInstanceDao.getJobInstances(jobName, startIndex, pageSize);
	}

	@Override
	public Collection<JobExecution> getJobExecutions(
			final JobInstance jobInstance) {
		final Collection<JobExecution> jobExecutions = new LinkedList<JobExecution>();
		final List<JobExecution> jobExecutionList = jobExecutionDao
				.findJobExecutions(jobInstance);
		jobExecutions.addAll(jobExecutionList);
		return jobExecutions;
	}

	@Override
	public JobExecution getJobExecution(final Long jobExecutionId) {
		return jobExecutionDao.getJobExecution(jobExecutionId);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		assert (jobOperator != null);
		assert (jobRegistry != null);
		assert (jobInstanceDao != null);
		assert (jobExecutionDao != null);
	}
}
