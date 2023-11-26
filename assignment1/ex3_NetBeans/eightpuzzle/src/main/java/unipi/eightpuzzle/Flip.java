/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unipi.eightpuzzle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 *
 * @author Francesco Lorenzoni
 */
public class Flip extends JButton implements ActionListener {
    
    private VetoableChangeSupport mVcs = new VetoableChangeSupport(this) {};
    
    private int
            label1 = 1, // This init values should never be used
            label2 = 2;
    
    public boolean flipTiles () {
        try {
            mVcs.fireVetoableChange("flip", null, null);
            // we locally swap label1 and label2 in this instance,
            // the label edit in EightTile instances will be made by theirselves
            System.out.print("Flippables: "+ label1 + " " + label2 + "-->");

            int tmp = this.label1;
            this.label1 = this.label2;
            this.label2 = tmp;
            System.out.println(label1 + " " + label2);
            return true;
        }
        catch(PropertyVetoException e){
            this.setBackground(Color.RED);

            // recover background SwingUtilities.invokeLater
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {  }
                this.setBackground(Color.CYAN);
            });
            System.out.println("Cannot flip tiles");
            return false;
        }
        
        
    }
    
    public Flip () {
        super();
        super.setText("FLIP");
        this.setBackground(Color.CYAN);
    }
    
    @Override
    public void addVetoableChangeListener(VetoableChangeListener l){
        mVcs.addVetoableChangeListener(l);
    }
    
    @Override
    public void removeVetoableChangeListener(VetoableChangeListener l){
        mVcs.removeVetoableChangeListener(l);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if("restart".equals(source.getActionCommand())){
            int[] permutation = (int[]) source.getClientProperty("permutation");
            // we assume that label in position 1 is the 1st in the permutation array
            // and that the label in position 2 is the 2nd in the permutation array
            
            this.label1 = permutation[0];
            this.label2 = permutation[1];

        }
        
        if("swapOK".equals(source.getActionCommand())
                // the swap is a regular move, not a flip
                && (int) source.getClientProperty("requestedLabel") == 9){
            // We can't use this since clickedTile gets modified by tiles
            // as soon as they receive the event.
            //  => we'll use another property
            // int newLabel = (int) source.getClientProperty("clickedTile");
            
            // swappedLabel became the hole
            int swappedLabel = (int) source.getClientProperty("swappedLabel");
            System.out.println("swapped: " + swappedLabel);
            // if one of the flippable labels were either the hole or 
            // the swapped one, they must be updated
            if (this.label1 == 9)
                this.label1 = swappedLabel;
            else if (this.label1 == swappedLabel)
                this.label1 = 9;
            
            if (this.label2 == 9)
                this.label2 = swappedLabel;
            else if (this.label2 == swappedLabel)
                this.label2 = 9;
            System.out.println("REGULAR SWAP - Flippables: "+ label1 + " " + label2);
            
        }
        
    }
    
    // tile1 must get the label of tile2
    public int getLabel1 () {return this.label1;}
}
