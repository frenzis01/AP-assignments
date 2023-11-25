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
        if (!"label".equals(pce.getPropertyName()))
            return;
                    
        // int tileToBeMoved = (int) pce.getOldValue();
        // Here we work with positions, not labels
        int tileToBeMoved = ((EightTile)pce.getSource()).getPosition();
        System.out.println("Clicked : " + tileToBeMoved + " | Hole : " + this.hole);
        if (false == this.adj.contains(tileToBeMoved)){
            this.setText("KO");
            System.out.println("ILLEGAL OP");
            throw new PropertyVetoException("Tile is not adjacent to hole",pce);
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
    private static List<Integer> getAdjacent (int p) {
        validatePosition(p);
        //There are at most 4 adjacent tiles
        List<Integer> adj = new ArrayList<>(4);
        
        int pos = mapPosToGridInverse(p);
        
        System.out.print("Hole -> " + pos + " ");
        
        
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
        
        System.out.println(adj.toString());
        
        return adj.stream().map(EightController::mapPosToGrid).toList();
    }
    
    private static int mapPosToGrid (int pos){
        /**
         * 1 2 3      1 3 5
         * 4 5 6  ->  7 9 2
         * 7 8 9      4 6 8
         */
        switch(pos) {
            case 1: return 1;
            case 2: return 3;
            case 3: return 5;
            case 4: return 7;
            case 5: return 9;
            case 6: return 2;
            case 7: return 4;
            case 8: return 8;
            case 9: return 6;
            default: return 0;
        }       
    }
    
    private static int mapPosToGridInverse (int pos){
        /**
         * 1 3 5      1 2 3
         * 7 9 2  ->  4 5 6
         * 4 8 6      7 8 9
         */
        switch(pos) {
            case 1: return 1;
            case 3: return 2;
            case 5: return 3;
            case 7: return 4;
            case 9: return 5;
            case 2: return 6;
            case 4: return 7;
            case 8: return 8;
            case 6: return 9;
            default: return 0;
        }   
    }
    
    
    public void setHole(int pos){
//        System.out.println(pos);
        validatePosition(pos);
        this.hole = pos;
        this.adj = getAdjacent(hole);
            System.out.println("Hole := " +  this.hole + this.adj.toString());
            
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if(source.getActionCommand().equals("restart")){
            // get permutation from restartButton's properties
            int[] permutation = (int[]) source.getClientProperty("permutation");
            this.setHole(1+indexOf(permutation,9));
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
