package org.alblang.nessy.server.setup;

import org.restlet.Context;
import org.restlet.ext.jaxrs.JaxRsApplication;

/**
 * @author jesus.navarrete  (22/10/14)
 */
public class MyJaxRsApplication extends JaxRsApplication {

    public MyJaxRsApplication(Context context) {
        super(context);
        this.add(new NessyApplication());
    }

}
