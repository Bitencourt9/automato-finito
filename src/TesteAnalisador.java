import java.util.List;

public class TesteAnalisador {
    
    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════════");
        System.out.println("   TESTES DO ANALISADOR LÉXICO");
        System.out.println("════════════════════════════════════════════\n");
        
        AnalisadorLexico analisador = new AnalisadorLexico();
        
        testar(analisador, "c", "Sentença válida simples");
        testar(analisador, "de", "Par (de)");
        testar(analisador, "ab c", "INVÁLIDO: (ab) ímpar");
        testar(analisador, "abab c", "Válido: (ab)(ab) com c");
        testar(analisador, "c + de", "Com operador aritmético");
        testar(analisador, "c @ de", "Com símbolo inválido (@)");
        testar(analisador, "abaab c + de de", "Teste completo misto");
        testar(analisador, "dede", "INVÁLIDO: (de)(de) tem m+p par");
        testar(analisador, "c\nde", "Com quebra de linha");
        testar(analisador, "   c   de   ", "Com espaçamento irregular");
        
        System.out.println("\n════════════════════════════════════════════");
        System.out.println("   TESTES CONCLUÍDOS");
        System.out.println("════════════════════════════════════════════");
    }
    
    private static void testar(AnalisadorLexico analisador, String entrada, String descricao) {
        System.out.println("┌─ TESTE: " + descricao);
        System.out.println("│ Entrada: \"" + entrada.replace("\n", "\\n") + "\"");
        System.out.println("├─ Análise:");
        
        List<Token> tokens = analisador.analisar(entrada);
        
        if (tokens.isEmpty()) {
            System.out.println("│  └─ [Nenhum token]");
        } else {
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);
                String prefixo = (i == tokens.size() - 1) ? "│  └─" : "│  ├─";
                System.out.println(prefixo + " [" + token.getTipo() + "] " + 
                                 "'" + token.getValor() + "' (L" + 
                                 token.getLinha() + ":C" + token.getColuna() + ")");
            }
        }
        
        long totalTokens = tokens.size();
        long erros = tokens.stream().filter(t -> t.getTipo().startsWith("ERRO")).count();
        long validos = tokens.stream().filter(t -> t.getTipo().equals("sentença válida")).count();
        
        if (totalTokens > 0) {
            System.out.println("├─ Estatísticas:");
            System.out.println("│  ├─ Total: " + totalTokens);
            System.out.println("│  ├─ Válidos: " + validos);
            System.out.println("│  └─ Erros: " + erros);
        }
        
        System.out.println("└─\n");
    }
}
