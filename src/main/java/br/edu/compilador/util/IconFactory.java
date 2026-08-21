package br.edu.compilador.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public final class IconFactory {

    private static final int SIZE = 24;

    private IconFactory() {
    }

    public static Icon novo() {
        return createIcon(g -> {
            g.setColor(Color.WHITE);
            g.fillRect(4, 2, 16, 20);
            g.setColor(Color.GRAY);
            g.drawRect(4, 2, 16, 20);
            g.setColor(new Color(255, 204, 0));
            g.fillOval(14, 2, 8, 8);
        });
    }

    public static Icon abrir() {
        return createIcon(g -> {
            g.setColor(new Color(255, 153, 51));
            g.fillRoundRect(2, 6, 20, 14, 4, 4);
            g.setColor(new Color(204, 102, 0));
            g.drawRoundRect(2, 6, 20, 14, 4, 4);
            g.setColor(new Color(255, 204, 102));
            g.fillRoundRect(4, 8, 16, 4, 2, 2);
        });
    }

    public static Icon salvar() {
        return createIcon(g -> {
            g.setColor(new Color(70, 130, 180));
            g.fillRect(6, 2, 12, 20);
            g.setColor(Color.WHITE);
            g.fillRect(8, 2, 8, 6);
            g.setColor(new Color(220, 220, 220));
            g.fillRect(8, 10, 8, 10);
            g.setColor(Color.DARK_GRAY);
            g.drawRect(6, 2, 12, 20);
        });
    }

    public static Icon copiar() {
        return createIcon(g -> {
            g.setColor(new Color(230, 230, 230));
            g.fillRect(8, 4, 12, 16);
            g.setColor(new Color(200, 200, 200));
            g.fillRect(4, 8, 12, 16);
            g.setColor(Color.GRAY);
            g.drawRect(8, 4, 12, 16);
            g.drawRect(4, 8, 12, 16);
        });
    }

    public static Icon colar() {
        return createIcon(g -> {
            g.setColor(new Color(160, 160, 160));
            g.fillRoundRect(8, 2, 8, 6, 2, 2);
            g.setColor(new Color(240, 240, 240));
            g.fillRect(4, 8, 16, 14);
            g.setColor(Color.GRAY);
            g.drawRoundRect(8, 2, 8, 6, 2, 2);
            g.drawRect(4, 8, 16, 14);
        });
    }

    public static Icon recortar() {
        return createIcon(g -> {
            g.setStroke(new BasicStroke(2f));
            g.setColor(Color.RED);
            g.drawLine(4, 4, 10, 10);
            g.drawLine(20, 4, 14, 10);
            g.drawLine(4, 20, 10, 14);
            g.drawLine(20, 20, 14, 14);
            g.setColor(Color.DARK_GRAY);
            g.drawLine(8, 12, 16, 12);
        });
    }

    public static Icon compilar() {
        return createIcon(g -> {
            g.setColor(new Color(34, 139, 34));
            int[] xPoints = {8, 20, 8};
            int[] yPoints = {4, 12, 20};
            g.fillPolygon(xPoints, yPoints, 3);
        });
    }

    public static Icon equipe() {
        return createIcon(g -> {
            g.setColor(new Color(255, 204, 0));
            g.fillOval(4, 4, 16, 16);
            g.setColor(Color.BLACK);
            g.fillOval(9, 10, 2, 2);
            g.fillOval(13, 10, 2, 2);
            g.drawArc(8, 12, 8, 6, 200, 140);
        });
    }

    private static Icon createIcon(DrawCommand command) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        command.draw(graphics);
        graphics.dispose();
        return new ImageIcon(image);
    }

    @FunctionalInterface
    private interface DrawCommand {
        void draw(Graphics2D graphics);
    }
}
