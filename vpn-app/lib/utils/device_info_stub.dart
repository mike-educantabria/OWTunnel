import 'package:device_info_plus/device_info_plus.dart';
import 'dart:io' show Platform;

import 'device_info.dart';

class DeviceInfoStub extends DeviceInfo {
  static Future<String> getUserAgent() async {
    final deviceInfoPlugin = DeviceInfoPlugin();

    if (Platform.isWindows) {
      final info = await deviceInfoPlugin.windowsInfo;
      return 'Windows ${info.computerName} (${info.csdVersion})';
    } else if (Platform.isAndroid) {
      final info = await deviceInfoPlugin.androidInfo;
      return 'Android ${info.model} (${info.version.sdkInt})';
    } else if (Platform.isIOS) {
      final info = await deviceInfoPlugin.iosInfo;
      return 'iOS ${info.utsname.machine} (${info.systemVersion})';
    } else {
      return 'Unknown device';
    }
  }
}
