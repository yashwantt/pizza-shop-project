package com.pizza.startup;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.pizza.util.HibernateUtils;

public abstract class ResourceStartupTask {
	
	/**
	 * Method called by StarupTaskListener after the application context has completed loading.
	 */
	public final void afterContextStart() {
		HibernateUtils.execute(new TransactionCallbackWithoutResult() {
			@Override
			public void doInTransactionWithoutResult(final TransactionStatus status) {
				doStartupActions();
			}
		});
	}
	
	/**
	 * Perform startup actions.
	 * @throws StartupException if actions cannot be performed
	 */
	protected abstract void doStartupActions();


}
