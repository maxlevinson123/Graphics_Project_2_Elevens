import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.swing.JButton;
import java.awt.BorderLayout;

class DrawPanel extends JPanel implements MouseListener {

    private Deck d;
    private Card[][] cards;

    public DrawPanel() {

        cards = new Card[3][3];
        d = new Deck();
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                cards[r][c] = d.getRandomCard();
            }
        }
        this.addMouseListener(this);

        JButton newGameButton = new JButton("New Game");
        JButton replaceCardsButton = new JButton("Replace Cards");

        replaceCardsButton.addActionListener(e -> {
            ArrayList<int[]> highlighted = new ArrayList<>();
            for (int r = 0; r < cards.length; r++) {
                for (int c = 0; c < cards[r].length; c++) {
                    if (cards[r][c] != null && cards[r][c].getHighlight()) {
                        highlighted.add(new int[]{r, c});
                    }
                }
            }

            if (highlighted.size() == 2) {
                if (getPoints(cards[highlighted.get(0)[0]][highlighted.get(0)[1]]) + getPoints(cards[highlighted.get(1)[0]][highlighted.get(1)[1]]) == 11) {
                    replace(highlighted);
                }
            }

            if (highlighted.size() == 3) {
                ArrayList<String> royals = new ArrayList<>();
                for (int[] i : highlighted){
                    royals.add(cards[i[0]][i[1]].getValue());
                }
                if (royals.contains("J") && royals.contains("Q") && royals.contains("K")) {
                    replace(highlighted);
                }
            }
        });

        newGameButton.addActionListener(e -> {
            d = new Deck();
            for (int r = 0; r < cards.length; r++)
                for (int c = 0; c < cards[r].length; c++)
                    cards[r][c] = d.getRandomCard();
        });

        this.setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(replaceCardsButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 50;
        int y = 10;
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                if (cards[r][c] != null) {
                    g.drawImage(cards[r][c].getImage(), x, y, null);
                    Rectangle cardHitBox = new Rectangle(x, y, cards[r][c].getImage().getWidth(), cards[r][c].getImage().getHeight());
                    cards[r][c].setHitbox(cardHitBox);
                    if (cards[r][c].getHighlight()) {
                        g.drawRect(x, y, (int) cardHitBox.getWidth(), (int) cardHitBox.getHeight());
                    }
                }
                x += 80;
            }
            y += 100;
            x = 50;
        }

        if (gameWon()) {
            g.drawString("You win!", x + 150, y + 20);
        }
        else if (!validMoves()) {
            g.drawString("No valid moves! You lose.", x + 150, y + 20);
        }

        g.drawString("Number of cards left: " + d.getDeck().size(), x, y + 100);
    }

    public int getPoints(Card c) {
        int p = 0;
        String value = c.getValue();
        if (value.equals("A")) p = 1;
        if (value.equals("02")) p = 2;
        if (value.equals("03")) p = 3;
        if (value.equals("04")) p = 4;
        if (value.equals("05")) p = 5;
        if (value.equals("06")) p = 6;
        if (value.equals("07")) p = 7;
        if (value.equals("08")) p = 8;
        if (value.equals("09")) p = 9;
        if (value.equals("10")) p = 10;
        return p;
    }

    public boolean gameWon() {
        if (d.getDeck().size() != 0) return false;
        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards[r].length; c++) {
                if (cards[r][c] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validMoves(){
        ArrayList<Card> inPlay = new ArrayList<>();
        for (int r = 0; r < cards.length; r++)
            for (int c = 0; c < cards[r].length; c++)
                if (cards[r][c] != null)
                    inPlay.add(cards[r][c]);

        for (int i = 0; i < inPlay.size(); i++)
            for (int j = i + 1; j < inPlay.size(); j++) {
                if (getPoints(inPlay.get(i)) + getPoints(inPlay.get(j)) == 11) return true;
            }

        ArrayList<String> values = new ArrayList<>();
        for (int i = 0; i < inPlay.size(); i++) {
            values.add(inPlay.get(i).getValue());
        }
        if (values.contains("J") && values.contains("Q") && values.contains("K")) {
            return true;
        }

        return false;
    }

    public void mousePressed(MouseEvent e) {

        Point p = e.getPoint();
        int button = e.getButton();

        for (int r = 0; r < cards.length; r++) {
            for (int c = 0; c < cards.length; c++) {
                if (button == 1) {
                    if (cards[r][c] != null && cards[r][c].getHitbox().contains(p)) {
                        cards[r][c].flipHighlight();
                    }
                }

                if (cards[r][c] != null && button == 3 && cards[r][c].getHitbox().contains(p)) {
                    cards[r][c].flipHighlight();;
                }
            }
        }
    }

    public void replace(ArrayList<int[]> highlighted) {
        for (int[] i : highlighted) {
            if (d.getDeck().size() > 0) cards[i[0]][i[1]] = d.getRandomCard();
            else cards[i[0]][i[1]] = null;
        }
    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}