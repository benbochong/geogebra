package org.geogebra.web.web.gui.view.probcalculator;

import org.geogebra.common.gui.view.probcalculator.ChiSquareCell;
import org.geogebra.common.gui.view.probcalculator.ChiSquarePanel;
import org.geogebra.common.gui.view.probcalculator.StatisticsCalculator;
import org.geogebra.common.gui.view.probcalculator.StatisticsCalculator.Procedure;
import org.geogebra.common.gui.view.probcalculator.StatisticsCollection;
import org.geogebra.common.main.Localization;
import org.geogebra.common.util.debug.Log;
import org.geogebra.web.html5.gui.inputfield.AutoCompleteTextFieldW;
import org.geogebra.web.html5.gui.util.ListBoxApi;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author gabor
 * 
 * ChiSquarePanel for Web
 *
 */
public class ChiSquarePanelW extends ChiSquarePanel implements ValueChangeHandler<Boolean>, ChangeHandler, FocusHandler, KeyPressHandler {

	private FlowPanel wrappedPanel;
	private Label lblRows;
	private Label lblColumns;
	private CheckBox ckExpected;
	private CheckBox ckChiDiff;
	private CheckBox ckRowPercent;
	private CheckBox ckColPercent;
	private ListBox cbRows;
	private ListBox cbColumns;
	private FlowPanel pnlCount;
	private ChiSquareCellW[][] cell;
	private boolean showColumnMargin;
	private FlowPanel pnlControl;

	/**
	 * Constructs chisquarepanel for web
	 * 
	 * @param loc
	 *            application
	 * @param statcalc
	 *            calculator
	 * 
	 */
	public ChiSquarePanelW(Localization loc, StatisticsCalculator statcalc) {
		super(loc, statcalc);
	    createGUI();
	    setLabels();
	    
	    
    }

	private void createGUI() {
	    this.wrappedPanel = new FlowPanel();
	    
	    createGUIElements();
	    createCountPanel();
	    createControlPanel();
	    
	    FlowPanel p = new FlowPanel();
	    p.add(pnlCount);
	    wrappedPanel.add(pnlControl);
	    wrappedPanel.add(p);
	    
	    
    }

	private void createControlPanel() {
		pnlControl = new FlowPanel();
		pnlControl.setStyleName("pnlControl");
		pnlControl.add(lblRows);
		pnlControl.add(cbRows);
		pnlControl.getElement().appendChild(Document.get().createBRElement());
		pnlControl.add(lblColumns);
		pnlControl.add(cbColumns);
		FlowPanel lineBreak = new FlowPanel();
		lineBreak.setStyleName("lineBreak");
		pnlControl.add(lineBreak);
		pnlControl.add(ckRowPercent);
		pnlControl.add(ckColPercent);
		pnlControl.add(ckExpected);
		pnlControl.add(ckChiDiff);
	    
    }

	private void createCountPanel() {
	    if (pnlCount == null) {
	    	pnlCount = new FlowPanel();
	    	pnlCount.addStyleName("pnlCount");
	    }
	    
	    pnlCount.clear();
	    cell = new ChiSquareCellW[getSc().rows + 2][getSc().columns + 2];
	    
	    for (int r = 0; r < getSc().rows + 2; r++) {
	    	FlowPanel row = new FlowPanel();
	    	row.addStyleName("chirow");
	    	for (int c = 0; c < getSc().columns + 2; c++) {
	    		cell[r][c] = new ChiSquareCellW(getSc(), r, c);
	    		cell[r][c].getInputField().addKeyPressHandler(this);
	    		cell[r][c].getInputField().addFocusHandler(this);
	    		
	    		if (getStatCalc().getSelectedProcedure() == Procedure.GOF_TEST) {
	    			//cell[r][c].setColumns(10);
	    		}
	    		
	    		row.add(cell[r][c].getWrappedPanel());
	    	}
	    	
	    	pnlCount.add(row);
	    }
	    
	    // upper-right corner cell
	   
	    cell[0][0].setMarginCell(true);
	    
	    //column headers and margins
	    for (int c = 1; c < getSc().columns + 2; c++) {
	    	cell[0][c].setHeaderCell(true);
	    	cell[getSc().rows + 1][c].setMarginCell(true);
	    }
	    
	    // row headers adn margins
	    for (int r = 0; r < getSc().rows + 1; r++) {
	    	cell[r][0].setHeaderCell(true);
	    	cell[r][getSc().columns + 1].setMarginCell(true);
	    }
	    
	    //set input cells
	    for (int r = 1; r < getSc().rows + 1; r++) {
	    	for (int c = 1; c < getSc().columns + 1; c++) {
	    		cell[r][c].setInputCell(true);
	    	}
	    }
	    
	    //clear other corners
	    cell[getSc().rows + 1][0].hideAll();
	    cell[0][getSc().columns + 1].hideAll();
	    
	    if (getStatCalc().getSelectedProcedure() == Procedure.GOF_TEST) {
	    	cell[0][1].setMarginCell(true);
	    	cell[0][2].setMarginCell(true);
	    }
	}
	    
