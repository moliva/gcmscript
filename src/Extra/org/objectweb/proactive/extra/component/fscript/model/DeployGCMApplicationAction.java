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

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

import org.objectweb.fractal.fscript.ScriptExecutionError;
import org.objectweb.fractal.fscript.ast.SourceLocation;
import org.objectweb.fractal.fscript.diagnostics.Diagnostic;
import org.objectweb.fractal.fscript.interpreter.Context;
import org.objectweb.fractal.fscript.types.Signature;
import org.objectweb.proactive.core.ProActiveException;
import org.objectweb.proactive.extensions.gcmdeployment.PAGCMDeployment;
import org.objectweb.proactive.gcmdeployment.GCMApplication;


/**
 * A GCM procedure to implement the <code>deploy-gcma()</code> action which starts the deployment described in a
 * GCM Application Descriptor.
 * <br>
 * Processes described in the GCM Application Descriptor are started on remote resources described by all GCM
 * Deployment Descriptor files.
 *
 * @author The ProActive Team
 */
public class DeployGCMApplicationAction extends AbstractGCMProcedure {
    /**
     * Returns the name of the procedure.
     *
     * @return The name of the procedure.
     */
    public String getName() {
        return "deploy-gcma";
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
        return new Signature(model.getNodeKind("gcmapplication"), STRING);
    }

    /**
     * Creates a {@link GCMApplicationNode} representing a new {@link GCMApplication} already started.
     *
     * @param args The arguments of the procedure call. Must contain as first element the GCM Application
     * Descriptor path of the {@link GCMApplication} to instantiate and start.
     * @param ctx The execution context in which to execute the procedure.
     * @return The {@link GCMApplicationNode} representing the new {@link GCMApplication} already started.
     * @throws ScriptExecutionError If any error occurred during the execution of the procedure.
     */
    public Object apply(List<Object> args, Context ctx) throws ScriptExecutionError {
        String gcmaPath = (String) args.get(0);
        try {
            GCMApplication gcma = PAGCMDeployment.loadApplicationDescriptor((new File(gcmaPath)).toURI()
                    .toURL());
            gcma.startDeployment();
            GCMNodeFactory nf = (GCMNodeFactory) model;
            return nf.createGCMApplicationNode(gcma);
        } catch (MalformedURLException mue) {
            throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                    "Unable to deploy GCMApplication: " + gcmaPath), mue);
        } catch (ProActiveException pae) {
            throw new ScriptExecutionError(Diagnostic.error(SourceLocation.UNKNOWN,
                    "Unable to deploy GCMApplication: " + gcmaPath), pae);
        }
    }
}
