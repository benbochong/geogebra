package geogebra.mobile.gui;

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

interface Resources extends ClientBundle
{
	public static final Resources INSTANCE = GWT.create(Resources.class);

	@Source("Youtube-Logo.png")
	ImageResource logo();
	
	@Source("Tux.svg")
	SVGResource tux();
}