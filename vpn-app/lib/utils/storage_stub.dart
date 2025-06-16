import 'storage.dart';

class Storage extends StorageBase {
  static String? _token;
  static String? _userId;

  static String? get token => _token;
  static String? get userId => _userId;

  static void saveToken(String token) {
    _token = token;
    print('Token guardado (stub): $token');
  }

  static void saveUserId(String userId) {
    _userId = userId;
    print('UserId guardado (stub): $userId');
  }

  static void clearToken() {
    _token = null;
  }

  static void clearUserId() {
    _userId = null;
  }
}