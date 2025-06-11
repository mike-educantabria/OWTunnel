import '/flutter_flow/flutter_flow_icon_button.dart';
import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import 'dart:ui';
import 'dart:convert';
import 'dart:html' as html;
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'account_page_model.dart';
export 'account_page_model.dart';

class AccountPageWidget extends StatefulWidget {
  const AccountPageWidget({super.key});

  static String routeName = 'account_page';
  static String routePath = '/accountPage';

  @override
  State<AccountPageWidget> createState() => _AccountPageWidgetState();
}

class _AccountPageWidgetState extends State<AccountPageWidget> {
  late AccountPageModel _model;
  final scaffoldKey = GlobalKey<ScaffoldState>();

  String? _originalEmail;

  @override
  void initState() {
    super.initState();
    _model = createModel(context, () => AccountPageModel());

    _model.textController1 ??= TextEditingController();
    _model.textFieldFocusNode1 ??= FocusNode();
    _model.textController2 ??= TextEditingController();
    _model.textFieldFocusNode2 ??= FocusNode();
    _model.textController3 ??= TextEditingController();
    _model.textFieldFocusNode3 ??= FocusNode();
    _model.textController4 ??= TextEditingController();
    _model.textFieldFocusNode4 ??= FocusNode();
    _model.textController5 ??= TextEditingController();
    _model.textFieldFocusNode5 ??= FocusNode();
    _model.textController6 ??= TextEditingController();
    _model.textFieldFocusNode6 ??= FocusNode();
    _model.textController7 ??= TextEditingController();
    _model.textFieldFocusNode7 ??= FocusNode();

    _fetchUserData();
  }

