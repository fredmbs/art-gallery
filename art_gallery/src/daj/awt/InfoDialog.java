// --------------------------------------------------------------------------
// $Id: InfoDialog.java,v 1.1 1997/10/25 15:50:01 ws Exp $
// information dialog with okay button
//
// (c) 1997, Wolfgang Schreiner <Wolfgang.Schreiner@risc.uni-linz.ac.at>
// http://www.risc.uni-linz.ac.at/software/daj
// --------------------------------------------------------------------------
package daj.awt;

import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import daj.Assertion;

@SuppressWarnings("serial")
public class InfoDialog extends Dialog implements ActionListener {

	// okay button
	private Button button;
	// parent frame
	private Frame parent;

	// --------------------------------------------------------------------------
	// create info dialog with parent `frame`, window `title` and `message`
	// `message` consists of a number of lines separated by "\n"
	// if `logo`, then our logo is inserted after the tex
	// --------------------------------------------------------------------------
	public InfoDialog(Frame frame, String title, String message, boolean logo) {
		// set window title
		super(frame, title, false);
		parent = frame;
		// set font
		Font font = new Font("TimesRoman", Font.PLAIN, 14);
		setFont(font);
		// create layout
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		// create panel for message
		MultiLineLabel text = new MultiLineLabel(message);
		add(this, text, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTH,
				1.0, 0.0, 15, 15, 0, 15);
		// create logo and center it
		if (logo) {
			Logo l = new Logo();
			add(this, l, 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTH,
					1.0, 0.0, 15, 15, 0, 15);
		}
		// create okay button and center it
		button = new Button("Okay");
		add(this, button, 0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.SOUTH,
				1.0, 1.0, 15, 15, 15, 15);
		// pack window to its natural size, resize resolves bug on some systems
		setSize(100, 100);
		pack();
		// register event handler
		button.addActionListener(this);
	}

	// --------------------------------------------------------------------------
	// add `component` to `container` using gridbag layout; see `Visualizer`
	// --------------------------------------------------------------------------
	public void add(Container container, Component component, int gridx, int gridy,
			int gridwidth, int gridheight, int fill, int anchor, double weightx,
			double weighty, int top, int left, int bottom, int right) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.fill = fill;
		c.anchor = anchor;
		c.weightx = weightx;
		c.weighty = weighty;
		if (top + left + bottom + right > 0)
			c.insets = new Insets(top, left, bottom, right);
		((GridBagLayout) container.getLayout()).setConstraints(component, c);
		container.add(component);
	}

	// --------------------------------------------------------------------------
	// try to handle `event` generated by component
	// --------------------------------------------------------------------------
	public void actionPerformed(ActionEvent event) {
		Assertion.test(event.getSource() == button, "unknown source");
		setVisible(false);
		parent.repaint();
	}
}
