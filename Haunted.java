import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Haunted extends JFrame {
    // Game constants
    private static final int LEVEL_1_TIME = 180; // 3 minutes
    private static final int LEVEL_2_TIME = 180; // 3 minutes
    private static final int LEVEL_3_TIME = 180; // 3 minutes
    private static final int LEVEL_4_TIME = 240; // 4 minutes
    private static final int LEVEL_5_TIME = 240; // 4 minutes
    
    // GUI Components
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel instructionLabel;
    private JLabel timerLabel;
    private JLabel scoreLabel;
    private JLabel inventoryLabel;
    private JButton hintButton;
    private JButton restartButton;
    
    // Game state
    private int currentLevel = 0;
    private int timeLeft;
    private int score = 0;
    private int hintsUsed = 0;
    private Timer gameTimer;
    private boolean gameActive = false;
    private ArrayList<String> inventory;
    
    public Haunted() {
        setTitle("🏚️ Haunted Room Escape");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(20, 10, 30));
        
        inventory = new ArrayList<>();
        
        createMenuBar();
        showMainMenu();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(40, 20, 50));
        
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setForeground(Color.WHITE);
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> showMainMenu());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGameItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }
    
    private void showMainMenu() {
        if (gameTimer != null) gameTimer.stop();
        
        getContentPane().removeAll();
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(20, 10, 30));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel title = new JLabel("HAUNTED ROOM ESCAPE");
        title.setFont(new Font("Serif", Font.BOLD, 42));
        title.setForeground(new Color(255, 50, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea story = new JTextArea(
            "\n\nYou wake up inside an old abandoned house.\n" +
            "The doors are locked.\n\n" +
            "Solve simple puzzles, find clues, and escape each room\n" +
            "before time runs out!\n\n" +
            "5 Easy Levels of Terror Await...\n\n"
        );
        story.setFont(new Font("Arial", Font.PLAIN, 16));
        story.setForeground(new Color(200, 200, 200));
        story.setBackground(new Color(20, 10, 30));
        story.setEditable(false);
        story.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton startButton = new JButton("START GAME");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setBackground(new Color(100, 30, 30));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startLevel1());
        
        menuPanel.add(title);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(story);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(startButton);
        
        add(menuPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void startLevel1() {
        currentLevel = 1;
        timeLeft = LEVEL_1_TIME;
        inventory.clear();
        
        getContentPane().removeAll();
        createGameHeader("LEVEL 1 - Power Circuit Room");
        
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(30, 20, 40));
        JLabel instruction = new JLabel("Turn on ALL switches to unlock the door!");
        instruction.setFont(new Font("Arial", Font.BOLD, 16));
        instruction.setForeground(new Color(255, 215, 0));
        instructionPanel.add(instruction);
        
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(3, 2, 15, 15));
        levelPanel.setBackground(new Color(30, 20, 40));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create 5 switches with interconnected logic
        final boolean[] switches = {false, false, false, false, false};
        
        JButton switch1 = createRoomButton("Switch 1", "OFF");
        JButton switch2 = createRoomButton("Switch 2", "OFF");
        JButton switch3 = createRoomButton("Switch 3", "OFF");
        JButton switch4 = createRoomButton("Switch 4", "OFF");
        JButton switch5 = createRoomButton("Switch 5", "OFF");
        JButton door = createRoomButton("DOOR", "Locked - Turn on all power!");
        
        JButton[] switchButtons = {switch1, switch2, switch3, switch4, switch5};
        
        // Switch 1 → toggles 1 & 2
        switch1.addActionListener(e -> {
            switches[0] = !switches[0];
            switches[1] = !switches[1];
            updateSwitchDisplay(switchButtons, switches);
            checkPowerComplete(switches, door);
        });
        
        // Switch 2 → toggles 2 & 3
        switch2.addActionListener(e -> {
            switches[1] = !switches[1];
            switches[2] = !switches[2];
            updateSwitchDisplay(switchButtons, switches);
            checkPowerComplete(switches, door);
        });
        
        // Switch 3 → toggles only 3
        switch3.addActionListener(e -> {
            switches[2] = !switches[2];
            updateSwitchDisplay(switchButtons, switches);
            checkPowerComplete(switches, door);
        });
        
        // Switch 4 → toggles 4 & 5
        switch4.addActionListener(e -> {
            switches[3] = !switches[3];
            switches[4] = !switches[4];
            updateSwitchDisplay(switchButtons, switches);
            checkPowerComplete(switches, door);
        });
        
        // Switch 5 → toggles only 5
        switch5.addActionListener(e -> {
            switches[4] = !switches[4];
            updateSwitchDisplay(switchButtons, switches);
            checkPowerComplete(switches, door);
        });
        
        // Door - check if all switches are on
        door.addActionListener(e -> {
            if (switches[0] && switches[1] && switches[2] && switches[3] && switches[4]) {
                score += (timeLeft * 10);
                completeLevel("All power circuits activated!\nDoor unlocked!");
            } else {
                JOptionPane.showMessageDialog(this, "Not all circuits are powered!\nKeep experimenting with the switches.");
            }
        });
        
        levelPanel.add(switch1);
        levelPanel.add(switch2);
        levelPanel.add(switch3);
        levelPanel.add(switch4);
        levelPanel.add(switch5);
        levelPanel.add(door);
        
        add(instructionPanel, BorderLayout.NORTH);
        add(levelPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        startTimer();
    }
    
    private void updateSwitchDisplay(JButton[] buttons, boolean[] switches) {
        for (int i = 0; i < switches.length; i++) {
            String status = switches[i] ? "ON ⚡" : "OFF";
            buttons[i].setText("<html><center>Switch " + (i+1) + "<br>" + status + "</center></html>");
            buttons[i].setBackground(switches[i] ? new Color(80, 150, 80) : new Color(100, 30, 30));
        }
    }
    
    private void checkPowerComplete(boolean[] switches, JButton door) {
        boolean allOn = true;
        for (boolean s : switches) {
            if (!s) {
                allOn = false;
                break;
            }
        }
        
        if (allOn) {
            door.setText("<html><center>DOOR<br>UNLOCKED! Click to exit!</center></html>");
            door.setBackground(new Color(80, 150, 80));
        } else {
            door.setText("<html><center>DOOR<br>Locked - Turn on all power!</center></html>");
            door.setBackground(new Color(100, 30, 30));
        }
    }
    
    private void startLevel2() {
        currentLevel = 2;
        timeLeft = LEVEL_2_TIME;
        inventory.clear();
        
        getContentPane().removeAll();
        createGameHeader("LEVEL 2 - The Study");
        
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(30, 20, 40));
        JLabel instruction = new JLabel("Find clues in the BOOK, DESK, and CLOCK to unlock the SAFE!");
        instruction.setFont(new Font("Arial", Font.BOLD, 14));
        instruction.setForeground(new Color(255, 215, 0));
        instructionPanel.add(instruction);
        
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(2, 2, 15, 15));
        levelPanel.setBackground(new Color(30, 20, 40));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        final boolean[] cluesFound = {false, false, false};
        
        JButton book = createRoomButton("BOOK", "Click to read");
        JButton desk = createRoomButton("DESK", "Click to search");
        JButton clock = createRoomButton("CLOCK", "Tick... tock...");
        JButton safe = createRoomButton("SAFE", "Locked - Need code!");
        
        book.addActionListener(e -> {
            if (!cluesFound[0]) {
                inventory.add("Clue 1: The first number is SEVEN");
                cluesFound[0] = true;
                updateInventory();
                book.setText("<html><center>BOOK<br>Already read</center></html>");
                book.setBackground(new Color(60, 60, 60));
                JOptionPane.showMessageDialog(this, "The book contains a riddle:\n'The first digit to freedom is SEVEN'");
            }
        });
        
        desk.addActionListener(e -> {
            if (!cluesFound[1]) {
                inventory.add("Clue 2: The second number is THREE");
                cluesFound[1] = true;
                updateInventory();
                desk.setText("<html><center>DESK<br>Already searched</center></html>");
                desk.setBackground(new Color(60, 60, 60));
                JOptionPane.showMessageDialog(this, "You found a note in the desk drawer:\n'THREE shall follow'");
            }
        });
        
        clock.addActionListener(e -> {
            if (!cluesFound[2]) {
                JOptionPane.showMessageDialog(this, "The clock chimes...\nWait for it...");
                Timer chimeTimer = new Timer(5000, evt -> {
                    inventory.add("Clue 3: The third number is FIVE");
                    cluesFound[2] = true;
                    updateInventory();
                    clock.setText("<html><center>CLOCK<br>Chimed</center></html>");
                    clock.setBackground(new Color(60, 60, 60));
                    JOptionPane.showMessageDialog(this, "The clock chimes FIVE times!");
                });
                chimeTimer.setRepeats(false);
                chimeTimer.start();
            }
        });
        
        safe.addActionListener(e -> {
            String code = JOptionPane.showInputDialog(this, "Enter the 3-digit code:");
            if ("735".equals(code)) {
                score += (timeLeft * 10);
                completeLevel("The safe opens!\nYou found the key to escape!");
            } else if (code != null) {
                JOptionPane.showMessageDialog(this, "Wrong code! Collect all clues first.");
            }
        });
        
        levelPanel.add(book);
        levelPanel.add(desk);
        levelPanel.add(clock);
        levelPanel.add(safe);
        
        add(instructionPanel, BorderLayout.NORTH);
        add(levelPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        startTimer();
    }
    
    private void startLevel3() {
        currentLevel = 3;
        timeLeft = LEVEL_3_TIME;
        inventory.clear();
        
        getContentPane().removeAll();
        createGameHeader("LEVEL 3 - The Gallery");
        
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(30, 20, 40));
        JLabel instruction = new JLabel("Count the symbols on the wall to unlock the door!");
        instruction.setFont(new Font("Arial", Font.BOLD, 14));
        instruction.setForeground(new Color(255, 215, 0));
        instructionPanel.add(instruction);
        
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BorderLayout());
        levelPanel.setBackground(new Color(30, 20, 40));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Wall with symbols
        JPanel wallPanel = new JPanel();
        wallPanel.setLayout(new GridLayout(2, 4, 10, 10));
        wallPanel.setBackground(new Color(40, 30, 50));
        wallPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 80, 120), 3),
            "Ancient Wall",
            0, 0,
            new Font("Serif", Font.BOLD, 16),
            new Color(200, 200, 200)
        ));
        
        String[] symbols = {"🕯️", "💀", "📚", "🦇", "🕯️", "📚", "🦇", "🦇", "📚", "🦇"};
        for (String symbol : symbols) {
            JLabel symbolLabel = new JLabel(symbol, SwingConstants.CENTER);
            symbolLabel.setFont(new Font("Serif", Font.PLAIN, 48));
            symbolLabel.setOpaque(true);
            symbolLabel.setBackground(new Color(30, 20, 40));
            wallPanel.add(symbolLabel);
        }
        
        JPanel doorPanel = new JPanel();
        doorPanel.setBackground(new Color(30, 20, 40));
        JButton door = createRoomButton("DOOR", "Enter 4-digit code");
        door.setPreferredSize(new Dimension(300, 100));
        
        door.addActionListener(e -> {
            String code = JOptionPane.showInputDialog(this, 
                "Count each symbol type (left to right order):\n" +
                "Candle, Skull, Book, Bat\n\n" +
                "Enter 4-digit code:");
            
            if ("2134".equals(code)) {
                score += (timeLeft * 10);
                completeLevel("Correct! The ancient door opens!");
            } else if (code != null) {
                JOptionPane.showMessageDialog(this, "Wrong code! Count the symbols carefully.");
            }
        });
        
        doorPanel.add(door);
        
        levelPanel.add(wallPanel, BorderLayout.CENTER);
        levelPanel.add(doorPanel, BorderLayout.SOUTH);
        
        add(instructionPanel, BorderLayout.NORTH);
        add(levelPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        startTimer();
    }
    
    private void startLevel4() {
        currentLevel = 4;
        timeLeft = LEVEL_4_TIME;
        inventory.clear();
        
        getContentPane().removeAll();
        createGameHeader("LEVEL 4 - Sliding Block Puzzle");
        
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(30, 20, 40));
        JLabel instruction = new JLabel("Move the RED block to the EXIT! Push blocks to clear the path.");
        instruction.setFont(new Font("Arial", Font.BOLD, 14));
        instruction.setForeground(new Color(255, 215, 0));
        instructionPanel.add(instruction);
        
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BorderLayout(10, 10));
        levelPanel.setBackground(new Color(30, 20, 40));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create 5x5 grid
        final int GRID_SIZE = 5;
        JButton[][] grid = new JButton[GRID_SIZE][GRID_SIZE];
        
        // Grid layout: 0=empty, 1=red block (key), 2-5=other blocks, 9=exit
        final int[][] gameGrid = {
            {0, 2, 2, 0, 9},
            {0, 0, 0, 0, 0},
            {3, 3, 1, 0, 4},
            {0, 0, 0, 0, 4},
            {0, 5, 5, 5, 0}
        };
        
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE, 3, 3));
        gridPanel.setBackground(new Color(20, 10, 30));
        
        // Create grid buttons
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JButton cell = new JButton();
                cell.setPreferredSize(new Dimension(80, 80));
                cell.setFocusPainted(false);
                cell.setFont(new Font("Arial", Font.BOLD, 20));
                
                switch (gameGrid[i][j]) {
                    case 0: // Empty
                        cell.setBackground(new Color(40, 30, 50));
                        cell.setText("");
                        break;
                    case 1: // Red key block
                        cell.setBackground(new Color(200, 50, 50));
                        cell.setForeground(Color.WHITE);
                        cell.setText("🔑");
                        break;
                    case 2: case 3: case 4: case 5: // Other blocks
                        cell.setBackground(new Color(80, 60, 100));
                        cell.setForeground(Color.WHITE);
                        cell.setText("▮");
                        break;
                    case 9: // Exit
                        cell.setBackground(new Color(50, 150, 50));
                        cell.setForeground(Color.WHITE);
                        cell.setText("🚪");
                        break;
                }
                
                grid[i][j] = cell;
                gridPanel.add(cell);
            }
        }
        
        // Control panel with directional buttons
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout(5, 5));
        controlPanel.setBackground(new Color(30, 20, 40));
        
        JPanel directionPanel = new JPanel();
        directionPanel.setLayout(new GridBagLayout());
        directionPanel.setBackground(new Color(30, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JButton upBtn = new JButton("↑");
        JButton downBtn = new JButton("↓");
        JButton leftBtn = new JButton("←");
        JButton rightBtn = new JButton("→");
        
        JButton[] dirButtons = {upBtn, downBtn, leftBtn, rightBtn};
        for (JButton btn : dirButtons) {
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            btn.setPreferredSize(new Dimension(70, 70));
            btn.setBackground(new Color(100, 30, 30));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
        }
        
        // Up button
        gbc.gridx = 1; gbc.gridy = 0;
        directionPanel.add(upBtn, gbc);
        
        // Left button
        gbc.gridx = 0; gbc.gridy = 1;
        directionPanel.add(leftBtn, gbc);
        
        // Right button
        gbc.gridx = 2; gbc.gridy = 1;
        directionPanel.add(rightBtn, gbc);
        
        // Down button
        gbc.gridx = 1; gbc.gridy = 2;
        directionPanel.add(downBtn, gbc);
        
        JLabel moveLabel = new JLabel("Move Red Block:", SwingConstants.CENTER);
        moveLabel.setFont(new Font("Arial", Font.BOLD, 14));
        moveLabel.setForeground(new Color(200, 200, 200));
        
        controlPanel.add(moveLabel, BorderLayout.NORTH);
        controlPanel.add(directionPanel, BorderLayout.CENTER);
        
        // Movement logic
        upBtn.addActionListener(e -> {
            if (moveBlock(gameGrid, grid, -1, 0, GRID_SIZE)) {
                checkWin(gameGrid, GRID_SIZE);
            }
        });
        
        downBtn.addActionListener(e -> {
            if (moveBlock(gameGrid, grid, 1, 0, GRID_SIZE)) {
                checkWin(gameGrid, GRID_SIZE);
            }
        });
        
        leftBtn.addActionListener(e -> {
            if (moveBlock(gameGrid, grid, 0, -1, GRID_SIZE)) {
                checkWin(gameGrid, GRID_SIZE);
            }
        });
        
        rightBtn.addActionListener(e -> {
            if (moveBlock(gameGrid, grid, 0, 1, GRID_SIZE)) {
                checkWin(gameGrid, GRID_SIZE);
            }
        });
        
        levelPanel.add(gridPanel, BorderLayout.CENTER);
        levelPanel.add(controlPanel, BorderLayout.SOUTH);
        
        add(instructionPanel, BorderLayout.NORTH);
        add(levelPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        startTimer();
    }
    
    private boolean moveBlock(int[][] gameGrid, JButton[][] grid, int dx, int dy, int gridSize) {
        // Find red block position
        int redRow = -1, redCol = -1;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gameGrid[i][j] == 1) {
                    redRow = i;
                    redCol = j;
                    break;
                }
            }
            if (redRow != -1) break;
        }
        
        int newRow = redRow + dx;
        int newCol = redCol + dy;
        
        // Check bounds
        if (newRow < 0 || newRow >= gridSize || newCol < 0 || newCol >= gridSize) {
            return false;
        }
        
        // Check if can move
        int targetCell = gameGrid[newRow][newCol];
        
        if (targetCell == 0 || targetCell == 9) {
            // Move to empty space or exit
            gameGrid[redRow][redCol] = 0;
            gameGrid[newRow][newCol] = 1;
            
            // Update display
            grid[redRow][redCol].setBackground(new Color(40, 30, 50));
            grid[redRow][redCol].setText("");
            
            if (targetCell == 9) {
                grid[newRow][newCol].setBackground(new Color(50, 150, 50));
                grid[newRow][newCol].setText("✓");
            } else {
                grid[newRow][newCol].setBackground(new Color(200, 50, 50));
                grid[newRow][newCol].setText("🔑");
            }
            
            return true;
        } else {
            // Try to push block
            int pushRow = newRow + dx;
            int pushCol = newCol + dy;
            
            // Check if can push
            if (pushRow >= 0 && pushRow < gridSize && pushCol >= 0 && pushCol < gridSize) {
                if (gameGrid[pushRow][pushCol] == 0) {
                    // Push block
                    int blockType = gameGrid[newRow][newCol];
                    gameGrid[pushRow][pushCol] = blockType;
                    gameGrid[newRow][newCol] = 1;
                    gameGrid[redRow][redCol] = 0;
                    
                    // Update display
                    grid[redRow][redCol].setBackground(new Color(40, 30, 50));
                    grid[redRow][redCol].setText("");
                    
                    grid[newRow][newCol].setBackground(new Color(200, 50, 50));
                    grid[newRow][newCol].setText("🔑");
                    
                    grid[pushRow][pushCol].setBackground(new Color(80, 60, 100));
                    grid[pushRow][pushCol].setText("▮");
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void checkWin(int[][] gameGrid, int gridSize) {
        // Check if red block is at exit (0, 4)
        if (gameGrid[0][4] == 1) {
            score += (timeLeft * 10);
            completeLevel("The key block reached the exit!\nDoor unlocked!");
        }
    }
    
    private void startLevel5() {
        currentLevel = 5;
        timeLeft = LEVEL_5_TIME;
        inventory.clear();
        
        getContentPane().removeAll();
        createGameHeader("LEVEL 5 - The Final Room");
        
        JPanel instructionPanel = new JPanel();
        instructionPanel.setBackground(new Color(30, 20, 40));
        JLabel instruction = new JLabel("Solve the final puzzle to escape the haunted house!");
        instruction.setFont(new Font("Arial", Font.BOLD, 14));
        instruction.setForeground(new Color(255, 215, 0));
        instructionPanel.add(instruction);
        
        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new GridLayout(2, 2, 15, 15));
        levelPanel.setBackground(new Color(30, 20, 40));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton painting = createRoomButton("PAINTING", "Shows room numbers");
        JButton book = createRoomButton("BOOK", "Ancient calculations");
        JButton mirror = createRoomButton("MIRROR", "Reflection of truth");
        JButton safe = createRoomButton("SAFE", "Final lock - 4 digits");
        
        final boolean[] examinedItems = {false, false, false};
        
        painting.addActionListener(e -> {
            if (!examinedItems[0]) {
                inventory.add("Painting: Rooms 2, 5, 8");
                examinedItems[0] = true;
                updateInventory();
                painting.setBackground(new Color(60, 60, 60));
                JOptionPane.showMessageDialog(this, 
                    "The painting shows three rooms:\n\n" +
                    "Room 2\nRoom 5\nRoom 8");
            }
        });
        
        book.addActionListener(e -> {
            if (!examinedItems[1]) {
                inventory.add("Book: Sum of room numbers");
                examinedItems[1] = true;
                updateInventory();
                book.setBackground(new Color(60, 60, 60));
                JOptionPane.showMessageDialog(this, 
                    "The ancient book says:\n\n" +
                    "'Add the room numbers together\n" +
                    "to unlock your freedom'");
            }
        });
        
        mirror.addActionListener(e -> {
            if (!examinedItems[2]) {
                inventory.add("Mirror: 4-digit code needed");
                examinedItems[2] = true;
                updateInventory();
                mirror.setBackground(new Color(60, 60, 60));
                JOptionPane.showMessageDialog(this, 
                    "The mirror shows:\n\n" +
                    "'Enter the sum as 4 digits\n" +
                    "Add zeros if needed'");
            }
        });
        
        safe.addActionListener(e -> {
            String code = JOptionPane.showInputDialog(this, 
                "Enter the 4-digit code:\n(Sum of room numbers)");
            
            if ("0015".equals(code) || "15".equals(code)) {
                score += (timeLeft * 10);
                gameActive = false;
                if (gameTimer != null) gameTimer.stop();
                winGame();
            } else if (code != null) {
                JOptionPane.showMessageDialog(this, 
                    "Wrong code! Examine all items and solve the puzzle.");
            }
        });
        
        levelPanel.add(painting);
        levelPanel.add(book);
        levelPanel.add(mirror);
        levelPanel.add(safe);
        
        add(instructionPanel, BorderLayout.NORTH);
        add(levelPanel, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        startTimer();
    }
    
    private JButton createRoomButton(String title, String subtitle) {
        JButton button = new JButton("<html><center>" + title + "<br>" + subtitle + "</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(100, 30, 30));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(150, 50, 50), 2));
        return button;
    }
    
    private void createGameHeader(String levelTitle) {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(40, 20, 50));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        titleLabel = new JLabel(levelTitle);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(40, 20, 50));
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        timerLabel = new JLabel("Time: " + formatTime(timeLeft));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setForeground(new Color(100, 255, 100));
        
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(255, 215, 0));
        
        inventoryLabel = new JLabel("Items: 0");
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        inventoryLabel.setForeground(new Color(150, 150, 255));
        
        hintButton = new JButton("Hint (-50pts)");
        hintButton.setBackground(new Color(80, 80, 100));
        hintButton.setForeground(Color.WHITE);
        hintButton.addActionListener(e -> showHint());
        
        restartButton = new JButton("Restart Level");
        restartButton.setBackground(new Color(80, 80, 100));
        restartButton.setForeground(Color.WHITE);
        restartButton.addActionListener(e -> restartCurrentLevel());
        
        infoPanel.add(timerLabel);
        infoPanel.add(scoreLabel);
        infoPanel.add(inventoryLabel);
        infoPanel.add(hintButton);
        infoPanel.add(restartButton);
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void showHint() {
        if (score >= 50) {
            score -= 50;
            hintsUsed++;
            updateScore();
        }
        
        String hint = "";
        switch (currentLevel) {
            case 1:
                hint = "Try this sequence:\n\n" +
                       "1. Click Switch 3\n" +
                       "2. Click Switch 1\n" +
                       "3. Click Switch 5\n\n" +
                       "All lights should be ON!";
                break;
            case 2:
                hint = "Steps to solve:\n\n" +
                       "1. Read the BOOK (first number: 7)\n" +
                       "2. Search the DESK (second number: 3)\n" +
                       "3. Wait for the CLOCK to chime (takes 5 seconds)";
                break;
            case 3:
                hint = "Count the symbols on the wall:\n\n" +
                       "2 Candles\n" +
                       "1 Skull\n" +
                       "3 Books\n" +
                       "4 Bats\n\n" +
                       "Code from left to right: 2134";
                break;
            case 4:
                hint = "Solution path:\n\n" +
                       "1. Move DOWN twice (push block)\n" +
                       "2. Move RIGHT three times\n" +
                       "3. Move UP twice to reach exit!\n\n" +
                       "You can push blocks to clear your path.";
                break;
            case 5:
                hint = "Look at the room numbers on the wall:\n" +
                       "Room 2, Room 5, Room 8\n\n" +
                       "The book says: 'Sum of room numbers'\n" +
                       "2 + 5 + 8 = 15\n\n" +
                       "Safe code (4 digits): 0015";
                break;
        }
        
        JOptionPane.showMessageDialog(this, hint, "HINT (-50 points)", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restartCurrentLevel() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Restart this level? Your progress will be lost.", 
            "Restart Level", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            switch (currentLevel) {
                case 1: startLevel1(); break;
                case 2: startLevel2(); break;
                case 3: startLevel3(); break;
                case 4: startLevel4(); break;
                case 5: startLevel5(); break;
            }
        }
    }
    
    private void startTimer() {
        gameActive = true;
        if (gameTimer != null) gameTimer.stop();
        
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            updateTimer();
            
            if (timeLeft <= 0) {
                gameOver();
            }
        });
        gameTimer.start();
    }
    
    private void updateTimer() {
        timerLabel.setText("Time: " + formatTime(timeLeft));
        if (timeLeft <= 30) {
            timerLabel.setForeground(new Color(255, 80, 80));
        }
    }
    
    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }
    
    private void updateInventory() {
        inventoryLabel.setText("Items: " + inventory.size());
    }
    
    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", mins, secs);
    }
    
    private void completeLevel(String message) {
        gameActive = false;
        if (gameTimer != null) gameTimer.stop();
        
        JOptionPane.showMessageDialog(this, message, 
            "Level Complete!", JOptionPane.INFORMATION_MESSAGE);
        
        if (currentLevel == 1) {
            startLevel2();
        } else if (currentLevel == 2) {
            startLevel3();
        } else if (currentLevel == 3) {
            startLevel4();
        } else if (currentLevel == 4) {
            startLevel5();
        }
    }
    
    private void winGame() {
        gameActive = false;
        if (gameTimer != null) gameTimer.stop();
        
        int bonusScore = score + (hintsUsed == 0 ? 500 : 0);
        
        String message = "*** CONGRATULATIONS! ***\n\n" +
                        "You escaped all 5 haunted rooms!\n\n" +
                        "Final Score: " + score + "\n" +
                        "Hints Used: " + hintsUsed + "\n" +
                        (hintsUsed == 0 ? "Perfect! +500 Bonus!\n" : "") +
                        "Total Score: " + bonusScore;
        
        JOptionPane.showMessageDialog(this, message, 
            "YOU WIN!", JOptionPane.INFORMATION_MESSAGE);
        
        showMainMenu();
    }
    
    private void gameOver() {
        gameActive = false;
        if (gameTimer != null) gameTimer.stop();
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Time's up! You're trapped forever...\n\nTry again?", 
            "Game Over", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            restartCurrentLevel();
        } else {
            showMainMenu();
        }
    }
    
    // Main method - entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Haunted());
    }
}