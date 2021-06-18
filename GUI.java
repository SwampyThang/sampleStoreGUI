import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GUI {

	private JFrame frmNileDotCom;
	private JTextField numItemsText;
	private JTextField itemIDText;
	private JTextField itemQtyText;
	private JTextField itemInfoText;
	private JTextField orderTotalText;
	private int currentItemNum = 1, totalItems = 0, flag = 0;
	private Double subtotal = 0.0, totalPriceRounded = 0.0, discAmountDb = 0.0;;
	private String itemInfoArray[][], orderLine, transactionOrderLine;
	private String itemQtyString, totalPriceRoundedString;
	private int itemQty, discAmount = 0;

	// ----------- INSERT FILE NAME HERE -----------
	File file = new File("inventory.txt");
	// ---------------------------------------------
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmNileDotCom.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNileDotCom = new JFrame();
		frmNileDotCom.setTitle("Nile Dot Com - Spring 2021");
		frmNileDotCom.setBounds(100, 100, 800, 300);
		frmNileDotCom.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNileDotCom.getContentPane().setLayout(null);
		
		JLabel itemIDLabel = new JLabel("Enter item ID for item #" + currentItemNum + ":");
		JLabel itemQtyLabel = new JLabel("Enter Quantity for item #" + currentItemNum + ":");
		JLabel itemInfoLabel = new JLabel("Item #" + currentItemNum + " info:");
		JLabel orderTotalLabel = new JLabel("Order subtotal for 0 item(s):");

		JButton processItemButton = new JButton("Process Item #" + currentItemNum);
		JButton confirmItemButton = new JButton("Confirm Item #" + currentItemNum);
		confirmItemButton.setEnabled(false);
		JButton viewOrderButton = new JButton("View Order");
		viewOrderButton.setEnabled(false);
		JButton finishOrderButton = new JButton("Finish Order");
		finishOrderButton.setEnabled(false);
		
		File myObj = new File("transactions.txt");
		
		processItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				itemQtyString = itemQtyText.getText();
				itemQty = Integer.parseInt(itemQtyString);
				String numItemsString = numItemsText.getText();
				String itemIDString = itemIDText.getText();
				String fullLine = null;
								
				int numItems = Integer.parseInt(numItemsString);
				
				Double itemPrice = 0.0;
				Double totalPrice = 0.0;
				
				totalItems = numItems;
				
				Scanner scan;
				try {
					scan = new Scanner(file);
					
					itemInfoArray = new String[numItems][4];
					
					if(currentItemNum > totalItems)
						processItemButton.setEnabled(false);
					
					if(currentItemNum <= totalItems) {
						
						while(scan.hasNextLine() ) {
						
							fullLine = scan.nextLine();
							itemInfoArray[currentItemNum - 1] = fullLine.split(", ");
							
							if(itemInfoArray[currentItemNum - 1][0].equals(itemIDString))
								break;
						}
						
						if(itemInfoArray[currentItemNum - 1][0].equals(itemIDText.getText()) == false) {
							JFrame itemIDError = new JFrame();
			
							JOptionPane.showMessageDialog(itemIDError, "item ID "  + itemIDText.getText() + " not in file");
						}
						
						else if(itemInfoArray[currentItemNum - 1][2].equals("false")) {					
							JFrame itemOutOfStock = new JFrame();
	
							JOptionPane.showMessageDialog(itemOutOfStock, "Sorry... that item is out of Stock");
						}
											
						else {
							
							if(itemQty >= 1 && itemQty <= 4) {
								discAmount = 0;
								discAmountDb = 0.0;
							}
							else if(itemQty >= 5 && itemQty <= 9) {
								discAmount = 10;
								discAmountDb = 0.1;
							}
							else if(itemQty >= 10 && itemQty <= 14) {
								discAmount = 15;
								discAmountDb = .15;
							}
							else if(itemQty >= 15) {
								discAmount = 20;
								discAmountDb = .2;
							}
							
							itemPrice = Double.parseDouble(itemInfoArray[currentItemNum - 1][3]);
								
							totalPrice = (itemPrice * itemQty) - ((itemPrice * itemQty) * discAmount/100);
								
							totalPriceRounded = Double.parseDouble(new DecimalFormat("##.00").format(totalPrice));
							totalPriceRoundedString = new DecimalFormat("##.00").format(totalPrice);
								
							subtotal += totalPriceRounded;
								
							itemInfoText.setText(itemInfoArray[currentItemNum - 1][0] + " "
									+ itemInfoArray[currentItemNum - 1][1] + " $"
									+ itemInfoArray[currentItemNum - 1][3] + " "
									+ itemQty + " "
									+ discAmount + "% $"
									+ totalPriceRoundedString + " ");

							flag = 1;
							confirmItemButton.setEnabled(true);
							processItemButton.setEnabled(false);
						}
					}
				}
				 			
				catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		processItemButton.setBounds(10, 227, 134, 23);
		frmNileDotCom.getContentPane().add(processItemButton);
		
		confirmItemButton.setEnabled(false);
		confirmItemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(currentItemNum <= totalItems) {
					if(flag == 1) {
						JFrame itemAccepted = new JFrame();
		
						JOptionPane.showMessageDialog(itemAccepted, "item #"  + currentItemNum + " accepted");
						orderTotalText.setText("$" + new DecimalFormat("##.00").format(subtotal));
						
						if(currentItemNum == 1) {
							orderLine = currentItemNum + ". " + itemInfoArray[currentItemNum - 1][0] + " "
									+ itemInfoArray[currentItemNum - 1][1] + " $"
									+ itemInfoArray[currentItemNum - 1][3] + " "
									+ itemQty + " "
									+ discAmount + "% $"
									+ totalPriceRoundedString + " \n";
							
						    transactionOrderLine = itemInfoArray[currentItemNum - 1][0] + ", "
									+ itemInfoArray[currentItemNum - 1][1] + ", "
									+ itemInfoArray[currentItemNum - 1][3] + ", "
									+ itemQty + ", "
									+ discAmountDb + ", $"
									+ totalPriceRoundedString;
						}
						
						else
							orderLine += currentItemNum + ". " + itemInfoArray[currentItemNum - 1][0] + " "
									+ itemInfoArray[currentItemNum - 1][1] + " $"
									+ itemInfoArray[currentItemNum - 1][3] + " "
									+ itemQty + " "
									+ discAmount + "% $"
									+ totalPriceRoundedString + " \n";
						
						
						currentItemNum++;
						
						if(currentItemNum <= totalItems) {
							processItemButton.setText("Process Item #" + currentItemNum);
							confirmItemButton.setText("Confirm Item #" + currentItemNum);
							
							itemIDLabel.setText("Enter item ID for item #" + currentItemNum + ":");
							itemQtyLabel.setText("Enter quantity for item #" + currentItemNum + ":");
							itemInfoLabel.setText("Item #" + currentItemNum + " info:");
							processItemButton.setEnabled(true);
						}
						
						currentItemNum--;
						orderTotalLabel.setText("Order subtotal for " + currentItemNum + " item(s)");
						currentItemNum++;
						
						itemIDText.setText(null);
						itemQtyText.setText(null);
					}
					flag = 0;
					confirmItemButton.setEnabled(false);
					viewOrderButton.setEnabled(true);
					finishOrderButton.setEnabled(true);
				}
			}
		});
		confirmItemButton.setBounds(154, 227, 134, 23);
		frmNileDotCom.getContentPane().add(confirmItemButton);
		
		viewOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame viewOrder = new JFrame();

				JOptionPane.showMessageDialog(viewOrder, orderLine);
			}
		});
		viewOrderButton.setBounds(298, 227, 99, 23);
		frmNileDotCom.getContentPane().add(viewOrderButton);
		
		finishOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int taxRate = 6;
				Double totalAfterTax = 0.0, totalTaxAmount = 0.0;
				
				JFrame finishOrder = new JFrame();
				
				SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy h:mm:ss a z");
				SimpleDateFormat formatter2 = new SimpleDateFormat("ddMMyyyyHHmm");

			    Date date = new Date(); 
			    	
			    if(currentItemNum > 2)
			    transactionOrderLine += formatter.format(date) + " \n" 
			    		+ formatter2.format(date) + ", "
			    		+ itemInfoArray[currentItemNum - 2][0] + ", "
						+ itemInfoArray[currentItemNum - 2][1] + ", "
						+ itemInfoArray[currentItemNum - 2][3] + ", "
						+ itemQty + ", "
						+ discAmountDb + ", $"
						+ totalPriceRoundedString + ", "
						+ formatter.format(date) + " \n";
			    	
			    try {
			        FileWriter myWriter = new FileWriter("transactions.txt", true);
			        if(currentItemNum == 2)
			        	myWriter.write(formatter2.format(date) + ", " + transactionOrderLine + formatter.format(date) + "\n");
			        else
			        	myWriter.write(formatter2.format(date) + ", " + transactionOrderLine);
			        myWriter.close();
			      } 
			    catch (IOException e1) {
			        e1.printStackTrace();
			      }
			    				
				totalTaxAmount = subtotal * taxRate/100;
				
				totalTaxAmount = Double.parseDouble(new DecimalFormat("##.00").format(totalTaxAmount));
				
				totalAfterTax = subtotal + totalTaxAmount;
				
				JOptionPane.showMessageDialog(finishOrder, "Date: " + formatter.format(date) + "\n\n"
						+ "Number of line items: " + totalItems + "\n\n"
						+ "Item# / ID / Title / Price / Qty / Disc% / Subtotal:\n\n" 
						+ orderLine + "\n\n"
						+ "Order subtotal: $" + new DecimalFormat("##.00").format(subtotal) + "\n\n"
						+ "Tax rate:       6%\n\n"
						+ "Tax amount:     $" + new DecimalFormat("##.00").format(totalTaxAmount) + "\n\n"
						+ "Order total:    $" + new DecimalFormat("##.00").format(totalAfterTax) + "\n\n"
						+ "Thanks for shopping at Nile Dot Com!");
				
				numItemsText.setText(null);
				itemIDText.setText(null);
				itemQtyText.setText(null);
				itemInfoText.setText(null);
				orderTotalText.setText(null);
				currentItemNum = 1;
				totalItems = 0;
				subtotal = 0.0;
				flag = 0;
				
				processItemButton.setEnabled(false);
				confirmItemButton.setEnabled(false);
				viewOrderButton.setEnabled(false);
				finishOrderButton.setEnabled(false);
				
				if(currentItemNum <= totalItems) {
					processItemButton.setText("Process Item #" + currentItemNum);
					confirmItemButton.setText("Confirm Item #" + currentItemNum);
					
					itemIDLabel.setText("Enter item ID for item #" + currentItemNum + ":");
					itemQtyLabel.setText("Enter quantity for item #" + currentItemNum + ":");
					itemInfoLabel.setText("Item #" + currentItemNum + " info:");
					orderTotalLabel.setText("Order subtotal for 0 item(s)");
				}
			}
		});
		finishOrderButton.setBounds(407, 227, 110, 23);
		frmNileDotCom.getContentPane().add(finishOrderButton);
		
		JButton newOrderButton = new JButton("New Order");
		newOrderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numItemsText.setText(null);
				itemIDText.setText(null);
				itemQtyText.setText(null);
				itemInfoText.setText(null);
				orderTotalText.setText(null);
				currentItemNum = 1;
				totalItems = 0;
				subtotal = 0.0;
				flag = 0;
				subtotal = 0.0; 
				totalPriceRounded = 0.0;
				discAmountDb = 0.0;
				itemInfoArray = null;
				orderLine = null; 
				transactionOrderLine = null;
				itemQtyString = null;
				totalPriceRoundedString = null;
				itemQty = 0;
				discAmount = 0;
				transactionOrderLine = null;
				
				confirmItemButton.setEnabled(false);
				viewOrderButton.setEnabled(false);
				finishOrderButton.setEnabled(false);
				processItemButton.setEnabled(true);
				
				processItemButton.setText("Process Item #" + currentItemNum);
				confirmItemButton.setText("Confirm Item #" + currentItemNum);
				
				itemIDLabel.setText("Enter item ID for item #" + currentItemNum + ":");
				itemQtyLabel.setText("Enter quantity for item #" + currentItemNum + ":");
				itemInfoLabel.setText("Item #" + currentItemNum + " info:");
				orderTotalLabel.setText("Order subtotal for 0 item(s)");
				
			}
		});
		newOrderButton.setBounds(527, 227, 99, 23);
		frmNileDotCom.getContentPane().add(newOrderButton);
		
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		exitButton.setBounds(636, 227, 89, 23);
		frmNileDotCom.getContentPane().add(exitButton);
		
		JLabel numItemsLabel = new JLabel("Enter number of items in this order:");
		numItemsLabel.setBounds(10, 11, 222, 14);
		frmNileDotCom.getContentPane().add(numItemsLabel);
		
		itemIDLabel.setBounds(10, 36, 177, 14);
		frmNileDotCom.getContentPane().add(itemIDLabel);
		
		itemQtyLabel.setBounds(10, 61, 157, 14);
		frmNileDotCom.getContentPane().add(itemQtyLabel);
		
		itemInfoLabel.setBounds(10, 86, 177, 14);
		frmNileDotCom.getContentPane().add(itemInfoLabel);
		
		orderTotalLabel.setBounds(10, 111, 177, 14);
		frmNileDotCom.getContentPane().add(orderTotalLabel);
		
		numItemsText = new JTextField();
		numItemsText.setBounds(223, 8, 384, 20);
		frmNileDotCom.getContentPane().add(numItemsText);
		numItemsText.setColumns(10);
		
		itemIDText = new JTextField();
		itemIDText.setBounds(223, 33, 384, 20);
		frmNileDotCom.getContentPane().add(itemIDText);
		itemIDText.setColumns(10);
		
		itemQtyText = new JTextField();
		itemQtyText.setBounds(223, 58, 384, 20);
		frmNileDotCom.getContentPane().add(itemQtyText);
		itemQtyText.setColumns(10);
		
		itemInfoText = new JTextField();
		itemInfoText.setBounds(223, 83, 384, 20);
		frmNileDotCom.getContentPane().add(itemInfoText);
		itemInfoText.setColumns(10);
		
		orderTotalText = new JTextField();
		orderTotalText.setBounds(223, 108, 384, 20);
		frmNileDotCom.getContentPane().add(orderTotalText);
		orderTotalText.setColumns(10);
	}
}