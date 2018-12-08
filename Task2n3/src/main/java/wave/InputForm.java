import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputForm extends JFrame {

    static boolean res;
    static int xBeg;
    static int yBeg;
    static int xEnd;
    static int yEnd;

    private JButton okBtn=new JButton("OK");
    private JLabel begLbl=new JLabel("Start coordinates:");
    private JLabel endLbl=new JLabel("End coordinates:");
    private JLabel row1Lbl=new JLabel("Row:");
    private JLabel col1Lbl=new JLabel("Column:");
    private JLabel row2Lbl=new JLabel("Row:");
    private JLabel col2Lbl=new JLabel("Column:");
    SpinnerModel numbers1 = new SpinnerNumberModel(0, 0, 7, 1);
    SpinnerModel numbers2 = new SpinnerNumberModel(0, 0, 7, 1);
    SpinnerModel numbers3 = new SpinnerNumberModel(0, 0, 7, 1);
    SpinnerModel numbers4 = new SpinnerNumberModel(0, 0, 7, 1);
    JSpinner spinInt1   = new JSpinner(numbers1);
    JSpinner spinInt2   = new JSpinner(numbers2);
    JSpinner spinInt3   = new JSpinner(numbers3);
    JSpinner spinInt4   = new JSpinner(numbers4);

    public InputForm(){
        super("Input");
        this.setBounds(100,100,200,450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        Container container=this.getContentPane();
        container.setLayout(new GridLayout(11,1, 10,10));

        okBtn.addActionListener(new ButtonOkEventListener());


        container.add(begLbl);
        container.add(row1Lbl);
        container.add(spinInt1);
        container.add(col1Lbl);
        container.add(spinInt2);
        container.add(endLbl);
        container.add(row2Lbl);
        container.add(spinInt3);
        container.add(col2Lbl);
        container.add(spinInt4);
        container.add(okBtn);
    }

    class  ButtonOkEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (spinInt1.getValue()==spinInt3.getValue() && spinInt2.getValue()==spinInt4.getValue()){
                JOptionPane.showMessageDialog(null, "Start and finish can not match!", "Output", JOptionPane.PLAIN_MESSAGE);
                res=false;
            }
            else {
                res=true;
                xBeg=(Integer.parseInt(spinInt1.getValue().toString()));
                yBeg=(Integer.parseInt(spinInt2.getValue().toString()));
                xEnd=(Integer.parseInt(spinInt3.getValue().toString()));
                yEnd=(Integer.parseInt(spinInt4.getValue().toString()));
                MainForm.helper();
                dispose();
            }
        }
    }

    public void FormCreate(){
        this.setVisible(true);
    }
}
