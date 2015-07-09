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
package org.objectweb.proactive.extra.component.fscript.control;

import java.util.Set;

import org.objectweb.proactive.annotation.PublicAPI;
import org.objectweb.proactive.extra.component.fscript.exceptions.ReconfigurationException;


/**
 * Component interface to control reconfiguration of the component to which it belongs. The reconfiguration is done
 * thanks to a FScript component.
 *
 * @author The ProActive Team
 */
@PublicAPI
public interface PAReconfigurationController {
    /** Controller name. */
    public static final String RECONFIGURATION_CONTROLLER = "reconfiguration-controller";

    /**
     * Instantiates a new GCMScript engine from the default GCMScript ADL file and sets it as
     * default engine for the controller.
     *
     * @throws ReconfigurationException If an error occurred during the instantiation.
     */
    public void setNewEngineFromADL() throws ReconfigurationException;

    /**
     * Instantiates a new GCMScript engine from an ADL file and sets it as default engine for the
     * controller.
     *
     * @param adlFile The ADL file name containing the GCMScript architecture to instantiate and to set
     * as default engine for the controller.
     * @throws ReconfigurationException If an error occurred during the instantiation.
     */
    public void setNewEngineFromADL(String adlFile) throws ReconfigurationException;

    /**
     * Loads procedure definitions from a file containing source code, and makes them available for later invocation
     * by name.
     *
     * @param fileName The name of the file containing the source code of the procedure definitions.
     * @return The names of all the procedures successfully loaded.
     * @throws ReconfigurationException If errors were detected in the procedure definitions.
     */
    Set<String> load(String fileName) throws ReconfigurationException;

    /**
     * Returns the names of all the currently defined global variables.
     *
     * @return The names of all the currently defined global variables.
     * @throws ReconfigurationException If an error occurred while getting global variable names.
     */
    Set<String> getGlobals() throws ReconfigurationException;

    /**
     * Executes a code fragment: either an FPath expression or a single FScript statement.
     *
     * @param source The code fragment to execute.
     * @return The value of the code fragment, if successfully executed.
     * @throws ReconfigurationException If an error occurred during the execution of the code fragment.
     */
    Object execute(String source) throws ReconfigurationException;
}
