package net.kyosho.logging;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 *
 */
public class LoggingSettings {

  /**
   *
   */
  @Getter
  @Setter
  @JsonProperty("logFilePath")
  private String logFilePath = "/log/log.log";

  /**
   *
   */
  @Getter
  @Setter
  @JsonProperty("layoutPattern")
  private String layoutPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} : %-5level : %msg%n";

  /**
   *
   */
  @Getter
  @Setter
  @JsonProperty("maxFileSize")
  private String maxFileSize = "64MB";

  /**
   *
   */
  @Getter
  @Setter
  @JsonProperty("rollingFileNamePostfix")
  private String rollingFileNamePostfix = "-%d{yyyy-MM-dd}.%i.zip";

  /**
   *
   */
  @Getter
  @Setter
  @JsonProperty("maxHistory")
  private int maxHistory = 14;

  /**
   *
   * @param context
   * @return
   */
  public File getLogFile(@NonNull final Context context) {
    return new File(context.getExternalFilesDir(null), this.logFilePath);
  }

  /**
   *
   * @param file
   * @return
   */
  public String createRollingFileNamePattern(@NonNull final File file) {
    return file.getParent() + "/" + file.getName() + this.rollingFileNamePostfix;
  }
}
