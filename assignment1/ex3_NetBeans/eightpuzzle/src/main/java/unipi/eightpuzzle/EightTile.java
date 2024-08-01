package unipi.eightpuzzle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 * This represents one of the tiles in the board.
 * The position of each tile is fixed, they do not 'move' in the board,
 * their Labels do change instead
 * @author Francesco Lorenzoni
 */
public class EightTile extends javax.swing.JButton implements ActionListener{
    private final int position;
    private int label;
    private VetoableChangeListener listener;
    private VetoableChangeSupport mVcs = new VetoableChangeSupport(this);
    
    // Constructor to set initial position and label,
    // but label will be changed later on
    public EightTile (int position, int label) {
        if (!isValid(position) || !isValid(label))
            throw new IllegalArgumentException();
        this.position = position;
        this.label = label;
       this.updateText();
       this.updateColor();
    }
    
    // function invoked on click
    public boolean labelRequest (int newLabel) {
        try{
            String propertyName = newLabel == 9 ? "labelSwap" : "flip";
            this.mVcs.fireVetoableChange(propertyName, label, newLabel);
            // if an exception is thrown, we do not update the label
            updateLabel(newLabel);
            return true;
        }
        catch(PropertyVetoException e){
            // Tile is not adjacent to the hole
            
            this.setBackground(Color.RED);

            // recover background after some time with SwingUtilities.invokeLater
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {  }
                updateColor();
            });
            System.out.println("Cannot move tile");
            return false;
        }
        
    }

    // On restart, get new label from permutation
    public void restart(int[] labelPermutation) {
        int newLabel = labelPermutation[this.position-1];
        updateLabel(newLabel);
        this.putClientProperty("clickedTile",newLabel);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) { 
        JButton source = (JButton) ae.getSource();
        if("restart".equals(source.getActionCommand())){
            int[] permutation = (int[]) source.getClientProperty("permutation");
            this.restart(permutation);
        }
        // If a swap has been approved by the Controller && this tile holds the requested Label then update the label of this Tile.
        // Assume the label from the requesting tile has already been updated, its not this tile's responsability to handle such thing
        if("swapOK".equals(source.getActionCommand()) && (this.label == (int)source.getClientProperty("requestedLabel"))){
            int newLabel = (int) source.getClientProperty("clickedTile");
            System.out.println(position + ":" + label + " received " + newLabel + " by " + ((EightTile)source).getPosition());
            this.updateLabel(newLabel);
            this.putClientProperty("clickedTile",newLabel);
            
        }
    }
    
    // ---------- Utilities from now on
    
    private void updateLabel(int newLabel){
        this.label = newLabel;
        this.updateColor();
        this.updateText();
    }
    
    private static boolean isValid(int pos) {
        return pos > 0 && pos <= 9;
    }
    
    public void updateColor (){
        System.out.println(position + " : " +label );
        if (label == 9)
            this.setBackground(Color.DARK_GRAY);
        else if (position == label)
            this.setBackground(Color.GREEN);
        else
            this.setBackground(Color.YELLOW);
    }
    
    public void updateText (){
        if (this.label == 9)
            this.setText(" ");
        else{
            this.setText(String.valueOf(this.label));
        }
    }
    
    @Override
    public void addVetoableChangeListener(VetoableChangeListener l){
        mVcs.addVetoableChangeListener(l);
    }
    
    @Override
    public void removeVetoableChangeListener(VetoableChangeListener l){
        mVcs.removeVetoableChangeListener(l);
    }
    
    public int getPosition (){return this.position;}
    public int getMyLabel (){return this.label;}
}
