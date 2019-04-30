package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


public class InstructionsPanel extends JPanel{
	private static final int ROW_HEIGHT = 60;

	private JDialog dialog;
	
	private ImageIcon startButton;
	private ImageIcon pauseButton;
	private ImageIcon speedSelect;
	private ImageIcon algoSelect;
	private ImageIcon clearButton;
	
	private Font headerFont = new Font(Font.SERIF, Font.BOLD, 16);
	private Font tableFontPlain = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
	private Font tableFontBold = new Font(Font.SANS_SERIF,Font.BOLD, 16); 

	private Border solidBorder =  BorderFactory.createLineBorder(Color.BLACK);

	public InstructionsPanel(JDialog dialog){
		super(new BorderLayout());
		this.dialog= dialog;
		Border padding = BorderFactory.createEmptyBorder(20,20,5,20);	//border padding
		createImageIcons();
		
		//create components
		JButton btnOkay = createOkayButton();
		JPanel controlsInstructionsPanel = createContolsInstructionsPanel();
		JPanel howItWorksPanel = createHowItWorksPanel();
		
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Controls", controlsInstructionsPanel);
		tabbedPane.addTab("How it works", howItWorksPanel);
		
		add(tabbedPane, BorderLayout.CENTER);
		add(btnOkay,BorderLayout.PAGE_END);
	}
	
