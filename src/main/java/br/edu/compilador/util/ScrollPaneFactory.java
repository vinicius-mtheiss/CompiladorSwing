package br.edu.compilador.util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

/**
 * Cria {@link JScrollPane} com barras vertical e horizontal sempre visiveis,
 * mesmo quando o conteudo esta vazio (requisito do enunciado).
 */
public final class ScrollPaneFactory {

    private ScrollPaneFactory() {
    }

    public static JScrollPane createAlwaysVisibleScrollPane(JComponent view) {
        ScrollableViewWrapper wrapper = new ScrollableViewWrapper(view);
        AlwaysVisibleScrollPane scrollPane = new AlwaysVisibleScrollPane(wrapper);

        scrollPane.getViewport().addChangeListener(event -> wrapper.refreshScrollableSize());
        scrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                wrapper.refreshScrollableSize();
            }

            @Override
            public void componentShown(ComponentEvent event) {
                wrapper.refreshScrollableSize();
            }
        });

        if (view instanceof JTextComponent textComponent) {
            textComponent.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent event) {
                    wrapper.refreshScrollableSize();
                }

                @Override
                public void removeUpdate(DocumentEvent event) {
                    wrapper.refreshScrollableSize();
                }

                @Override
                public void changedUpdate(DocumentEvent event) {
                    wrapper.refreshScrollableSize();
                }
            });
        }

        SwingUtilities.invokeLater(wrapper::refreshScrollableSize);
        return scrollPane;
    }

    private static final class AlwaysVisibleScrollPane extends JScrollPane {

        AlwaysVisibleScrollPane(JComponent view) {
            super(view, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
            getVerticalScrollBar().setBlockIncrement(16);
            getHorizontalScrollBar().setBlockIncrement(16);
        }

        @Override
        public void doLayout() {
            super.doLayout();
            forceScrollBarVisible(getVerticalScrollBar());
            forceScrollBarVisible(getHorizontalScrollBar());
        }

        private void forceScrollBarVisible(JScrollBar scrollBar) {
            scrollBar.setVisible(true);
        }
    }

    /**
     * Envolve o componente editavel e garante que seu tamanho preferido seja
     * ligeiramente maior que a viewport, forcando as barras a permanecerem visiveis.
     */
    private static final class ScrollableViewWrapper extends JPanel implements Scrollable {

        private final JComponent content;

        ScrollableViewWrapper(JComponent content) {
            super(new BorderLayout());
            this.content = content;
            setOpaque(false);
            add(content, BorderLayout.CENTER);
        }

        void refreshScrollableSize() {
            revalidate();
            repaint();
            Container parent = getParent();
            if (parent != null) {
                parent.revalidate();
                parent.repaint();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension contentSize = content.getPreferredSize();
            JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, this);
            if (viewport != null) {
                Dimension extent = viewport.getExtentSize();
                if (extent.width > 0 && extent.height > 0) {
                    return new Dimension(
                            Math.max(contentSize.width, extent.width + 1),
                            Math.max(contentSize.height, extent.height + 1));
                }
            }
            return new Dimension(
                    Math.max(contentSize.width, 1),
                    Math.max(contentSize.height, 1));
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return content.getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (content instanceof Scrollable scrollable) {
                return scrollable.getScrollableUnitIncrement(visibleRect, orientation, direction);
            }
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            if (content instanceof Scrollable scrollable) {
                return scrollable.getScrollableBlockIncrement(visibleRect, orientation, direction);
            }
            return 16;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
