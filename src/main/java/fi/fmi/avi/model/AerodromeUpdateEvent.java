package fi.fmi.avi.model;

import java.util.EventObject;

/**
 * Created by rinne on 13/10/17.
 */
public class AerodromeUpdateEvent extends EventObject {

    private static final long serialVersionUID = -8361285292002724875L;

    public AerodromeUpdateEvent(final Aerodrome source) {
        super(source);
    }

    public Aerodrome getAerodrome() {
        return (Aerodrome) super.getSource();
    }

}