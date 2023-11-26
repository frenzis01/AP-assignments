/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unipi.eightpuzzle;

import java.awt.Color;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 *
 * @author Francesco Lorenzoni
 */
public class Flip extends JButton {
    
    private VetoableChangeSupport mVcs = new VetoableChangeSupport(this) {};
    
    public boolean flipTiles () {
        try {
            mVcs.fireVetoableChange("flip", null, null);
            return true;
        }
        catch(PropertyVetoException e){
            var oldBG = this.getBackground();
            this.setBackground(Color.RED);

            // recover background SwingUtilities.invokeLater
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {  }
                
            });
            this.setBackground(oldBG);
            System.out.println("Cannot flip tiles");
            return false;
        }
        
        
    }
    
    public Flip () {
        super();
        super.setText("FLIP");
    }
    
    @Override
    public void addVetoableChangeListener(VetoableChangeListener l){
        mVcs.addVetoableChangeListener(l);
    }
    
    @Override
    public void removeVetoableChangeListener(VetoableChangeListener l){
        mVcs.removeVetoableChangeListener(l);
    }
    
}
