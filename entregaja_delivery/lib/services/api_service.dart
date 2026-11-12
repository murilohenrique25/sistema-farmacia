import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:entregaja_delivery/models/entrega_avulsa.dart';
import 'package:entregaja_delivery/models/entrega_model.dart';
import 'package:entregaja_delivery/models/storage_service.dart';
import 'package:flutter/foundation.dart';
import 'package:geolocator/geolocator.dart';
import 'package:get/get.dart';

class ApiService {
  final Dio _dio = Dio(
    BaseOptions(
      baseUrl: 'https://tomcat.vps6912.panel.icontainer.net:8443/control-api',
      connectTimeout: Duration(seconds: 10),
      receiveTimeout: Duration(seconds: 10),
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
      },
    ),
  );

  Future<Map<String, dynamic>> finalizarEntrega(EntregaModel entrega) async {
    Position? position;
    try {
      position = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
        timeLimit: const Duration(seconds: 10),
      );
    } catch (_) {}
    final lat = position?.latitude.toString() ?? '0';
    final long = position?.longitude.toString() ?? '0';
    final data = {
      'idEntrega': entrega.idEntrega,
      'observacaoEntregador': entrega.observacaoEntregador,
      'latitude': lat,
      'longitude': long,
    };
    try {
      final response = await _dio.put(
        '/api/app/v1/entrega/finalizar',
        data: data,
      );
      // Aceita qualquer 2xx como sucesso (200 OK ou 204 No Content)
      final status = response.statusCode ?? 0;
      return {'success': status >= 200 && status < 300};
    } on DioException catch (e) {
      final status = e.response?.statusCode ?? 0;
      // 2xx com body vazio pode chegar como DioException em alguns casos
      if (status >= 200 && status < 300) {
        return {'success': true};
      }
      final msg =
          e.response?.data is Map
              ? e.response?.data['message']
              : e.response?.data?.toString();
      return {'success': false, 'message': msg ?? 'Erro na requisição'};
    } catch (_) {
      return {'success': false, 'message': 'Erro desconhecido'};
    }
  }

  Future<Map<String, dynamic>> login(String email, String password) async {
    try {
      final response = await _dio.get(
        '/api/app/v1/login?user_id=$email&password=$password',
      );
      final data = response.data;

      return {'success': response.statusCode == 200, 'dados': data ?? {}};
    } catch (e) {
      if (e is DioException) {
        return {
          'success': false,
          'message': e.response?.data['message'] ?? 'Erro na requisição',
        };
      }
      return {'success': false, 'message': 'Erro desconhecido'};
    }
  }

  /// Busca só os dados de resumo do entregador (sem precisar de senha)
  Future<Map<String, dynamic>?> getDadosEntregador(String userId) async {
    try {
      final response = await _dio.get(
        '/api/app/v1/login/dados?user_id=$userId',
      );
      if (response.statusCode == 200) {
        return response.data as Map<String, dynamic>;
      }
      return null;
    } catch (_) {
      return null;
    }
  }

  Future<List<Map<String, int>>> getData() async {
    final dados = Get.find<StorageService>().getUsuarioLogado();

    try {
      final response = await _dio.get(
        '/api/app/v1/entrega/quantidade_tipo/${dados?.usuario}',
      );

      final List<dynamic> rawList = response.data;

      final List<Map<String, int>> quantidadeEntregasDisponiveisPorTipo =
          rawList.map<Map<String, int>>((item) {
            final tipo = item['tipo'] as String;

            final quantidadeValue = item['quantidade'];
            int quantidade;

            if (quantidadeValue is int) {
              quantidade = quantidadeValue;
            } else if (quantidadeValue is String) {
              quantidade = int.tryParse(quantidadeValue) ?? 0;
            } else if (quantidadeValue is double) {
              quantidade = quantidadeValue.toInt();
            } else {
              quantidade = 0;
            }

            return {tipo: quantidade};
          }).toList();

      return quantidadeEntregasDisponiveisPorTipo;
    } catch (e) {
      return [];
    }
  }

  Future<Map<String, dynamic>> getEntregas(String tipoAcao) async {
    try {
      final dados = Get.find<StorageService>().getUsuarioLogado();
      final response = await _dio.get(
        '/api/app/v1/entrega/tipo/$tipoAcao/${dados?.usuario}',
      );

      final data = response.data;

      return {'success': true, 'data': data};
    } catch (e) {
      return {
        'success': false,
        'data': [],
        'message': 'Falha ao buscar entregas',
      };
    }
  }

  Future<Map<String, dynamic>> selecionarEntregas(
    List<EntregaModel> entregas,
  ) async {
    try {
      final dados = Get.find<StorageService>().getUsuarioLogado();
      final response = await _dio.put(
        '/api/app/v1/entrega/selecionar/${dados?.usuario}',
        data: jsonEncode(entregas),
      );

      return {
        'success': response.statusCode == 200,
        'message': 'Entregas selecionadas com sucesso',
      };
    } catch (e) {
      return {
        'success': false,
        'data': [],
        'message': 'Falha ao selecionar entregas',
      };
    }
  }

  Future<Map<String, dynamic>> iniciarEntregas(
    List<int> idsEntrega,
    String latitude,
    String longitude,
  ) async {
    try {
      final dados = Get.find<StorageService>().getUsuarioLogado();
      final response = await _dio.put(
        '/api/app/v1/entrega/iniciar/${dados?.usuario}',
        data: jsonEncode({
          'idsEntrega': idsEntrega,
          'latitude': latitude,
          'longitude': longitude,
        }),
      );

      return {
        'success': response.statusCode == 200,
        'message': 'Entregas iniciadas com sucesso',
      };
    } catch (e) {
      if (e is DioException) {
        return {
          'success': false,
          'message': e.response?.data['message'] ?? 'Erro ao iniciar entregas',
        };
      }
      return {'success': false, 'message': 'Erro desconhecido'};
    }
  }

  Future<Map<String, dynamic>> getEntregasSelecionadas() async {
    try {
      final dados = Get.find<StorageService>().getUsuarioLogado();
      final response = await _dio.get(
        '/api/app/v1/entrega/entregador/${dados?.usuario}',
      );

      final data = response.data;
      debugPrint('[ApiService] getEntregasSelecionadas: $data');
      return {'success': true, 'data': data};
    } catch (e) {
      return {
        'success': false,
        'data': [],
        'message': 'Falha ao buscar entregas selecionadas',
      };
    }
  }

  Future<Map<String, dynamic>> inserirEntregaAvulsa(
    EntregaAvulsa entregaAvulsa,
  ) async {
    try {
      final dados = Get.find<StorageService>().getUsuarioLogado();
      final response = await _dio.post(
        '/api/app/v1/entrega/avulsa/${dados?.usuario}',
        data: jsonEncode(entregaAvulsa),
      );

      final data = response.data;

      return {'success': true, 'data': data};
    } catch (e) {
      return {
        'success': false,
        'data': [],
        'message': 'Falha ao enviar serviço',
      };
    }
  }
}
