package com.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.drools.RulesExecutionService;

import com.example.SimpleResource;

public class SimpleApplicationComponentsService  implements KieServerApplicationComponentsService {
	private static final String OWNER_EXTENSION = "Drools";
    
    public Collection<Object> getAppComponents(String extension, SupportedTransports type, Object... services) {
        // skip calls from other than owning extension
        if ( !OWNER_EXTENSION.equals(extension) ) {
            return Collections.emptyList();
        }
        
        RulesExecutionService rulesExecutionService = null;
        KieServerRegistry context = null;
       
        for( Object object : services ) { 
            if( RulesExecutionService.class.isAssignableFrom(object.getClass()) ) { 
                rulesExecutionService = (RulesExecutionService) object;
                continue;
            } else if( KieServerRegistry.class.isAssignableFrom(object.getClass()) ) {
                context = (KieServerRegistry) object;
                continue;
            }
        }
        
        List<Object> components = new ArrayList<Object>(1);
        if( SupportedTransports.REST.equals(type) ) {
            components.add(new SimpleResource(rulesExecutionService, context));
        }
        
        return components;
    }

}