  Future<void> _fetchUserData() async {
    try {
      final token = html.window.localStorage['token'] ?? '';
      final userId = html.window.localStorage['userId'] ?? '';
      if (token.isEmpty || userId.isEmpty) return;

      final response = await http.get(
        Uri.parse('http://localhost:8080/api/v1/users/$userId'),
        headers: {'Authorization': 'Bearer $token'},
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        setState(() {
          _model.textController1?.text = data['email'] ?? '';
          _originalEmail = data['email'] ?? '';
          _model.textController2?.text = data['first_name'] ?? '';
          _model.textController3?.text = data['last_name'] ?? '';
          _model.textController4?.text = data['locale'] ?? '';
        });
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Could not load user data')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error loading user data: $e')),
      );
    }
  }

  Future<void> _updateUserData() async {
    final token = html.window.localStorage['token'] ?? '';
    final userId = html.window.localStorage['userId'] ?? '';
    if (token.isEmpty || userId.isEmpty) return;

    final newEmail = _model.textController1?.text.trim();

    final body = {
      "email": newEmail,
      "password": null,
      "firstName": _model.textController2?.text.trim(),
      "lastName": _model.textController3?.text.trim(),
      "locale": _model.textController4?.text.trim(),
      "role": null
    };

    try {
      final response = await http.put(
        Uri.parse('http://localhost:8080/api/v1/users/$userId'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode(body),
      );

      if (response.statusCode == 200) {
        // Check if email was changed using the original email
        if (_originalEmail != null && _originalEmail != newEmail) {
          html.window.localStorage.remove('token');
          html.window.localStorage.remove('userId');
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Email updated. Please log in again.')),
          );
          context.go('/loginPage');
          return;
        }
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('User updated successfully')),
        );
        _fetchUserData();
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to update user')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error updating user: $e')),
      );
    }
  }

  Future<void> _changePassword() async {
    final token = html.window.localStorage['token'] ?? '';
    final userId = html.window.localStorage['userId'] ?? '';
    final currentPassword = _model.textController5?.text ?? '';
    final newPassword = _model.textController6?.text ?? '';
    final confirmPassword = _model.textController7?.text ?? '';

    if (currentPassword.isEmpty ||
        newPassword.isEmpty ||
        confirmPassword.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('All password fields are required')),
      );
      return;
    }
    if (newPassword != confirmPassword) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('New passwords do not match')),
      );
      return;
    }
    // Verify current password by trying to login
    final loginResponse = await http.post(
      Uri.parse('http://localhost:8080/api/v1/auth/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'email': _model.textController1?.text.trim(),
        'password': currentPassword,
      }),
    );
    if (loginResponse.statusCode != 200) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Current password is incorrect')),
      );
      return;
    }

    // Update user with new password, keep other fields as is
    final body = {
      "email": _model.textController1?.text.trim(),
      "password": newPassword,
      "firstName": _model.textController2?.text.trim(),
      "lastName": _model.textController3?.text.trim(),
      "locale": _model.textController4?.text.trim(),
      "role": null
    };

    try {
      final response = await http.put(
        Uri.parse('http://localhost:8080/api/v1/users/$userId'),
        headers: {
          'Authorization': 'Bearer $token',
          'Content-Type': 'application/json',
        },
        body: jsonEncode(body),
      );

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Password updated successfully')),
        );
        _model.textController5?.clear();
        _model.textController6?.clear();
        _model.textController7?.clear();
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to update password')),
        );
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error updating password: $e')),
      );
    }
  }

  void _logout() {
    html.window.localStorage.remove('token');
    html.window.localStorage.remove('userId');
    context.go('/loginPage');
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
            'Account',
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
                  _buildPersonalInfoSection(context),
                  _buildChangePasswordSection(context),
                  _buildLogoutButton(context),
                ].divide(SizedBox(height: 12.0)),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildPersonalInfoSection(BuildContext context) {
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
          child: Padding(
            padding: EdgeInsets.all(16.0),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Icon(
                      Icons.person_rounded,
                      color: FlutterFlowTheme.of(context).primary,
                      size: 32.0,
                    ),
                    SizedBox(width: 8.0),
                    Text(
                      'Personal Information',
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
                  ],
                ),
                Divider(
                  thickness: 2.0,
                  color: FlutterFlowTheme.of(context).alternate,
                ),
                _buildTextField(
                  context,
                  label: 'Email address',
                  controller: _model.textController1,
                  focusNode: _model.textFieldFocusNode1,
                  hintText: 'N/A',
                  validator: _model.textController1Validator,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                _buildTextField(
                  context,
                  label: 'First name',
                  controller: _model.textController2,
                  focusNode: _model.textFieldFocusNode2,
                  hintText: 'N/A',
                  validator: _model.textController2Validator,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                _buildTextField(
                  context,
                  label: 'Last name',
                  controller: _model.textController3,
                  focusNode: _model.textFieldFocusNode3,
                  hintText: 'N/A',
                  validator: _model.textController3Validator,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                _buildTextField(
                  context,
                  label: 'Locale',
                  controller: _model.textController4,
                  focusNode: _model.textFieldFocusNode4,
                  hintText: 'N/A',
                  validator: _model.textController4Validator,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                FFButtonWidget(
                  onPressed: _updateUserData,
                  text: 'Apply changes',
                  options: FFButtonOptions(
                    width: double.infinity,
                    height: 46.0,
                    padding: EdgeInsets.all(8.0),
                    color: FlutterFlowTheme.of(context).primary,
                    textStyle: FlutterFlowTheme.of(context).titleSmall.override(
                          font: GoogleFonts.interTight(
                            fontWeight: FlutterFlowTheme.of(context)
                                .titleSmall
                                .fontWeight,
                            fontStyle: FlutterFlowTheme.of(context)
                                .titleSmall
                                .fontStyle,
                          ),
                          color: FlutterFlowTheme.of(context).info,
                          letterSpacing: 0.0,
                        ),
                    elevation: 2.0,
                  ),
                ),
              ].divide(SizedBox(height: 8.0)),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildChangePasswordSection(BuildContext context) {
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
          child: Padding(
            padding: EdgeInsets.all(16.0),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(
                  children: [
                    Icon(
                      Icons.lock_rounded,
                      color: FlutterFlowTheme.of(context).primary,
                      size: 28.0,
                    ),
                    SizedBox(width: 8.0),
                    Text(
                      'Change Password',
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
                  ],
                ),
                Divider(
                  thickness: 2.0,
                  color: FlutterFlowTheme.of(context).alternate,
                ),
                _buildTextField(
                  context,
                  label: 'Current password',
                  controller: _model.textController5,
                  focusNode: _model.textFieldFocusNode5,
                  hintText: 'Enter your password',
                  validator: _model.textController5Validator,
                  obscureText: true,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                _buildTextField(
                  context,
                  label: 'New password',
                  controller: _model.textController6,
                  focusNode: _model.textFieldFocusNode6,
                  hintText: 'Enter your new password',
                  validator: _model.textController6Validator,
                  obscureText: true,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                _buildTextField(
                  context,
                  label: 'Confirm new password',
                  controller: _model.textController7,
                  focusNode: _model.textFieldFocusNode7,
                  hintText: 'Enter your new password',
                  validator: _model.textController7Validator,
                  obscureText: true,
                ),
                Divider(
                    thickness: 1.0,
                    color: FlutterFlowTheme.of(context).alternate),
                FFButtonWidget(
                  onPressed: _changePassword,
                  text: 'Apply changes',
                  options: FFButtonOptions(
                    width: double.infinity,
                    height: 46.0,
                    padding: EdgeInsets.all(8.0),
                    color: FlutterFlowTheme.of(context).primary,
                    textStyle: FlutterFlowTheme.of(context).titleSmall.override(
                          font: GoogleFonts.interTight(
                            fontWeight: FlutterFlowTheme.of(context)
                                .titleSmall
                                .fontWeight,
                            fontStyle: FlutterFlowTheme.of(context)
                                .titleSmall
                                .fontStyle,
                          ),
                          color: FlutterFlowTheme.of(context).info,
                          letterSpacing: 0.0,
                        ),
                    elevation: 2.0,
                  ),
                ),
              ].divide(SizedBox(height: 8.0)),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildLogoutButton(BuildContext context) {
    return Align(
      alignment: AlignmentDirectional(0.0, 0.0),
      child: Padding(
        padding: EdgeInsetsDirectional.fromSTEB(16.0, 24.0, 16.0, 0.0),
        child: FFButtonWidget(
          onPressed: _logout,
          text: 'Log out',
          icon: Icon(
            Icons.logout_rounded,
            size: 24.0,
          ),
          options: FFButtonOptions(
            width: 800.0,
            height: 46.0,
            padding: EdgeInsets.all(8.0),
            iconPadding: EdgeInsetsDirectional.fromSTEB(0.0, 0.0, 0.0, 0.0),
            iconColor: FlutterFlowTheme.of(context).info,
            color: FlutterFlowTheme.of(context).error,
            textStyle: FlutterFlowTheme.of(context).titleMedium.override(
                  font: GoogleFonts.interTight(
                    fontWeight:
                        FlutterFlowTheme.of(context).titleMedium.fontWeight,
                    fontStyle:
                        FlutterFlowTheme.of(context).titleMedium.fontStyle,
                  ),
                  color: FlutterFlowTheme.of(context).info,
                  fontSize: 16.0,
                  letterSpacing: 0.0,
                ),
            elevation: 2.0,
          ),
        ),
      ),
    );
  }

  Widget _buildTextField(
    BuildContext context, {
    required String label,
    required TextEditingController? controller,
    required FocusNode? focusNode,
    required String hintText,
    required String? Function(BuildContext, String?)? validator,
    bool obscureText = false,
  }) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: EdgeInsetsDirectional.fromSTEB(4.0, 0.0, 4.0, 0.0),
          child: Text(
            label,
            style: FlutterFlowTheme.of(context).bodyMedium.override(
                  font: GoogleFonts.inter(
                    fontWeight: FontWeight.w600,
                    fontStyle:
                        FlutterFlowTheme.of(context).bodyMedium.fontStyle,
                  ),
                  letterSpacing: 0.0,
                ),
          ),
        ),
        TextFormField(
          controller: controller,
          focusNode: focusNode,
          autofocus: false,
          obscureText: obscureText,
          decoration: InputDecoration(
            hintText: hintText,
            hintStyle: FlutterFlowTheme.of(context).bodyMedium.override(
                  font: GoogleFonts.inter(
                    fontWeight:
                        FlutterFlowTheme.of(context).bodyMedium.fontWeight,
                    fontStyle:
                        FlutterFlowTheme.of(context).bodyMedium.fontStyle,
                  ),
                  color: FlutterFlowTheme.of(context).secondaryText,
                  letterSpacing: 0.0,
                ),
            enabledBorder: OutlineInputBorder(
              borderSide: BorderSide(
                color: Color(0x00000000),
                width: 1.0,
              ),
              borderRadius: BorderRadius.circular(8.0),
            ),
            focusedBorder: OutlineInputBorder(
              borderSide: BorderSide(
                color: Color(0x00000000),
                width: 1.0,
              ),
              borderRadius: BorderRadius.circular(8.0),
            ),
            errorBorder: OutlineInputBorder(
              borderSide: BorderSide(
                color: Color(0x00000000),
                width: 1.0,
              ),
              borderRadius: BorderRadius.circular(8.0),
            ),
            focusedErrorBorder: OutlineInputBorder(
              borderSide: BorderSide(
                color: Color(0x00000000),
                width: 1.0,
              ),
              borderRadius: BorderRadius.circular(8.0),
            ),
            filled: true,
            fillColor: FlutterFlowTheme.of(context).primaryBackground,
          ),
          style: FlutterFlowTheme.of(context).bodyMedium.override(
                font: GoogleFonts.inter(
                  fontWeight:
                      FlutterFlowTheme.of(context).bodyMedium.fontWeight,
                  fontStyle: FlutterFlowTheme.of(context).bodyMedium.fontStyle,
                ),
                letterSpacing: 0.0,
              ),
          validator:
              validator == null ? null : (value) => validator(context, value),
        ),
      ].divide(SizedBox(height: 8.0)),
    );
  }
}
