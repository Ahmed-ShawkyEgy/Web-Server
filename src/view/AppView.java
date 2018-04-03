package view;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class AppView extends JFrame
{
	private static final long serialVersionUID = 1L;
	DefaultListModel<String> model , model2;
	
	public AppView(ActionListener listener,String name)
	{
		super(name);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent event)
			{
				listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"quit"));
			}
		});
		setSize(840,1020);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = dim.width/2-this.getSize().width/2, y = dim.height/2-this.getSize().height/2;
		setLocation(x, y);
		setLayout(null);
		
		
		
		/* Files */
		model = new DefaultListModel<String>();
	    JList<String> list = new JList<String>(model);
	    JScrollPane pane = new JScrollPane(list);
	    
	    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    add(pane);
	    pane.setBounds(4, 4, getWidth()/3, (int) (getHeight()/1.3));
	    
	    /* Console */
	    model2 = new DefaultListModel<String>();
	    JList<String> list2 = new JList<String>(model2);
	    JScrollPane pane2 = new JScrollPane(list2);
	    add(pane2);
	    pane2.setBounds(getWidth()/3 + 15 , 4 , getWidth()-getWidth()/3,(int)(getHeight()/1.3));
	    
	    
	    /* Connection */
	    
	    CheckboxGroup cbg = new CheckboxGroup();
	    JPanel panel2 = new JPanel();
	    panel2.add(new JLabel("Select Connection"));
	    panel2.add(new Checkbox("keep-alive", cbg, true));
	    panel2.add(new Checkbox("close", cbg, false));
	    
	    add(panel2);
	    panel2.setBounds(getWidth()/4 + 10, (int)(getHeight()/1.3)+60, getWidth()/3, (int) (getHeight()/1.3));
	    

	    
	    /* Formats */
	    
	    JCheckBox[] formats = new JCheckBox[5];
	    formats[0] = new JCheckBox("txt");
	    formats[1] = new JCheckBox("jpeg");
	    formats[2] = new JCheckBox("png");
	    formats[3] = new JCheckBox("mp3");
	    formats[4] = new JCheckBox("mp4");
	    
	    JPanel panel = new JPanel();
	    for (int i = 0; i < formats.length; i++) {
	    	panel.add(formats[i]);
		}
	    add(panel);
	    
	    panel.setBounds(getWidth()/4 + 10, (int)(getHeight()/1.3)+10, getWidth()/3, (int) (getHeight()/1.3));
	    
	    
	    
	    
		/* Message Form */
		JButton b = new JButton("Request");
		
		
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String command = "request "+list.getSelectedValue()+"|";
				for (int i = 0; i < formats.length; i++) {
					if(formats[i].isSelected())
						command += formats[i].getText()+" ";
				}
				command+="|"+cbg.getSelectedCheckbox().getLabel()+"\n";
				listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,command));
			}
		});
		
		b.setPreferredSize(new Dimension(140,50));
		
		
		JPanel bottom = new JPanel();
		bottom.add(b);
		
		add(bottom);
		bottom.setBounds(1, (int) (getHeight()/1.3) + 100, getWidth(), getHeight());
		
		setVisible(true);
	}
	

	
	public void addMember(String name)
	{
		model.addElement(name);
	}
	
	public void removeMember(String name)
	{
		model.removeElement(name);
	}
	
	public void clearMembers()
	{
		model.clear();
	}
	
	public void print(String s)
	{
		model2.addElement(s);
	}

}
