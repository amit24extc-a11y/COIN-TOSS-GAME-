import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CoinTossGame extends JFrame {

    private final Random random = new Random();

    private int wins = 0;
    private int losses = 0;
    private int totalGames = 0;
    private int playerChoice = 0;
    private int animationFrame = 0;
    private int finalResult = 0;

    private boolean flipping = false;

    private JLabel resultLabel;
    private JLabel scoreLabel;
    private JButton flipButton;
    private JRadioButton headsButton;
    private JRadioButton tailsButton;
    private CoinPanel coinPanel;
    private Timer animationTimer;

    public CoinTossGame() {
        setTitle("Coin Toss Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        Color background = new Color(20, 25, 40);
        Color gold = new Color(240, 190, 70);
        Color white = Color.WHITE;

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(background);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("COIN TOSS GAME");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(gold);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Choose your side and test your luck!");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(white);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        coinPanel = new CoinPanel();
        coinPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel chooseLabel = new JLabel("CHOOSE YOUR SIDE");
        chooseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseLabel.setForeground(white);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headsButton = new JRadioButton("HEADS", true);
        tailsButton = new JRadioButton("TAILS");

        headsButton.setBackground(background);
        tailsButton.setBackground(background);
        headsButton.setForeground(white);
        tailsButton.setForeground(white);
        headsButton.setFont(new Font("Arial", Font.BOLD, 16));
        tailsButton.setFont(new Font("Arial", Font.BOLD, 16));

        ButtonGroup choiceGroup = new ButtonGroup();
        choiceGroup.add(headsButton);
        choiceGroup.add(tailsButton);

        JPanel choicePanel = new JPanel();
        choicePanel.setBackground(background);
        choicePanel.add(headsButton);
        choicePanel.add(tailsButton);

        flipButton = new JButton("FLIP COIN");
        flipButton.setFont(new Font("Arial", Font.BOLD, 18));
        flipButton.setBackground(gold);
        flipButton.setForeground(Color.BLACK);
        flipButton.setFocusPainted(false);
        flipButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        flipButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        flipButton.setMaximumSize(new Dimension(220, 48));

        resultLabel = new JLabel("Ready to flip?");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        resultLabel.setForeground(white);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 15));
        scoreLabel.setForeground(gold);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateScore();

        JButton resetButton = new JButton("RESET SCORE");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> resetGame());

        flipButton.addActionListener(e -> startFlip());

        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(6));
        mainPanel.add(subtitle);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(coinPanel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(chooseLabel);
        mainPanel.add(choicePanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(flipButton);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(resultLabel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(scoreLabel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(resetButton);

        add(mainPanel);
        setVisible(true);
    }

    private void startFlip() {
        if (flipping) {
            return;
        }

        playerChoice = headsButton.isSelected() ? 0 : 1;
        finalResult = random.nextInt(2);

        flipping = true;
        animationFrame = 0;

        flipButton.setEnabled(false);
        headsButton.setEnabled(false);
        tailsButton.setEnabled(false);

        resultLabel.setForeground(Color.WHITE);
        resultLabel.setText("Flipping the coin...");

        animationTimer = new Timer(30, e -> {
            animationFrame++;
            coinPanel.repaint();

            if (animationFrame >= 60) {
                animationTimer.stop();
                flipping = false;

                coinPanel.repaint();
                showResult();
            }
        });

        animationTimer.start();
    }

    private void showResult() {
        totalGames++;

        String result = finalResult == 0 ? "HEADS" : "TAILS";

        if (playerChoice == finalResult) {
            wins++;
            resultLabel.setText("YOU WIN! - " + result);
            resultLabel.setForeground(new Color(70, 220, 130));
        } else {
            losses++;
            resultLabel.setText("YOU LOSE! - " + result);
            resultLabel.setForeground(new Color(255, 100, 100));
        }

        updateScore();

        flipButton.setEnabled(true);
        headsButton.setEnabled(true);
        tailsButton.setEnabled(true);
    }

    private void updateScore() {
        scoreLabel.setText(
                "WINS: " + wins +
                        "       LOSSES: " + losses +
                        "       GAMES: " + totalGames
        );
    }

    private void resetGame() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        flipping = false;
        wins = 0;
        losses = 0;
        totalGames = 0;
        animationFrame = 0;
        finalResult = 0;

        resultLabel.setText("Ready to flip?");
        resultLabel.setForeground(Color.WHITE);

        flipButton.setEnabled(true);
        headsButton.setEnabled(true);
        tailsButton.setEnabled(true);

        updateScore();
        coinPanel.repaint();
    }

    private class CoinPanel extends JPanel {

        CoinPanel() {
            setPreferredSize(new Dimension(400, 235));
            setMaximumSize(new Dimension(500, 235));
            setBackground(new Color(20, 25, 40));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int centerX = getWidth() / 2;
            int centerY = 115;

            // Shadow beneath the coin
            g2.setColor(new Color(5, 8, 15, 150));
            g2.fillOval(centerX - 78, centerY + 64, 156, 20);

            int side = finalResult;
            double scale = 1.0;

            if (flipping) {
                double angle = animationFrame * Math.PI / 5.0;
                scale = Math.abs(Math.cos(angle));

                if (Math.cos(angle) < 0) {
                    side = 1;
                } else {
                    side = 0;
                }
            }

            int coinWidth = Math.max(8, (int) (150 * scale));
            int coinHeight = 150;

            int x = centerX - coinWidth / 2;
            int y = centerY - coinHeight / 2;

            // Coin thickness / 3D-style edge
            g2.setColor(new Color(145, 91, 20));
            g2.fillOval(x, y + 7, coinWidth, coinHeight);

            g2.setColor(new Color(255, 215, 100));
            g2.fillOval(x, y, coinWidth, coinHeight);

            if (coinWidth > 35) {
                g2.setColor(new Color(190, 130, 30));
                g2.setStroke(new BasicStroke(4));
                g2.drawOval(x + 5, y + 5,
                        Math.max(1, coinWidth - 10), coinHeight - 10);

                g2.setColor(new Color(255, 235, 160));
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(x + 11, y + 11,
                        Math.max(1, coinWidth - 22), coinHeight - 22);

                String face = side == 0 ? "H" : "T";

                g2.setColor(new Color(110, 65, 12));
                g2.setFont(new Font("Serif", Font.BOLD, 55));

                FontMetrics fm = g2.getFontMetrics();
                int textX = centerX - fm.stringWidth(face) / 2;
                int textY = centerY + fm.getAscent() / 3;

                g2.drawString(face, textX, textY);

                g2.setFont(new Font("Arial", Font.BOLD, 12));
                String label = side == 0 ? "HEADS" : "TAILS";
                FontMetrics labelMetrics = g2.getFontMetrics();

                g2.drawString(label,
                        centerX - labelMetrics.stringWidth(label) / 2,
                        centerY + 43);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CoinTossGame::new);
    }
}