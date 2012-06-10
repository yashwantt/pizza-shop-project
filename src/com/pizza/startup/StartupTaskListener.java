package com.pizza.startup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class StartupTaskListener implements ApplicationListener, ApplicationContextAware {

	private final Log logger = LogFactory.getLog(getClass());
	private ApplicationContext applicationContext;

	/**
	 * {@inheritDoc}
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(
	 * 		org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(final ApplicationEvent event) {
//		logger.debug("Spring event:" + event.getClass().getSimpleName());
		if (!(event instanceof ContextRefreshedEvent)) {
			return;
		}

		if (isRootContext((ApplicationContext)event.getSource())) {
//			Logger.debug(logger, "Received application event ContextRefreshedEvent: {0}", event);
			String[] taskBeans = applicationContext.getBeanNamesForType(ResourceStartupTask.class);
			long start;
			for (String task : taskBeans) {
//				Logger.debug(logger, "Executing {0}.afterPropertiesSet()", task);
				start = System.currentTimeMillis();
				((ResourceStartupTask)applicationContext.getBean(task)).afterContextStart();
//				if (logger.isDebugEnabled()) {
//					Logger.debug(logger, "{0} took {1} ms to load.", task, System.currentTimeMillis() - start);
//				}
			}
		}
	}

	private boolean isRootContext(final ApplicationContext source) {
		return source.getParent() == null;
	}
	/**
	 * Make this spring applicationContext aware.
	 *
	 * @param applicationContext
	 *            ths spring application context
	 */
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
