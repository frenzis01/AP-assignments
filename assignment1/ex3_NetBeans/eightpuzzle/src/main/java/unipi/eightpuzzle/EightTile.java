package unipi.eightpuzzle;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeSupport;
import java.util.List;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 *
 * @author Francesco
 */
public class EightTile extends javax.swing.JButton implements ActionListener{
    private int position;
    private int label;
    private EightController listener;
    private VetoableChangeSupport mVcs = new VetoableChangeSupport(this);
    
    public EightTile (int position, int label, EightController listener) {
        if (!isValid(position) || !isValid(label))
            throw new IllegalArgumentException();
        this.position = position;
        this.label = label;
        // Register listener
       this.listener = listener;
       this.updateText();
       this.updateColor();
       this.addVetoableChangeListener(this.listener);
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
    
    public void moveTile () {
        try{
            this.mVcs.fireVetoableChange("label", label, 9);
            this.label=9;
            updateColor();
            updateText();
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
        }
        
    }

    public void restart(int[] labelPermutation) {
        int newLabel = labelPermutation[this.position-1];
        this.label = newLabel;
        this.updateColor();
        this.updateText();
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        //TODO: add the control to distinguish between the restart and flip button. 
        JButton button = (JButton) ae.getSource();
        if(button.getActionCommand().equals("restart")){
            // get permutation from restartButton's properties
            int[] permutation = (int[]) button.getClientProperty("permutation");
//            int[] permutation = ((List<Integer>) button.getClientProperty("permutation")).stream().mapToInt(Integer::intValue).toArray();
            this.restart(permutation);
        }
    }
    
    public int getPosition (){return position;}
    public int getMyLabel (){return label;}

}
