/**
 * Copyright (C) 2022, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import org.silverbulleters.dt.silverlint.SilverCore;
import org.sonarsource.sonarlint.core.client.api.common.LogOutput;

public class CustomLogOutput implements LogOutput {

  @Override
  public void log(String formattedMessage, Level level) {
      if (level == Level.INFO || level == Level.DEBUG || level == Level.WARN) {
          System.out.println(formattedMessage);
      }
      if (level == Level.ERROR) {
          SilverCore.logError(formattedMessage);
      }
  }
}
