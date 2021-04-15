package net.kyosho.logging;

/**
 * コールバック種別
 * RXを参考に種別を決定しています。
 */
public enum CallbackType {
  /**
   * NEXT
   */
  NEXT("next"),
  /**
   * COMPLETE
   */
  COMPLETE("complete"),
  /**
   * ERROR
   */
  ERROR("error"),
  /**
   * DUMMY
   * テスト以外では利用してはいけない。
   */
  DUMMY("dummy");

  /**
   * コールバック種別
   */
  private final String type;

  /**
   * コンストラクタ
   *
   * @param type コールバック種別
   */
  CallbackType(final String type) {
    this.type = type;
  }

  /**
   * コールバック種別文字列を取得します。
   *
   * @return コールバック種別
   */
  public String value() {
    return this.type;
  }
}
