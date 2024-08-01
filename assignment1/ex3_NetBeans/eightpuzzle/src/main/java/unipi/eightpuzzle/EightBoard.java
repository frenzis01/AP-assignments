/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unipi.eightpuzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.SwingUtilities;

/**
 * Graphical dashboard and Main Class.
 * Noticed that tiles are positioned accordingly to the assignment
 * they are not numerically ordered.
 * @author Francesco Lorenzoni
 */
public class EightBoard extends javax.swing.JFrame {

    // EightController controller; // controller is init as component
    EightTile[] tiles = new EightTile[9];
    
    /**
     * Creates new form EightBoard
     */
    public EightBoard() {
        initComponents();
        this.tiles[0] = eightTile1;
        this.tiles[1] = eightTile2;
        this.tiles[2] = eightTile3;
        this.tiles[3] = eightTile4;
        this.tiles[4] = eightTile5;
        this.tiles[5] = eightTile6;
        this.tiles[6] = eightTile7;
        this.tiles[7] = eightTile8;
        this.tiles[8] = eightTile9;
        
        // Controller displaying START/OK/KO and vetoing changes
        flip1.addVetoableChangeListener(eightController1);
        flip1.setActionCommand("flip");
        
        flip1.addActionListener((ActionEvent ae) -> {
            // Attempt to Flip tiles
            if (flip1.flipTiles()){
                // Note that getLabel1() yields the label which will go in the position
                // after (!) the swap, not the current one. 
                // Checking terminal output might help understand
                // i.e. tile_1=4 && tile_2=7  =>  getLabel1()=7
                eightTile1.putClientProperty("requestedLabel", flip1.getLabel1());
                System.out.println("Setting request -> "+ flip1.getLabel1());
                eightTile1.doClick();
            }
        });
        
        restart.setActionCommand("restart");
        
        // Initialize the first permutation
        int[] permutation = generatePermutation(1,9);
        // Assign permutation to the RESTART button
        restart.putClientProperty("permutation",permutation);
        // Controller and Flip btn should be aware of restarts (and first start)
        restart.addActionListener(eightController1);
        restart.addActionListener(flip1);
        

        // Tiles must listen for 'restart' event
        for (EightTile t : tiles) {
            restart.addActionListener(t);
            // Tiles swap must be vetoed by the controller
            t.addVetoableChangeListener(eightController1);
        } 
        // This will actually initialize the board, 
        // assigning position to the tiles
        restart.doClick();
        
        
        for (EightTile t : tiles) {
            // When clicked a tile requests to be swapped with the hole
            t.setActionCommand("swapRequest");
            t.putClientProperty("clickedTile",t.getMyLabel());
            // A tile will always (unless 'Flip') request to be swapped with the hole
            // label == 9 => Hole
            t.putClientProperty("requestedLabel", 9);
            
            // A tile should hear the event of a swap request or that a swap has succeeded
            t.addActionListener((ActionEvent ae) -> {
                if ("swapRequest".equals(ae.getActionCommand())) {
                    int 
                            oldLabel = t.getMyLabel(),
                            requestedLabel = (int) t.getClientProperty("requestedLabel");

                    // requestedLabel is always 9 unless t == eightTile1 and flip has been clicked
                    // labelRequest returns false is the change gets blocked by Controller
                    if (t.labelRequest(requestedLabel)) { 
                        // if here, tile can be successfully moved and has updated its label becoming the new hole,
                        // but the other tile (old hole) has yet to update its label
                        // it will be done once it hears swapOK
                        System.out.println(t.getPosition() + ":" + requestedLabel + " sent " + t.getClientProperty("clickedTile"));
                        
                        // We will fire a second event labeled "swapOK"
                        // Every listener will know that the tile swap was successful
                        t.setActionCommand("swapOK");
                        // This is needed by Flip button to know where the hole is
                        t.putClientProperty("swappedLabel", oldLabel);
                        // Fire swapOK event to all listeners,
                        // so that the old hole updates its label
                        t.doClick();
                    }
                }
                if ("swapOK".equals(ae.getActionCommand()) ){
                    // Need to revert back to previous actionCommand 'swapRequest'
                    
                    // invokeLater is needed to ensure sequentiality
                    //    i.e. swapRequest -> swapOK
                    // Otherwise some Tile might lose the swapOK sent
                    // by t.doClick()
                    SwingUtilities.invokeLater(() -> {
                        t.setActionCommand("swapRequest");
                        t.putClientProperty("requestedLabel", 9);
                        t.putClientProperty("clickedTile",t.getMyLabel());
                    });
                }

            });
            // A tile listens for the events fired by other tiles expect itself
            Stream  .of(tiles)
                    .filter(tile -> tile.getPosition() != t.getPosition())
                    .forEach(tile -> tile.addActionListener(t));
            t.addActionListener(flip1);
        }
        
    }

