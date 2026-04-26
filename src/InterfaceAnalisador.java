import java.awt.*;
import java.util.List;
import javax.swing.*;

public class InterfaceAnalisador extends JFrame {
    
    private JTextArea campoEntrada;
    private JTextArea campoSaida;
    private JButton botaoAnalisar;
    private JButton botaoLimpar;
    private AnalisadorLexico analisador;

    public InterfaceAnalisador() {
        inicializarInterface();
        this.analisador = new AnalisadorLexico();
    }

    private void inicializarInterface() {
        setTitle("Analisador Léxico - AFD para L = (ab)^n c^m (de)^p");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelEntrada = criarPainelCampo("Campo A - Entrada", true);
        campoEntrada = (JTextArea) painelEntrada.getClientProperty("textArea");

        JPanel painelBotoes = criarPainelBotoes();

        JPanel painelSaida = criarPainelCampo("Campo B - Saída (Tokens)", false);
        campoSaida = (JTextArea) painelSaida.getClientProperty("textArea");

        painelPrincipal.add(painelEntrada, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        painelPrincipal.add(painelSaida, BorderLayout.SOUTH);

        add(painelPrincipal);
        setVisible(true);
    }

    private JPanel criarPainelCampo(String titulo, boolean editavel) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBorder(BorderFactory.createTitledBorder(titulo));

        JTextArea textArea = new JTextArea(10, 50);
        textArea.setEditable(editavel);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        painel.add(scrollPane, BorderLayout.CENTER);

        painel.putClientProperty("textArea", textArea);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painel.setBorder(BorderFactory.createTitledBorder("Controles"));

        botaoAnalisar = new JButton("Analisar");
        botaoAnalisar.setFont(new Font("Arial", Font.BOLD, 12));
        botaoAnalisar.setPreferredSize(new Dimension(120, 40));
        botaoAnalisar.addActionListener(e -> executarAnalise());

        botaoLimpar = new JButton("Limpar");
        botaoLimpar.setFont(new Font("Arial", Font.BOLD, 12));
        botaoLimpar.setPreferredSize(new Dimension(120, 40));
        botaoLimpar.addActionListener(e -> limparCampos());

        painel.add(botaoAnalisar);
        painel.add(botaoLimpar);

        return painel;
    }

    private void executarAnalise() {
        String entrada = campoEntrada.getText();

        if (entrada.trim().isEmpty()) {
            campoSaida.setText("Erro: Campo de entrada está vazio!");
            return;
        }

        try {
            List<Token> tokens = analisador.analisar(entrada);

            StringBuilder saida = new StringBuilder();
            saida.append("=== RESULTADO DA ANÁLISE LÉXICA ===\n\n");

            if (tokens.isEmpty()) {
                saida.append("Nenhum token identificado.\n");
            } else {
                for (Token token : tokens) {
                    saida.append(String.format("[%s] '%s' (L%d:C%d)\n",
                            token.getTipo(),
                            token.getValor(),
                            token.getLinha(),
                            token.getColuna()));
                }

                saida.append("\n=== ESTATÍSTICAS ===\n");
                long totalTokens = tokens.size();
                long erros = tokens.stream().filter(t -> t.getTipo().startsWith("ERRO")).count();
                long validos = tokens.stream().filter(t -> t.getTipo().equals("sentença válida")).count();
                long operadores = tokens.stream().filter(t -> t.getTipo().equals("operador aritmético")).count();

                saida.append(String.format("Total de tokens: %d\n", totalTokens));
                saida.append(String.format("Sentenças válidas: %d\n", validos));
                saida.append(String.format("Operadores aritméticos: %d\n", operadores));
                saida.append(String.format("Erros: %d\n", erros));
            }

            campoSaida.setText(saida.toString());

        } catch (Exception ex) {
            campoSaida.setText("ERRO NA ANÁLISE:\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        campoEntrada.setText("");
        campoSaida.setText("");
        campoEntrada.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceAnalisador());
    }
}
