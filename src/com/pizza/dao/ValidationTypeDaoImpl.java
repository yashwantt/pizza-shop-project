package com.pizza.dao;

import java.io.InputStream;
import java.util.List;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.bean.jibx.BeanListHolder;
import com.pizza.bean.validation.ValidationType;

public class ValidationTypeDaoImpl {
	
	private static final String DEFAULT_VALIDATION_TYPES_RESOURCE = "ValidationType_defaults.pzdata.xml";
	
	/**
	 * Load default {@link ValidationType}s as a static resource. These are the basic, out-of-the-box
	 * validation types, which are not stored in the database.
	 * Do NOT call this method during runtime, it should be initialized by the DAO at startup.
	 * @return The base validation types.
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<ValidationType> findDefaultValidationTypes() {
		try {
			final InputStream inStream = ValidationType.class.getResourceAsStream(DEFAULT_VALIDATION_TYPES_RESOURCE);
			if (inStream == null) {
				throw new RuntimeException("Cannot find resource " + DEFAULT_VALIDATION_TYPES_RESOURCE);
			}
	        
			final IBindingFactory fact = BindingDirectory.getFactory(
	        		"input_" + ValidationType.class.getSimpleName(),
	        		BeanListHolder.class);
	        
//			final IBindingFactory fact = BindingDirectory.getFactory(
//	        		ValidationType.class.getSimpleName(),
//	        		BeanListHolder.class);

			
	        final IUnmarshallingContext context = fact.createUnmarshallingContext();
	        context.setDocument(inStream, null);
	        final BeanListHolder<ValidationType> holder = (BeanListHolder<ValidationType>)context.unmarshalElement();
	        final List<ValidationType> fileResult = holder.getList();

	        return fileResult;
	      
//	        final Map<String, RegexEntity> entityMap = getRegexEntityMap();
//	        for (final ValidationType validationType : fileResult) {
//				addRegexMasks(validationType, entityMap);
//			}
//	        List<ValidationType> result = findDefaultDbValidationTypes();
//	        result.addAll(fileResult);
//	        filterDupes(result);
//	        return result;
		}
		catch (final JiBXException e) {
			throw new RuntimeException("Cannot process " + DEFAULT_VALIDATION_TYPES_RESOURCE, e);
		}
	}
	

	
	
	
}