    public static int indexOf(int arr[], int v) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == v) {
                return i;
            }
        }
        return -1;
    }

    private static int[] generatePermutation(int low, int high) {
        // Initialize with numbers between low and high
        List<Integer> numbers = IntStream.rangeClosed(low, high)
                .boxed()
                .collect(Collectors.toList());

        // Random permutation through list shuffling
        Collections.shuffle(numbers);
        // Map to int[]
        return numbers.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        eightTile1 = new unipi.eightpuzzle.EightTile(1,1);
        eightTile3 = new unipi.eightpuzzle.EightTile(3,3);
        eightTile5 = new unipi.eightpuzzle.EightTile(5,5);
        eightTile7 = new unipi.eightpuzzle.EightTile(7,7);
        eightTile9 = new unipi.eightpuzzle.EightTile(9,9);
        eightTile2 = new unipi.eightpuzzle.EightTile(2,2);
        eightTile4 = new unipi.eightpuzzle.EightTile(4,4);
        eightTile8 = new unipi.eightpuzzle.EightTile(8,8);
        eightTile6 = new unipi.eightpuzzle.EightTile(6,6);
        jInternalFrame2 = new javax.swing.JInternalFrame();
        eightController1 = new unipi.eightpuzzle.EightController();
        restart = new javax.swing.JButton();
        flip1 = new unipi.eightpuzzle.Flip();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jInternalFrame1.setVisible(true);
        jInternalFrame1.getContentPane().setLayout(new java.awt.GridLayout(3, 3));

        eightTile1.setText("eightTile1");
        jInternalFrame1.getContentPane().add(eightTile1);

        eightTile3.setText("eightTile3");
        jInternalFrame1.getContentPane().add(eightTile3);

        eightTile5.setText("eightTile5");
        jInternalFrame1.getContentPane().add(eightTile5);

        eightTile7.setText("eightTile7");
        jInternalFrame1.getContentPane().add(eightTile7);

        eightTile9.setText("eightTile9");
        jInternalFrame1.getContentPane().add(eightTile9);

        eightTile2.setText("eightTile2");
        jInternalFrame1.getContentPane().add(eightTile2);

        eightTile4.setText("eightTile4");
        jInternalFrame1.getContentPane().add(eightTile4);

        eightTile8.setText("eightTile8");
        jInternalFrame1.getContentPane().add(eightTile8);

        eightTile6.setText("eightTile6");
        jInternalFrame1.getContentPane().add(eightTile6);

        jInternalFrame2.setVisible(true);
        jInternalFrame2.getContentPane().setLayout(new java.awt.GridLayout(1, 3));

        eightController1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jInternalFrame2.getContentPane().add(eightController1);

        restart.setBackground(new java.awt.Color(255, 153, 0));
        restart.setText("RESTART");
        restart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartActionPerformed(evt);
            }
        });
        jInternalFrame2.getContentPane().add(restart);

        flip1.setText("FLIP");
        jInternalFrame2.getContentPane().add(flip1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void restartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartActionPerformed
        System.out.println("-----------RESTARTED----------");
        
        // This permutation  will be adopted by the next restart click
        // Not the current one
        int[] permutation = generatePermutation(1,9);
        restart.setActionCommand("restart");
        restart.putClientProperty("permutation",permutation);
    }//GEN-LAST:event_restartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EightBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EightBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EightBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EightBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EightBoard().setVisible(true);
            }
        });
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private unipi.eightpuzzle.EightController eightController1;
    private unipi.eightpuzzle.EightTile eightTile1;
    private unipi.eightpuzzle.EightTile eightTile2;
    private unipi.eightpuzzle.EightTile eightTile3;
    private unipi.eightpuzzle.EightTile eightTile4;
    private unipi.eightpuzzle.EightTile eightTile5;
    private unipi.eightpuzzle.EightTile eightTile6;
    private unipi.eightpuzzle.EightTile eightTile7;
    private unipi.eightpuzzle.EightTile eightTile8;
    private unipi.eightpuzzle.EightTile eightTile9;
    private unipi.eightpuzzle.Flip flip1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JButton restart;
    // End of variables declaration//GEN-END:variables
}
