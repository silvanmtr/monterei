package com.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.thymeleaf.processor.MenuAttributeTagProcessor;
import com.thymeleaf.processor.MessageElementTagProcessor;
import com.thymeleaf.processor.OrderElementTagProcessor;
import com.thymeleaf.processor.PaginationElementTagProcessor;

public class AppDialect extends AbstractProcessorDialect {

	public AppDialect() {
		super("Projeto Web", "thm", StandardDialect.PROCESSOR_PRECEDENCE);
	}
	
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		processadores.add(new OrderElementTagProcessor(dialectPrefix));
		processadores.add(new PaginationElementTagProcessor(dialectPrefix));
		processadores.add(new MenuAttributeTagProcessor(dialectPrefix));
		return processadores;
	}

}
