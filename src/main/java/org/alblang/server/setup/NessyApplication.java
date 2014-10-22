package org.alblang.server.setup;

import com.jenaiz.services.RootHandler;
import com.jenaiz.services.StatusHandler;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class NessyApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<>();

        classes.add(RootHandler.class);
        classes.add(StatusHandler.class);

        return classes;
	}
}
