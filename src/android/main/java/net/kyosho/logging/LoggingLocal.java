package net.kyosho.logging;

import android.Manifest;
import android.content.pm.PackageManager;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import lombok.NonNull;

import static net.kyosho.logging.CallbackType.COMPLETE;
import static net.kyosho.logging.CallbackType.ERROR;
import static net.kyosho.logging.CallbackType.NEXT;

/**
 * This class echoes a string called from JavaScript.
 */
public class LoggingLocal extends CordovaPlugin {
  /**
   * TAG
   */
  private static final String TAG = LoggingLocal.class.getSimpleName();

  private static final Logger log = LoggerFactory.getLogger(LoggingLocal.class);

  private static final int PERMISSION_REQUEST_CODE = 0;

  private static final String[] PERMISSIONS = {
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
  };

  private CallbackContext permissionCallbackContext;

  private JSONArray permissionArgs;

  private boolean hasPermissions(@NonNull final String... permissions) {
    for (String permission : permissions) {
      if (!(cordova.hasPermission(permission))) {
        return false;
      }
    }
    return true;
  }

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

      if (requestCode == PERMISSION_REQUEST_CODE) {
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
        target -> target.sendPluginResult(PluginResultFactory.createPluginResult(ERROR, t.getMessage())));
    } finally {
      this.permissionCallbackContext = null;
      this.permissionArgs = null;
    }
  }

  @Override
  public boolean execute(
    @NonNull final String action,
    @NonNull final JSONArray args,
    @NonNull final CallbackContext callbackContext) {
    boolean result = false;

    try {
      switch (action) {
        case "log": {
          if (!this.hasPermissions(PERMISSIONS)) {
            this.permissionCallbackContext = callbackContext;
            this.permissionArgs = args;
            cordova.requestPermissions(
              this, PERMISSION_REQUEST_CODE, PERMISSIONS);
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
      callbackContext.sendPluginResult(
        PluginResultFactory.createPluginResult(ERROR, t.getMessage()));
    }

    return result;
  }

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

        callbackContext.sendPluginResult(
          PluginResultFactory.createPluginResult(NEXT, null));
        callbackContext.sendPluginResult(
          PluginResultFactory.createPluginResult(COMPLETE, null));
      } catch (Exception e) {
        LOG.e(TAG, e.getMessage(), e);
        callbackContext.sendPluginResult(
          PluginResultFactory.createPluginResult(ERROR, e.getMessage()));
      }
    });
  }
}
