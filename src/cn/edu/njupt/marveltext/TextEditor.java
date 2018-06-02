package cn.edu.njupt.marveltext;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.font.*;
import java.io.*;
import java.text.*;
import javax.swing.text.*;
import java.text.*;
import javax.swing.JMenuBar;
import java.util.*;
import javax.swing.undo.*;
import javax.swing.event.*;
import java.net.*;
import java.beans.*;
import javax.swing.event.*;


class MyRunnable implements Runnable {
	public void run() {
		while(true) {
			break;
		}
	}
}

public class TextEditor {
	public int spcount = 0, lines = 0, mouseY = 0, mouseX = 0;
	public String full1 = null, full2 = null, w = null;
	Scanner sc = null;
	File f = new File("an.txt");
	
	JFrame frame = new JFrame("MarvelText");

	//JLabel statusLabel  = new JLabel();
	JTextArea area = new JTextArea(0, 0);                    //参数是行数和列数，0,0是最大化
	JScrollPane scroller = new JScrollPane(area);
	JPopupMenu pop = new JPopupMenu();
	
	String fileM = null;
	String fileN;
	
	String source = "";
	String target = "";
	
	boolean opened = false;
	JPanel statusPanel = new JPanel();
	JLabel statusLabel = new JLabel("Line Number : ");
	JLabel dateLabel = new JLabel();
	//statusLabel.setBackground(white);
	//statusPanel.add(statusLabel);
	JPanel aboutPanel = new JPanel();
	JPanel introductionPanel = new JPanel();
	
	int ind = 0;
	StringBuffer sbufer;
	String findString;
	fontSelector fontS = new fontSelector();
	
	public TextEditor(){
		
		//监听JTextArea触发的事件
		area.getDocument().addDocumentListener(new DocumentListener() {     //获取Document实例再注册实现DocumentListener类作为监听器      
            public void insertUpdate(DocumentEvent e) {                     //给出对文档执行了插入操作的通知                    	
                if(area.getText().length() > 0) {
                	lines = countLines(area);
                    statusLabel.setText("Line Number : " + lines + "      (X,Y) : " + mouseX + "," + mouseY);
                } else {
                   statusLabel.setText("                ");
                }
            }           
            public void removeUpdate(DocumentEvent e) {                     //给出移除了一部分文档的通知                         
                if(area.getText().length() > 0) {
                	lines = countLines(area);
                   statusLabel.setText("Line Number : " + lines + "      (X,Y) : " + mouseX + "," + mouseY);
                }else {
                    statusLabel.setText("0");
                }
            }
            public void changedUpdate(DocumentEvent e) {                    //给出属性或属性集发生了更改的通知
	            if(area.getText().length() > 0) {
	            	lines = countLines(area);
	               statusLabel.setText("Line Number : " + lines + "      (X,Y) : " + mouseX + "," + mouseY);
	            }else {
	              	statusLabel.setText("0");
	            }
            }
        });
		
		Font consolas = new Font("微软雅黑", 4, 13);
		Color white = new Color(255,255,255);
		Color purpleDarken = new Color(74,20,140);
		Color cyan = new Color(24,255,255);
		Color greenAccent = new Color(0,200,83);
		Color brown = new Color(121,85,72);
		Color blueGrey = new Color(96,125,139);
		Color pink = new Color(245,0,87);
		Color purple = new Color(156,39,176);
		Color yellow = new Color(255,255,59);
		Color best = new Color(6,38,60); 
		UIManager.put("Menu.font", consolas);
		UIManager.put("MenuItem.font", consolas);  
		UIManager.put("MenuBar.background",white);
		UIManager.put("Menu.background",white);
		UIManager.put("MenuItem.background",white);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	} 
    	catch (UnsupportedLookAndFeelException e) {
        }
		catch (ClassNotFoundException e) {
        }
		catch (InstantiationException e) {
    	}
    	catch (IllegalAccessException e) {
    	}

