import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.JSONObject;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Main extends JFrame implements ActionListener{
	private JLabel chooseFiletitle,chooseFileSubTitle,fileInfoTitle,fileInfoAuthor,fileInfoCreatedDate,fileInfoNKeys,outOf,keyLabel;
	private JTextArea fileInfoMessage;
	private JScrollPane fileInfoMessageScrollPane;
	private int nkeys;
	private JTextField key;
	private JButton chooseFileButton,fileInfoOpen,fileInfoBack,next,redo,finish;
	private JPanel chooseFile,fileInfo,keys;
	private BoxFileZipUtils bfzu;
	
	public void initComponents() {
		//title= new JLabel("SafeBox v 1.0");
		this.setTitle("SafeBox v 1.0");
		keyLabel = new JLabel("Key: ");
		outOf= new JLabel("x out of y");
		key= new JTextField(20);
		next= new JButton("Next");
		redo= new JButton("Redo");
		finish= new JButton("Finish");
		
		chooseFile = new JPanel();
		chooseFile.setLayout(new GridLayout(3,2));
		chooseFiletitle= new JLabel("SafeBox v 1.0");
		chooseFiletitle.setHorizontalAlignment(SwingConstants.CENTER);
		chooseFiletitle.setFont(new Font("Serif", Font.PLAIN,30));
		chooseFileSubTitle= new JLabel("Decryption utility");
		chooseFileSubTitle.setHorizontalAlignment(SwingConstants.CENTER);
		chooseFileSubTitle.setFont(new Font("Serif", Font.PLAIN,15));
		chooseFileButton= new JButton("Open Safe");
		
		chooseFile.add(chooseFiletitle);
		chooseFile.add(chooseFileSubTitle);
		chooseFile.add(chooseFileButton);
		chooseFile.setVisible(true);
		chooseFileButton.addActionListener(this);
		this.add(chooseFile);
		
	}
	private void fileChoose() {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(new BoxFilter());
		jfc.setAcceptAllFileFilterUsed(false);
		int rv= jfc.showOpenDialog(this);
		if(rv != JFileChooser.APPROVE_OPTION) System.exit(1);
        File file = jfc.getSelectedFile();
        try {
			bfzu= new BoxFileZipUtils(file);
			JSONObject j= bfzu.getDetails();
			this.fileInfoCreatedDate= new JLabel(Instant.ofEpochSecond(j.getLong("unixcreated")).toString());
			this.fileInfoTitle= new JLabel(j.getString("title"));
			this.fileInfoAuthor= new JLabel(j.getString("author"));
			this.fileInfoMessage= new JTextArea(j.getString("message"));
			this.nkeys=j.getInt("nkeys");
			this.fileInfoNKeys=new JLabel(String.valueOf(this.nkeys));
			this.fileInfoMessageScrollPane= new JScrollPane(this.fileInfoMessage);
			this.fileInfoOpen= new JButton("Open Safe");
			this.fileInfoBack= new JButton("Back");
			this.fileInfo = new JPanel();
			this.fileInfo.setLayout(new GridLayout(6,1));
			this.fileInfo.add(fileInfoTitle);
			this.fileInfo.add(fileInfoAuthor);
			this.fileInfo.add(this.fileInfoCreatedDate);
			this.fileInfo.add(fileInfoMessageScrollPane);
			this.fileInfo.add(this.fileInfoOpen);
			this.fileInfo.add(this.fileInfoBack);
			this.chooseFile.hide();
			this.add(this.fileInfo);
			this.fileInfo.show();
			this.fileInfoOpen.addActionListener(this);
			this.fileInfoBack.addActionListener(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
				    "The selected file is corrupted",
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
        
	}
	private ArrayList<String> getKeys(){
		ArrayList<String> s = new ArrayList<String>();
		for(int i=0;i<this.nkeys;i++) {
		String r = (String)JOptionPane.showInputDialog(
                this,
                "Insert key No. "+String.valueOf(i+1),
                "Decyption",
                JOptionPane.QUESTION_MESSAGE);
			s.add(r);
		}
		return s;
	}
	public Main() {
		initComponents();
	}
	public static void main(String[] args) {
		Main m = new Main();
		m.setSize(500,500);
		m.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.chooseFileButton)) {
			this.fileChoose();
		}
		else if(e.getSource().equals(this.fileInfoOpen)) {
			File f= new File("data.zip");
			try {
				f.createNewFile();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				this.bfzu.extract("data.zip",f);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this,
					    "Something Went Wrong",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			String keys="|";
			for(int i=0;i<this.nkeys;i++){
				keys+=JOptionPane.showInputDialog("Key No. "+String.valueOf(i+1))+"|";
			}
			 ZipFile zipFile = new ZipFile(f);
			    try {
					if (zipFile.isEncrypted()) {
					    zipFile.setPassword(keys.toCharArray());
					}
				} catch (ZipException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(this,
						    "Something Went Wrong",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
							System.exit(1);
				}
			try {
				zipFile.extractAll(".");
			} catch (ZipException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this,
					    "Something Went Wrong",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			JOptionPane.showMessageDialog(this,
				    "Files have been extracted in the current working folder",
				    "Ok",
				    JOptionPane.INFORMATION_MESSAGE);
		}
		else if(e.getSource().equals(this.fileInfoBack)) {
			this.remove(this.fileInfo);
			this.chooseFile.show();
		}
		
	}
}
