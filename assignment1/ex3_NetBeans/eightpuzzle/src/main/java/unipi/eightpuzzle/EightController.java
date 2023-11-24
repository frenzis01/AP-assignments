/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unipi.eightpuzzle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author Francesco Lorenzoni
 */
public class EightController extends JLabel implements VetoableChangeListener {
    
    int hole = 0;
    List<Integer> adj = new ArrayList<Integer>();
    
    public EightController (int hole){
        validatePosition(hole);
        this.hole = hole;
    }

    @Override
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
        int tileToBeMoved = (int) pce.getNewValue();
        if (false == this.adj.contains(tileToBeMoved))
            throw new UnsupportedOperationException("Tile is not adjacent to hole");
        // If tile can be moved, update hole
        this.hole = tileToBeMoved;
        this.adj = getAdjacent(hole);
        
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
        List<Integer> adj = new ArrayList<Integer>(4); 
        
        
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
}
