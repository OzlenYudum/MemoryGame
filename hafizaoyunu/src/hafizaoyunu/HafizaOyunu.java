package hafizaoyunu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Random;
 //1--------------------------------------- 
public class HafizaOyunu extends JFrame {
    private HafizaPaneli memoryPanel;
    private JPanel mesajPaneli;
    private JPanel zamanPaneli;
    private JLabel mesajEtiketi;
    private JLabel zamanEtiketi;
    private Color[] renkler;
    private boolean[] acik;
    private boolean[] eslesme;
    private long oyunBaslangicZamani;
    private int yanlisTahminSayisi = 0;
    private int ilkKartIndex = -1;
    private int ikinciKartIndex = -1;
    private Timer sayac;
    private Timer acilmaSayaci;
    private boolean ilkAcilma = true;

      public HafizaOyunu() { //constructor
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Pressing the cross causes the window to close and the application to end.
        setSize(400, 500); //determines the width and size of the window, taking into account the size of the labels
        
         //panel and labels of the message part of the game field and the time
        memoryPanel = new HafizaPaneli(); 
        mesajPaneli = new JPanel();  
        mesajEtiketi = new JLabel("Memory Game - Find all the pairs!");
        mesajEtiketi.setForeground(Color.WHITE);  
        mesajPaneli.add(mesajEtiketi); 
        mesajPaneli.setPreferredSize(new Dimension(400, 50)); // size of message panel

        zamanPaneli = new JPanel();
        zamanEtiketi = new JLabel("Time passed: 0 seconds");
        zamanEtiketi.setForeground(Color.WHITE);
        zamanPaneli.add(zamanEtiketi);

        mesajPaneli.setBackground(Color.BLACK); 
        zamanPaneli.setBackground(Color.BLACK);

        oyunuBaslat(); //involves placing and shuffling cards
        memoryPanel.addMouseListener(new MouseAdapter() {//MouseAdapter is added to react to mouse events such as clicks, movements, etc.
            @Override
            public void mouseClicked(MouseEvent e) { //This method is called every time the user clicks the mouse
                if (!oyunBittiMi()) {//Checks whether the game is over or not
                    int hucreBoyutu = memoryPanel.getWidth() / 4; //the width of each cell of the game board
                    int satir = e.getY() / hucreBoyutu;  // Calculates the row and column where the clicked point is located.
                    int sutun = e.getX() / hucreBoyutu; 
                    int kartIndex = satir * 4 + sutun;  // Calculates the index of the clicked card.
                    if (!acik[kartIndex] && ilkKartIndex == -1) { 
                    	
                    	/* if the clicked card has not been opened before
                        and if the first card has not yet been selected,
                        mark this card as the first card and
                        turn it on and redraw the panel */
                    	
                        ilkKartIndex = kartIndex;
                        acik[kartIndex] = true; 
                        memoryPanel.repaint(); 
                    } else if (!acik[kartIndex] && ilkKartIndex != -1 && ikinciKartIndex == -1) {
                    	
                    	/* if the clicked card has not been opened before,
                        If the first card is selected and
                        If the second card has not been selected yet,
                         mark this card as the second card and
                         turn it on and redraw the panel */
                    	
                        ikinciKartIndex = kartIndex;
                        acik[kartIndex] = true; 
                        memoryPanel.repaint();
                        if (!renkler[ilkKartIndex].equals(renkler[ikinciKartIndex])) {//If the prediction is not correct
                            yanlisTahminSayisi++;
                            if (yanlisTahminSayisi >= 3) { //End the game when the number of incorrect guesses is 3.
                                oyunuBitir();
                                return;
                            }
                        }
                        if (renkler[ilkKartIndex].equals(renkler[ikinciKartIndex])) {
                        	
                        	// if the match is correct, this is indicated and the indexes are reset. It is checked whether the game is over or not.
                        	
                            eslesme[ilkKartIndex] = true;
                            eslesme[ikinciKartIndex] = true;
                            ilkKartIndex = -1;  
                            ikinciKartIndex = -1;
                            if (oyunBittiMi()) { // If the game is completed successfully, congratulation message and elapsed time are shown
                                long gecenSure = System.currentTimeMillis() - oyunBaslangicZamani;
                                DecimalFormat df = new DecimalFormat("0.##");
                                String gecenSureStr = df.format(gecenSure / 1000.0);
                                mesajEtiketi.setText("Congratulations! You've matched all pairs in " + gecenSureStr + " seconds!");
                            }
                        } else {
                            Timer sayac = new Timer(1000, e1 -> {//If two cards do not match, close the cards after 1 second
                                acik[ilkKartIndex] = false;
                                acik[ikinciKartIndex] = false;
                                ilkKartIndex = -1;    //Initialize card indexes
                                ikinciKartIndex = -1;
                                memoryPanel.repaint(); //Reflect changes to the screen
                            });
                            sayac.setRepeats(false); //Makes the timer run only once
                            sayac.start(); // Makes the timer start
                        }
                    }
                }
            }
        });
            
        setLayout(new BorderLayout());    
        add(mesajPaneli, BorderLayout.NORTH);     //Positioning the locations of the panels
        add(memoryPanel, BorderLayout.CENTER);
        add(zamanPaneli, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);// the game window becomes visible

        oyunBaslangicZamani = System.currentTimeMillis();// game start time is recorded
        sayac = new Timer(1000, e -> {
            if (!oyunBittiMi()) {//If the game is not over, prints the elapsed time every second.
                long gecenSure = System.currentTimeMillis() - oyunBaslangicZamani;
                DecimalFormat df = new DecimalFormat("0.##");
                String gecenSureStr = df.format(gecenSure / 1000.0);
                zamanEtiketi.setText("Time passed: " + gecenSureStr + " seconds");
            }
        });
        sayac.setInitialDelay(0);  //The counter continues without delay
        sayac.start();
    }
//2---------------------------------------
    private void oyunuBaslat() {
        kart[][] kartlar = new kart[4][4];
        int index = 0;
        for (int i = 0; i < 4; i++) {//Filling a 4x4 array from 0 to 15
            for (int j = 0; j < 4; j++) {
                kartlar[i][j] = new kart(index++);
            }
        }
        index = 0;
        for (int i = 2; i < 4; i++) {  //filling the array sequentially from 0 to 7, starting from index [2][0]
            for (int j = 0; j < 4; j++) {
                kartlar[i][j] = new kart(index++);
            }
        }
        kartlariKaristir(kartlar); // The cards are mixed
        renkler = new Color[16];   
        acik = new boolean[16]; 
        eslesme = new boolean[16];
        acilmaSayaci = new Timer(2500, e -> {
            if (ilkAcilma) {  //If opened for the first time, close all cards after 2.5 seconds
                ilkAcilma = false;
                for (int i = 0; i < acik.length; i++) {
                    acik[i] = false;
                }
            } else { //Close all cards at startup, even if they are not opened the first time
                for (int i = 0; i < acik.length; i++) {
                    acik[i] = false;
                }
                memoryPanel.repaint();  
                acilmaSayaci.stop();
            }
            memoryPanel.repaint();
        });
        acilmaSayaci.setRepeats(true);
        acilmaSayaci.start();
        for (int i = 0; i < 4; i++) {    
            for (int j = 0; j < 4; j++) {
                int kartIndex = i * 4 + j;
                renkler[kartIndex] = kartlar[i][j].getColor();//match the colors with the integers in the cards
                acik[kartIndex] = false;  
                eslesme[kartIndex] = false;
            }
        }
        yanlisTahminSayisi = 0;
        memoryPanel.setEnabled(true);
    }
  //3---------------------------------------
    private void kartlariKaristir(kart[][] dizi) {
        Random rastgele = new Random();
        kart[] duzDizi = new kart[dizi.length * dizi[0].length];
        int index = 0;
        for (int i = 0; i < dizi.length; i++) {
            for (int j = 0; j < dizi[i].length; j++) {
                duzDizi[index++] = dizi[i][j];
            }
        }
        for (int i = duzDizi.length - 1; i > 0; i--) {
            int rastgeleIndex = rastgele.nextInt(i + 1); //randomize the index
            kart gecici = duzDizi[i];
            duzDizi[i] = duzDizi[rastgeleIndex];
            duzDizi[rastgeleIndex] = gecici;
        }
        index = 0;
        for (int i = 0; i < dizi.length; i++) {
            for (int j = 0; j < dizi[i].length; j++) {
                dizi[i][j] = duzDizi[index++];
            }
        }
    }
  //4---------------------------------------
    private boolean oyunBittiMi() { //checking if the game is over
        for (boolean eslendiMi : eslesme) {// checks all elements of the matching array
            if (!eslendiMi) {       // game will not end if even one card is not matched 
                return false;
            }
        }
        return true;  //if all cards are matched, the game will end
    }
  //5---------------------------------------
    private void oyunuBitir() {
        memoryPanel.setEnabled(false); //stop clicking cards when the game ends
        mesajEtiketi.setForeground(Color.WHITE);
        mesajEtiketi.setText("Game Over! You've reached the maximum incorrect guesses.");
        sayac.stop();
    }
  //6---------------------------------------
    private class HafizaPaneli extends JPanel { //draw the appropriate image depending on the state of the cards on the game board
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int hucreBoyutu = getWidth() / 4;
            for (int i = 0; i < renkler.length; i++) {
                int satir = i / 4;
                int sutun = i % 4;
                int x = sutun * hucreBoyutu;  // x coordinate of the upper left corner of the cell
                int y = satir * hucreBoyutu;// y coordinate of the upper left corner of the cell
                Color renk = renkler[i];
                g.setColor(Color.BLACK);
                g.fillRect(x, y, hucreBoyutu, hucreBoyutu);
                g.setColor(Color.WHITE);
                g.drawRect(x, y, hucreBoyutu, hucreBoyutu);
                if (ilkAcilma || acik[i] || eslesme[i]) { // If the card is opened for the first time, or if it is face up or matched, draw a circle in its own color
                    g.setColor(renk);
                    g.fillOval(x + hucreBoyutu / 4, y + hucreBoyutu / 4, hucreBoyutu / 2, hucreBoyutu / 2);
                } else {// If the card is face down, color the circle black
                    g.setColor(Color.BLACK);
                    g.fillOval(x + hucreBoyutu / 4, y + hucreBoyutu / 4, hucreBoyutu / 2, hucreBoyutu / 2);
                }
                if (acik[i] || eslesme[i]) {
                    g.setColor(renk);
                    g.fillOval(x + hucreBoyutu / 4, y + hucreBoyutu / 4, hucreBoyutu / 2, hucreBoyutu / 2);
                }
            }
        }
    }
  //7---------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {  //Allows the construction of the HafizaOyunu to execute
            new HafizaOyunu();
        });
    }
}