	 // ==========================================
	// Event handlers
	// ==========================================

		public void updateGUI() {

			if (getStatCalc().getSelectedProcedure() == Procedure.CHISQ_TEST) {
				cbColumns.setVisible(true);
				lblColumns.setVisible(true);
				ckRowPercent.setVisible(true);
				ckExpected.setVisible(true);
				ckChiDiff.setVisible(true);

			} else if (getStatCalc().getSelectedProcedure() == Procedure.GOF_TEST) {
				cbColumns.setVisible(false);
				lblColumns.setVisible(false);
				ckRowPercent.setVisible(false);
				ckExpected.setVisible(false);
				ckChiDiff.setVisible(false);

				cbColumns.setSelectedIndex(0);

			}

			getSc().setChiSqData(Integer.parseInt(cbRows.getValue(cbRows.getSelectedIndex())),
					Integer.parseInt(cbColumns.getValue(cbColumns.getSelectedIndex())));

			createCountPanel();
			setLabels();
		}
		
		private void updateVisibility() {
			for (int i = 1; i < getSc().rows + 1; i++) {
				for (int j = 1; j < getSc().columns + 1; j++) {
					cell[i][j].setLabelVisible(1, ckExpected.getValue());
					cell[i][j].setLabelVisible(2, ckChiDiff.getValue());
					cell[i][j].setLabelVisible(3, ckRowPercent.getValue());
					cell[i][j].setLabelVisible(4, ckColPercent.getValue());
				}
			}

			// column percent for bottom margin
			for (int r = 0; r < getSc().rows; r++) {
				cell[r + 1][getSc().columns + 1].setLabelVisible(3,
						ckColPercent.getValue());
			}

			// row percent for right margin
			for (int c = 0; c < getSc().columns; c++) {
				cell[getSc().rows + 1][c + 1].setLabelVisible(4,
						ckRowPercent.getValue());
			}

			updateCellContent();
		}

