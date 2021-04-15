package net.kyosho.logging;

import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import lombok.NonNull;

/**
 * Cordova PluginResult 生成用ファクトリ
 */
public class PluginResultFactory {
  /**
   * TAG
   */
  private static final String TAG = PluginResultFactory.class.getSimpleName();

  /**
   * Error Message CREATE_CALLBACK_OBJECT_000
   */
  static final String MESSAGE_ERROR_CREATE_CALLBACK_OBJECT_000 =
    "Unsupported callback type. (type=%s)";

  /**
   * KEY: JSON type
   */
  static final String KEY_JSON_TYPE = "type";

  /**
   * KEY: JSON name
   */
  static final String KEY_JSON_NAME = "name";

  /**
   * KEY: JSON message
   */
  static final String KEY_JSON_MESSAGE = "message";

  /**
   * KEY: JSON message
   */
  static final String KEY_JSON_DATA = "data";

  /**
   * メソッド名・クラス名の取得深さ
   */
  private static final int STACK_DEPTH = 4;

  /**
   * コールバックIDフォーマット文字列
   */
  private static final String CALLBACK_ID_FORMAT = "%s#%s";

  private PluginResultFactory() {
  }

  /**
   * コールバック用オブジェクトを生成します。
   *
   * @param type    コールバック種別
   * @param name    コールバック名称（IDとして利用する)
   * @param message メッセージ
   * @param dataObject    データ
   * @return コールバックオブジェクト
   */
  static PluginResult createPluginResult(
    final CallbackType type,
    final String name,
    final String message,
    final JSONObject rootObject,
    final Object dataObject) {
    PluginResult result;

    try {
      // KEY_JSON_TYPE
      rootObject.put(KEY_JSON_TYPE, type.value());

      // KEY_JSON_NAME
      rootObject.put(KEY_JSON_NAME, name);

      // KEY_JSON_MESSAGE
      if (Optional.ofNullable(message).isPresent()) {
        rootObject.put(KEY_JSON_MESSAGE, message);
      }

      // KEY_JSON_DATA
      if (Optional.ofNullable(dataObject).isPresent()) {
        rootObject.put(KEY_JSON_DATA, dataObject);
      }
    } catch (JSONException e) {
      // TIPS: 実体として例外は出力されない。ログ出力のみ実装するものとします。
      LOG.e(TAG, e.getMessage(), e);
    }

    switch (type) {
      case NEXT:
        // コールバックオブジェクト生成
        result = new PluginResult(PluginResult.Status.OK, rootObject);
        // コールバックは継続利用
        result.setKeepCallback(true);
        break;
      case COMPLETE:
        // コールバックオブジェクト生成
        result = new PluginResult(PluginResult.Status.OK, rootObject);
        // コールバック利用完了
        result.setKeepCallback(false);
        break;
      case ERROR:
        // コールバックオブジェクト生成
        result = new PluginResult(PluginResult.Status.ERROR, rootObject);
        // コールバック利用完了
        result.setKeepCallback(false);
        break;
      default:
        // サポート外
        throw new UnsupportedOperationException(
          String.format(MESSAGE_ERROR_CREATE_CALLBACK_OBJECT_000, type.value()));
    }

    return result;
  }

  /**
   * コールバック用オブジェクトを生成します。
   *
   * @param type    コールバック種別
   * @param message メッセージ
   * @param data    データ
   * @return コールバックオブジェクト
   */
  public static PluginResult createPluginResult(
    @NonNull final CallbackType type,
    final String message,
    @NonNull final Object data) {
    final String name = getCallbackId();
    final JSONObject rootObject = new JSONObject();
    return createPluginResult(type, name, message, rootObject, data);
  }

  /**
   * コールバック用オブジェクトを生成します。
   *
   * @param type    コールバック種別
   * @param message メッセージ
   * @return コールバックオブジェクト
   */
  public static PluginResult createPluginResult(
    @NonNull final CallbackType type,
    final String message) {
    final String name = getCallbackId();
    final JSONObject rootObject = new JSONObject();
    return createPluginResult(type, name, message, rootObject, null);
  }

  /**
   * 実行中のクラス名を取得します。
   * 3かな
   *
   * @return クラス名
   */
  private static String getClassName() {
    return Thread.currentThread().getStackTrace()[STACK_DEPTH].getClassName();
  }

  /**
   * 実行中のメソッド名を取得します。
   *
   * @return メソッド名
   */
  private static String getMethodName() {
    return Thread.currentThread().getStackTrace()[STACK_DEPTH].getMethodName();
  }

  /**
   * コールバックIDを取得します。
   *
   * @return クラス名#メソッド名の形式出力
   */
  private static String getCallbackId() {
    return String.format(CALLBACK_ID_FORMAT, getClassName(), getMethodName());
  }
}
