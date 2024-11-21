/**
*This class is used to add the menu and also add menuItems.
*Only adds if there is an object to add it to.
*
*Also provides the a method for checking if there is a menu, submenu or submenuitem
*which equals the name it is being checked with.
*
* @author Johan Boström 2207
* @version 1.0
* @since 2024-10-25
*/

package se.miun.id2207.dt187g.jpaint.gui;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class Menu extends JMenuBar {

	//Constructor that accepts a name and adds a new JMenu object with that name.
	public void addJMenu(String name) {
		JMenu jMenu = new JMenu(name);
		this.add(jMenu);
	}
	
	
	//adds a JmenuItem to the given parentMenu
	public void addJMenuItem(String parentName, String itemName) {
		JMenu parentMenu = (JMenu) getComponentByName(parentName);
		//checks if there is a parent JMenu to add the JMenuItem to if not, do nothing
		if (parentMenu == null) {
			return;
		}
		else {
			//creates a JMenuItem then add it to the parentMenu
			JMenuItem jMenuItem = new JMenuItem(itemName);
			parentMenu.add(jMenuItem);
		}
	}

	//adds a JmenuItem to the given parentmenu if there is one, also adds an actionlistener
	public void addJMenuItem(String parentName, String itemName, ActionListener al) {
		JMenu parentMenu = (JMenu) getComponentByName(parentName);
		//checks if there is a parent JMenu to add the JMenuItem to if not, do nothing
		if (parentMenu == null) {
			return;
		}
		else {
			//creates a JMenuItem and adds an ActionListener then add it to the parentMenu
			JMenuItem jMenuItem = new JMenuItem(itemName);
			jMenuItem.addActionListener(al);
			parentMenu.add(jMenuItem);
		}
	}

	//adds a JmenuItem to the given parentmenu if there is one, 
	//also adds an actionlistener and keystroke Action Listener.
	public void addJMenuItem(String parentName, String itemName, ActionListener al, KeyStroke keyStroke) {
		JMenu parentMenu = (JMenu) getComponentByName(parentName);
		//checks if there is a parent JMenu to add the JMenuItem toif not, do nothing
		if (parentMenu == null) {
			return;
		}
		else {
			//creates a JMenuItem and adds an ActionListener and a keystroke ActionListener
			//then add it to the parentMenu
			JMenuItem jMenuItem = new JMenuItem(itemName);
			jMenuItem.addActionListener(al);
			jMenuItem.setAccelerator(keyStroke);
			parentMenu.add(jMenuItem);
		}
	}


	//adds a submenu to the parent menu if there is a parentmenu
	public void addSubJMenu(String parentName, String subMenuName) {
		JMenu parentMenu = (JMenu) getComponentByName(parentName);
		//Checks if there is a parentMenu, if not, does nothing
		if (parentMenu == null) {
			return;
		}
		//since there is a parentMenu, adds submenu to the parentMenu
		else {
			JMenu subJMenu = new JMenu(subMenuName);
			parentMenu.add(subJMenu);
		}
	}

	//Returns the JMenu on the given index on the JMenuBar.
	public JMenu getJMenu(int index) {
		return this.getMenu(index);
	}


	//Method for checking the name and if there is such a JMenu or JmenuItem in the JMenuBar.
	//meaning it checks all the menus and submenus to see if the name is found.
	//If found returns the menu object with that name
	private JComponent getComponentByName(String name) {
				
		//this for loop iterates through all the JMenus and their subMenus in order to see
		//if there is a JMenu name equal to the name entered as an argument 
		for (Component component : this.getComponents()) {
			JMenu testMenu = (JMenu) component;
			//compares if the first level of JMenus have the same name equal to the name argument.
			//if yes, returns that JMenu.
			if (testMenu.getText().equals(name))
				return testMenu;

			//If not, checks each subMenu first if it is a JMenu or JMenuItem.
			//If it is, checks if the name of the submenu equals to the name argument
			else {
				for (Component subComponent : testMenu.getMenuComponents()) {
					//Checks if the subMenu is a JMenu.
					if (subComponent instanceof JMenu) {
						JMenu subTestMenu = (JMenu) subComponent;
						
						//If the submenu equals the name argument returns the subMenu
						if (subTestMenu.getText().equals(name))
							return subTestMenu;
					}
					//Checks if the subMenu is a JMenuItem.
					else if (subComponent instanceof JMenuItem) {
						JMenuItem subTestMenu = (JMenuItem) subComponent;
						
						//If the submenu equals the name argument returns the subMenu
						if (subTestMenu.getText().equals(name))
							return subTestMenu;
					}
				}
			}
		}
		//If the named argument can't be found among the menus or submenus in the Menubar returns null.
		return null;
	}
}
