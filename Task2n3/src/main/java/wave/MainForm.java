import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MainForm extends JFrame {
    private JButton newBtn = new JButton("New task");
    private static JButton calcBtn = new JButton("Сalculate");
    public static JLabel squares[][] = new JLabel[8][8];
    public JPanel panel;

    public static int[][] a=new int[8][8];
    static int rowbeg, colbeg, rowend, colend;

    public MainForm() {
        super("Chessboard");
        this.setBounds(100, 100, 500, 450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setSize(500, 500);
        panel.setLayout(new GridLayout(8, 8));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new JLabel();
                squares[i][j].setOpaque(true);

                if ((i + j) % 2 == 0) {
                    squares[i][j].setBackground(new Color(205,133,63));
                } else {
                    squares[i][j].setBackground(Color.white);
                }
                panel.add(squares[i][j]);
            }
        }


        Container container = this.getContentPane();
        container.setLayout(new BorderLayout());

        newBtn.addActionListener(new MainForm.ButtonNewEventListener());
        calcBtn.addActionListener(new MainForm.ButtonCalcEventListener());

        container.add(panel,BorderLayout.CENTER);
        container.add(newBtn,BorderLayout.NORTH);
        container.add(calcBtn,BorderLayout.SOUTH);
    }

    class ButtonNewEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((i + j) % 2 == 0) {
                        squares[i][j].setBackground(new Color(205,133,63));
                    } else {
                        squares[i][j].setBackground(Color.white);
                    }
                }
            }
            Random random = new Random();
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    float c = random.nextFloat();
                    if (c <= 0.5)
                        a[i][j] = -1;
                    else
                        a[i][j] = 0;
                }
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    squares[i][j].setText(String.valueOf(a[i][j]));
                }
            InputForm inForm=new InputForm();
            inForm.FormCreate();
        }

    }

    public static void helper(){
        rowbeg = InputForm.xBeg;
        colbeg = InputForm.yBeg;
        rowend = InputForm.xEnd;
        colend = InputForm.yEnd;
        a[colend][rowend] = 0;
        squares[colend][rowend].setText("Start");
        squares[colbeg][rowbeg].setText("Finish");
        calcBtn.setEnabled(true);
    }

    class ButtonCalcEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (!Algorithm.findWay(rowbeg, colbeg, rowend, colend, a))
                JOptionPane.showMessageDialog(null, "Unable to find a way!", "Output", JOptionPane.PLAIN_MESSAGE);
            else {
                try {
                    Algorithm.buildWay(rowbeg, colbeg, rowend, colend, a);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                calcBtn.setEnabled(false);
                JOptionPane.showMessageDialog(null, "The way is found!", "Output", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }

    public void FormCreate(){
        this.setVisible(true);
        calcBtn.setEnabled(false);
    }

}