	private ImageIcon getScaledImage(Image image, int height) {
		double scaledWidth = (double)(height/(double)image.getHeight(null))*image.getWidth(null);
		BufferedImage resizedImg = new BufferedImage((int)scaledWidth,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image,0,0,(int)scaledWidth,height,null);
		g2.dispose();

		return new ImageIcon(resizedImg);
	}
	
	
	private JPanel getBindingsTable() throws IOException {
		String setTarget = "Sets selected position as target";
		String setSource = "Sets selected position as source";
		String setBlock = "Blocks selected position, drag to select multiple";
		String setUnblock = "Unblocks selected position, drag to select multiple"; 
		
		ImageIcon leftClick = getScaledImage(ImageIO.read(getClass().getResource("/leftMouseClick.jpg")),ROW_HEIGHT);
		ImageIcon rightClick = getScaledImage(ImageIO.read(getClass().getResource("/rightMouseClick.jpg")),ROW_HEIGHT);
		String ctrl = "ctrl";
		String none = "-";
		
		//create column name sfor table
		String[] columnNames = {"BTN1", "BTN2", "Affect"};
		//create data for table
		Object[][] data = {
				{leftClick,ctrl, setSource},
				{rightClick,ctrl, setTarget},
				{leftClick,none, setBlock},
				{rightClick,none, setUnblock},
		};
		
		DefaultTableModel newModel = new DefaultTableModel(data, columnNames){
			@Override
			public Class<?> getColumnClass(int column) {
					if(column ==0) {
						return ImageIcon.class;
					}
					else {
					}
						return String.class;
			}
		};
		
		//Construct the table and set sizes
		JTable table = new JTable(newModel);
		table.setFont(tableFontPlain);
		table.setEnabled(false);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setBorder(solidBorder);
		table.getColumnModel().getColumn(2).setPreferredWidth(table.getColumnModel().getColumn(0).getWidth()*5);
		
		//Set column Headers
		
		//set column 1 bold and centered by creating custom renderer for the column 
		DefaultTableCellRenderer newRender = new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, 
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column){
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setFont(tableFontBold);
				return this;
			}
		};
		newRender.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(1).setCellRenderer(newRender);
		table.setRowHeight(ROW_HEIGHT);
	
		
		//Construct header
		JTableHeader header = new JTableHeader(table.getColumnModel());
		header.setReorderingAllowed(false);
		header.setFont(headerFont);
		table.setTableHeader(header);
		//Construct JScrollPane with table in it, add to JPanel
		JPanel tablePanel = new JPanel(new BorderLayout());
		JScrollPane tableScrollPane = new JScrollPane(table);
		Dimension tableSize = table.getPreferredSize();
		Dimension scrollPaneSize = new Dimension((int)tableSize.getWidth(),(int)(tableSize.getHeight()+header.getPreferredSize().getHeight()+10));
		//tableScrollPane.setPreferredSize(scrollPaneSize);
		//make Table non movable
		//table.getTableHeader().setReorderingAllowed(false);
		//table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		
		tablePanel.add(tableScrollPane, BorderLayout.CENTER);
		tablePanel.setPreferredSize(scrollPaneSize);
		
		return addHeaderToPanel("Key and Mouse Bindings", tablePanel);
	}
	
	private JPanel createKeyboardMousePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		try {
			panel.add(getBindingsTable(),BorderLayout.CENTER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return panel;
	}
	
	public JPanel createButtonsTable() {
		//Create table data
		String startString = "Starts search algorithm and visulaization";
		String pauseString = "Pauses visualization of algorithm";
		String clearString = "Completely clears the board, removing any set grid states";
		String algoSelectString = "Drop-down selection of alogirthm to run";
		String speedSelectString = "Slider to select speed of algorithm visualization";
		String[] columns = {"Button", "Description"};
		Object[][] data = {
				{startButton,startString},
				{pauseButton,pauseString},
				{clearButton,clearString},
				{speedSelect, speedSelectString},
				{algoSelect, algoSelectString},
		};
		//set first column row as images:
		DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
			@Override
			public Class<?> getColumnClass(int column){
				if(column == 0) {
					return ImageIcon.class;
				}
				else {
					return String.class;
				}
				
			}
		};
		
		//create the table 
		JTable table = new JTable(tableModel);
		table.setFont(tableFontPlain);
		table.setFont(tableFontPlain);
		table.setEnabled(false);
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setBorder(solidBorder);
		table.setRowHeight(ROW_HEIGHT);
		
		class TextTableRenderer extends JLabel  implements TableCellRenderer{

			public TextTableRenderer() {
				setOpaque(true);
				//setLineWrap(true);
				//setWrapStyleWord(true);
				}
			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row,
					int column) {
				setForeground(Color.BLACK);
				setFont(tableFontPlain);
				setBackground(table.getBackground());
				setText((value == null)
						? ""
						:value.toString());
				setVerticalAlignment(CENTER);
				return this;
			}
		}
		table.getColumnModel().getColumn(1).setCellRenderer(new TextTableRenderer());
		
		JTableHeader header = new JTableHeader(table.getColumnModel());
		header.setReorderingAllowed(false);
		header.setFont(headerFont);
		table.setTableHeader(header);
		
		//create JScrollPane for table
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane,BorderLayout.CENTER);
		
		//set table and panel size
		Dimension tableSize = new Dimension(630, ROW_HEIGHT*5);
		table.setPreferredSize(tableSize);
		table.getColumnModel().getColumn(0).setPreferredWidth(210);
		table.getColumnModel().getColumn(1).setPreferredWidth(420);
		
		Dimension panelSize = new Dimension((int)tableSize.getWidth(),(int)(tableSize.getHeight()+header.getPreferredSize().getHeight()+10));

		panel.setPreferredSize(panelSize);
		return addHeaderToPanel("Control Panel Buttons", panel);
	}
	
	private JPanel addHeaderToPanel(String headerText, JPanel sourcePanel) {
		JPanel panel = new JPanel(new BorderLayout());
		JLabel headerLabel = new JLabel(headerText.toUpperCase());
		headerLabel.setFont(headerFont);
		headerLabel.setBorder(solidBorder);
		headerLabel.setHorizontalAlignment(JLabel.CENTER);
		headerLabel.setVerticalAlignment(JLabel.CENTER);
		panel.add(sourcePanel,BorderLayout.CENTER);
		panel.add(headerLabel, BorderLayout.PAGE_START);
		return panel;
	}
	
	private JPanel createButtonInstructionsPanel() {
		return new JPanel();
	}
	
	
	private JPanel createContolsInstructionsPanel() {
		JPanel controlsPanel = new JPanel(new BorderLayout());
		
		JPanel keybindingsPanel = createKeyboardMousePanel();
		JPanel buttonsPanel = createButtonsTable();
		controlsPanel.add(keybindingsPanel, BorderLayout.WEST);
		controlsPanel.add(buttonsPanel, BorderLayout.EAST);
		
			
		return controlsPanel;
		
	}
	
	private JPanel createHowItWorksPanel() {
		return new JPanel();
	}
	
	//Create the close button for exiting the instructions panel
	private JButton createOkayButton() {
		JButton btnOkay = new JButton("Okay");
		btnOkay.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		return btnOkay;
	}
	
	
	private void createImageIcons() {
		try {
			startButton = getScaledImage(ImageIO.read(getClass().getResource("/btnSprites/start.PNG")), ROW_HEIGHT);
			pauseButton = getScaledImage(ImageIO.read(getClass().getResource("/btnSprites/pause.PNG")), ROW_HEIGHT);
			speedSelect = getScaledImage(ImageIO.read(getClass().getResource("/btnSprites/setSpeed.PNG")), ROW_HEIGHT);
			algoSelect = getScaledImage(ImageIO.read(getClass().getResource("/btnSprites/selectAlgo.PNG")), ROW_HEIGHT);
			clearButton = getScaledImage(ImageIO.read(getClass().getResource("/btnSprites/clear.PNG")), ROW_HEIGHT);
		}
		catch(IOException ioe) {
			System.out.println("Error when trying to load imageFiles for instructions");
		}
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame ("Instructions");
		JDialog dialog = new JDialog(frame);
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		InstructionsPanel instructions =  new InstructionsPanel(dialog);
		
		frame.setContentPane(instructions);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void displayInstructions() {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();		
			}
		});
		
	}
}
