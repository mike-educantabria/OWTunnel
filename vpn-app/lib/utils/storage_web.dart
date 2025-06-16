import 'dart:html' as html;
import 'storage.dart';

class Storage extends StorageBase {
  static String? get token => html.window.localStorage['token'];
  static String? get userId => html.window.localStorage['userId'];

  static void saveToken(String token) {
    html.window.localStorage['token'] = token;
  }

  static void saveUserId(String userId) {
    html.window.localStorage['userId'] = userId;
  }

  static void clearToken() {
    html.window.localStorage.remove('token');
  }

  static void clearUserId() {
    html.window.localStorage.remove('userId');
  }
}