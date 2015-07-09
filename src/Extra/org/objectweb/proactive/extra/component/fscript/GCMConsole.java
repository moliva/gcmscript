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
package org.objectweb.proactive.extra.component.fscript;

import java.util.Collections;

import org.objectweb.fractal.adl.Factory;
import org.objectweb.fractal.adl.FactoryFactory;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.console.Main;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.proactive.annotation.PublicAPI;


/**
 * Main class to launch the GCMScript console.
 *
 * @author The ProActive Team
 */
@PublicAPI
public class GCMConsole extends Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            configureFractal(null);
        } else if (args.length == 1) {
            configureFractal(args[0]);
        } else {
            System.err.println("Usage: java " + GCMConsole.class.getName() + " [file.properties]");
            System.exit(1);
        }

        Factory factory = FactoryFactory.getFactory(FactoryFactory.FRACTAL_BACKEND);
        String engineDef = System.getProperty("fscript.engine", GCMScript.GCMSCRIPT_ADL);
        Component gcmScript = (Component) factory.newComponent(engineDef, Collections.emptyMap());
        Fractal.getLifeCycleController(gcmScript).startFc();
        FScript.loadStandardLibrary(gcmScript);
        GCMScript.loadGCMLibrary(gcmScript);
        new GCMTextConsole(gcmScript).run();
        System.exit(0);
    }
}
