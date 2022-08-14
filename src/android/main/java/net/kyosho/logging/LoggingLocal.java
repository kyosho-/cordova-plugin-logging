package net.kyosho.logging;

import android.Manifest;
import android.content.pm.PackageManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import lombok.NonNull;

/**
 * This class echoes a string called from JavaScript.
 */
public class LoggingLocal extends CordovaPlugin {
  /**
   * TAG
   */
  private static final String TAG = LoggingLocal.class.getSimpleName();

  /**
   *
   */
  private static final Logger log = LoggerFactory.getLogger(LoggingLocal.class);

  /**
   *
   */
  private static final int PERMISSION_REQUEST_CODE_CONFIGURE = 0;

  /**
   *
   */
  private static final int PERMISSION_REQUEST_CODE_LOG = 1;

  /**
   *
   */
  private static final String[] PERMISSIONS = {
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
  };

  /**
   * Jackson ObjectMapper
   */
  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JsonOrgModule());

  /**
   *
   */
  private CallbackContext permissionCallbackContext;

  /**
   *
   */
  private JSONArray permissionArgs;

  /**
   *
   */
  private LoggingSettings settings;

  /**
   *
   * @param permissions
   * @return
   */
  private boolean hasPermissions(@NonNull final String... permissions) {
    for (String permission : permissions) {
      if (!(cordova.hasPermission(permission))) {
        return false;
      }
    }
    return true;
  }

  /**
   *
   * @param requestCode
   * @param permissions
   * @param grantResults
   */
  @Override
  public void onRequestPermissionResult(
    final int requestCode,
    @NonNull final String[] permissions,
    @NonNull final int[] grantResults) {
    try {
      super.onRequestPermissionResult(requestCode, permissions, grantResults);

      for (int grantResult : grantResults) {
        if (grantResult == PackageManager.PERMISSION_DENIED) {
          permissionCallbackContext.sendPluginResult(
            new PluginResult(PluginResult.Status.ILLEGAL_ACCESS_EXCEPTION));
          return;
        }
      }

      if (requestCode == PERMISSION_REQUEST_CODE_CONFIGURE) {
        final LoggingSettings settings = this.mapper.convertValue(
          permissionArgs.getJSONObject(0),
          LoggingSettings.class);
        this.configure(settings, permissionCallbackContext);
      } else if (requestCode == PERMISSION_REQUEST_CODE_LOG) {
        String level = permissionArgs.getString(0);
        String message = permissionArgs.getString(1);
        this.log(level, message, permissionCallbackContext);
      } else {
        permissionCallbackContext.sendPluginResult(
          new PluginResult(PluginResult.Status.INVALID_ACTION));
      }
    } catch (Throwable t) {
      LOG.e(TAG, t.getMessage(), t);
      Optional.ofNullable(this.permissionCallbackContext).ifPresent(
        target -> target.error(t.getMessage()));
    } finally {
      this.permissionCallbackContext = null;
      this.permissionArgs = null;
    }
  }

  /**
   *
   * @param action          The action to execute.
   * @param args            The exec() arguments.
   * @param callbackContext The callback context used when calling back into JavaScript.
   * @return
   */
  @Override
  public boolean execute(
    @NonNull final String action,
    @NonNull final JSONArray args,
    @NonNull final CallbackContext callbackContext) {
    boolean result = false;

    try {
      switch (action) {
        case "configure": {
          if (!this.hasPermissions(PERMISSIONS)) {
            this.permissionCallbackContext = callbackContext;
            this.permissionArgs = args;
            cordova.requestPermissions(
              this, PERMISSION_REQUEST_CODE_CONFIGURE, PERMISSIONS);
          } else {
            final LoggingSettings settings = this.mapper.convertValue(
              args.getJSONObject(0),
              LoggingSettings.class);
            this.configure(settings, callbackContext);
          }
        }
        break;
        case "log": {
          if (!this.hasPermissions(PERMISSIONS)) {
            this.permissionCallbackContext = callbackContext;
            this.permissionArgs = args;
            cordova.requestPermissions(
              this, PERMISSION_REQUEST_CODE_LOG, PERMISSIONS);
          } else {
            String level = args.getString(0);
            String message = args.getString(1);
            this.log(level, message, callbackContext);
          }
        }
        break;
        default:
          throw new UnsupportedOperationException(
            String.format("Unsupported action. (action=%s)", action));
      }
      result = true;
    } catch (Throwable t) {
      LOG.e(TAG, t.getMessage(), t);
      callbackContext.error(t.getMessage());
    }

    return result;
  }

  /**
   *
   * @param settings
   * @param callbackContext
   */
  private void configure(
    @NonNull final LoggingSettings settings,
    @NonNull final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(() -> {
      try {
        final File logFile = settings.getLogFile(this.cordova.getContext());

        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();

        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        {
          encoder.setContext(loggerContext);
          encoder.setPattern(settings.getLayoutPattern());
          encoder.start();
        }

        final SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP<>();
        sizeAndTimeBasedFNATP.setMaxFileSize(FileSize.valueOf(settings.getMaxFileSize()));

        final TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy<>();
        {
          final String filePattern = settings.createRollingFileNamePattern(logFile);
          rollingPolicy.setFileNamePattern(filePattern);
          rollingPolicy.setMaxHistory(settings.getMaxHistory());
          rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
          rollingPolicy.setContext(loggerContext);
        }

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<>();
        {
          rollingFileAppender.setAppend(true);
          rollingFileAppender.setName("FILE");
          rollingFileAppender.setContext(loggerContext);
          rollingFileAppender.setFile(logFile.getAbsolutePath());
          rollingFileAppender.setRollingPolicy(rollingPolicy);
          rollingFileAppender.setEncoder(encoder);
        }

        rollingPolicy.setParent(rollingFileAppender);
        rollingPolicy.start();
        rollingFileAppender.start();

        // Logcat appender
        final LogcatAppender logcatAppender = new LogcatAppender();
        {
          logcatAppender.setContext(loggerContext);
          logcatAppender.setName("LOGCAT");
          logcatAppender.setEncoder(encoder);
          logcatAppender.start();
        }

        final ch.qos.logback.classic.Logger root =
          (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        {
          root.setLevel(Level.toLevel(settings.getLevel()));
          root.addAppender(rollingFileAppender);
          root.addAppender(logcatAppender);
        }

        callbackContext.success();
      } catch (Exception e) {
        LOG.e(TAG, e.getMessage(), e);
        callbackContext.error(e.getMessage());
      }
    });
  }

  /**
   *
   * @param level
   * @param message
   * @param callbackContext
   */
  private void log(
    @NonNull final String level,
    @NonNull final String message,
    @NonNull final CallbackContext callbackContext) {
    cordova.getThreadPool().execute(() -> {
      try {
        switch (level) {
          case "debug":
            log.debug(message);
            break;
          case "info":
            log.info(message);
            break;
          case "warn":
            log.warn(message);
            break;
          case "error":
            log.error(message);
            break;
          default:
            throw new UnsupportedOperationException(
              String.format("Unsupported Log Level. (level=%s)", level));
        }

        callbackContext.success();
      } catch (Exception e) {
        LOG.e(TAG, e.getMessage(), e);
        callbackContext.error(e.getMessage());
      }
    });
  }
}