		private void updateCellContent() {

			getStatProcessor().doCalculate();

			for (int r = 0; r < getSc().rows; r++) {
				for (int c = 0; c < getSc().columns; c++) {
					if (ckExpected.getValue()) {
						cell[r + 1][c + 1].setLabelText(1,
								getStatCalc().format(getSc().expected[r][c]));
					}
					if (ckChiDiff.getValue()) {
						cell[r + 1][c + 1].setLabelText(2,
								getStatCalc().format(getSc().diff[r][c]));
					}
					if (ckRowPercent.getValue()) {
						cell[r + 1][c + 1].setLabelText(
								3,
								getStatCalc().format(100 * getSc().observed[r][c]
										/ getSc().rowSum[r]));
					}
					if (ckColPercent.getValue()) {
						cell[r + 1][c + 1].setLabelText(
								4,
								getStatCalc().format(100 * getSc().observed[r][c]
										/ getSc().columnSum[c]));
					}
				}
			}

			// column margin
			if (showColumnMargin) {
				for (int r = 0; r < getSc().rows; r++) {
					cell[r + 1][getSc().columns + 1].setLabelText(0,
							getStatCalc().format(getSc().rowSum[r]));
					if (ckRowPercent.getValue()) {
						cell[r + 1][getSc().columns + 1].setLabelText(3,
								getStatCalc().format(100 * getSc().rowSum[r] / getSc().total));
					}
				}
			}

			// bottom margin
			for (int c = 0; c < getSc().columns; c++) {
				cell[getSc().rows + 1][c + 1].setLabelText(0,
						getStatCalc().format(getSc().columnSum[c]));

				if (ckColPercent.getValue()) {
					cell[getSc().rows + 1][c + 1].setLabelText(4,
							getStatCalc().format(100 * getSc().columnSum[c] / getSc().total));
				}

			}

			// bottom right corner
			if (showColumnMargin) {
				cell[getSc().rows + 1][getSc().columns + 1].setLabelText(0,
						getStatCalc().format(getSc().total));
			}

		}
	    
	    
	/**
	 * Update translation
	 */
	public void setLabels() {
		lblRows.setText(getMenu("Rows"));
		lblColumns.setText(getMenu("Columns"));
		ckExpected.setText(getMenu("ExpectedCount"));
		ckChiDiff.setText(getMenu("ChiSquaredContribution"));
		ckRowPercent.setText(getMenu("RowPercent"));
		ckColPercent.setText(getMenu("ColumnPercent"));

		if (getStatCalc().getSelectedProcedure() == Procedure.GOF_TEST) {
			cell[0][1].setLabelText(0, getMenu("ObservedCount"));
			cell[0][2].setLabelText(0, getMenu("ExpectedCount"));
		}

	}

	private void createGUIElements() {
	    lblRows = new Label();
	    lblColumns = new Label();
	    
	    ckExpected = new CheckBox();
	    ckChiDiff = new CheckBox();
	    ckRowPercent = new CheckBox();
	    ckColPercent = new CheckBox();
	    
	    ckExpected.addValueChangeHandler(this);
	    ckChiDiff.addValueChangeHandler(this);
	    ckRowPercent.addValueChangeHandler(this);
	    ckColPercent.addValueChangeHandler(this);
	    

	    
	    // drop down menu for rows/columns 2-12
	 	String[] num = new String[11];
	 		for (int i = 0; i < num.length; i++) {
	 			num[i] = "" + (i + 2);
	 	}
	    
	    cbRows = new ListBox();
	    cbColumns = new ListBox();
	    
	    for (int i = 0; i < num.length; i++) {
	    	cbRows.addItem(num[i]);
	    	cbColumns.addItem(num[i]);
	    }
	    
		Log.debug(getSc().rows + " :: " + getSc().columns);
	    cbRows.setSelectedIndex(ListBoxApi.getIndexOf(String.valueOf(getSc().rows), cbRows));
	    cbRows.addChangeHandler(this);
	    
	    cbColumns.setSelectedIndex(ListBoxApi.getIndexOf(String.valueOf(getSc().columns -1), cbColumns));
	    cbColumns.addChangeHandler(this);
	        
    }

	//@Override
    @Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
    	  Object source = event.getSource();
    	  

