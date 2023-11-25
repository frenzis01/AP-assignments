/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package unipi.eightpuzzle;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Francesco Lorenzoni
 */
public class EightBoard extends javax.swing.JFrame {

//    EightController controller;
    EightTile[] tiles = new EightTile[9];
    
    /**
     * Creates new form EightBoard
     */
    public EightBoard() {
        initComponents();
        // ugly init
        this.tiles[0] = eightTile1;
        this.tiles[1] = eightTile2;
        this.tiles[2] = eightTile3;
        this.tiles[3] = eightTile4;
        this.tiles[4] = eightTile5;
        this.tiles[5] = eightTile6;
        this.tiles[6] = eightTile7;
        this.tiles[7] = eightTile8;
        this.tiles[8] = eightTile9;
        restart.setActionCommand("restart");
        int[] permutation = generatePermutation(1,9);
        restart.putClientProperty("permutation",permutation);
        initBoard(permutation);
        
        // Tiles must listen for 'restart' event
        // and for the 'swap'event of other tiles
        for (EightTile t : tiles) {
            restart.addActionListener(t);
            t.setActionCommand("swap");
            t.putClientProperty("clickedTile",t.getMyLabel());
            t.addActionListener((ActionEvent ae) -> {
                int oldLabel = t.getMyLabel();
                if (1 == t.moveTile()){ //tile has been successfully moved
                    t.setActionCommand("swap");
                    t.putClientProperty("clickedTile",oldLabel);
                }
            });
            Stream  .of(tiles)
                    .filter(tile -> tile.getPosition() != t.getPosition())
                    .forEach(tile -> tile.addActionListener(t));
            
        }  
        
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
        eightTile1 = new unipi.eightpuzzle.EightTile(1,1,eightController1);
        eightTile2 = new unipi.eightpuzzle.EightTile(2,2,eightController1);
        eightTile3 = new unipi.eightpuzzle.EightTile(3,3,eightController1);
        eightTile4 = new unipi.eightpuzzle.EightTile(4,4,eightController1);
        eightTile5 = new unipi.eightpuzzle.EightTile(5,5,eightController1);
        eightTile6 = new unipi.eightpuzzle.EightTile(6,6,eightController1);
        eightTile7 = new unipi.eightpuzzle.EightTile(7,7,eightController1);
        eightTile8 = new unipi.eightpuzzle.EightTile(8,8,eightController1);
        eightTile9 = new unipi.eightpuzzle.EightTile(9,9,eightController1);
        jInternalFrame2 = new javax.swing.JInternalFrame();
        eightController1 = new unipi.eightpuzzle.EightController();
        restart = new javax.swing.JButton();
        flip1 = new unipi.eightpuzzle.Flip();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jInternalFrame1.setVisible(true);
        jInternalFrame1.getContentPane().setLayout(new java.awt.GridLayout(3, 3));

        eightTile1.setText("eightTile1");
        jInternalFrame1.getContentPane().add(eightTile1);

        eightTile2.setText("eightTile2");
        jInternalFrame1.getContentPane().add(eightTile2);

        eightTile3.setText("eightTile3");
        jInternalFrame1.getContentPane().add(eightTile3);

        eightTile4.setText("eightTile4");
        jInternalFrame1.getContentPane().add(eightTile4);

        eightTile5.setText("eightTile5");
        jInternalFrame1.getContentPane().add(eightTile5);

        eightTile6.setText("eightTile6");
        jInternalFrame1.getContentPane().add(eightTile6);

        eightTile7.setText("eightTile7");
        jInternalFrame1.getContentPane().add(eightTile7);

        eightTile8.setText("eightTile8");
        jInternalFrame1.getContentPane().add(eightTile8);

        eightTile9.setText("eightTile9");
        jInternalFrame1.getContentPane().add(eightTile9);

        jInternalFrame2.setVisible(true);
        jInternalFrame2.getContentPane().setLayout(new java.awt.GridLayout(1, 3));

        eightController1.setText("ST");
        jInternalFrame2.getContentPane().add(eightController1);

        restart.setText("RESTART");
        restart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartActionPerformed(evt);
            }
        });
        jInternalFrame2.getContentPane().add(restart);

        flip1.setText("flip1");
        jInternalFrame2.getContentPane().add(flip1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(535, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jInternalFrame2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void restartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartActionPerformed
        int[] permutation = generatePermutation(1,9);
//        Stream.of(permutation).forEach(System.out::println);
        eightController1.setHole(1+indexOf(permutation,9));
        restart.setActionCommand("restart");
        // restart.putClientProperty("permutation",List.of(permutation));
        restart.putClientProperty("permutation",permutation);

    }//GEN-LAST:event_restartActionPerformed

    public static int indexOf (int arr[], int v){
        for(int i = 0; i<arr.length;i++){
            if (arr[i] == v)
                return i;
        }
        return -1;
    }
    
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

//        EightTile t = new EightTile(2,3);

        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EightBoard().setVisible(true);
            }
        });
    }

    private static int[] generatePermutation (int low, int high) {
        // Initialize with numbers between low and high
        List<Integer> numbers = IntStream.rangeClosed(low, high)
                .boxed()
                .collect(Collectors.toList());

        // Random permutation through list shuffling
        Collections.shuffle(numbers);
        System.out.println("---------------------");
        for(Integer i:numbers)
            System.out.println(i + (i + 1 == numbers.indexOf(i) ? " ------": ""));
        // Map to int[]
        return numbers.stream().mapToInt(Integer::intValue).toArray();
    }
    
    private void initBoard(int[] permutation){
//        for (int i = 0; i < 9; i++){
//            this.tiles[i].initLabel(i+1);
//            this.tiles[i].setText(String.valueOf(i+1));
////            this.tiles[i].addActionListener(l);
//        }
//        int[] permutation = generatePermutation(1,9);
        for(EightTile t: this.tiles)
            t.restart(permutation);
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
