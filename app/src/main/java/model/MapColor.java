package model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapColor {
    private float color;
    private float[] floatColors;

    public MapColor() {
        floatColors = new float[] {BitmapDescriptorFactory.HUE_AZURE,
                                    BitmapDescriptorFactory.HUE_GREEN,
                                    BitmapDescriptorFactory.HUE_RED,
                                    BitmapDescriptorFactory.HUE_BLUE,
                                    BitmapDescriptorFactory.HUE_CYAN,
                                    BitmapDescriptorFactory.HUE_ORANGE,
                                    BitmapDescriptorFactory.HUE_MAGENTA,
                                    BitmapDescriptorFactory.HUE_ROSE,
                                    BitmapDescriptorFactory.HUE_VIOLET,
                                    BitmapDescriptorFactory.HUE_YELLOW};
    }

    public float[] getFloatColors() {
        return floatColors;
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }
}
