/** ************************************************************************* */
// Eclipse SUMO, Simulation of Urban MObility; see https://eclipse.org/sumo
// Copyright (C) 2016-2018 German Aerospace Center (DLR) and others.
// This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v2.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v20.html
// SPDX-License-Identifier: EPL-2.0
/** ************************************************************************* */
/// @file    Constants.java
/// @author  Maximiliano Bottazzi
/// @date    2016
/// @version $Id$
///
//
/** ************************************************************************* */
package de.dlr.ts.lisum.sumo;

import de.dlr.ts.lisum.enums.LightColor;
import de.dlr.ts.lisum.simulation.SimulationControlUnits;
import de.tudresden.sumo.cmd.Trafficlight;
import it.polito.appeal.traci.SumoTraciConnection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author @author <a href="mailto:maximiliano.bottazzi@dlr.de">Maximiliano
 * Bottazzi</a>
 */
class SumoControlUnit {

    private final List<SignalGroup> signalGroups = new ArrayList<>();
    private final String name;
    //private TrafficLight trafficLight;
    //private ControlUnitInterface cityControlUnit;
    private SimulationControlUnits controlUnits;

    //private String trafficLightId;
    private SumoTraciConnection sumoTraciConnection;

    /**
     *
     */
    public SumoControlUnit(String name, String[] sumoStrings) {
        this.name = name;
        int phasesCount = sumoStrings[0].length();

        for (int i = 0; i < phasesCount; i++) {
            String nodeLine = extractStates(i, sumoStrings);

            char greenType = 'G';

            for (int j = 0; j < nodeLine.length(); j++) {
                if (nodeLine.charAt(j) == 'g') {
                    greenType = 'g';
                }
            }

            SignalGroup sg = new SignalGroup(greenType);
            signalGroups.add(sg);
        }
    }

    public void setSumoTraciConnection(SumoTraciConnection sumoTraciConnection) {
        this.sumoTraciConnection = sumoTraciConnection;
    }

    /**
     *
     * @param index
     * @param lines
     * @return
     */
    private String extractStates(int index, String[] lines) {
        String tmp = "";

        for (String line : lines) {
            tmp += line.charAt(index);
        }

        return tmp;
    }

    /**
     *
     * @param controlUnits
     */
    void setControlUnits(SimulationControlUnits controlUnits) {
        this.controlUnits = controlUnits;
    }

    /**
     *
     */
    public void executeSimulationStep() {
        for (int j = 0; j < signalGroups.size(); j++) {
            signalGroups.get(j).setCurrentLightColor(controlUnits.getLightColor(this.name, j));
        }

        set();
    }

    /**
     *
     */
    private void set() {
        String signalGroupState = "";

        for (SignalGroup signalGroup : signalGroups) {
            char sumoCurrentLightColor = signalGroup.getSumoCurrentLightColor();
            signalGroupState += sumoCurrentLightColor;
        }

        try {
            //trafficlight.
            sumoTraciConnection.do_job_set(Trafficlight.setRedYellowGreenState(name, signalGroupState));

            //trafficLight.changeLightsState(new TLState(signalGroupState));
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        } catch (Exception ex) {
            Logger.getLogger(SumoControlUnit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public int getSignalGroupsCount() {
        return signalGroups.size();
    }

    /**
     *
     * @param signalGroupIndex
     * @param color
     */
    public void setLightColor(int signalGroupIndex, LightColor color) {
        signalGroups.get(signalGroupIndex).setCurrentLightColor(color);
    }

    /**
     *
     */
    public static class SignalGroup {

        private char green = 'G';
        private LightColor currentLightColor = LightColor.OFF;

        /**
         *
         */
        public SignalGroup(char green) {
            this.green = green;
        }

        /**
         *
         * @param currentLightColor
         */
        public void setCurrentLightColor(LightColor currentLightColor) {
            this.currentLightColor = currentLightColor;
        }

        /**
         *
         * @return
         */
        public char getSumoCurrentLightColor() {
            if (currentLightColor.isGreen()) {
                return green;
            }

            return currentLightColor.getSumoCode();
        }
    }

}