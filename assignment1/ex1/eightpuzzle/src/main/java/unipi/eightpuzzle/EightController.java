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
import unipi.eightpuzzle.utils.IntWrapper;

/**
 * Label which displays info on the last performed move, and which establishes whether
 * tiles may be moved and if 'Flip' may be invoked, implementing VetoableChangeListener
 * @author Francesco Lorenzoni
 */
public class EightController extends JLabel implements VetoableChangeListener,ActionListener{
    
    // The only information the controller has on the board is where the hole is placed.
    // It will get the position of the tile requesting a swap using the params of the event fired
    private int hole = 0;
    // A list is used to avoid recomputing adjacent positions also on illegal swaps
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
        // this corresponds to the event fired by EightTile.labelRequest
        if ("label".equals(pce.getPropertyName())) {
            // tile got clicked
            if ((int) pce.getNewValue() == 9) {

                // Here we work with positions, not labels
                // labels are handled by the tiles theirselves
                /**
                 * int tileToBeMoved = ((EightTile)
                 * pce.getSource()).getPosition();
                 *
                 * The assignment requires to do not use methods to read
                 * 'labels', but assuming that this applies also to 'position',
                 * we avoid the statement above, in favour of exploiting
                 * pce.oldValue to pass the position.
                 *
                 * position must be wrapped, otherwise in case oldValue ==
                 * newValue the event does not get fired. (it clearly may happen
                 * that position == newLabel)
                 */
                int tileToBeMoved = ((IntWrapper) pce.getOldValue()).value;

                System.out.println("Clicked : " + tileToBeMoved + " | Hole : " + this.hole);
                if (false == this.adj.contains(tileToBeMoved)) {
                    this.setText("KO");
                    throw new PropertyVetoException("Tile is not adjacent to hole", pce);
                }
                // If tile can be moved, update hole
                this.setHole(tileToBeMoved);
                this.setText("OK");
                // No exception is thrown, so the method invoking the change may proceed
            }
        }
            if (("label".equals(pce.getPropertyName()) && (int) pce.getNewValue() != 9) ||
                "flip".equals(pce.getPropertyName())){
                // Flip button got pressed
                if (this.hole != 5) {
                    throw new PropertyVetoException("Hole is not in the middle", pce);
                }
            }
    }
    
    /**
     * Updates hole and the adjacent position list
     * @param pos 
     */
    public void setHole(int pos){
        validatePosition(pos);
        this.hole = pos;
        this.adj = getAdjacent(hole);
            System.out.println("Hole := " +  this.hole + " " + this.adj.toString());
    }

    /**
     * The controller must be aware of restart events, to update the hole
     * @param ae 
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        JButton source = (JButton) ae.getSource();
        if(ae.getActionCommand().equals("restart")){
            // get permutation from restartButton's properties
            int[] permutation = (int[]) source.getClientProperty("permutation");
            this.setHole(1+indexOf(permutation,9));
            this.setText("START");
        }
    }
    
    public static int indexOf (int arr[], int v){
        for(int i = 0; i<arr.length;i++){
            if (arr[i] == v)
                return i;
        }
        return -1;
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
        
        // Statically defined map and remapping are exploited for a more
        // intuitive adjacent positions calculation,
        // but other approaches may be possible
        
        int pos = p;

        
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
