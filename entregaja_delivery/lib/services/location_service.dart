import 'dart:async';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:get/get.dart';

class LocationService extends GetxService {
  StreamSubscription<Position>? _positionSub;
  final Rx<Position?> currentPosition = Rx<Position?>(null);
  final RxBool isTracking = false.obs;

  /// Callback chamado a cada atualização de posição.
  Function(double lat, double lng)? onLocationUpdate;

  // ─── Permissões ──────────────────────────────────────────────────────────

  /// Mostra diálogo explicando o uso antes de pedir permissão ao sistema.
  /// Atende o requisito do Google Play: "declaração em destaque".
  Future<bool> requestPermissionsWithConsent() async {
    final existing = await Geolocator.checkPermission();
    if (existing == LocationPermission.whileInUse ||
        existing == LocationPermission.always) {
      await _requestNotificationPermission();
      return true;
    }

    final confirmed = await Get.dialog<bool>(
      AlertDialog(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
        title: Row(
          children: [
            Icon(Icons.location_on, color: Colors.blue.shade700),
            const SizedBox(width: 8),
            const Flexible(child: Text('Permissão de Localização')),
          ],
        ),
        content: const Text(
          'O EntregaJá Delivery precisa acessar sua localização '
          'enquanto você realiza entregas para que o cliente possa '
          'acompanhar em tempo real onde está o seu pedido.\n\n'
          'A localização só é coletada durante uma entrega ativa '
          'e é exibida exclusivamente ao cliente daquela entrega.',
          style: TextStyle(fontSize: 14, height: 1.5),
        ),
        actionsPadding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
        actions: [
          TextButton(
            onPressed: () => Get.back(result: false),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () => Get.back(result: true),
            style: ElevatedButton.styleFrom(
              backgroundColor: Colors.blue.shade700,
              foregroundColor: Colors.white,
            ),
            child: const Text('Permitir'),
          ),
        ],
      ),
      barrierDismissible: false,
    );

    if (confirmed != true) return false;

    // Android 13+: pede permissão de notificação antes de pedir localização
    // (necessário para a notificação persistente do foreground service)
    await _requestNotificationPermission();

    final permission = await Geolocator.requestPermission();

    if (permission == LocationPermission.deniedForever) {
      Get.snackbar(
        'Permissão Negada',
        'Ative a permissão de localização nas configurações.',
        snackPosition: SnackPosition.BOTTOM,
        backgroundColor: Colors.red.shade600,
        colorText: Colors.white,
        duration: const Duration(seconds: 4),
        mainButton: TextButton(
          onPressed: openAppSettings,
          child: const Text('Abrir', style: TextStyle(color: Colors.white)),
        ),
      );
      return false;
    }

    return permission == LocationPermission.whileInUse ||
        permission == LocationPermission.always;
  }

  Future<void> _requestNotificationPermission() async {
    if (Platform.isAndroid) {
      final status = await Permission.notification.status;
      if (!status.isGranted) {
        await Permission.notification.request();
      }
    }
  }

  /// Verifica permissão e GPS antes de iniciar rastreamento.
  Future<bool> checkReady() async {
    final hasPermission = await requestPermissionsWithConsent();
    if (!hasPermission) return false;

    final serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) {
      Get.snackbar(
        'GPS Desativado',
        'Ative o GPS do seu dispositivo para continuar.',
        snackPosition: SnackPosition.BOTTOM,
      );
      return false;
    }

    return true;
  }

  // ─── Rastreamento ────────────────────────────────────────────────────────

  /// Inicia o rastreamento com foreground service (notificação persistente).
  /// O app continua rastreando mesmo minimizado.
  Future<void> startTracking() async {
    if (isTracking.value) {
      await stopTracking();
      await Future.delayed(const Duration(milliseconds: 300));
    }

    try {
      isTracking.value = true;

      // AndroidSettings com foregroundNotificationConfig ativa o foreground
      // service nativo do geolocator — sem lib extra, sem isolate separado.
      final locationSettings =
          Platform.isAndroid
              ? AndroidSettings(
                accuracy: LocationAccuracy.high,
                distanceFilter: 10,
                forceLocationManager: false,
                foregroundNotificationConfig:
                    const ForegroundNotificationConfig(
                      notificationText: 'Rastreando sua entrega em tempo real',
                      notificationTitle: 'EntregaJá — Entrega em andamento',
                      enableWakeLock: true,
                      notificationIcon: AndroidResource(
                        name: 'ic_launcher',
                        defType: 'mipmap',
                      ),
                    ),
              )
              : const LocationSettings(
                accuracy: LocationAccuracy.high,
                distanceFilter: 10,
              );

      _positionSub = Geolocator.getPositionStream(
        locationSettings: locationSettings,
      ).listen(
        (Position pos) {
          currentPosition.value = pos;
          onLocationUpdate?.call(pos.latitude, pos.longitude);
        },
        onError: (e) => debugPrint('❌ Erro GPS: $e'),
        cancelOnError: false,
      );

      debugPrint('✅ Rastreamento foreground iniciado');
    } catch (e) {
      debugPrint('❌ Erro ao iniciar rastreamento: $e');
      isTracking.value = false;
    }
  }

  /// Para o rastreamento e descarta o foreground service.
  Future<void> stopTracking() async {
    await _positionSub?.cancel();
    _positionSub = null;
    isTracking.value = false;
    currentPosition.value = null;
    onLocationUpdate = null;
    debugPrint('✅ Rastreamento parado');
  }

  /// Retorna a posição atual uma única vez (sem stream).
  Future<Position?> getCurrentPosition() async {
    try {
      return await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
        timeLimit: const Duration(seconds: 10),
      );
    } catch (_) {
      return null;
    }
  }

  @override
  Future<void> onClose() async {
    await stopTracking();
    super.onClose();
  }
}
