/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2013 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package functionalTests.component.fscript.model;

import java.net.URL;

import org.junit.Before;
import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.api.Component;
import org.objectweb.proactive.core.component.adl.FactoryFactory;
import org.objectweb.proactive.extensions.gcmdeployment.PAGCMDeployment;
import org.objectweb.proactive.extra.component.fscript.model.GCMModel;
import org.objectweb.proactive.gcmdeployment.GCMApplication;

import functionalTests.ComponentTest;


public abstract class CommonSetup extends ComponentTest {
    protected static GCMApplication gcmaLocal;
    protected Component composite;
    protected Component serverWebService;
    protected GCMModel model;

    @Before
    public void setUp() throws Exception {
        if (gcmaLocal == null) {
            URL descriptorPath = this.getClass().getResource("GCMA.xml");
            gcmaLocal = PAGCMDeployment.loadApplicationDescriptor(descriptorPath);
            gcmaLocal.startDeployment();
        }
        Factory factory = FactoryFactory.getFactory();
        composite = (Component) factory.newComponent(
                "functionalTests.component.fscript.model.components.adl.Composite", null);
        serverWebService = (Component) factory.newComponent(
                "functionalTests.component.fscript.model.components.adl.ServerWebService", null);
        System.setProperty("fractal.provider", "org.objectweb.fractal.julia.Julia");
        model = new GCMModel();
        model.startFc();
    }
}
