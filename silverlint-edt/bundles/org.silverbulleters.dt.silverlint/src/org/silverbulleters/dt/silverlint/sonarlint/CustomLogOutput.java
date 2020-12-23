/**
 * Copyright (C) 2020, SilverBulleters LLC
 */
package org.silverbulleters.dt.silverlint.sonarlint;

import org.sonarsource.sonarlint.core.client.api.common.LogOutput;

public class CustomLogOutput implements LogOutput {
  @Override
  public void log(String formattedMessage, Level level) {
    if (level == Level.INFO || level == Level.DEBUG) {
      System.out.println(formattedMessage);
    }
  }
}
