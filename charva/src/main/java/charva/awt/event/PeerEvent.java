/* class PeerEvent
 *
 * Copyright (C) 2001  R M Pitman
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package charva.awt.event;

import charva.awt.Component;


/**
 * A special event which is used for peer action processing.
 */
public class PeerEvent extends AWTEvent {

    private static final long serialVersionUID  = 4051754785719674538L;

    public static final int PEER_EVENT          = 2;
    
    /**
     * A special action which is used for coalescing screen-refresh actions
     * if possible, in order to speed up redrawing.
     */
    public static final int ACT_SYNC            = 1;
    
    /**
     * A special action which is used for closing peer window, which may 
     * require stop processing events in this window.
     */
    public static final int ACT_CLOSE           = 2;
    
    private final int       actionId;
    
    
    public PeerEvent(Component source, int actionId) {
        super(source, PEER_EVENT);
        
        if (actionId < ACT_SYNC || actionId > ACT_CLOSE) {
            throw new IllegalArgumentException("actionId: " + actionId);
        }
        
        this.actionId = actionId;
    }

    /**
     * Returns peer action id.
     * @return peer action id
     */
    public int getActionId() {
        return actionId;
    }

    /**
     * Returns a parameter string identifying this item event.
     * This method is useful for event-logging and for debugging.
     *
     * @return a string identifying the event and its attributes
     */
    public String paramString() {
        String actStr;
        switch (actionId) {
        case ACT_SYNC:
            actStr = "ACT_SYNC";
            break;
        
        case ACT_CLOSE:
            actStr = "ACT_CLOSE";
            break;
        
        default:
            actStr = "unknown action";
        }
        
        return "PEER_EVENT(" + actStr + ")";
    }
}
