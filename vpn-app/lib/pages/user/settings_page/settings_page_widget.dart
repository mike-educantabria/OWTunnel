import '/flutter_flow/flutter_flow_drop_down.dart';
import '/flutter_flow/flutter_flow_icon_button.dart';
import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import '/flutter_flow/form_field_controller.dart';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'settings_page_model.dart';
import '../../../main.dart';
export 'settings_page_model.dart';

class SettingsPageWidget extends StatefulWidget {
  const SettingsPageWidget({super.key});

  static String routeName = 'settings_page';
  static String routePath = '/settingsPage';

  @override
  State<SettingsPageWidget> createState() => _SettingsPageWidgetState();
}

class _SettingsPageWidgetState extends State<SettingsPageWidget> {
  late SettingsPageModel _model;
  final scaffoldKey = GlobalKey<ScaffoldState>();

  bool _appearanceExpanded = true;
  bool _notificationsExpanded = true;
  bool _privacyExpanded = true;
  bool _languageExpanded = true;
  bool _aboutExpanded = true;

  @override
  void initState() {
    super.initState();
    _model = createModel(context, () => SettingsPageModel());
    _model.switchValue1 = true;
    _model.switchValue2 = false;
    _model.switchValue3 = false;
    _model.switchValue4 = false;
  }

