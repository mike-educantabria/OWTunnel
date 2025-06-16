import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'dart:convert';
import 'dart:html' as html;
import 'package:http/http.dart' as http;
import 'dashboard_page_model.dart';
export 'dashboard_page_model.dart';

class VpnServer {
  final int id;
  final String country;
  final String city;
  final String hostname;
  final String ipAddress;
  final String configFileUrl;
  final bool isFree;
  final bool isActive;

  VpnServer({
    required this.id,
    required this.country,
    required this.city,
    required this.hostname,
    required this.ipAddress,
    required this.configFileUrl,
    required this.isFree,
    required this.isActive,
  });

  factory VpnServer.fromJson(Map<String, dynamic> json) => VpnServer(
        id: json['id'],
        country: json['country'],
        city: json['city'],
        hostname: json['hostname'],
        ipAddress: json['ip_address'],
        configFileUrl: json['config_file_url'],
        isFree: json['is_free'],
        isActive: json['is_active'],
      );
}

class Subscription {
  final int id;
  final int userId;
  final int planId;
  final String status;
  final bool autoRenew;

  Subscription({
    required this.id,
    required this.userId,
    required this.planId,
    required this.status,
    required this.autoRenew,
  });

  factory Subscription.fromJson(Map<String, dynamic> json) => Subscription(
        id: json['id'],
        userId: json['user_id'],
        planId: json['plan_id'],
        status: json['status'],
        autoRenew: json['auto_renew'],
      );
}

class DashboardPageWidget extends StatefulWidget {
  const DashboardPageWidget({super.key});
  static String routeName = 'dashboard_page';
  static String routePath = '/dashboardPage';

  @override
  State<DashboardPageWidget> createState() => _DashboardPageWidgetState();
}

class _DashboardPageWidgetState extends State<DashboardPageWidget> {
  late DashboardPageModel _model;
  final scaffoldKey = GlobalKey<ScaffoldState>();
  late Future<List<VpnServer>> _vpnServersFuture;
  late Future<Subscription?> _subscriptionFuture;
  VpnServer? _connectedServer;
  int? _currentConnectionId;

  @override
  void initState() {
    super.initState();
    _model = createModel(context, () => DashboardPageModel());
    _vpnServersFuture = fetchVpnServers();
    _subscriptionFuture = fetchUserSubscription();
  }

  @override
  void dispose() {
    _model.dispose();
    super.dispose();
  }

