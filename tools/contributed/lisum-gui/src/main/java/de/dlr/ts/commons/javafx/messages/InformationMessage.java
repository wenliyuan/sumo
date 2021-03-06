/*
 * Copyright (C) 2016
 * Deutsches Zentrum fuer Luft- und Raumfahrt e.V.
 * Institut fuer Verkehrssystemtechnik
 * 
 * German Aerospace Center
 * Institute of Transportation Systems
 * 
 */
package de.dlr.ts.commons.javafx.messages;

import javafx.scene.control.Alert;

/**
 *
 * @author @author <a href="mailto:maximiliano.bottazzi@dlr.de">Maximiliano Bottazzi</a>
 */
public class InformationMessage extends Message
{

    /**
     * 
     * @param text 
     */
    public InformationMessage(String text)
    {
        alert = new Alert(Alert.AlertType.INFORMATION); 
        alert.setHeaderText(null);
        alert.setContentText(text);
        
        init();
    }
    
}
