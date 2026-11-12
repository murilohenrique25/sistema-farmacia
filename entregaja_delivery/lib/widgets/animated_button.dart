import 'package:flutter/material.dart';

class AnimatedButton extends StatefulWidget {
  final String text;
  final VoidCallback? onPressed;

  const AnimatedButton({Key? key, required this.text, this.onPressed})
    : super(key: key);

  @override
  _AnimatedButtonState createState() => _AnimatedButtonState();
}

class _AnimatedButtonState extends State<AnimatedButton>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _scaleAnimation;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      vsync: this,
      duration: Duration(milliseconds: 300),
    );
    _scaleAnimation = Tween<double>(
      begin: 1.0,
      end: 0.95,
    ).animate(CurvedAnimation(parent: _controller, curve: Curves.easeInOut));
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTapDown: (_) => _controller.forward(),
      onTapUp: (_) => _controller.reverse(),
      onTapCancel: () => _controller.reverse(),
      onTap: widget.onPressed,
      child: AnimatedBuilder(
        animation: _scaleAnimation,
        builder:
            (context, child) => Transform.scale(
              scale: _scaleAnimation.value,
              child: Container(
                width: double.infinity,
                height: 56,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    colors:
                        widget.onPressed == null
                            ? [Colors.grey, Colors.grey.shade600]
                            : [Color(0xFFFF416C), Color(0xFFFF4B2B)],
                    begin: Alignment.centerLeft,
                    end: Alignment.centerRight,
                  ),
                  borderRadius: BorderRadius.circular(16),
                  boxShadow: [
                    BoxShadow(
                      color:
                          widget.onPressed == null
                              ? Colors.grey.withOpacity(0.3)
                              : Color(0xFFFF416C).withOpacity(0.3),
                      blurRadius: 10,
                      offset: Offset(0, 5),
                    ),
                  ],
                ),
                child: Center(
                  child: Text(
                    widget.text,
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
            ),
      ),
    );
  }
}
