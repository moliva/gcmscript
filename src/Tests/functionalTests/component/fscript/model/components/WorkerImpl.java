/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2010 INRIA/University of 
 *              Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 
 * or a different license than the GPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package functionalTests.component.fscript.model.components;

import java.io.Serializable;

import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;


public class WorkerImpl implements Service, ServiceAttributes, BindingController, Serializable {
    private String id = "";
    private String separator = "";

    private Service service;

    public StringWrapper process(String message) {
        return service.process(id + separator + message);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(final String separator) {
        this.separator = separator;
    }

    public String[] listFc() {
        return new String[] { "service-client" };
    }

    public Object lookupFc(final String cItf) {
        if (cItf.equals("service-client")) {
            return service;
        }
        return null;
    }

    public void bindFc(final String cItf, final Object sItf) {
        if (cItf.equals("service-client")) {
            service = (Service) sItf;
        }
    }

    public void unbindFc(final String cItf) {
        if (cItf.equals("service-client")) {
            service = null;
        }
    }
}
