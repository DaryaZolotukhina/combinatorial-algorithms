import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class FormGUI extends JFrame{
    private JButton mainBtn=new JButton("Count");
    private JButton conBtn=new JButton("Continue");
    private JButton addBtn=new JButton("Add word");
    private JButton delBtn=new JButton("Delete word");
    private JButton cleanBtn=new JButton("Clean voc.");
    private JButton openVocBtn=new JButton("Open voc. from..");
    private JTextField inputWrd=new JTextField("",10);
    private JTextField addWrd=new JTextField("",10);
    public JTextPane txtVoc=new JTextPane();
    public JTextPane txtSeq=new JTextPane();
    private JLabel label=new JLabel();

    public FormGUI(){
        super("Sequences");
        this.setBounds(100,100,650,450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainBtn.setBounds(48,100,95,40);
        inputWrd.setBounds(23,70,150,25);
        label.setBounds(23,150,150,70);
        conBtn.setBounds(48,220,95,30);
        txtVoc.setBounds(375,10,250,230);
        txtVoc.setEditable(false);
        txtSeq.setBounds(375,250,250,150);
        txtSeq.setEditable(false);
        addBtn.setBounds(220,80,130,30);
        addWrd.setBounds(220,130,130,30);
        delBtn.setBounds(220,180,130,30);
        cleanBtn.setBounds(220,230,130,30);
        openVocBtn.setBounds(220,280,130,30);

        Container container=this.getContentPane();
        container.setLayout(null);

        mainBtn.addActionListener(new ButtonMainEventListener());
        conBtn.addActionListener(new ButtonConEventListener());
        addBtn.addActionListener(new ButtonAddEventListener());
        delBtn.addActionListener(new ButtonDelEventListener());
        cleanBtn.addActionListener(new ButtonCleanEventListener());
        openVocBtn.addActionListener(new ButtonOpenVocEventListener());

        container.add(mainBtn);
        container.add(conBtn);
        container.add(addBtn);
        container.add(delBtn);
        container.add(cleanBtn);
        container.add(openVocBtn);
        container.add(inputWrd);
        container.add(addWrd);
        container.add(txtVoc);
        container.add(txtSeq);
        container.add(label);

    }

    class  ButtonOpenVocEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { //open vocabulary from the file
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                if (!checkFile(file))
                    JOptionPane.showMessageDialog(null, "File contains invalid characters!", "Output", JOptionPane.PLAIN_MESSAGE);
                else {
                    txtVoc.setText("");//clean the vocabulary field
                    try {
                        FileReader fr = new FileReader(file);
                        BufferedReader reader = new BufferedReader(fr);
                        String line = reader.readLine();//read the first line
                        while (line != null) {//read all lines in the file
                            try {//add to the vocabulary field
                                Document doc = txtVoc.getDocument();
                                doc.insertString(doc.getLength(), line+'\n', null);
                            } catch (BadLocationException exc) {
                                exc.printStackTrace();
                            }
                            line = reader.readLine();
                        }
                    } catch (FileNotFoundException m) {
                        m.printStackTrace();
                    } catch (IOException m) {
                        m.printStackTrace();
                    }
                    }
                }
            }
        }

    public boolean checkFile(File file) { //check the file for the content of invalid characters
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();//read the first line
            while (line != null) {//read all lines in the file
                if (Pattern.matches("[a-zA-Z]+", line)==false)//checking
                    return false;
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    class  ButtonMainEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) { //count sequences
            if (inputWrd.getText().equals("")) //if word wasn't entered
                JOptionPane.showMessageDialog(null, "Enter the word!", "Output", JOptionPane.PLAIN_MESSAGE);
            else if (!Pattern.matches("[a-zA-Z]+", inputWrd.getText())) { //if there are invalid characters in the word
                JOptionPane.showMessageDialog(null, "Only English letters are allowed!", "Output", JOptionPane.PLAIN_MESSAGE);
                addWrd.setText("");
            }
            else
            if (txtVoc.getText().equals("")) //if vocabulary is empty
                JOptionPane.showMessageDialog(null, "Fill the vocabulary!", "Output", JOptionPane.PLAIN_MESSAGE);
            else {
                String[] voc = txtVoc.getText().split("\\r\\n"); //split the vocabulary
                ArrayList<String> seq=new ArrayList<String>();
                MakeEnable(false);
                //print the result
                label.setText("Number of sequences: "+Sequences.Check(inputWrd.getText(),voc,seq));
                for(int j=0;j<seq.size();j++) { //print result sequences
                    try {
                        Document doc = txtSeq.getDocument();
                        doc.insertString(doc.getLength(), seq.get(j) + '\n', null);
                    } catch (BadLocationException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }
    }

    class  ButtonConEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) { // "continue" button
            MakeEnable(true);
            addWrd.setText("");
            txtSeq.setText("");
        }
    }

    class  ButtonAddEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { //add new word
            String inputStr = addWrd.getText();
            if (inputStr.equals(""))//if word wasn't entered
                JOptionPane.showMessageDialog(null, "Enter the word!", "Output", JOptionPane.PLAIN_MESSAGE);
            else if (!Pattern.matches("[a-zA-Z]+", inputStr)) { //if there are invalid characters in the word
                JOptionPane.showMessageDialog(null, "Only English letters are allowed!", "Output", JOptionPane.PLAIN_MESSAGE);
                addWrd.setText("");
            }
            else {
                String[] arrayOfString = txtVoc.getText().split("\\r\\n"); //split the vocabulary
                if (!Arrays.asList(arrayOfString).contains(inputStr)) {
                    try { //add to the vocabulary
                        Document doc = txtVoc.getDocument();
                        doc.insertString(doc.getLength(), inputStr+'\n', null);
                    } catch (BadLocationException exc) {
                        exc.printStackTrace();
                    }
                } else //if the word already exists
                    JOptionPane.showMessageDialog(null, "This word already exists in the vocabulary!", "Output", JOptionPane.PLAIN_MESSAGE);
                addWrd.setText("");
            }
        }
    }
    class  ButtonDelEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) { //delete the word
            String inputStr = addWrd.getText();
            if (inputStr.equals("")) //if word wasn't entered
                JOptionPane.showMessageDialog(null, "Enter the word!", "Output", JOptionPane.PLAIN_MESSAGE);
            else {
                String[] arrayOfString = txtVoc.getText().split("\\r\\n"); //split the vocabulary
                ArrayList<String> listOfString = new ArrayList<String>();
                Collections.addAll(listOfString, arrayOfString);
                int i=listOfString.indexOf(inputStr); //search the word in the vocabulary
                if (i!=(-1)) {
                    listOfString.remove(i); //delete
                    txtVoc.setText("");
                    for(int j=0;j<listOfString.size();j++) { //print the vocabulary
                        try {
                            Document doc = txtVoc.getDocument();
                            doc.insertString(doc.getLength(), listOfString.get(j) + '\n', null);
                        } catch (BadLocationException exc) {
                            exc.printStackTrace();
                        }
                    }
                }
                else //if this word isn't in the vocabulary
                    JOptionPane.showMessageDialog(null, "This word doesn't exist in the vocabulary!", "Output", JOptionPane.PLAIN_MESSAGE);
            }
            addWrd.setText("");
        }
    }
    class  ButtonCleanEventListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            txtVoc.setText("");
        } //clean the vocabulary
    }


    public void MakeEnable(boolean flag){
        addBtn.setEnabled(flag);
        mainBtn.setEnabled(flag);
        delBtn.setEnabled(flag);
        cleanBtn.setEnabled(flag);
        openVocBtn.setEnabled(flag);
        inputWrd.setEnabled(flag);
        addWrd.setEnabled(flag);
        txtVoc.setEnabled(flag);
        conBtn.setVisible(!flag);
        label.setVisible(!flag);
    }
}
