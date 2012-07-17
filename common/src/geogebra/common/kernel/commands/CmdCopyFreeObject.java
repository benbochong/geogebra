package geogebra.common.kernel.commands;

import geogebra.common.kernel.Kernel;
import geogebra.common.kernel.StringTemplate;
import geogebra.common.kernel.arithmetic.Command;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.main.MyError;

/**
 *CopyFreeObject
 */
public class CmdCopyFreeObject extends CommandProcessor {

	/**
	 * Create new command processor
	 * 
	 * @param kernel
	 *            kernel
	 */
	public CmdCopyFreeObject(Kernel kernel) {
		super(kernel);
	}

	@Override
	final public GeoElement[] process(Command c) throws MyError {
		int n = c.getArgumentNumber();
		GeoElement[] arg;
		arg = resArgs(c);

		switch (n) {
		case 1:

			String label = c.getLabel();
			if (arg[0].isGeoFunctionConditional()|| arg[0].isGeoFunctionNVar() || arg[0].isGeoFunction()) {
				String command = label == null ? "" : label + "="; 

				StringTemplate highPrecision = StringTemplate.maxPrecision;
				command += arg[0].toOutputValueString(highPrecision); 

				try { 

					GeoElement[] ret = kernelA.getAlgebraProcessor() 
							.processAlgebraCommandNoExceptions(command, true); 

					ret[0].setVisualStyle(arg[0]); 
					if(!arg[0].isLabelSet())
						arg[0].remove();
					return ret; 

				} catch (Exception e) { 
					if(!arg[0].isLabelSet())
						arg[0].remove();
					e.printStackTrace(); 
					throw argErr(app, c.getName(), arg[0]); 
				} 
			}
			// changed to deepCopyGeo() so that it works for lists
			// https://www.geogebra.org/forum/viewtopic.php?f=8&t=26356
			GeoElement geo = arg[0].deepCopyGeo();
			geo.setLabel(label);
			GeoElement[] ret = { geo };
			if(!arg[0].isLabelSet())
				arg[0].remove();
			return ret;


			// more than one argument
		default:
			throw argNumErr(app, c.getName(), n);
		}
	}
}
