import 'package:o_w_tunnel/utils/storage.dart';

import '/flutter_flow/flutter_flow_icon_button.dart';
import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'dart:convert';
import '/utils/storage_selector.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';
import 'subscription_page_model.dart';
export 'subscription_page_model.dart';

class Plan {
  final int id;
  final String name;
  final String description;
  final double price;
  final String currency;
  final int durationDays;
  final bool isActive;

  Plan({
    required this.id,
    required this.name,
    required this.description,
    required this.price,
    required this.currency,
    required this.durationDays,
    required this.isActive,
  });

  factory Plan.fromJson(Map<String, dynamic> json) => Plan(
        id: json['id'],
        name: json['name'],
        description: json['description'] ?? '',
        price: (json['price'] as num).toDouble(),
        currency: json['currency'],
        durationDays: json['duration_days'],
        isActive: json['is_active'],
      );
}

class Subscription {
  final int id;
  final int userId;
  final int planId;
  final String status;
  final bool autoRenew;
  final String? expiresAt;

  Subscription({
    required this.id,
    required this.userId,
    required this.planId,
    required this.status,
    required this.autoRenew,
    this.expiresAt,
  });

  factory Subscription.fromJson(Map<String, dynamic> json) => Subscription(
        id: json['id'],
        userId: json['user_id'],
        planId: json['plan_id'],
        status: json['status'],
        autoRenew: json['auto_renew'],
        expiresAt: json['expires_at'],
      );
}

class SubscriptionPageWidget extends StatefulWidget {
  const SubscriptionPageWidget({super.key});

  static String routeName = 'subscription_page';
  static String routePath = '/subscriptionPage';

  @override
  State<SubscriptionPageWidget> createState() => _SubscriptionPageWidgetState();
}

class _SubscriptionPageWidgetState extends State<SubscriptionPageWidget> {
  late SubscriptionPageModel _model;
  final scaffoldKey = GlobalKey<ScaffoldState>();

  late Future<List<Plan>> _plansFuture;
  late Future<Subscription?> _currentSubscriptionFuture;

  @override
  void initState() {
    super.initState();
    _model = createModel(context, () => SubscriptionPageModel());
    _model.switchValue = false;
    _plansFuture = fetchPlans();
    _currentSubscriptionFuture = fetchCurrentSubscription();
  }

  @override
  void dispose() {
    _model.dispose();
    super.dispose();
  }

  Future<List<Plan>> fetchPlans() async {
    final token = Storage.token ?? '';
    final response = await http.get(
      Uri.parse('http://localhost:8080/api/v1/plans'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
    );
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data
          .map((json) => Plan.fromJson(json))
          .where((p) => p.isActive)
          .toList();
    }
    throw Exception('Error loading plans');
  }

