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
 *
 * @author Francesco
 */
public class EightTile extends javax.swing.JButton implements ActionListener{
    private int position;
    private int label;
    private VetoableChangeListener listener;
    private VetoableChangeSupport mVcs = new VetoableChangeSupport(this);
    
    public EightTile (int position, int label) {
        if (!isValid(position) || !isValid(label))
            throw new IllegalArgumentException();
        this.position = position;
        this.label = label;
       this.updateText();
       this.updateColor();
    }
    
    
    public boolean labelRequest (int newLabel) {
        try{
            String propertyName = newLabel == 9 ? "labelSwap" : "flip";
            this.mVcs.fireVetoableChange(propertyName, label, newLabel);
            updateLabel(newLabel);
            return true;
        }
        catch(PropertyVetoException e){
            this.setBackground(Color.RED);

            // recover background SwingUtilities.invokeLater
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
        if("swapOK".equals(source.getActionCommand()) && (this.label == (int)source.getClientProperty("requestedLabel"))){
            int newLabel = (int) source.getClientProperty("clickedTile");
            System.out.println(position + ":" + label + " received " + newLabel + " by " + ((EightTile)source).getPosition());
            this.updateLabel(newLabel);
            this.putClientProperty("clickedTile",newLabel);
            
        }
    }
    
    private void updateLabel(int newLabel){
        this.label = newLabel;
        this.updateColor();
        this.updateText();
    }

    public EightTile(){
        super();
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
