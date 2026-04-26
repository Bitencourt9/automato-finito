import java.util.ArrayList;
import java.util.List;

public class AnalisadorLexico {
    private List<Token> tokens;
    private StringBuilder tokenAtual;
    private int linha, coluna, colunaInicio;

    private final int[][] tabela = {
        { 1, -1,  4,  6, -1}, 
        {-1,  2, -1, -1, -1}, 
        { 3, -1, -1, -1, -1}, 
        {-1,  0, -1, -1, -1}, 
        {-1, -1,  5,  8, -1}, 
        {-1, -1,  4, -1, -1}, 
        {-1, -1, -1, -1,  7}, 
        {-1, -1, -1,  8, -1}, 
        {-1, -1, -1, -1,  9}, 
        {-1, -1, -1,  6, -1}  
    };

    private final int[] EF = {0, 0, 0, 0, 1, 0, 0, 1, 0, 0};

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
            
            if (c == '\n') {
                finalizarTokenSeNecessario();
                linha++;
                coluna = 1;
                continue;
            }
            
            if (isDelimitador(c)) {
                finalizarTokenSeNecessario();
                coluna++;
                continue;
            }

            if (isOperador(c)) {
                finalizarTokenSeNecessario();
                tokens.add(new Token("operador aritmético", String.valueOf(c), linha, coluna));
                coluna++;
            } else {
                if (tokenAtual.length() == 0) colunaInicio = coluna;
                tokenAtual.append(c);
                coluna++;
            }
        }
        finalizarTokenSeNecessario();
        return tokens;
    }

    private void finalizarTokenSeNecessario() {
        if (tokenAtual.length() > 0) {
            String valor = tokenAtual.toString();
            
            if (contemSimboloInvalido(valor)) {
                tokens.add(new Token("ERRO - símbolo(s) inválido(s)", valor, linha, colunaInicio));
            } else if (isValidoAFD(valor)) {
                tokens.add(new Token("sentença válida", valor, linha, colunaInicio));
            } else {
                tokens.add(new Token("ERRO - sentença inválida", valor, linha, colunaInicio));
            }
            tokenAtual.setLength(0);
        }
    }

    private boolean contemSimboloInvalido(String palavra) {
        for (int i = 0; i < palavra.length(); i++) {
            if (mapearSimbolo(palavra.charAt(i)) == -1) {
                return true;
            }
        }
        return false;
    }

    private boolean isOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean isDelimitador(char c) {
        return c == ' ' || c == '\t' || c == '\r';
    }

    private int mapearSimbolo(char c) {
        switch (c) {
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            default: return -1;
        }
    }

    private boolean isValidoAFD(String palavra) {
        int estado = 0;
        for (int i = 0; i < palavra.length(); i++) {
            int simbolo = mapearSimbolo(palavra.charAt(i));
            if (simbolo == -1) return false;
            estado = tabela[estado][simbolo];
            if (estado == -1) return false;
        }
        return EF[estado] == 1;
    }
}