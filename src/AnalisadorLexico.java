import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    private List<Token> tokens;
    private StringBuilder tokenAtual;
    private int linha, coluna, colunaInicio;

    private static final int SIMBOLO_L_VALIDO = 1;
    private static final int OPERADOR_ARITMETICO = 2;
    private static final int SIMBOLO_INVALIDO = 3;

    public AnalisadorLexico() {
        this.tokens = new ArrayList<>();
        this.tokenAtual = new StringBuilder();
    }

    public List<Token> analisar(String entrada) {
        tokens.clear();
        tokenAtual.setLength(0);
        linha = 1; coluna = 1; colunaInicio = 1;

        for (int i = 0; i < entrada.length(); i++) {
            char c = entrada.charAt(i);
            if (c == '\n') { finalizarTokenSeNecessario(); linha++; coluna = 1; continue; }
            if (isDelimitador(c)) { finalizarTokenSeNecessario(); coluna++; continue; }

            switch (classificarCaractere(c)) {
                case OPERADOR_ARITMETICO:
                    finalizarTokenSeNecessario();
                    tokens.add(new Token("operador aritmético", String.valueOf(c), linha, coluna));
                    coluna++;
                    break;
                case SIMBOLO_L_VALIDO:
                    if (tokenAtual.length() == 0) colunaInicio = coluna;
                    tokenAtual.append(c);
                    coluna++;
                    break;
                case SIMBOLO_INVALIDO:
                    finalizarTokenSeNecessario();
                    tokens.add(new Token("ERRO - símbolo inválido", String.valueOf(c), linha, coluna));
                    coluna++;
                    break;
            }
        }
        finalizarTokenSeNecessario();
        return tokens;
    }

    private void finalizarTokenSeNecessario() {
        if (tokenAtual.length() > 0) {
            String valor = tokenAtual.toString();
            if (isValidoAFD(valor)) {
                tokens.add(new Token("sentença válida", valor, linha, colunaInicio));
            } else {
                tokens.add(new Token("ERRO - sentença inválida", valor, linha, colunaInicio));
            }
            tokenAtual.setLength(0);
        }
    }

    private int classificarCaractere(char c) {
        if (c >= 'a' && c <= 'e') return SIMBOLO_L_VALIDO;
        if (c == '+' || c == '-' || c == '*' || c == '/') return OPERADOR_ARITMETICO;
        return SIMBOLO_INVALIDO;
    }

    private boolean isDelimitador(char c) {
        return c == ' ' || c == '\t' || c == '\r';
    }

    private boolean isValidoAFD(String palavra) {
        int estado = 0;

        for (int i = 0; i < palavra.length(); i++) {
            char c = palavra.charAt(i);
            switch (estado) {
                case 0:
                    if (c == 'a') estado = 1;
                    else if (c == 'c') estado = 5;
                    else if (c == 'd') estado = 6;
                    else return false;
                    break;
                case 1:
                    if (c == 'b') estado = 2;
                    else return false;
                    break;
                case 2:
                    if (c == 'a') estado = 3;
                    else return false;
                    break;
                case 3:
                    if (c == 'b') estado = 0;
                    else return false;
                    break;
                case 5:
                    if (c == 'c') estado = 9;
                    else if (c == 'd') estado = 7;
                    else return false;
                    break;
                case 6:
                    if (c == 'e') estado = 5;
                    else return false;
                    break;
                case 7:
                    if (c == 'e') estado = 9;
                    else return false;
                    break;
                case 9:
                    if (c == 'c') estado = 5;
                    else if (c == 'd') estado = 6;
                    else return false;
                    break;
                default:
                    return false;
            }
        }
        return (estado == 5);
    }
}