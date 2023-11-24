package unipi.eightpuzzle;

import java.awt.Color;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import javax.swing.JButton;

/**
 *
 * @author Francesco
 */
public class EightTile extends javax.swing.JButton {
    private int position;
    private int label;
    private EightController listener;
    private VetoableChangeSupport mVcs = new VetoableChangeSupport(this);
    
    public EightTile (int position, int label, EightController listener) {
        if (!isValid(position) || isValid(label))
            throw new IllegalArgumentException();
        this.position = position;
        this.label = label;
        // Register listener
       this.listener = listener;
       this.addVetoableChangeListener(this.listener);
    }
    
    public EightTile(){
        super();
    }
    
    private static boolean isValid(int pos) {
        return pos > 0 && pos <= 9;
    }
    
    public void updateColor (){
        if (position == 9)
            this.setBackground(Color.DARK_GRAY);
        else if (position == label)
            this.setBackground(Color.GREEN);
        else
            this.setBackground(Color.YELLOW);
    }
    
    public void updateLabel (){
        if (position == 9)
            this.setText("");
        else
            this.setText("" + label);
    }
    
    public void moveTile () {
        try{
            this.mVcs.fireVetoableChange("position", position, position);
        }
        catch(PropertyVetoException e){
            System.out.println("Cannot move tile");
        }
        
    }
    
    public int getPosition (){return position;}
    public int getMyLabel (){return label;}

}
