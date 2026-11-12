import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:url_launcher/url_launcher.dart';
import '../controllers/auth_controller.dart';
import '../widgets/glass_container.dart';
import '../widgets/animated_button.dart';

class LoginView extends StatelessWidget {
  final emailController = TextEditingController();
  final passwordController = TextEditingController();
  final AuthController authController = Get.find<AuthController>();
  final _obscureText = true.obs;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
            colors: [Color(0xFF6A11CB), Color(0xFF2575FC)],
          ),
        ),
        child: SafeArea(
          child: Center(
            child: SingleChildScrollView(
              child: Padding(
                padding: const EdgeInsets.all(24.0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    // Logo animado
                    TweenAnimationBuilder(
                      tween: Tween<double>(begin: 0, end: 1),
                      duration: Duration(seconds: 1),
                      builder: (context, value, child) {
                        return Transform.scale(scale: value, child: child);
                      },
                      child: Container(
                        height: 120,
                        width: 120,
                        decoration: BoxDecoration(
                          color: Colors.white,
                          shape: BoxShape.circle,
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.1),
                              blurRadius: 20,
                              spreadRadius: 5,
                            ),
                          ],
                        ),
                        child: Icon(
                          Icons.lock_outline_rounded,
                          size: 50,
                          color: Color.fromRGBO(106, 17, 203, 1),
                        ),
                      ),
                    ),
                    SizedBox(height: 40),

                    // Formulário de login dentro de um container de vidro
                    GlassContainer(
                      child: Padding(
                        padding: const EdgeInsets.all(24.0),
                        child: Column(
                          children: [
                            Text(
                              'Bem-vindo',
                              style: TextStyle(
                                fontSize: 28,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                            ),
                            SizedBox(height: 24),

                            // Campo de email com animação
                            TextFormField(
                              controller: emailController,
                              decoration: InputDecoration(
                                labelText: 'Usuário',
                                prefixIcon: Icon(
                                  Icons.supervised_user_circle,
                                  color: Colors.white70,
                                ),
                                labelStyle: TextStyle(color: Colors.white70),
                                enabledBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(12),
                                  borderSide: BorderSide(color: Colors.white30),
                                ),
                                focusedBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.circular(12),
                                  borderSide: BorderSide(color: Colors.white),
                                ),
                                filled: true,
                                fillColor: Colors.white.withOpacity(0.1),
                              ),
                              style: TextStyle(color: Colors.white),
                              keyboardType: TextInputType.emailAddress,
                            ),
                            SizedBox(height: 16),

                            // Campo de senha com animação
                            Obx(() {
                              return TextFormField(
                                controller: passwordController,
                                obscureText: _obscureText.value,
                                decoration: InputDecoration(
                                  labelText: 'Senha',
                                  prefixIcon: Icon(
                                    Icons.lock,
                                    color: Colors.white70,
                                  ),
                                  suffixIcon: IconButton(
                                    icon: Icon(
                                      _obscureText.value
                                          ? Icons.visibility
                                          : Icons.visibility_off,
                                      color: Colors.white70,
                                    ),
                                    onPressed:
                                        () =>
                                            _obscureText.value =
                                                !_obscureText.value,
                                  ),
                                  labelStyle: TextStyle(color: Colors.white70),
                                  enabledBorder: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(12),
                                    borderSide: BorderSide(
                                      color: Colors.white30,
                                    ),
                                  ),
                                  focusedBorder: OutlineInputBorder(
                                    borderRadius: BorderRadius.circular(12),
                                    borderSide: BorderSide(color: Colors.white),
                                  ),
                                  filled: true,
                                  fillColor: Colors.white.withOpacity(0.1),
                                ),
                                style: TextStyle(color: Colors.white),
                              );
                            }),
                            SizedBox(height: 24),

                            // Botão de login animado
                            Obx(
                              () => AnimatedButton(
                                text:
                                    authController.isLoading.value
                                        ? 'Entrando...'
                                        : 'Entrar',
                                onPressed:
                                    authController.isLoading.value
                                        ? null
                                        : () => authController.login(
                                          emailController.text,
                                          passwordController.text,
                                        ),
                              ),
                            ),

                            // Mensagem de erro
                            Obx(
                              () =>
                                  authController.errorMessage.value.isNotEmpty
                                      ? Padding(
                                        padding: const EdgeInsets.only(
                                          top: 16.0,
                                        ),
                                        child: Text(
                                          authController.errorMessage.value,
                                          style: TextStyle(
                                            color: Colors.red[300],
                                            fontWeight: FontWeight.w500,
                                          ),
                                        ),
                                      )
                                      : SizedBox.shrink(),
                            ),
                            SizedBox(height: 24),
                            Text(
                              '1.0.8',
                              style: TextStyle(
                                fontSize: 14,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                            ),
                            SizedBox(height: 24),
                            Center(
                              child: RichText(
                                text: TextSpan(
                                  text: 'Termo de privacidade ',
                                  style: TextStyle(color: Colors.white),
                                  children: [
                                    TextSpan(
                                      text: 'acessar',
                                      style: TextStyle(
                                        color: Colors.white,
                                        fontWeight: FontWeight.bold,
                                        decoration: TextDecoration.underline,
                                      ),
                                      recognizer:
                                          TapGestureRecognizer()
                                            ..onTap = () {
                                              launchUrl(
                                                Uri.parse(
                                                  'https://termo-entregaja.vercel.app',
                                                ),
                                              );
                                            },
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
