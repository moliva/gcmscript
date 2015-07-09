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

import java.io.InputStream;
import java.io.InputStreamReader;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.ScriptLoader;
import org.objectweb.proactive.annotation.PublicAPI;


/**
 * This utility class provides convenience methods to create GCMScript interpreters.
 *
 * @author The ProActive Team
 */
@PublicAPI
public final class GCMScript {
    /**
     * The name of the Fractal ADL definition which describes the standard GCMScript engine
     * configuration.
     */
    public static final String GCMSCRIPT_ADL = "org.objectweb.proactive.extra.component.fscript.GCMScript";

    /**
     * Instantiates a new GCMScript engine from the default GCMScript ADL file.
     *
     * @return The instantiated GCMScript architecture as a Fractal component.
     * @throws Exception If an error occurred during the instantiation.
     */
    public static Component newEngineFromAdl() throws Exception {
        return newEngineFromAdl(GCMSCRIPT_ADL);
    }

    /**
     * Instantiates a new GCMScript engine from an ADL file.
     *
     * @param adlFile The ADL file name containing the GCMScript architecture to instantiate.
     * @return The instantiated GCMScript architecture as a Fractal component.
     * @throws Exception If an error occurred during the instantiation.
     */
    public static Component newEngineFromAdl(String adlFile) throws Exception {
        Component gcmScript = FScript.newEngineFromAdl(adlFile);
        loadGCMLibrary(gcmScript);
        return gcmScript;
    }

    /**
     * Loads the GCM library into an already created GCMScript engine.
     *
     * @param gcmScript The GCMScript engine in which to load the GCM library.
     * @throws IllegalArgumentException If the engine's {@link ScriptLoader} interface could not be found.
     * @throws InvalidScriptException If an error occurred while loading the GCM library.
     */
    public static void loadGCMLibrary(Component gcmScript) throws IllegalArgumentException,
            InvalidScriptException {
        ScriptLoader loader = FScript.getScriptLoader(gcmScript);
        InputStream gcmlib = GCMScript.class.getResourceAsStream("gcmlib.fscript");
        loader.load(new InputStreamReader(gcmlib));
    }
}
