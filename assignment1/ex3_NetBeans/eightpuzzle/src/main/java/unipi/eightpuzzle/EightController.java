/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unipi.eightpuzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Francesco Lorenzoni
 */
public class EightController extends JLabel implements VetoableChangeListener,ActionListener{
    
    private int hole = 0;
    private List<Integer> adj = new ArrayList<>();
    
    public EightController (int hole){
        validatePosition(hole);
        this.hole = hole;
    }
    
    public EightController (){
        super();
        this.setText("START");
    }

    @Override
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
//        System.out.println("Event : " + pce.getPropertyName() +" | Hole : " + this.hole);
        System.out.println("Controller invoked");
        if ("label".equals(pce.getPropertyName()))
            return;
                    
        // int tileToBeMoved = (int) pce.getOldValue();
        // Here we work with positions, not labels
        int tileToBeMoved = ((EightTile)pce.getSource()).getPosition();
        System.out.println("Clicked : " + tileToBeMoved + " | Hole : " + this.hole);
        if (false == this.adj.contains(tileToBeMoved)){
            this.setText("KO");
            System.out.println("ILLEGAL OP");
            throw new UnsupportedOperationException("Tile is not adjacent to hole");
        }
        // If tile can be moved, update hole
        this.setHole(tileToBeMoved);
        this.setText("OK");
        
    }
    
    private static boolean isValid(int pos) {
        return pos > 0 && pos <= 9;
    }
    
    private static void validatePosition(int pos){
        if (!isValid(pos))
            throw new IllegalArgumentException();
    }
    
    /**
     * @param 1 <= pos <= 9
     * @return 
     */
    private static List<Integer> getAdjacent (int pos) {
        validatePosition(pos);
        //There are at most 4 adjacent tiles
        List<Integer> adj = new ArrayList<>(4); 
        
        
        // Upper tile
        if (pos + 3 <= 9)
            adj.add(pos + 3);
        
        // Lower tile
        if (pos - 3 >= 1)
            adj.add(pos - 3);
        
        // Next tile
        if ((pos + 1) % 3 != 1) // pos not in rightmost column
            adj.add(pos + 1);
        
        // Prev tile
        if ((pos - 1) % 3 != 0) // pos not in leftmost column
            adj.add(pos - 1);
        
        return adj;
    }
    
    public void setHole(int pos){
//        System.out.println(pos);
        validatePosition(pos);
        this.hole = pos;
        this.adj = getAdjacent(hole);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if(source.getActionCommand().equals("restart")){
            // get permutation from restartButton's properties
            int[] permutation = (int[]) source.getClientProperty("permutation");
            this.setHole(indexOf(permutation,9)+1);
        }
    }
    
    public static int indexOf (int arr[], int v){
        for(int i = 0; i<arr.length;i++){
            if (arr[i] == v)
                return i;
        }
        return -1;
    }
}
