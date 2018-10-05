package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class FormGUI extends JFrame {
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
    private JLabel inputWrdLbl=new JLabel("Input word:");
    private JLabel addWrdLbl=new JLabel("Input word:");
    private JLabel vocLbl=new JLabel("Vocabulary:");
    private JLabel resLbl=new JLabel("Suitable words:");
    JScrollPane outputscrollVoc = new JScrollPane(txtVoc);
    JScrollPane outputscrollSeq = new JScrollPane(txtSeq);

    public FormGUI(){
        super("Sequences");
        this.setBounds(100,100,850,450);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        outputscrollVoc.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        outputscrollSeq.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        mainBtn.setBounds(215,80,130,30);
        inputWrd.setBounds(23,80,130,30);
        inputWrdLbl.setBounds(23,60,130,15);
        label.setBounds(215,150,150,70);
        conBtn.setBounds(215,130,130,30);
        outputscrollVoc.setBounds(575,35,250,165);
        txtVoc.setEditable(false);
        vocLbl.setBounds(575, 10, 250, 15);
        outputscrollSeq.setBounds(575,235,250,165);
        txtSeq.setEditable(false);
        resLbl.setBounds(575,210,250,15);
        addBtn.setBounds(420,130,130,30);
        addWrdLbl.setBounds(420,60,130,15);
        addWrd.setBounds(420,80,130,30);
        delBtn.setBounds(420,180,130,30);
        cleanBtn.setBounds(420,230,130,30);
        openVocBtn.setBounds(420,280,130,30);

        Container container=this.getContentPane();
        container.setLayout(new GridLayout(6,4, 10,10));

        mainBtn.addActionListener(new ButtonMainEventListener());
        conBtn.addActionListener(new ButtonConEventListener());
        addBtn.addActionListener(new ButtonAddEventListener());
        delBtn.addActionListener(new ButtonDelEventListener());
        cleanBtn.addActionListener(new ButtonCleanEventListener());
        openVocBtn.addActionListener(new ButtonOpenVocEventListener());

        container.add(inputWrdLbl);
        container.add(new JLabel());
        container.add(addWrdLbl);
        container.add(vocLbl);

        container.add(inputWrd);
        container.add(mainBtn);
        container.add(addWrd);
        container.add(outputscrollVoc);

        container.add(new JLabel());
        container.add(conBtn);
        container.add(addBtn);
        container.add(resLbl);

        container.add(new JLabel());
        container.add(label);
        container.add(delBtn);
        container.add(outputscrollSeq);

        container.add(new JLabel());
        container.add(new JLabel());
        container.add(cleanBtn);
        container.add(new JLabel());

        container.add(new JLabel());
        container.add(new JLabel());
        container.add(openVocBtn);
        container.add(new JLabel());

    }

    class  ButtonOpenVocEventListener implements ActionListener {
        public void actionPerformed(ActionEvent e) { //open vocabulary from the file
            JFileChooser fileopen = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "txt", "txt");
            fileopen.setFileFilter(filter);
            int ret = fileopen.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                if (!checkFile(file))
                    JOptionPane.showMessageDialog(null, "Error opening file! Only English letters are allowed! Every word must begin with a new line!", "Output", JOptionPane.PLAIN_MESSAGE);
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
                label.setText("Number of sequences: "+Sequences.check(inputWrd.getText(),voc,seq));
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

    public void FormCreate(){
        this.setVisible(true);
        conBtn.setVisible(false);
    }
}
