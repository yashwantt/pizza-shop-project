package com.pizza.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.pizza.spring.SpringContext;

public class HibernateUtils {

	/**
	 * Execute a block of code transactionally expecting no result.
	 * @param callback the transaction callback to execute
	 */
	public static void execute(final TransactionCallbackWithoutResult callback) {
		execute(Propagation.REQUIRED, callback);
	}
	/**
	 * Execute a block of code transactionally expecting no result.
	 * @param propagation  the propagation level
	 * @param callback the transaction callback to execute
	 */
	public static void execute(final Propagation propagation, final TransactionCallbackWithoutResult callback) {
		final TransactionTemplate template = createTransactionTemplate();
		template.setPropagationBehavior(propagation.ordinal());
		template.execute(callback);
	}
	private static TransactionTemplate createTransactionTemplate() {
		return new TransactionTemplate(getTransactionManager());
	}
	private static PlatformTransactionManager getTransactionManager() {
		return SpringContext.instance().getBean("transactionManager");
	}
	
}
