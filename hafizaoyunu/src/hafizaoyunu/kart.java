package hafizaoyunu;

import java.awt.Color;
//This class represents a card with a numerical value and a boolean indicating if it has been guessed or not.
public class kart {        //
    private int deger;    
    private boolean tahmin = false;

    public kart(int deger) {    
        this.deger = deger;
    }

    public int getDeger() {       
        return deger;
    }

    public void setDeger(int deger) {
        this.deger = deger;
    }

    public boolean isTahmin() {
        return tahmin;
    }

    public void setTahmin(boolean tahmin) {
        this.tahmin = tahmin;
    }
    
    
    
    
    
    
    public Color getColor() {
        // Match the integer value to a color
        switch (deger) {
            case 0:
                return Color.GREEN;
            case 1:
                return Color.PINK;
            case 2:
                return Color.BLUE;
          
            case 3:
            	return Color.RED;
            case 4:
            	return Color.MAGENTA;
            case 5:
            	return Color.YELLOW;
            case 6:
            	return Color.WHITE;
            case 7:
            	return Color.GRAY;
            
            default:
                return Color.BLACK; // Default color if the value is not matched
        }
    }
    
    
}