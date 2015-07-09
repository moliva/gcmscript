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
package org.objectweb.proactive.extra.component.fscript.model;

import static org.objectweb.fractal.fscript.types.PrimitiveType.STRING;
import static org.objectweb.fractal.fscript.types.VoidType.VOID_TYPE;

import java.util.List;

import org.etsi.uri.gcm.util.GCM;
import org.objectweb.fractal.api.Interface;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.types.Signature;


/**
 * A GCM procedure to implement the <code>ws-bind()</code> action, which binds a client interface of a GCM
 * component to a web service.
 *
 * @author The ProActive Team
 */
public class WSBindAction extends AbstractGCMProcedure {
    /**
     * Returns the name of the procedure.
     *
     * @return The name of the procedure.
     */
    public String getName() {
        return "ws-bind";
    }

    /**
     * Indicates that the procedure is an action.
     *
     * @return <code>false</code> to indicate that the procedure is an action.
     */
    public boolean isPureFunction() {
        return false;
    }

    /**
     * Returns the signature of the procedure.
     *
     * @return The signature of the procedure.
     */
    public Signature getSignature() {
        return new Signature(VOID_TYPE, model.getNodeKind("interface"), STRING);
    }

    /**
     * Binds a client interface of a GCM component to a web service.
     *
     * @param args The arguments of the procedure call. Must contain as first element the {@link GCMInterfaceNode}
     * representing the client interface of the GCM component to bind to the web service and as second element the
     * web service URL to bind to.
     * @param ctx The execution context in which to execute the procedure.
     * @return <code>null</code>.
     * @throws ScriptExecutionError If any error occurred during the execution of the procedure.
     */
    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        if (args.get(0) instanceof GCMInterfaceNode) {
            Interface clientItf = ((GCMInterfaceNode) args.get(0)).getInterface();
            String wsURL = (String) args.get(1);
            try {
                BindingController bc = GCM.getBindingController(clientItf.getFcItfOwner());
                bc.bindFc(clientItf.getFcItfName(), wsURL);
            } catch (NoSuchInterfaceException nsie) {
                throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                        "Unable to bind interface \'" + clientItf.getFcItfName() +
                            "\' to the web service located at " + wsURL, nsie));
            } catch (IllegalBindingException ibe) {
                throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                        "Unable to bind interface \'" + clientItf.getFcItfName() +
                            "\' to the web service located at " + wsURL, ibe));
            } catch (IllegalLifeCycleException ilce) {
                throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                        "Unable to bind interface \'" + clientItf.getFcItfName() +
                            "\' to the web service located at " + wsURL, ilce));
            }
        }
        return null;
    }
}
