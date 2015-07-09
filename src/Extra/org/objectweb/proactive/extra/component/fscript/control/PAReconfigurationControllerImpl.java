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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.InstantiationException;
import org.objectweb.fractal.api.type.TypeFactory;
import org.objectweb.fractal.fscript.FScript;
import org.objectweb.fractal.fscript.FScriptEngine;
import org.objectweb.fractal.fscript.FScriptException;
import org.objectweb.fractal.fscript.InvalidScriptException;
import org.objectweb.fractal.fscript.ScriptLoader;
import org.objectweb.proactive.core.ProActiveRuntimeException;
import org.objectweb.proactive.core.component.control.AbstractPAController;
import org.objectweb.proactive.core.component.type.PAGCMTypeFactoryImpl;
import org.objectweb.proactive.extra.component.fscript.GCMScript;
import org.objectweb.proactive.extra.component.fscript.exceptions.ReconfigurationException;
import org.objectweb.proactive.extra.component.fscript.model.GCMNodeFactory;


/**
 * Implementation of {@link PAReconfigurationController}.
 * <br>
 * By default, the component owning this controller interface is set as global variable (named "this") in the
 * controller's engine.
 *
 * @author The ProActive Team
 */
public class PAReconfigurationControllerImpl extends AbstractPAController implements
        PAReconfigurationController {
    /** The {@link ScriptLoader} used by the controller. */
    private transient ScriptLoader loader;
    /** The {@link FScriptEngine} used by the controller. */
    private transient FScriptEngine engine;

    /**
     * Default public constructor.
     *
     * @param owner The component owning this controller interface.
     */
    public PAReconfigurationControllerImpl(Component owner) {
        super(owner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setControllerItfType() {
        try {
            setItfType(PAGCMTypeFactoryImpl.instance().createFcItfType(
                    PAReconfigurationController.RECONFIGURATION_CONTROLLER,
                    PAReconfigurationController.class.getName(), TypeFactory.SERVER, TypeFactory.MANDATORY,
                    TypeFactory.SINGLE));
        } catch (InstantiationException ie) {
            throw new ProActiveRuntimeException("cannot create controller type for controller " +
                this.getClass().getName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewEngineFromADL() throws ReconfigurationException {
        setNewEngineFromADL(GCMScript.GCMSCRIPT_ADL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNewEngineFromADL(String adlFile) throws ReconfigurationException {
        try {
            String defaultFcProvider = System.getProperty("fractal.provider");
            System.setProperty("fractal.provider", "org.objectweb.fractal.julia.Julia");
            Component gcmScript = GCMScript.newEngineFromAdl(adlFile);
            this.loader = FScript.getScriptLoader(gcmScript);
            this.engine = FScript.getFScriptEngine(gcmScript);
            this.engine.setGlobalVariable("this", ((GCMNodeFactory) FScript.getNodeFactory(gcmScript))
                    .createGCMComponentNode(this.owner));
            System.setProperty("fractal.provider", defaultFcProvider);
        } catch (Exception e) {
            throw new ReconfigurationException("Unable to set new engine for reconfiguration controller", e);
        }
    }

    /**
     * Checks if the {@link ScriptLoader} and the {@link FScriptEngine} have been initialized. If not, the
     * instantiation is done by using {@link #setNewEngineFromADL()}.
     *
     * @throws ReconfigurationException If an error occurred during the instantiation.
     */
    private void checkInitialized() throws ReconfigurationException {
        if ((this.loader == null) || (this.engine == null)) {
            setNewEngineFromADL();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> load(String fileName) throws ReconfigurationException {
        checkInitialized();

        try {
            return this.loader.load(new FileReader(fileName));
        } catch (FileNotFoundException fnfe) {
            throw new ReconfigurationException("Unable to load procedure definitions", fnfe);
        } catch (InvalidScriptException ise) {
            throw new ReconfigurationException("Unable to load procedure definitions\n" + ise.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getGlobals() throws ReconfigurationException {
        checkInitialized();

        return this.engine.getGlobals();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(String source) throws ReconfigurationException {
        checkInitialized();

        try {
            return this.engine.execute(source);
        } catch (FScriptException fse) {
            throw new ReconfigurationException("Unable to execute the procedure", fse);
        }
    }
}
