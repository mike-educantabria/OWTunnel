abstract class StorageBase {
  static String? get token => null;
  static String? get userId => null;

  static void saveToken(String token) {}
  static void saveUserId(String userId) {}

  static void clearToken() {}
  static void clearUserId() {}
}