  		if (source == ckExpected || source == ckChiDiff
  				|| source == ckRowPercent || source == ckColPercent) {
  			updateVisibility();
  		}
    }

	//@Override
    @Override
	public void onChange(ChangeEvent event) {
	    updateGUI();
    }
    
	public class ChiSquareCellW extends ChiSquareCell implements FocusHandler,
			KeyUpHandler {
    	
    	private FlowPanel wrappedPanel;
    	
    	private AutoCompleteTextFieldW fldInput;
    	private Label[] label;

		private Boolean isInputCell = false;
    	
    	/**
    	 * Construct ChiSquareCell with given row, column
    	 */
    	public ChiSquareCellW(StatisticsCollection sc, int row, int column) {
    		this(sc);
			init(row, column);
    	}

		/**
    	 * Construct ChiSquareCell
    	 */
    	public ChiSquareCellW(StatisticsCollection sc) {

			super(sc);
    		this.wrappedPanel = new FlowPanel();
    		this.wrappedPanel.addStyleName("ChiSquarePanelW");
			fldInput = new AutoCompleteTextFieldW(statCalc.getApp());
			fldInput.addKeyUpHandler(this);
    		fldInput.addFocusHandler(this);
    		wrappedPanel.add(fldInput);

    		label = new Label[5];
    		for (int i = 0; i < label.length; i++) {
    			label[i] = new Label();
    			wrappedPanel.add(label[i]);
    		}
    		setColumns(4);
    		setVisualStyle();
    		hideAllLabels();

    	}

    	public void setColumns(int columns) {
    		//fldInput.setColumns(columns); no good for layout

    		// force a minimum width for margin cells
    		wrappedPanel.add(fldInput);

    	}

    	/**
    	 * hide all labels
    	 */
    	public void hideAllLabels() {
    		for (int i = 0; i < label.length; i++) {
    			label[i].setVisible(false);
    		}
    	}

    	/**
    	 * hide all
    	 */
    	public void hideAll() {
    		hideAllLabels();
    		fldInput.setVisible(false);
    	}

    	/**
    	 * @return input field
    	 */
    	public AutoCompleteTextFieldW getInputField() {
    		return fldInput;
    	}

    	/**
    	 * @return label array
    	 */
    	public Label[] getLabel() {
    		return label;
    	}

    	public void setLabelText(int index, String s) {
    		label[index].setText(s);
    	}

    	public void setLabelVisible(int index, boolean isVisible) {
    		label[index].setVisible(isVisible);
    	}
    	
    	public void setInputCell(boolean isInputCell) {
    		this.isInputCell = isInputCell;
    		setVisualStyle();
        }

		@Override
		protected void setVisualStyle() {
    		fldInput.setVisible(false);

			if (isMarginCell()) {
    			setLabelVisible(0, true);

			} else if (isHeaderCell()) {
    			
    			fldInput.setVisible(true);
    			wrappedPanel.addStyleName("headercell");
    			//TODO CSSfldInput.setBackground(geogebra.awt.GColorD
    					//.getAwtColor(GeoGebraColorConstants.TABLE_BACKGROUND_COLOR_HEADER));

    		} else if (isInputCell) {
    			fldInput.setVisible(true);
    			wrappedPanel.addStyleName("inputcell");
    		} else {
    			fldInput.setVisible(true);
    			wrappedPanel.removeStyleName("headercell");
    			//TODO csswrappedPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    			//TODO cssfldInput.setBackground(geogebra.awt.GColorD
    					//.getAwtColor(GeoGebraColorConstants.WHITE));
    		}

    	}

    	private void updateCellData() {
			updateCellData(fldInput.getText());
    	}

		// TODO attach the listener
    	public void focusLost(FocusEvent e) {
    		updateCellData();
    		getStatCalc().updateResult();
    	}
    	
    	public FlowPanel getWrappedPanel() {
    		return wrappedPanel;
    	}

		@Override
		public void onKeyUp(KeyUpEvent e) {

			updateCellData();
			getStatCalc().updateResult();
			updateCellContent();
    	    
        }

        @Override
		public void onFocus(FocusEvent event) {
    		if (event.getSource() instanceof TextBox) {
    			((TextBox) event.getSource()).selectAll();
    		}
        }

    }

	@Override
	public void onFocus(FocusEvent event) {
	    if (event.getSource() instanceof TextBox) {
	    	((TextBox) event.getSource()).selectAll();
	    }
	    
    }

	@Override
	public void onKeyPress(KeyPressEvent event) {
	   Object source = event.getSource();
	   if (source instanceof TextBox) {
			doTextFieldActionPerformed();
	   }
    }

	private void doTextFieldActionPerformed() {
	    updateCellContent();
    }

	/**
	 * @return the wrapped panel
	 */
	public FlowPanel getWrappedPanel() {
	    return wrappedPanel;
    }


}
