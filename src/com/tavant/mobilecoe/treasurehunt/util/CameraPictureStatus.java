package com.tavant.mobilecoe.treasurehunt.util;

import java.util.HashMap;

public interface CameraPictureStatus {
	 void onPicturetaken(String path);
	 void onPicturefailed(HashMap error);

}
