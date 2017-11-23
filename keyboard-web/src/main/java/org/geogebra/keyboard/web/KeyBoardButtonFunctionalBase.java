package org.geogebra.keyboard.web;

import org.geogebra.keyboard.base.Action;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.impl.ImageResourcePrototype;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Image;

/**
 * A KeyBoardButton with functional character. This button doesn't insert a
 * character or anything else, this button performs an action.
 * 
 */
public class KeyBoardButtonFunctionalBase extends KeyBoardButtonBase {

	/**
	 * the possible actions for a functional button
	 * 
	 */
	// public enum Action {
	// ENTER, BACKSPACE, SHIFT, ARROW_LEFT, ARROW_RIGHT, SWITCH_KEYBOARD;
	// }
	
	private Image image;
	private Action action;

	/**
	 * used for keyboardButtons with an image.
	 * 
	 * @param image
	 *            {@link ImageResource}
	 * @param handler
	 *            {@link ClickHandler}
	 * @param action
	 *            {@link Action}
	 */
	public KeyBoardButtonFunctionalBase(ImageResource image,
			ButtonHandler handler,
	        Action action) {
		super(handler);
		this.image = new Image(image);
		this.action = action;
		this.add(this.image);
		addStyleName("colored");
		this.addStyleName("waves-light");
	}

	public KeyBoardButtonFunctionalBase(ImageResource image,
			String feedback, ButtonHandler handler) {
		super(handler);
		this.image = new Image(image);
		this.feedback = feedback;
		this.add(this.image);
	}

	/**
	 * @param svg
	 * @param feedback
	 * @param handler
	 * @param addSupSyle
	 */
	public KeyBoardButtonFunctionalBase(SafeUri svg, String feedback,
			ButtonHandler handler, boolean addSupSyle) {
		super(handler);
		this.image = new Image(new ImageResourcePrototype(null,
				svg, 0, 0, 24, 24, false, false));
		this.feedback = feedback;
		this.add(this.image);
		if (addSupSyle) {
			this.addStyleName("sup");
		}
	}

	/**
	 * used for keyboardButtons with an image.
	 * 
	 * @param caption
	 *            String
	 * 
	 * @param handler
	 *            {@link ClickHandler}
	 * @param action
	 *            {@link Action}
	 */
	public KeyBoardButtonFunctionalBase(String caption, ButtonHandler handler,
	        Action action) {
		super(caption, caption, handler);
		this.action = action;
		addStyleName("colored");
	}

	/**
	 * sets the image of the button
	 * 
	 * @param keyboard_shiftDown
	 *            {@link ImageResource}
	 */
	public void setPicture(ImageResource keyboard_shiftDown) {
		if (this.image != null) {
			this.remove(this.image);
		}
		this.image = new Image(keyboard_shiftDown);
		this.add(this.image);
	}

	/**
	 * @return {@link Action}
	 */
	public Action getAction() {
		return this.action;
	}
}