  Future<List<VpnServer>> fetchVpnServers() async {
    final response = await http.get(
      Uri.parse('http://localhost:8080/api/v1/vpn-servers'),
      headers: {'Content-Type': 'application/json'},
    );
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data
          .map((json) => VpnServer.fromJson(json))
          .where((server) => server.isActive)
          .toList();
    }
    throw Exception('Error loading VPN servers');
  }

  Future<Subscription?> fetchUserSubscription() async {
    final token = html.window.localStorage['token'] ?? '';
    final userId = html.window.localStorage['userId'] ?? '';
    if (token.isEmpty || userId.isEmpty) return null;
    final response = await http.get(
      Uri.parse('http://localhost:8080/api/v1/subscriptions'),
      headers: {'Authorization': 'Bearer $token'},
    );
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      final userSubs = data
          .map((json) => Subscription.fromJson(json))
          .where((sub) =>
              sub.userId.toString() == userId &&
              sub.status.toUpperCase() == 'ACTIVE')
          .toList();
      return userSubs.isNotEmpty ? userSubs.first : null;
    }
    return null;
  }

  Future<int?> _createConnection({
    required VpnServer server,
    required String status,
  }) async {
    final token = html.window.localStorage['token'] ?? '';
    final userId = html.window.localStorage['userId'] ?? '';
    if (token.isEmpty || userId.isEmpty) return null;

    final deviceInfo = html.window.navigator.userAgent;

    final body = jsonEncode({
      "userId": int.tryParse(userId),
      "vpnServerId": server.id,
      "deviceInfo": deviceInfo,
      "status": status,
    });

    final response = await http.post(
      Uri.parse('http://localhost:8080/api/v1/connections'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: body,
    );

    if (response.statusCode == 201) {
      final data = json.decode(response.body);
      return data['id'] as int?;
    }
    return null;
  }

  Future<void> _updateConnectionStatus({
    required int connectionId,
    required VpnServer server,
    required String status,
  }) async {
    final token = html.window.localStorage['token'] ?? '';
    final userId = html.window.localStorage['userId'] ?? '';
    if (token.isEmpty || userId.isEmpty) return;

    final deviceInfo = html.window.navigator.userAgent;

    final body = jsonEncode({
      "userId": int.tryParse(userId),
      "vpnServerId": server.id,
      "deviceInfo": deviceInfo,
      "status": status,
    });

    await http.put(
      Uri.parse('http://localhost:8080/api/v1/connections/$connectionId'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: body,
    );
  }

void _connectToServer(VpnServer server, bool canConnect) async {
  if (!canConnect) return;

  final previousServer = _connectedServer;
  final previousConnectionId = _currentConnectionId;
  if (previousServer != null && previousConnectionId != null) {
    await _updateConnectionStatus(
      connectionId: previousConnectionId,
      server: previousServer,
      status: "DISCONNECTED",
    );
  }

  setState(() {
    _connectedServer = server;
    _currentConnectionId = null;
  });

  final connectionId = await _createConnection(server: server, status: "CONNECTED");
  setState(() => _currentConnectionId = connectionId);
}

  void _disconnect() async {
    final server = _connectedServer;
    final connectionId = _currentConnectionId;
    setState(() {
      _connectedServer = null;
      _currentConnectionId = null;
    });
    if (server != null && connectionId != null) {
      await _updateConnectionStatus(
        connectionId: connectionId,
        server: server,
        status: "DISCONNECTED",
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final isConnected = _connectedServer != null;
    return GestureDetector(
      onTap: () {
        FocusScope.of(context).unfocus();
        FocusManager.instance.primaryFocus?.unfocus();
      },
      child: Scaffold(
        key: scaffoldKey,
        backgroundColor: FlutterFlowTheme.of(context).primaryBackground,
        body: SafeArea(
          top: true,
          child: FutureBuilder<Subscription?>(
            future: _subscriptionFuture,
            builder: (context, subSnapshot) {
              final hasSubscription =
                  subSnapshot.hasData && subSnapshot.data != null;
              return Center(
                child: SingleChildScrollView(
                  child: ConstrainedBox(
                    constraints: BoxConstraints(maxWidth: 800),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        _buildConnectionStatus(context, isConnected),
                        if (!hasSubscription)
                          _buildNoSubscriptionBanner(context),
                        _buildServerList(context, hasSubscription),
                        SizedBox(height: 24),
                      ].divide(SizedBox(height: 18.0)),
                    ),
                  ),
                ),
              );
            },
          ),
        ),
        bottomNavigationBar: _buildBottomNavBar(context),
      ),
    );
  }

  Widget _buildConnectionStatus(BuildContext context, bool isConnected) {
    return Align(
      alignment: AlignmentDirectional(0.0, 0.0),
      child: Padding(
        padding: EdgeInsetsDirectional.fromSTEB(16.0, 0.0, 16.0, 0.0),
        child: MouseRegion(
          cursor:
              isConnected ? SystemMouseCursors.click : SystemMouseCursors.basic,
          child: GestureDetector(
            onTap: isConnected ? _disconnect : null,
            child: Container(
              width: 220.0,
              height: 220.0,
              decoration: BoxDecoration(
                color: FlutterFlowTheme.of(context).secondaryBackground,
                shape: BoxShape.circle,
              ),
              child: Align(
                alignment: AlignmentDirectional(0.0, 0.0),
                child: Padding(
                  padding: EdgeInsets.all(16.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Padding(
                        padding: EdgeInsetsDirectional.fromSTEB(
                            16.0, 0.0, 16.0, 0.0),
                        child: Material(
                          color: Colors.transparent,
                          elevation: 3.0,
                          shape: const CircleBorder(),
                          child: Container(
                            width: 100.0,
                            height: 100.0,
                            decoration: BoxDecoration(
                              color: FlutterFlowTheme.of(context)
                                  .primaryBackground,
                              shape: BoxShape.circle,
                              border: Border.all(
                                color: FlutterFlowTheme.of(context)
                                    .primaryBackground,
                                width: 2.0,
                              ),
                            ),
                            child: Icon(
                              Icons.security,
                              color: isConnected
                                  ? Colors.green
                                  : FlutterFlowTheme.of(context).primary,
                              size: 60.0,
                            ),
                          ),
                        ),
                      ),
                      Padding(
                        padding: EdgeInsetsDirectional.fromSTEB(
                            16.0, 0.0, 16.0, 0.0),
                        child: Text(
                          isConnected ? 'Connected' : 'Disconnected',
                          style: FlutterFlowTheme.of(context)
                              .headlineSmall
                              .override(
                                font: GoogleFonts.interTight(
                                  fontWeight: FlutterFlowTheme.of(context)
                                      .headlineSmall
                                      .fontWeight,
                                  fontStyle: FlutterFlowTheme.of(context)
                                      .headlineSmall
                                      .fontStyle,
                                ),
                                color: isConnected
                                    ? Colors.green
                                    : FlutterFlowTheme.of(context).error,
                                fontSize: 18.0,
                                letterSpacing: 0.0,
                                fontWeight: FlutterFlowTheme.of(context)
                                    .headlineSmall
                                    .fontWeight,
                                fontStyle: FlutterFlowTheme.of(context)
                                    .headlineSmall
                                    .fontStyle,
                              ),
                        ),
                      ),
                    ].divide(SizedBox(height: 16.0)),
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildNoSubscriptionBanner(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16.0),
      child: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          color: FlutterFlowTheme.of(context).error.withOpacity(0.1),
          borderRadius: BorderRadius.circular(8.0),
        ),
        padding: EdgeInsets.all(16.0),
        child: Row(
          children: [
            Icon(Icons.warning, color: FlutterFlowTheme.of(context).error),
            SizedBox(width: 8),
            Expanded(
              child: Text(
                'You do not have an active subscription. Please subscribe to connect to servers.',
                style: FlutterFlowTheme.of(context).bodyMedium.override(
                      font: GoogleFonts.inter(
                        fontWeight: FontWeight.w600,
                      ),
                      color: FlutterFlowTheme.of(context).error,
                    ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildServerList(BuildContext context, bool hasSubscription) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 0),
      child: Container(
        width: double.infinity,
        constraints: BoxConstraints(maxWidth: 800),
        decoration: BoxDecoration(
          color: FlutterFlowTheme.of(context).secondaryBackground,
          borderRadius: BorderRadius.circular(12.0),
        ),
        child: Padding(
          padding: EdgeInsetsDirectional.fromSTEB(16.0, 16.0, 16.0, 16.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Available Locations',
                style: FlutterFlowTheme.of(context).headlineSmall.override(
                      font: GoogleFonts.interTight(
                        fontWeight: FlutterFlowTheme.of(context)
                            .headlineSmall
                            .fontWeight,
                        fontStyle: FlutterFlowTheme.of(context)
                            .headlineSmall
                            .fontStyle,
                      ),
                      color: FlutterFlowTheme.of(context).primaryText,
                      letterSpacing: 0.0,
                      fontWeight:
                          FlutterFlowTheme.of(context).headlineSmall.fontWeight,
                      fontStyle:
                          FlutterFlowTheme.of(context).headlineSmall.fontStyle,
                    ),
              ),
              Text(
                'Select a country to connect',
                style: FlutterFlowTheme.of(context).bodyMedium.override(
                      font: GoogleFonts.inter(
                        fontWeight:
                            FlutterFlowTheme.of(context).bodyMedium.fontWeight,
                        fontStyle:
                            FlutterFlowTheme.of(context).bodyMedium.fontStyle,
                      ),
                      color: FlutterFlowTheme.of(context).secondaryText,
                      letterSpacing: 0.0,
                      fontWeight:
                          FlutterFlowTheme.of(context).bodyMedium.fontWeight,
                      fontStyle:
                          FlutterFlowTheme.of(context).bodyMedium.fontStyle,
                    ),
              ),
              FutureBuilder<List<VpnServer>>(
                future: _vpnServersFuture,
                builder: (context, snapshot) {
                  if (snapshot.connectionState == ConnectionState.waiting) {
                    return Padding(
                      padding: const EdgeInsets.all(32.0),
                      child: Center(child: CircularProgressIndicator()),
                    );
                  } else if (snapshot.hasError) {
                    return Padding(
                      padding: const EdgeInsets.all(32.0),
                      child: Center(child: Text('Error loading servers')),
                    );
                  } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
                    return Padding(
                      padding: const EdgeInsets.all(32.0),
                      child: Center(child: Text('No servers available')),
                    );
                  }
                  final servers = snapshot.data!;
                  return ListView.builder(
                    shrinkWrap: true,
                    physics: NeverScrollableScrollPhysics(),
                    itemCount: servers.length,
                    itemBuilder: (context, index) {
                      final server = servers[index];
                      final isSelected = _connectedServer?.id == server.id;
                      final canConnect = hasSubscription ? true : server.isFree;
                      return MouseRegion(
                        cursor: canConnect
                            ? SystemMouseCursors.click
                            : SystemMouseCursors.forbidden,
                        child: GestureDetector(
                          onTap: () {
                            if (canConnect) {
                              _connectToServer(server, true);
                            }
                          },
                          child: _buildLocationTile(
                            context,
                            server,
                            isSelected: isSelected,
                            canConnect: canConnect,
                          ),
                        ),
                      );
                    },
                  );
                },
              ),
            ].divide(SizedBox(height: 12.0)),
          ),
        ),
      ),
    );
  }

  Widget _buildLocationTile(
    BuildContext context,
    VpnServer server, {
    bool isSelected = false,
    bool canConnect = true,
  }) {
    return Opacity(
      opacity: canConnect ? 1.0 : 0.5,
      child: Container(
        width: double.infinity,
        decoration: BoxDecoration(
          color: isSelected
              ? Colors.green.withOpacity(0.15)
              : FlutterFlowTheme.of(context).secondaryBackground,
          borderRadius: BorderRadius.circular(8.0),
        ),
        child: Padding(
          padding: EdgeInsetsDirectional.fromSTEB(12.0, 12.0, 12.0, 12.0),
          child: Row(
            mainAxisSize: MainAxisSize.max,
            children: [
              Container(
                width: 48.0,
                height: 48.0,
                decoration: BoxDecoration(
                  color: FlutterFlowTheme.of(context).primaryBackground,
                  borderRadius: BorderRadius.circular(24.0),
                ),
                child: Icon(
                  server.isFree ? Icons.public : Icons.lock,
                  color: server.isFree
                      ? FlutterFlowTheme.of(context).primary
                      : Colors.amber[800],
                  size: 24.0,
                ),
              ),
              Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Expanded(
                          child: Text(
                            server.country,
                            overflow: TextOverflow.ellipsis,
                            style:
                                FlutterFlowTheme.of(context).bodyLarge.override(
                                      font: GoogleFonts.inter(
                                        fontWeight: FlutterFlowTheme.of(context)
                                            .bodyLarge
                                            .fontWeight,
                                        fontStyle: FlutterFlowTheme.of(context)
                                            .bodyLarge
                                            .fontStyle,
                                      ),
                                      letterSpacing: 0.0,
                                      fontWeight: FlutterFlowTheme.of(context)
                                          .bodyLarge
                                          .fontWeight,
                                      fontStyle: FlutterFlowTheme.of(context)
                                          .bodyLarge
                                          .fontStyle,
                                    ),
                          ),
                        ),
                        SizedBox(width: 8),
                        Container(
                          padding:
                              EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                          decoration: BoxDecoration(
                            color: server.isFree
                                ? Colors.green.withOpacity(0.2)
                                : Colors.amber.withOpacity(0.2),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            server.isFree ? 'Free' : 'Premium',
                            style: TextStyle(
                              color: server.isFree
                                  ? Colors.green[800]
                                  : Colors.amber[900],
                              fontWeight: FontWeight.bold,
                              fontSize: 12,
                            ),
                          ),
                        ),
                      ],
                    ),
                    Text(
                      server.city,
                      style: FlutterFlowTheme.of(context).labelMedium.override(
                            font: GoogleFonts.inter(
                              fontWeight: FlutterFlowTheme.of(context)
                                  .labelMedium
                                  .fontWeight,
                              fontStyle: FlutterFlowTheme.of(context)
                                  .labelMedium
                                  .fontStyle,
                            ),
                            color: FlutterFlowTheme.of(context).secondaryText,
                            letterSpacing: 0.0,
                            fontWeight: FlutterFlowTheme.of(context)
                                .labelMedium
                                .fontWeight,
                            fontStyle: FlutterFlowTheme.of(context)
                                .labelMedium
                                .fontStyle,
                          ),
                    ),
                  ],
                ),
              ),
              Icon(
                Icons.arrow_forward_ios,
                color: FlutterFlowTheme.of(context).secondaryText,
                size: 20.0,
              ),
            ].divide(SizedBox(width: 12.0)),
          ),
        ),
      ),
    );
  }

  Widget _buildNavButton(
    BuildContext context, {
    required IconData icon,
    required String label,
    required Color color,
    required VoidCallback onTap,
    bool isActive = false,
  }) {
    return MouseRegion(
      cursor: SystemMouseCursors.click,
      child: GestureDetector(
        onTap: onTap,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Icon(
              icon,
              color: color,
              size: 24.0,
            ),
            Text(
              label,
              style: FlutterFlowTheme.of(context).bodySmall.override(
                    font: GoogleFonts.inter(
                      fontWeight:
                          FlutterFlowTheme.of(context).bodySmall.fontWeight,
                      fontStyle:
                          FlutterFlowTheme.of(context).bodySmall.fontStyle,
                    ),
                    color: color,
                    letterSpacing: 0.0,
                    fontWeight:
                        FlutterFlowTheme.of(context).bodySmall.fontWeight,
                    fontStyle: FlutterFlowTheme.of(context).bodySmall.fontStyle,
                  ),
            ),
          ].divide(SizedBox(height: 4.0)),
        ),
      ),
    );
  }

  void _navigateTo(String route) {
    context.go(route);
  }

  Widget _buildBottomNavBar(BuildContext context) {
    return Container(
      width: double.infinity,
      height: 80.0,
      decoration: BoxDecoration(
        color: FlutterFlowTheme.of(context).secondaryBackground,
        borderRadius: BorderRadius.vertical(top: Radius.circular(12.0)),
      ),
      child: Padding(
        padding: EdgeInsetsDirectional.fromSTEB(16.0, 16.0, 16.0, 16.0),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          mainAxisAlignment: MainAxisAlignment.spaceAround,
          children: [
            _buildNavButton(
              context,
              icon: Icons.dashboard,
              label: 'Dashboard',
              color: FlutterFlowTheme.of(context).primary,
              onTap: () {},
              isActive: true,
            ),
            _buildNavButton(
              context,
              icon: Icons.subscriptions,
              label: 'Subscription',
              color: FlutterFlowTheme.of(context).secondaryText,
              onTap: () => _navigateTo('/subscriptionPage'),
            ),
            _buildNavButton(
              context,
              icon: Icons.account_circle,
              label: 'Account',
              color: FlutterFlowTheme.of(context).secondaryText,
              onTap: () => _navigateTo('/accountPage'),
            ),
            _buildNavButton(
              context,
              icon: Icons.settings,
              label: 'Settings',
              color: FlutterFlowTheme.of(context).secondaryText,
              onTap: () => _navigateTo('/settingsPage'),
            ),
          ].divide(SizedBox(width: 12.0)),
        ),
      ),
    );
  }
}
