package com.yunhu.yhshxc.utility;

import android.app.Activity;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class CroutonUtil {

	public static final Style CROUTON_RED = Style.ALERT;
	public static final Style CROUTON_GREEN = Style.CONFIRM;
	public static final Style CROUTON_BULE = Style.INFO;

	public static void showCrouton(Activity activity,String croutonText) {
		showCrouton(activity,croutonText, Style.MESSAGE, Configuration.DEFAULT, null);
	}
	public static void showCrouton(Activity activity,String croutonText, Style bgColor) {
		showCrouton(activity,croutonText, bgColor, Configuration.DEFAULT, null);
	}
	public static void showCrouton(Activity activity,String croutonText, Style bgColor,
			int durationInMilliseconds) {
		Configuration croutonConfiguration = new Configuration.Builder()
				.setDuration(durationInMilliseconds).build();
		showCrouton(activity,croutonText, bgColor, croutonConfiguration, null);
	}

	private static void showCrouton(Activity activity,String croutonText, Style croutonStyle,
			Configuration configuration, Integer viewGroupResId) {
		Crouton crouton;
		if (viewGroupResId == null) {
			crouton = Crouton.makeText(activity, croutonText, croutonStyle);
		} else {
			crouton = Crouton.makeText(activity, croutonText, croutonStyle,viewGroupResId);
		}
		crouton.setConfiguration(configuration).show();
	}
}