		JMenuBar menuBar = new javax.swing.JMenuBar();
		menuBar.setBorder(BorderFactory.createEmptyBorder());
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu format = new JMenu("Format");
		JMenu view = new JMenu("View");
		JMenu help = new JMenu("Help");
	

		JMenuItem newFile = new JMenuItem("New", new ImageIcon("picture//new.jpg"));
		JMenuItem openFile = new JMenuItem("Open", new ImageIcon("picture//open.jpg"));
		JMenuItem saveFile = new JMenuItem("Save", new ImageIcon("picture//save.jpg"));
		JMenuItem saveAsFile = new JMenuItem("Save As...");

		JMenuItem exitFile = new JMenuItem("Exit");

		JMenuItem undo = new JMenuItem("Undo");
		JMenuItem redo = new JMenuItem("Redo");
		JMenuItem copy = new JMenuItem("copy", new ImageIcon("picture//copy.jpg"));
		JMenuItem cut = new JMenuItem("Cut", new ImageIcon("picture//cut.jpg"));
		JMenuItem paste = new JMenuItem("paste", new ImageIcon("picture//paste.jpg"));
		JMenuItem delete = new JMenuItem("Delete");
		JMenuItem find = new JMenuItem("Find");
		JMenuItem findNext = new JMenuItem("Find Next");
		JMenuItem replace = new JMenuItem("Replace");
		JMenuItem replaceAll = new JMenuItem("Replace All");
		JMenuItem goToEdit = new JMenuItem("Go To");
		JMenuItem selectAlll = new JMenuItem("Select All");
		JMenuItem timeDate = new JMenuItem("Time/Date");
		JCheckBoxMenuItem wordFormat = new JCheckBoxMenuItem("Word Wrap");
		JMenuItem font = new JMenuItem("Font");
		JCheckBoxMenuItem statusView = new JCheckBoxMenuItem("Status Bar");
		JMenuItem about = new JMenuItem("About");
		JMenuItem introduction = new JMenuItem("Introduction");

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;
		frame.setSize(screenWidth*2/5,screenHeight*3/5);
		frame.setIconImage(new ImageIcon(getClass().getResource("icon.jpg")).getImage());
		frame.setLocationByPlatform(true);		
		
		
		frame.getContentPane().setLayout(new BorderLayout());
		area.setTabSize(4);
		area.setLineWrap(true);
		area.setColumns(800);                                         //这里两行是想干啥？
		area.setRows(800);
		area.setWrapStyleWord(true);
		area.requestFocus(true);
		area.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 16));
		scroller.setViewportView(area);                  
		area.setBackground(white);                                          //这两行用来设置背景      
		area.setForeground(best);
		frame.getContentPane().add(scroller, BorderLayout.CENTER);
		frame.getContentPane().add(statusLabel, BorderLayout.SOUTH);
		frame.getContentPane().add(dateLabel, BorderLayout.NORTH);
		//frame.getContentPane().add(statusLabel, BorderLayout.WEST);
		area.setDragEnabled(true);
		MouseListener popupListener = new PopupListener();
		area.addMouseListener(popupListener);
		//statusLabel.setBounds();
		//frame.add(statusLabel);
		//dateLabel.addText("                    "+dd)

		//UNDO LISTENER ON AREA
		/*	area.getDocument().addUndoableEditListener(new MyUndoableEditListener());*/		

		//SETS THE MENUBAR
		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);
		file.add(saveAsFile);
		file.addSeparator();

		file.addSeparator();
		file.add(exitFile);
		
		
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.add(findNext);
		edit.add(replace);
		edit.add(goToEdit);
		edit.addSeparator();
		edit.add(selectAlll);
		edit.add(timeDate);
		edit.add(replaceAll);

		format.add(wordFormat);
		wordFormat.setSelected(true);
		format.add(font);
		view.add(statusView);
		statusView.setSelected(true);
		help.add(about);
		help.add(introduction);

		file.setMnemonic(KeyEvent.VK_F);
		menuBar.add(file);
		edit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(edit);
		format.setMnemonic(KeyEvent.VK_T);
		menuBar.add(format);
		view.setMnemonic(KeyEvent.VK_V);
		menuBar.add(view);
		help.setMnemonic(KeyEvent.VK_H);
		menuBar.add(help);
		//ADD MENUBAR TO THE FRAME
		frame.setJMenuBar(menuBar);

		//ADD ITEMS TO THE POPUP
		/*pop.add(undoAction);
		pop.add(redoAction);*/
		pop.addSeparator();
		pop.add(copy);
		pop.add(cut);
		pop.add(paste);
		pop.add(delete);
		//ACTIONLISTENER FOR ITEMS IN THE MENUBAR
		//OPEN A NEW FILE
		
		area.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent mme) {
				mouseX = mme.getX();
				mouseY = mme.getY();				
				statusLabel.setText("Line Number : "+lines+"      (X,Y) : "+mouseX+","+mouseY);
			}
			public void mouseDragged(MouseEvent mde) {
			}
		});
		area.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent kp){
				if (kp.getKeyCode()==KeyEvent.VK_SPACE) {
					if (spcount==0) {
						spcount++;
						full1 = area.getText();
						//System.out.println(spcount);
					}
					if (spcount==1) {
						System.out.println(spcount);
						full2 = area.getText();
						//w = full2.substring(full2.length()-full1.length());
						w = "Anraug";
						//area.setText(w.);
						/*if (findWord("hi",f)==1) {
							area.setText(full1);
						}*/
						spcount=0;
						//System.out.println(w);
					}
					else{
						//System.out.println(spcount);
					}
					
				}
			}
			public void keyReleased(KeyEvent kr){

			}
			public void keyTyped(KeyEvent kt){

			}
		});

		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newFile.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opened = false;
				int confirm = JOptionPane.showConfirmDialog(null,
					"Would you like to save?",
					"New File",
					JOptionPane.YES_NO_CANCEL_OPTION);

				if( confirm == JOptionPane.YES_OPTION ) {
					saveFile();
					area.setText(null);
					statusPanel.removeAll();
					statusPanel.validate();
				}
				else
					if( confirm == JOptionPane.CANCEL_OPTION ){
					}
				else {
					area.setText(null);
					statusPanel.removeAll();
					statusPanel.validate();
				}
			}
		});
		
		//SAVE OPTION. HAS A VALIDATION CHECK THAT CHECKS WETHER ITS AN OPENED FILE OR NEW FILE
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveFile.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		
		//OPTION THAT WILL BRING UP A DIALOG WHICH SAVES THE FILE WITH A NAME AND FORMAT DESIRED
		saveAsFile.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				opened = false;
				saveFile();
			}
		});
		selectAlll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		selectAlll.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				area.selectAll();
			}
		});
		

		
		//ACTION FOR STATUS BUTTON
		statusView.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(statusView.isSelected())
					statusPanel.setVisible(true);
				else
					statusPanel.setVisible(false);
			}
		});
		
		//ACTION FOR OPEN BUTTON
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFile.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				openFile();
			}
		});
		
		//ACTION FOR CUT BUTTON
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		cut.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				area.cut();
			}
		});
		
		//ACTION FOR COPY BUTTON
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		copy.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				area.copy();
			}
		});
		
		replaceAll.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame rep = new JFrame();
				rep.setSize(screenWidth*2/15,screenHeight*3/25);
				rep.setBounds(area.getWidth(), area.getWidth(), screenWidth*1/8, screenHeight*4/25);
				rep.setLocationByPlatform(true);
				rep.setResizable(false);
		        //rep.setSize(500,100);  
		        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
		        FlowLayout flo =new FlowLayout(FlowLayout.CENTER);  
		        rep.setLayout(flo);  
		        JLabel sourceLabel=new JLabel("Source Text:",JLabel.LEFT);
		        JTextField sourceText=new JTextField("",29); 
		        JLabel targetLabel=new JLabel("Target Text:",JLabel.LEFT);
		        JTextField targetText=new JTextField("",29);
		        JButton jrep = new JButton("替换");
		        rep.add(sourceLabel);  
		        rep.add(sourceText);
		        rep.add(targetLabel);  
		        rep.add(targetText);
		        rep.add(jrep);
		        rep.setVisible(true);
		        
		        String content = area.getText();	        
		        jrep.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
				       
				        if (sourceText.getText().equals("") || sourceText.getText().equals(" "))
				        {
				        	JOptionPane valIn = new JOptionPane();
				        	valIn.showMessageDialog(null, "输入无效", "提示", JOptionPane.ERROR_MESSAGE);
				        } else {
				        	source = sourceText.getText();
				        	target = targetText.getText();
				        	area.setText(content.replace(source, target));
				        	source = "";
				        	target = "";
				        }
				     }
		        });
			}		
		});
		
		//ACTION FOR PASTE BUTTON
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		paste.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//System.out.println("line 371 proceeding!");
				area.paste();
			}
		});
		
		//font SELECTOR OPTION
		font.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
		font.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fontS.setVisible(true);
				fontS.okButton.addActionListener(new ActionListener () {
					public void actionPerformed(ActionEvent ae) {
						Font selectedFont = fontS.returnFont();
						area.setFont(selectedFont);
						fontS.setVisible(false);
					}
				});

				fontS.cancelButton.addActionListener(new ActionListener () {
					public void actionPerformed(ActionEvent ae) {
						fontS.setVisible(false);
					}
				});
			}
		});
		
		Date currentDate;
				SimpleDateFormat formatter;
				String dd;
				formatter = new SimpleDateFormat("KK:mm aa MMMMMMMMM dd yyyy", Locale.getDefault());
				currentDate = new java.util.Date();
				dd = formatter.format(currentDate);
		//PRINTS THE SYSTEM DATE AND TIME IN THE EDITOR
		timeDate.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				area.insert(dd,area.getCaretPosition());
			}
		});
		timeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		//ADD THE PRINT OPTION 
		paste.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//area.paste();
			}
		});
		
		//FINDS A WORD IN THE EDITOR
		findNext.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sbufer = new StringBuffer(area.getText());
					findString = JOptionPane.showInputDialog(null, "Find");
					ind = sbufer.indexOf(findString, area.getCaretPosition());
					area.setCaretPosition(ind);
					area.setSelectionStart(ind);
					area.setSelectionEnd(ind+findString.length());
				}
				catch(IllegalArgumentException npe) {
					JOptionPane.showMessageDialog(null, "String not found");
				}catch(NullPointerException nfe){}
			}
		});
		
		//EXITS THE APPLICATION AND CHECKS FOR ANY CHANGES MADE
		exitFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exitFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null,
					"Would you like to save?",
					"Exit Application",
					JOptionPane.YES_NO_CANCEL_OPTION);

				if( confirm == JOptionPane.YES_OPTION ) {
					saveFile();
					frame.dispose();
					System.exit(0);
				}
				else
					if( confirm == JOptionPane.CANCEL_OPTION )
					{}
				else {
					frame.dispose();
					System.exit(0);
				}
			}
		});
		
		
		//ACTION FOR REPLACE OPTION
		replace.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String replaceStr = JOptionPane.showInputDialog(null, "Replace");
					area.replaceSelection(replaceStr);
				}catch(NumberFormatException nfe){}
			}
		});
		
		//ACTION FOR GO TO OPTION
		goToEdit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {	
					Element root = area.getDocument().getDefaultRootElement();
					Element paragraph=root.getElement(Integer.parseInt(JOptionPane.showInputDialog(null, "Go to line")));
					area.setCaretPosition(paragraph.getStartOffset()-1);
				}catch(NullPointerException npe) {
					JOptionPane.showMessageDialog(null, "Invalid line number");
				}catch(NumberFormatException nfe) {
					
				}
			}
		});
		
		//ACTION FOR DELETE OPTION
		paste.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//System.out.println("line 500 proceeding!");
				area.replaceSelection(null);
			}
		});
		
		//SETS THE LINEWRAP OT TRUE OR FALSE
		wordFormat.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(wordFormat.isSelected())
					area.setLineWrap(true);
				else
					area.setLineWrap(false);
			}
		});
		
		//about
		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));
		about.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		URL img = getClass().getResource("/cn/edu/njupt/marveltext/logo.jpg");
		String imagesrc = "<img src=\"" + img + "\" width=\"300\" height=\"111\">";
		String message = "MarvelText \n"+
		"Written by 曹周祥、朱林超、王昕\n"+
		"Version 1.0\n"+
		"No purchase or Subspcription\n"+
		"For any query mail at 260468654@qq.com\n";
		JOptionPane.showMessageDialog(aboutPanel,"<html><center>" + imagesrc + "<br>" + message);
		}
		});
		
		
		//introduction
		introduction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.CTRL_MASK));
		introduction.addActionListener( new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		URL img = getClass().getResource("/cn/edu/njupt/marveltext/logo.jpg");
		String imagesrc = "<img src=\"" + img + "\" width=\"300\" height=\"111\">";
		String message = "欢迎使用MarvelText，MarvelText是一种可供学习与工作的简约文本编辑器，具有丰富的功能鲜明的特色,创意来自于美国漫威英雄文化。\n\n"+
		"1.MarevlText安装\n"+"MarvelText的安装与使用需要有java环境，可以安装在各类系统上。\n\n"+
		"2.MarvelText功能\n"+"File栏：文件新建、打开、保存、另存、退出\n"+"Edit栏：关键字查找、替换、到达指定行、打印当前时间\n"+"Format栏：开启自动换行、字体设置\n"+
		"Help栏：关于我们、帮助文档\n\n"+
		"3.MarvelText特色\n"+"使用了Marvel的主题，添加了java语言的关键字加亮功能，让代码读写更加容易。\n\n"+
		"4.后续开发\n"+"可以添加更多编辑功能，支持对更多语言的关键字加亮，优化编辑界面。\n\n"+
		"5.加入QQ群\n"+"可以加入QQ群695606071来讨论关于MarvelText。";
		JOptionPane.showMessageDialog(introductionPanel,"<html><center>" + imagesrc + "<br>" + message);
		}
		});
		
		//CLOSES THE WINDOW WHEN THE CLOSE BUTTON IS PRESSED
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null,
					"Would you like to save?",
					"Exit Application",
					JOptionPane.YES_NO_CANCEL_OPTION);

				if( confirm == JOptionPane.YES_OPTION ) {
					saveFile();
					frame.dispose();
					System.exit(0);
				}
				else
					if( confirm == JOptionPane.CANCEL_OPTION )
					{

					}
					else
					{
						frame.dispose();
						System.exit(0);
					}
				}
			});
		try{
 			frame.setIconImage(new ImageIcon("./logo.jpg").getImage());
		}catch(Exception e){
   			System.out.println("icon not found");
		}
		frame.setVisible(true);

	}
	
	//MAIN method
	public static void main(String args[]){
		TextEditor l = new TextEditor();	               //从未被使用过
		MyRunnable runnable = new MyRunnable();
		Thread t = new Thread(runnable);
		t.start();
	}
	
	public static int countLines(JTextArea textArea) {
        AttributedString text = new AttributedString(textArea.getText());
        FontRenderContext frc = textArea.getFontMetrics(textArea.getFont()).getFontRenderContext();
        AttributedCharacterIterator charIt = text.getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(charIt, frc);
        float formatWidth = (float) textArea.getSize().width;
        lineMeasurer.setPosition(charIt.getBeginIndex());

        int noLines = 0;
        while (lineMeasurer.getPosition() < charIt.getEndIndex()) {
            lineMeasurer.nextLayout(formatWidth);
            noLines++;
        }

        int lineEnter = textArea.getLineCount();
        int countLine = noLines + lineEnter;

        return countLine-1;
    } 

	/*public int findWord(String word, File file) {
		int count = 0;
		try{
			sc = new Scanner(file);
		}catch (Exception e) {
			e.printStackTrace();
		}
		while (sc.hasNextLine()) {
    		String nextToken = sc.next();
    		if (nextToken.equalsIgnoreCase(word))
    		count=1;
		}
		return count;
	}*/
	
	//FUNCTION CALLED BY THE SAVE BUTTON
	public void saveFile(){
		String line = area.getText();
		if(opened == true) {
			try {
				FileWriter output = new FileWriter(fileM);
				BufferedWriter bufout = new BufferedWriter(output);
				bufout.write(line, 0, line.length());
				JOptionPane.showMessageDialog(null, "Save Successful");
				bufout.close();
				output.close();
			}catch(IOException ioe){ioe.printStackTrace();}
		}
		else {
			JFileChooser fc = new JFileChooser();
			int result = fc.showSaveDialog(new JPanel());
	
			if(result == JFileChooser.APPROVE_OPTION) {
				fileN = String.valueOf(fc.getSelectedFile());
	
				try {
					FileWriter output = new FileWriter(fileN);
					BufferedWriter bufout = new BufferedWriter(output);
					bufout.write(line, 0, line.length());
					JOptionPane.showMessageDialog(null, "Save Successful");
					bufout.close();
					output.close();
					opened = true;
				}catch(IOException ioe){ioe.printStackTrace();}
			}
		}
	}
	
	//FUNCTION TO OPEN THE FILE
	public void openFile(){
		statusPanel.removeAll();
		statusPanel.validate();
		area.setText(null);
		JFileChooser fc = new JFileChooser();
		int result = fc.showOpenDialog(new JPanel());

		if(result == JFileChooser.APPROVE_OPTION) {
			String file = String.valueOf(fc.getSelectedFile());
				//String dirn = fc.getDirectory();

			File fil = new File(file);
			/*newFile.setEnabled(false);*/

				//START THIS THREAD WHILE READING FILE
			Thread loader = new FileLoader(fil, area.getDocument());
			loader.start();
			statusPanel.removeAll();
			statusPanel.revalidate();
		}else{}
	}
	
	class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				pop.show(e.getComponent(),
					e.getX(), e.getY());
			}
		}
	}
    
    class FileLoader extends Thread {

    	JLabel state;

    	FileLoader(File f, Document doc) {
    		setPriority(4);
    		this.f = f;
    		this.doc = doc;
    	}

    	public void run() {
    		try {
    			statusPanel.removeAll();
    			JProgressBar progress = new JProgressBar();
    			progress.setMinimum(0);
    			progress.setMaximum((int) f.length());
    			statusPanel.add(new JLabel("opened so far "));
    			statusPanel.add(progress);
    			statusPanel.revalidate();
    			Reader in = new FileReader(f);
    			char[] buff = new char[4096];
    			int nch;
    			while ((nch = in.read(buff, 0, buff.length)) != -1) {
    				doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
    				progress.setValue(progress.getValue() + nch);
    			}

    			statusPanel.removeAll();
    			statusPanel.revalidate();

    		}
    		catch (IOException e) {
    			System.err.println(e.toString());
    		}
    		catch (BadLocationException e) {
    			System.err.println(e.getMessage());
    		}
    		/*newFile.setEnabled(true);*/
    	}
    	Document doc;
    	File f;
    }

}