  @override
  void dispose() {
    _model.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        FocusScope.of(context).unfocus();
        FocusManager.instance.primaryFocus?.unfocus();
      },
      child: Scaffold(
        key: scaffoldKey,
        backgroundColor: FlutterFlowTheme.of(context).primaryBackground,
        appBar: AppBar(
          backgroundColor: FlutterFlowTheme.of(context).secondaryBackground,
          automaticallyImplyLeading: false,
          leading: FlutterFlowIconButton(
            buttonSize: 60.0,
            icon: Icon(
              Icons.arrow_back_rounded,
              color: FlutterFlowTheme.of(context).primaryText,
              size: 30.0,
            ),
            onPressed: () {
              context.go('/dashboardPage');
            },
          ),
          title: Text(
            'Settings',
            style: FlutterFlowTheme.of(context).headlineSmall.override(
                  font: GoogleFonts.interTight(
                    fontWeight:
                        FlutterFlowTheme.of(context).headlineSmall.fontWeight,
                    fontStyle:
                        FlutterFlowTheme.of(context).headlineSmall.fontStyle,
                  ),
                  letterSpacing: 0.0,
                ),
          ),
          centerTitle: true,
          elevation: 0.0,
        ),
        body: SafeArea(
          top: true,
          child: Padding(
            padding: EdgeInsets.all(16.0),
            child: SingleChildScrollView(
              child: Column(
                mainAxisSize: MainAxisSize.max,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _buildExpandableSection(
                    context,
                    expanded: _appearanceExpanded,
                    onTap: () {
                      setState(() {
                        _appearanceExpanded = !_appearanceExpanded;
                      });
                    },
                    title: 'Appearance',
                    icon: Icons.brightness_6_outlined,
                    child: _buildAppearanceSection(context),
                  ),
                  _buildExpandableSection(
                    context,
                    expanded: _notificationsExpanded,
                    onTap: () {
                      setState(() {
                        _notificationsExpanded = !_notificationsExpanded;
                      });
                    },
                    title: 'Notifications',
                    icon: Icons.notifications_outlined,
                    child: _buildNotificationsSection(context),
                  ),
                  _buildExpandableSection(
                    context,
                    expanded: _privacyExpanded,
                    onTap: () {
                      setState(() {
                        _privacyExpanded = !_privacyExpanded;
                      });
                    },
                    title: 'Privacy & Security',
                    icon: Icons.fingerprint,
                    child: _buildPrivacySection(context),
                  ),
                  _buildExpandableSection(
                    context,
                    expanded: _languageExpanded,
                    onTap: () {
                      setState(() {
                        _languageExpanded = !_languageExpanded;
                      });
                    },
                    title: 'Language',
                    icon: Icons.translate,
                    child: _buildLanguageSection(context),
                  ),
                  _buildExpandableSection(
                    context,
                    expanded: _aboutExpanded,
                    onTap: () {
                      setState(() {
                        _aboutExpanded = !_aboutExpanded;
                      });
                    },
                    title: 'About & Support',
                    icon: Icons.info_outline,
                    child: _buildAboutSection(context),
                  ),
                ].divide(SizedBox(height: 12.0)),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildExpandableSection(
    BuildContext context, {
    required bool expanded,
    required VoidCallback onTap,
    required String title,
    required IconData icon,
    required Widget child,
  }) {
    return Align(
      alignment: AlignmentDirectional(0.0, 0.0),
      child: Padding(
        padding: EdgeInsetsDirectional.fromSTEB(0.0, 16.0, 0.0, 0.0),
        child: Container(
          width: 800.0,
          decoration: BoxDecoration(
            color: FlutterFlowTheme.of(context).secondaryBackground,
            borderRadius: BorderRadius.circular(12.0),
            border: Border.all(
              color: FlutterFlowTheme.of(context).alternate,
              width: 1.0,
            ),
          ),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              InkWell(
                onTap: onTap,
                borderRadius: BorderRadius.vertical(top: Radius.circular(12.0)),
                child: Padding(
                  padding: EdgeInsets.all(16.0),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        title,
                        style: FlutterFlowTheme.of(context).titleMedium.override(
                              font: GoogleFonts.interTight(
                                fontWeight: FlutterFlowTheme.of(context)
                                    .titleMedium
                                    .fontWeight,
                                fontStyle: FlutterFlowTheme.of(context)
                                    .titleMedium
                                    .fontStyle,
                              ),
                              letterSpacing: 0.0,
                            ),
                      ),
                      Icon(
                        expanded
                            ? Icons.keyboard_arrow_up_rounded
                            : Icons.keyboard_arrow_down_rounded,
                        color: FlutterFlowTheme.of(context).primaryText,
                        size: 28.0,
                      ),
                    ],
                  ),
                ),
              ),
              if (expanded) ...[
                Divider(
                  thickness: 2.0,
                  color: FlutterFlowTheme.of(context).alternate,
                  height: 0,
                ),
                Padding(
                  padding: EdgeInsets.all(16.0),
                  child: child,
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildAppearanceSection(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Row(
          children: [
            Icon(Icons.brightness_6_outlined,
                color: FlutterFlowTheme.of(context).primaryText, size: 24.0),
            SizedBox(width: 8.0),
            Text(
              'Theme Mode',
              style: FlutterFlowTheme.of(context).bodyMedium,
            ),
          ],
        ),
        FlutterFlowDropDown<String>(
          controller: _model.dropDownValueController1 ??=
              FormFieldController<String>(null),
          options: ['Light', 'Dark', 'System'],
          onChanged: (val) {
            safeSetState(() => _model.dropDownValue1 = val);
            if (val == 'Light') {
              MyApp.of(context).setThemeMode(ThemeMode.light);
            } else if (val == 'Dark') {
              MyApp.of(context).setThemeMode(ThemeMode.dark);
            } else {
              MyApp.of(context).setThemeMode(ThemeMode.system);
            }
          },
          width: 140.0,
          height: 40.0,
          textStyle: FlutterFlowTheme.of(context).bodyMedium,
          hintText: 'System',
          fillColor: FlutterFlowTheme.of(context).alternate,
          elevation: 0.0,
          borderColor: Colors.transparent,
          borderWidth: 0.0,
          borderRadius: 8.0,
          margin: EdgeInsetsDirectional.fromSTEB(12.0, 0.0, 0.0, 0.0),
          hidesUnderline: true,
          isOverButton: false,
          isSearchable: false,
          isMultiSelect: false,
        ),
      ],
    );
  }

  Widget _buildNotificationsSection(BuildContext context) {
    return Column(
      children: [
        _buildSwitchRow(
          context,
          icon: Icons.notifications_outlined,
          title: 'Push Notifications',
          subtitle: 'Receive notifications on your device.',
          value: _model.switchValue1!,
          onChanged: (val) => setState(() => _model.switchValue1 = val),
        ),
        Divider(thickness: 1.0, color: FlutterFlowTheme.of(context).alternate),
        _buildSwitchRow(
          context,
          icon: Icons.email_outlined,
          title: 'Email Notifications',
          subtitle: 'Receive updates via email.',
          value: _model.switchValue2!,
          onChanged: (val) => setState(() => _model.switchValue2 = val),
        ),
      ],
    );
  }

  Widget _buildPrivacySection(BuildContext context) {
    return Column(
      children: [
        _buildSwitchRow(
          context,
          icon: Icons.fingerprint,
          title: 'Biometric Auth',
          subtitle: 'Use fingerprint or face ID to sign in.',
          value: _model.switchValue3!,
          onChanged: (val) => setState(() => _model.switchValue3 = val),
        ),
        Divider(thickness: 1.0, color: FlutterFlowTheme.of(context).alternate),
        _buildSwitchRow(
          context,
          icon: Icons.data_usage,
          title: 'Data Collection',
          subtitle: 'Allow anonymous usage statistics.',
          value: _model.switchValue4!,
          onChanged: (val) => setState(() => _model.switchValue4 = val),
        ),
      ],
    );
  }

  Widget _buildLanguageSection(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Row(
          children: [
            Icon(Icons.translate,
                color: FlutterFlowTheme.of(context).primaryText, size: 24.0),
            SizedBox(width: 8.0),
            Text(
              'App Language',
              style: FlutterFlowTheme.of(context).bodyMedium,
            ),
          ],
        ),
        FlutterFlowDropDown<String>(
          controller: _model.dropDownValueController2 ??=
              FormFieldController<String>(null),
          options: [
            'English (US)',
            'Spanish',
            'French',
            'German',
            'Chinese'
          ],
          onChanged: (val) =>
              safeSetState(() => _model.dropDownValue2 = val),
          width: 140.0,
          height: 40.0,
          textStyle: FlutterFlowTheme.of(context).bodyMedium,
          hintText: 'English (US)',
          fillColor: FlutterFlowTheme.of(context).alternate,
          elevation: 0.0,
          borderColor: Colors.transparent,
          borderWidth: 0.0,
          borderRadius: 8.0,
          margin: EdgeInsetsDirectional.fromSTEB(12.0, 0.0, 0.0, 0.0),
          hidesUnderline: true,
          isSearchable: false,
          isMultiSelect: false,
        ),
      ],
    );
  }

  Widget _buildAboutSection(BuildContext context) {
    return Column(
      children: [
        _buildLinkRow(
          context,
          icon: Icons.privacy_tip_outlined,
          title: 'Privacy Policy',
        ),
        Divider(thickness: 1.0, color: FlutterFlowTheme.of(context).alternate),
        _buildLinkRow(
          context,
          icon: Icons.description_outlined,
          title: 'Terms of Service',
        ),
        Divider(thickness: 1.0, color: FlutterFlowTheme.of(context).alternate),
        _buildLinkRow(
          context,
          icon: Icons.support_agent,
          title: 'Contact Support',
        ),
        Divider(thickness: 1.0, color: FlutterFlowTheme.of(context).alternate),
        Row(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Row(
              children: [
                Icon(Icons.info_outline,
                    color: FlutterFlowTheme.of(context).primaryText,
                    size: 24.0),
                SizedBox(width: 8.0),
                Text(
                  'App Version',
                  style: FlutterFlowTheme.of(context).bodyMedium,
                ),
              ],
            ),
            Padding(
              padding: EdgeInsetsDirectional.fromSTEB(0.0, 0.0, 4.0, 0.0),
              child: Text(
                'v0.1.0-alpha',
                style: FlutterFlowTheme.of(context).bodySmall.override(
                      font: GoogleFonts.inter(
                        fontWeight:
                            FlutterFlowTheme.of(context).bodySmall.fontWeight,
                        fontStyle:
                            FlutterFlowTheme.of(context).bodySmall.fontStyle,
                      ),
                      color: FlutterFlowTheme.of(context).secondaryText,
                      letterSpacing: 1.0,
                    ),
              ),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildSwitchRow(
    BuildContext context, {
    required IconData icon,
    required String title,
    required String subtitle,
    required bool value,
    required ValueChanged<bool> onChanged,
  }) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon,
                    color: FlutterFlowTheme.of(context).primaryText,
                    size: 24.0),
                SizedBox(width: 8.0),
                Text(
                  title,
                  style: FlutterFlowTheme.of(context).bodyMedium,
                ),
              ],
            ),
            Text(
              subtitle,
              style: FlutterFlowTheme.of(context).labelSmall.override(
                    font: GoogleFonts.inter(
                      fontWeight:
                          FlutterFlowTheme.of(context).labelSmall.fontWeight,
                      fontStyle:
                          FlutterFlowTheme.of(context).labelSmall.fontStyle,
                    ),
                    color: FlutterFlowTheme.of(context).secondaryText,
                  ),
            ),
          ].divide(SizedBox(height: 4.0)),
        ),
        Switch(
          value: value,
          onChanged: onChanged,
          activeColor: FlutterFlowTheme.of(context).primary,
          activeTrackColor: FlutterFlowTheme.of(context).alternate,
          inactiveTrackColor: FlutterFlowTheme.of(context).alternate,
          inactiveThumbColor: FlutterFlowTheme.of(context).accent1,
        ),
      ],
    );
  }

  Widget _buildLinkRow(
    BuildContext context, {
    required IconData icon,
    required String title,
  }) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Row(
          children: [
            Icon(icon,
                color: FlutterFlowTheme.of(context).primaryText, size: 24.0),
            SizedBox(width: 8.0),
            Text(
              title,
              style: FlutterFlowTheme.of(context).bodyMedium,
            ),
          ],
        ),
        Icon(
          Icons.chevron_right_rounded,
          color: FlutterFlowTheme.of(context).primaryText,
          size: 24.0,
        ),
      ],
    );
  }
}