  Future<Subscription?> fetchCurrentSubscription() async {
    final token = Storage.token ?? '';
    final userId = Storage.userId ?? '';
    if (token.isEmpty || userId.isEmpty) return null;
    final response = await http.get(
      Uri.parse('http://localhost:8080/api/v1/subscriptions'),
      headers: {'Authorization': 'Bearer $token'},
    );
    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      final userSubs = data
          .map((json) => Subscription.fromJson(json))
          .where((sub) => sub.userId.toString() == userId && sub.status == "ACTIVE")
          .toList();
      return userSubs.isNotEmpty ? userSubs.first : null;
    }
    return null;
  }

  Future<void> updateSubscription(Subscription sub, {String? status, bool? autoRenew}) async {
    final token = Storage.token ?? '';
    final body = jsonEncode({
      "userId": sub.userId,
      "planId": sub.planId,
      "status": status ?? sub.status,
      "autoRenew": autoRenew ?? sub.autoRenew,
      "expiresAt": sub.expiresAt,
    });
    final response = await http.put(
      Uri.parse('http://localhost:8080/api/v1/subscriptions/${sub.id}'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: body,
    );
    if (response.statusCode == 200) {
      setState(() {
        _currentSubscriptionFuture = fetchCurrentSubscription();
      });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error updating subscription')),
      );
    }
  }

  Future<void> subscribeToPlan(Plan plan) async {
    final token = Storage.token ?? '';
    final userId = Storage.userId ?? '';
    if (token.isEmpty || userId.isEmpty) return;
    // Calcular expiresAt sumando durationDays a la fecha actual
    final now = DateTime.now();
    final expiresAt = now.add(Duration(days: plan.durationDays));
    final body = jsonEncode({
      "userId": int.parse(userId),
      "planId": plan.id,
      "status": "ACTIVE",
      "autoRenew": false,
      "expiresAt": expiresAt.toIso8601String(),
    });
    final response = await http.post(
      Uri.parse('http://localhost:8080/api/v1/subscriptions'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $token',
      },
      body: body,
    );
    if (response.statusCode == 201 || response.statusCode == 200) {
      setState(() {
        _currentSubscriptionFuture = fetchCurrentSubscription();
      });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error subscribing to plan')),
      );
    }
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
            'Subscription',
            style: FlutterFlowTheme.of(context).headlineSmall.override(
                  font: GoogleFonts.interTight(
                    fontWeight:
                        FlutterFlowTheme.of(context).headlineSmall.fontWeight,
                    fontStyle:
                        FlutterFlowTheme.of(context).headlineSmall.fontStyle,
                  ),
                  letterSpacing: 0.0,
                  fontWeight:
                      FlutterFlowTheme.of(context).headlineSmall.fontWeight,
                  fontStyle:
                      FlutterFlowTheme.of(context).headlineSmall.fontStyle,
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
              child: FutureBuilder<List<Plan>>(
                future: _plansFuture,
                builder: (context, plansSnapshot) {
                  if (plansSnapshot.connectionState == ConnectionState.waiting) {
                    return Center(child: CircularProgressIndicator());
                  }
                  if (plansSnapshot.hasError) {
                    return Center(child: Text('Error loading plans'));
                  }
                  final plans = plansSnapshot.data ?? [];
                  return FutureBuilder<Subscription?>(
                    future: _currentSubscriptionFuture,
                    builder: (context, subSnapshot) {
                      if (subSnapshot.connectionState == ConnectionState.waiting) {
                        return Center(child: CircularProgressIndicator());
                      }
                      if (subSnapshot.hasError) {
                        return Center(child: Text('Error loading subscription'));
                      }
                      final subscription = subSnapshot.data;
                      Plan? currentPlan;
                      if (subscription != null) {
                        if (plans.isNotEmpty) {
                          currentPlan = plans.firstWhere(
                            (plan) => plan.id == subscription.planId,
                            orElse: () => plans.first,
                          );
                        } else {
                          currentPlan = null;
                        }
                      }
                      return Column(
                        mainAxisSize: MainAxisSize.max,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          _buildCurrentPlan(context, currentPlan, subscription),
                          _buildAvailablePlans(context, plans, currentPlan, subscription),
                        ].divide(SizedBox(height: 12.0)),
                      );
                    },
                  );
                },
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildCurrentPlan(BuildContext context, Plan? plan, Subscription? subscription) {
    if (plan == null || subscription == null) {
      return SizedBox.shrink();
    }
    return Align(
      alignment: AlignmentDirectional(0.0, 0.0),
      child: Padding(
        padding: EdgeInsetsDirectional.fromSTEB(0.0, 16.0, 0.0, 0.0),
        child: Material(
          color: Colors.transparent,
          elevation: 2.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12.0),
          ),
          child: Container(
            width: 800.0,
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
                  Row(
                    mainAxisSize: MainAxisSize.max,
                    children: [
                      Icon(
                        Icons.sell,
                        color: FlutterFlowTheme.of(context).primary,
                        size: 32.0,
                      ),
                      SizedBox(width: 8.0),
                      Text(
                        'Current Plan',
                        style:
                            FlutterFlowTheme.of(context).titleMedium.override(
                                  font: GoogleFonts.interTight(
                                    fontWeight: FlutterFlowTheme.of(context)
                                        .titleMedium
                                        .fontWeight,
                                    fontStyle: FlutterFlowTheme.of(context)
                                        .titleMedium
                                        .fontStyle,
                                  ),
                                  letterSpacing: 0.0,
                                  fontWeight: FlutterFlowTheme.of(context)
                                      .titleMedium
                                      .fontWeight,
                                  fontStyle: FlutterFlowTheme.of(context)
                                      .titleMedium
                                      .fontStyle,
                                ),
                      ),
                    ],
                  ),
                  Divider(
                    thickness: 2.0,
                    color: FlutterFlowTheme.of(context).alternate,
                  ),
                  Padding(
                    padding: EdgeInsetsDirectional.fromSTEB(0.0, 0.0, 0.0, 6.0),
                    child: Container(
                      width: double.infinity,
                      height: 150.0,
                      decoration: BoxDecoration(
                        color: FlutterFlowTheme.of(context).accent4,
                        borderRadius: BorderRadius.circular(8.0),
                        border: Border.all(
                          color: FlutterFlowTheme.of(context).secondaryText,
                          width: 1.0,
                        ),
                      ),
                      child: Padding(
                        padding: EdgeInsets.all(16.0),
                        child: Column(
                          mainAxisSize: MainAxisSize.min,
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text(
                                  plan.name,
                                  style: FlutterFlowTheme.of(context)
                                      .titleMedium
                                      .override(
                                        font: GoogleFonts.interTight(
                                          fontWeight:
                                              FlutterFlowTheme.of(context)
                                                  .titleMedium
                                                  .fontWeight,
                                          fontStyle:
                                              FlutterFlowTheme.of(context)
                                                  .titleMedium
                                                  .fontStyle,
                                        ),
                                        color: FlutterFlowTheme.of(context)
                                            .primaryText,
                                        letterSpacing: 0.0,
                                      ),
                                ),
                              ],
                            ),
                            Text(
                              subscription.expiresAt != null
                                  ? 'Your subscription is active until ${DateFormat('d MMMM yyyy').format(DateTime.parse(subscription.expiresAt!))}.'
                                  : 'Your subscription is active.',
                              style: FlutterFlowTheme.of(context)
                                  .bodyMedium
                                  .override(
                                    font: GoogleFonts.inter(
                                      fontWeight: FlutterFlowTheme.of(context)
                                          .bodyMedium
                                          .fontWeight,
                                      fontStyle: FlutterFlowTheme.of(context)
                                          .bodyMedium
                                          .fontStyle,
                                    ),
                                    color: FlutterFlowTheme.of(context)
                                        .secondaryText,
                                    letterSpacing: 0.0,
                                  ),
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.spaceBetween,
                              children: [
                                Text(
                                  '${plan.price.toStringAsFixed(2)} ${plan.currency} / ${plan.durationDays} days',
                                  style: FlutterFlowTheme.of(context)
                                      .bodyLarge
                                      .override(
                                        font: GoogleFonts.inter(
                                          fontWeight:
                                              FlutterFlowTheme.of(context)
                                                  .bodyLarge
                                                  .fontWeight,
                                          fontStyle:
                                              FlutterFlowTheme.of(context)
                                                  .bodyLarge
                                                  .fontStyle,
                                        ),
                                        color: FlutterFlowTheme.of(context)
                                            .primaryText,
                                        letterSpacing: 0.0,
                                      ),
                                ),
                                Container(
                                  width: 100.0,
                                  height: 40.0,
                                  decoration: BoxDecoration(
                                    color: FlutterFlowTheme.of(context).primary,
                                    borderRadius: BorderRadius.circular(8.0),
                                  ),
                                  child: Align(
                                    alignment: AlignmentDirectional(0.0, 0.0),
                                    child: Padding(
                                      padding:
                                          EdgeInsets.symmetric(horizontal: 8.0),
                                      child: Text(
                                        'Active',
                                        style: FlutterFlowTheme.of(context)
                                            .labelSmall
                                            .override(
                                              font: GoogleFonts.inter(
                                                fontWeight: FontWeight.w600,
                                                fontStyle:
                                                    FlutterFlowTheme.of(context)
                                                        .labelSmall
                                                        .fontStyle,
                                              ),
                                              color:
                                                  FlutterFlowTheme.of(context)
                                                      .info,
                                              fontSize: 13.0,
                                              letterSpacing: 0.0,
                                            ),
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ].divide(SizedBox(height: 8.0)),
                        ),
                      ),
                    ),
                  ),
                  Padding(
                    padding:
                        EdgeInsetsDirectional.fromSTEB(16.0, 0.0, 16.0, 0.0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Row(
                          children: [
                            Text(
                              'Auto-renew',
                              style: FlutterFlowTheme.of(context)
                                  .bodyMedium
                                  .override(
                                    font: GoogleFonts.inter(
                                      fontWeight: FlutterFlowTheme.of(context)
                                          .bodyMedium
                                          .fontWeight,
                                      fontStyle: FlutterFlowTheme.of(context)
                                          .bodyMedium
                                          .fontStyle,
                                    ),
                                    color: FlutterFlowTheme.of(context)
                                        .primaryText,
                                    letterSpacing: 0.0,
                                  ),
                            ),
                            Switch(
                              value: subscription.autoRenew,
                              onChanged: (value) {
                                updateSubscription(subscription, autoRenew: value);
                              },
                              activeColor: FlutterFlowTheme.of(context).primary,
                              activeTrackColor:
                                  FlutterFlowTheme.of(context).alternate,
                              inactiveTrackColor:
                                  FlutterFlowTheme.of(context).alternate,
                              inactiveThumbColor:
                                  FlutterFlowTheme.of(context).accent1,
                            ),
                          ].divide(SizedBox(width: 8.0)),
                        ),
                        FFButtonWidget(
                          onPressed: () {
                            updateSubscription(subscription, status: "CANCELLED");
                          },
                          text: 'Cancel',
                          options: FFButtonOptions(
                            width: 100.0,
                            height: 40.0,
                            padding: EdgeInsets.all(8.0),
                            color: FlutterFlowTheme.of(context).error,
                            textStyle: FlutterFlowTheme.of(context)
                                .labelSmall
                                .override(
                                  font: GoogleFonts.inter(
                                    fontWeight: FontWeight.w600,
                                    fontStyle: FlutterFlowTheme.of(context)
                                        .labelSmall
                                        .fontStyle,
                                  ),
                                  color: FlutterFlowTheme.of(context).info,
                                  letterSpacing: 0.0,
                                ),
                            elevation: 0.0,
                            borderRadius: BorderRadius.circular(8.0),
                          ),
                        ),
                      ],
                    ),
                  ),
                ].divide(SizedBox(height: 8.0)),
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildAvailablePlans(
      BuildContext context, List<Plan> plans, Plan? currentPlan, Subscription? subscription) {
    final availablePlans =
        plans.where((plan) => plan.id != currentPlan?.id).toList();
    final hasActiveSubscription = currentPlan != null && subscription != null;
    return Align(
      alignment: AlignmentDirectional(0.0, 0.0),
      child: Padding(
        padding: EdgeInsetsDirectional.fromSTEB(0.0, 16.0, 0.0, 0.0),
        child: Material(
          color: Colors.transparent,
          elevation: 2.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12.0),
          ),
          child: Container(
            width: 800.0,
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
                  Row(
                    children: [
                      Icon(
                        Icons.sell,
                        color: FlutterFlowTheme.of(context).primary,
                        size: 32.0,
                      ),
                      SizedBox(width: 8.0),
                      Text(
                        'Available Plans',
                        style:
                            FlutterFlowTheme.of(context).titleMedium.override(
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
                  ...availablePlans.map((plan) => Padding(
                        padding: const EdgeInsets.only(bottom: 8.0),
                        child: _buildPlanCard(
                          context,
                          title: plan.name,
                          description: plan.description,
                          price:
                              '${plan.price.toStringAsFixed(2)} ${plan.currency} / ${plan.durationDays} days',
                          buttonColor: FlutterFlowTheme.of(context).primary,
                          buttonTextColor: FlutterFlowTheme.of(context).info,
                          onPressed: hasActiveSubscription
                              ? null
                              : () {
                                  subscribeToPlan(plan);
                                },
                        ),
                      )),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildPlanCard(
    BuildContext context, {
    required String title,
    required String description,
    required String price,
    required Color buttonColor,
    required Color buttonTextColor,
    required VoidCallback? onPressed,
  }) {
    return Container(
      width: double.infinity,
      height: 150.0,
      decoration: BoxDecoration(
        color: FlutterFlowTheme.of(context).accent4,
        borderRadius: BorderRadius.circular(8.0),
        border: Border.all(
          color: FlutterFlowTheme.of(context).secondaryText,
          width: 1.0,
        ),
      ),
      child: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
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
                        color: FlutterFlowTheme.of(context).primaryText,
                        letterSpacing: 0.0,
                      ),
                ),
              ],
            ),
            Text(
              description,
              style: FlutterFlowTheme.of(context).bodyMedium.override(
                    font: GoogleFonts.inter(
                      fontWeight:
                          FlutterFlowTheme.of(context).bodyMedium.fontWeight,
                      fontStyle:
                          FlutterFlowTheme.of(context).bodyMedium.fontStyle,
                    ),
                    color: FlutterFlowTheme.of(context).secondaryText,
                    letterSpacing: 0.0,
                  ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  price,
                  style: FlutterFlowTheme.of(context).bodyLarge.override(
                        font: GoogleFonts.inter(
                          fontWeight:
                              FlutterFlowTheme.of(context).bodyLarge.fontWeight,
                          fontStyle:
                              FlutterFlowTheme.of(context).bodyLarge.fontStyle,
                        ),
                        color: FlutterFlowTheme.of(context).primaryText,
                        letterSpacing: 0.0,
                      ),
                ),
                FFButtonWidget(
                  onPressed: onPressed,
                  text: 'Subscribe',
                  options: FFButtonOptions(
                    width: 100.0,
                    height: 40.0,
                    padding: EdgeInsets.all(8.0),
                    color: buttonColor,
                    textStyle: FlutterFlowTheme.of(context).labelSmall.override(
                          font: GoogleFonts.inter(
                            fontWeight: FontWeight.w600,
                            fontStyle: FlutterFlowTheme.of(context)
                                .labelSmall
                                .fontStyle,
                          ),
                          color: buttonTextColor,
                          fontSize: 13.0,
                          letterSpacing: 0.0,
                        ),
                    elevation: 0.0,
                    borderRadius: BorderRadius.circular(8.0),
                    disabledColor: Colors.grey,
                  ),
                ),
              ],
            ),
          ].divide(SizedBox(height: 8.0)),
        ),
      ),
    );
  }
}
